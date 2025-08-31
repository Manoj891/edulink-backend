/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.SmsService;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dto.ResultSMS;
import com.ms.ware.online.solution.school.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Student/SMS")
public class StudentSMSRestController {
    @Autowired
    private SmsService o;
    @Autowired
    private AuthenticationFacade facade;

    @PostMapping("/exam")
    public ResponseEntity<List<Long>> doSave(@RequestBody ResultSMS req) throws IOException {
        if (!o.isConfigured()) throw new CustomException("SMS Not Configured");
        List<Long> ids = new ArrayList<>();
        DB db = new DB();
        String username = facade.getAuthentication().getUserName();;
        Map<String, Object> map = db.getRecord("select ifnull(organization_name,name) name from organization_master").get(0);
        String orgName = (map.get("name").toString()).trim().replace("  ", " ");
        req.getData().forEach(d -> db.getRecord("SELECT STU_NAME name,MOBILE_NO mobile FROM student_info where id=" + d.getId()).forEach(m -> {
            String name = m.get("name").toString();
            if (name.contains(" ")) name = name.substring(0, name.indexOf(" "));
            String message = ("Dear " + name + ",\nYour result details of " + req.getExamName() + ":\nClass " + req.getClassName() + "\nGPA:" + d.getGpa() + "\nGrade:" + d.getGrade() + "\n" + d.getPercentage() + "%\nThank you\n" + orgName).replace("  ", " ");
            String mobile = m.get("mobile").toString().trim().replace(" ", "");
            if (o.sendSMS(mobile, message, username) == 1) {
                ids.add(d.getId());
            }
        }));
        return ResponseEntity.status(HttpStatus.OK).body(ids);
    }

    @GetMapping
    public Object studentBillSms(@RequestParam long billYear, @RequestParam String month, @RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup) {
        DB db = new DB();
        Message msg = new Message();
        Map map;
        String sql = "", regNo, studentName, message, mobileNo;
        sql = "SELECT COUNT(AD_DATE)  AS totalDay FROM ad_bs_calender WHERE BS_DATE LIKE '" + billYear + "-" + month + "%'";
        map = db.getRecord(sql).get(0);
        int totalDay = Integer.parseInt(map.get("totalDay").toString());
        String endDateAd = DateConverted.bsToAd(billYear + "-" + month + "-" + totalDay);
        String startDateAd = DateConverted.bsToAd(billYear + "-" + month + "-01");
        if (startDateAd.length() != 10) {
            return msg.respondWithError("year " + billYear + " month " + month + " not list in school calendar!!");
        }
        sql = "SELECT ROLL_NO rollNo,SECTION section,ID regNo,STU_NAME studentName,FATHERS_NAME fathersName,MOBILE_NO mobileNo,IFNULL(email,'') email FROM student_info WHERE ACADEMIC_YEAR=" + academicYear + " AND PROGRAM=" + program + " AND CLASS_ID=IFNULL(" + classId + ",CLASS_ID) AND SUBJECT_GROUP=IFNULL(" + subjectGroup + ",SUBJECT_GROUP) ORDER BY PROGRAM,CLASS_ID,ROLL_NO";
        List list = db.getRecord(sql);
        List l = new ArrayList();
        System.gc();
        for (int i = 0; i < list.size(); i++) {
            try {
                map = (Map) list.get(i);
                regNo = map.get("regNo").toString();
                studentName = map.get("studentName").toString();
                mobileNo = map.get("mobileNo").toString();
//                System.out.println(mobileNo.startsWith("98") + " " + mobileNo);
                if (mobileNo.length() != 10 || (!mobileNo.startsWith("98") && !mobileNo.startsWith("97"))) {
                    continue;
                }
                message = "Dear " + studentName.substring(0, studentName.indexOf(" ")) + ", Your due amount of " + billYear + " " + msg.getMonthName(month) + " Rs. " + msg.getFeeDueAmount(regNo, startDateAd, endDateAd) + " please pay within the time";
                map.put("message", message);
                l.add(map);
            } catch (Exception e) {
                System.gc();
            }
        }
        System.gc();

        return l;
    }

    @GetMapping("/report")
    public Map<String, Object> smsReport(@RequestParam String dateFrom, @RequestParam String dateTo) {
        return o.getSmsReport(dateFrom, dateTo);
    }


}
