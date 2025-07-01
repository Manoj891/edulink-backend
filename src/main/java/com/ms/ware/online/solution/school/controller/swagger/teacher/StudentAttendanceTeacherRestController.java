/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.teacher;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.entity.student.StudentAttendance;
import com.ms.ware.online.solution.school.service.student.StudentAttendanceService;
import com.ms.ware.online.solution.school.config.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Student/StudentAttendance")
public class StudentAttendanceTeacherRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    StudentAttendanceService service;

    @GetMapping("/Teacher")
    public ResponseEntity<List<Map<String, Object>>> indexTeacher(@RequestParam Long acadeicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam String date) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            throw new CustomException("invalid token");
        }
        String sql = "SELECT IS_CLASS_TEACHER FROM teachers_class_subject WHERE ACADEMIC_YEAR='" + acadeicYear + "' AND CLASS_ID='" + classId + "' AND PROGRAM='" + program + "' AND SUBJECT_GROUP='" + subjectGroup + "' AND TEACHER='" + td.getUserId() + "' AND IS_CLASS_TEACHER='Y'";
        DB db = new DB();
        List<Map<String, Object>> list = db.getRecord(sql);
        if (list.isEmpty()) {
            throw new CustomException("You can not access this feature");
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(acadeicYear, program, classId, subjectGroup, date));
    }

    @PutMapping
    public Object doSaveTeacher(@RequestBody List<StudentAttendance> list) throws IOException {
        return service.saveTeacher(list);
    }
}
