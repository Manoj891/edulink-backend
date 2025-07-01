/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.teacher;

import com.ms.ware.online.solution.school.config.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/TeacherPanel/TeachersHomework")
public class TeachersDashboardRestController {
    @Autowired
    private DB db;

    @GetMapping("/Dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long classId) {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT BS_DATE 'date',DAY 'day',EVENT event,STUDENT_HOLYDAY holiday FROM ad_bs_calender WHERE AD_DATE BETWEEN SUBDATE(SYSDATE(),15) AND ADDDATE(SYSDATE(),30)";
        map.put("calender", db.getRecord(sql));
        sql = "SELECT ENTER_DATE 'date',NOTICE notice FROM notice_board WHERE ENTER_DATEAD BETWEEN SUBDATE(SYSDATE(),90) AND ADDDATE(SYSDATE(),270) ORDER BY ENTER_DATEAD DESC";
        map.put("noticeBoard", db.getRecord(sql));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
