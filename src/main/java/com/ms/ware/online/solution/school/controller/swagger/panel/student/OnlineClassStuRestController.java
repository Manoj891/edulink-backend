/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel/student/OnlineClass")
public class OnlineClassStuRestController {
    @Autowired
    private AuthenticationFacade facade;

    @GetMapping
    public Object homework() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        String sql = "SELECT CONCAT(first_name,' ',last_name)  teacher,SM.NAME subject,O.LINK link,O.START_TIME startTime,O.END_TIME endTime  FROM student_info S,online_class O,subject_master SM,employee_info T WHERE SM.ID=O.SUBJECT AND O.TEACHER=T.ID AND S.ACADEMIC_YEAR=O.ACADEMIC_YEAR AND S.PROGRAM=O.PROGRAM AND S.CLASS_ID=O.CLASS_ID AND S.SUBJECT_GROUP=O.SUBJECT_GROUP AND S.ID='" + td.getUserId() + "'";
        return new DB().getRecord(sql);
    }
}
