/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.NoticeBoardDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/panel/student/Attendance")
public class StudentAttendanceRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    NoticeBoardDao da;
    @Autowired
    private Message message;
    @GetMapping
    public Object attendance() {
        
         AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String sql;
        sql = "SELECT ACADEMIC_YEAR academicYear,CLASS_ID classId,PROGRAM program FROM student_info WHERE ID='" + td.getUserId() + "'";
        List l = da.getRecord(sql);
        Map map = (Map) l.get(0);
        String academicYear = map.get("academicYear").toString();
        String classId = map.get("classId").toString();
        String program = map.get("program").toString();
        String endDate = DateConverted.today();
        sql = "SELECT START_DATE stardDate,END_DATE endDate,DATEDIFF(END_DATE,'" + endDate + "') dateDiff FROM school_class_session WHERE PROGRAM='" + program + "' AND CLASS_ID='" + classId + "' AND ACADEMIC_YEAR='" + academicYear + "'";
        l = da.getRecord(sql);
        if (l.isEmpty()) {
            return message.respondWithError("Session not Started!!");
        }
        map = (Map) l.get(0);
        String stardDate = map.get("stardDate").toString();
        int dateDiff = Integer.parseInt(map.get("dateDiff").toString());
        if (dateDiff <= 0) {
            endDate = map.get("endDate").toString();
        }
        message.map = new HashMap();
        sql = "SELECT GET_BS_DATE(ATT_DATE) date,IN_TIME inTime,OUT_TIME outTime,STATUS status FROM student_attendance A WHERE A.STU_ID='" + td.getUserId() + "' AND ATT_DATE BETWEEN '" + stardDate + "' AND '" + endDate + "' UNION SELECT BS_DATE date,'' inTime,'' outTime,EVENT status FROM ad_bs_calender WHERE AD_DATE BETWEEN '" + stardDate + "' AND '" + endDate + "' AND AD_DATE NOT IN(SELECT ATT_DATE date FROM student_attendance A WHERE A.STU_ID='" + td.getUserId() + "' AND ATT_DATE BETWEEN '" + stardDate + "' AND '" + endDate + "') ORDER BY date DESC";
        message.map.put("attendance", da.getRecord(sql));
        sql = "SELECT COUNT(*) totalHoliday,DATEDIFF('" + endDate + "','" + stardDate + "') totalDay FROM ad_bs_calender WHERE STUDENT_HOLYDAY='Y' AND AD_DATE BETWEEN '" + stardDate + "' AND '" + endDate + "'";
        map = (Map) da.getRecord(sql).get(0);
        int totalHoliday = Integer.parseInt(map.get("totalHoliday").toString());
        int totalDay = Integer.parseInt(map.get("totalDay").toString());
        message.map.put("totalHoliday", totalHoliday);
        message.map.put("totalDay", totalDay);
        message.map.put("totalSchoolDaty", (totalDay - totalHoliday));
        sql = "SELECT COUNT(*) totalAttendance FROM student_attendance WHERE STATUS='Y' AND STU_ID='" + td.getUserId() + "' AND ATT_DATE BETWEEN '" + stardDate + "' AND '" + endDate + "' ";
        map = (Map) da.getRecord(sql).get(0);
        message.map.put("totalAttendance", map.get("totalAttendance").toString());
        return message.map;
    }

}
