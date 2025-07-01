/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;


import java.util.HashMap;
import java.util.Map;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.beans.factory.annotation.Autowired;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel/student")
public class StudentFeeInformationRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private DB db;

    @GetMapping("/Dashboard")
    public Object dashboard() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        message.map = new HashMap<>();
        String sql = "SELECT BS_DATE 'date',DAY 'day',EVENT event,STUDENT_HOLYDAY holiday FROM ad_bs_calender WHERE AD_DATE BETWEEN SUBDATE(SYSDATE(),15) AND ADDDATE(SYSDATE(),30)";
        message.map.put("calender", db.getRecord(sql));
        sql = "SELECT ENTER_DATE 'date',NOTICE notice FROM notice_board WHERE ENTER_DATEAD BETWEEN SUBDATE(SYSDATE(),90) AND ADDDATE(SYSDATE(),270) ORDER BY ENTER_DATEAD DESC";
        message.map.put("noticeBoard", db.getRecord(sql));
        String date = DateConverted.today();
        sql = "SELECT GET_BS_DATE(ATT_DATE) date,IN_TIME inTime,OUT_TIME outTime,STATUS status FROM student_attendance A WHERE A.STU_ID='" + td.getUserId() + "' AND ATT_DATE BETWEEN SUBDATE('" + date + "',15) AND '" + date + "' UNION SELECT BS_DATE date,'' inTime,'' outTime,EVENT status FROM ad_bs_calender WHERE AD_DATE BETWEEN SUBDATE('" + date + "',15) AND '" + date + "' AND AD_DATE NOT IN(SELECT ATT_DATE date FROM student_attendance A WHERE A.STU_ID='" + td.getUserId() + "' AND ATT_DATE BETWEEN SUBDATE('" + date + "',15) AND '" + date + "') ORDER BY date DESC";
        message.map.put("attendance", db.getRecord(sql));
        return message.map;
    }

    @GetMapping("/FeeInformation")
    public Object index(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long classId) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        DB db = new DB();
        String sql = "SELECT GET_PAID_AMOUNT(M.REG_NO,D.BILL_ID," + classId + "," + academicYear + ") AS paid,SUM(CR)-SUM(DR) AS due,B.NAME feeName,D.ACADEMIC_YEAR academicYear,C.NAME className FROM stu_billing_master M,stu_billing_detail D,bill_master B,class_master C WHERE M.BILL_NO=D.BILL_NO AND D.BILL_ID=B.ID AND D.CLASS_ID=C.ID AND M.REG_NO='" + td.getUserId() + "' AND D.CLASS_ID=IFNULL(" + classId + ",D.CLASS_ID) AND D.ACADEMIC_YEAR=IFNULL(" + academicYear + ",D.ACADEMIC_YEAR) GROUP BY D.ACADEMIC_YEAR,D.CLASS_ID,D.BILL_ID ORDER BY academicYear,className,due DESC";
        return db.getRecord(sql);
    }

    @GetMapping("/Exam")
    public Object exam() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String sql = "SELECT E.EXAM_NAME name,E.ID id FROM exam_student_registration R,exam_master E WHERE R.EXAM=E.ID AND STUDENT_ID='" + td.getUserId() + "' ORDER BY E.ID DESC";
        return db.getRecord(sql);
    }

    @GetMapping("/Paid")
    public Object billPaid(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long classId) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String sql = "SELECT BILL_NO billNo,BILL_AMOUNT billAmount,C.NAME AS className,ACADEMIC_YEAR academicYear,GET_BS_DATE(ENTER_DATE) date FROM stu_billing_master B,class_master C WHERE B.CLASS_ID=C.ID AND BILL_TYPE='DR' AND REG_NO='" + td.getUserId() + "' AND B.CLASS_ID=IFNULL(" + classId + ",B.CLASS_ID) AND B.ACADEMIC_YEAR=IFNULL(" + academicYear + ",B.ACADEMIC_YEAR) ORDER BY ENTER_DATE";
        return db.getRecord(sql);
    }

    @GetMapping("/Vehicle")
    public Object vehicle() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        String sql;
        Map map = new HashMap();
        sql = "SELECT ID id,CONCAT(BUS_NAME,' ',BUS_NO) AS name FROM bus_master";
        map.put("vehicle", db.getRecord(sql));
        sql = "SELECT ID id,NAME name FROM bus_station_master ";
        map.put("location", db.getRecord(sql));
        return map;
    }

    @GetMapping("/Vehicle/Data")
    public Object vehicle(@RequestParam Long vehicle, @RequestParam Long location) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }


        String sql = "SELECT S.NAME location,V.BUS_NAME busName,V.BUS_NO busNo,T.ARRIVAL_TIME arrivalTime,T.DEPARTURE_TIME departureTime,T.GO_RETURN type FROM bus_station_time T,bus_master V,bus_station_master S WHERE T.BUS=V.ID AND T.STATION=S.ID AND T.BUS=IFNULL(" + vehicle + ",T.BUS) AND T.STATION=IFNULL(" + location + ",T.STATION) ORDER BY GO_RETURN,BUS_NO,S.NAME";
        Map map = new HashMap();
        map.put("timing", db.getRecord(sql));
        sql = "SELECT BUS_NAME AS vehicleName,BUS_NO vehicleNo,DRIVER_NAME drivereName,MOBILE_NO mobileNo FROM bus_master";
        map.put("driver", db.getRecord(sql));
        return map;
    }
}
