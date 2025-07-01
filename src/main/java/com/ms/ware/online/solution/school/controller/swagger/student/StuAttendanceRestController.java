package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.entity.student.StudentAttendance;
import com.ms.ware.online.solution.school.service.student.StudentAttendanceService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Student/StudentAttendance")
public class StuAttendanceRestController {

    @Autowired
    StudentAttendanceService service;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> index(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup, @RequestParam(required = false) String date) {
        return ResponseEntity.status(200).body(service.getAll(academicYear, program, classId, subjectGroup, date));
    }

    @PostMapping
    public Object doSave(@RequestBody List<StudentAttendance> list) throws IOException {
        return service.save(list);
    }

    @GetMapping("Monthly")
    public ResponseEntity<List<Map<String, Object>>> getAttendance(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup, @RequestParam String section, @RequestParam String year, @RequestParam String month, @RequestParam String type) {
        return ResponseEntity.status(200).body(service.getAttendance(academicYear, program, classId, subjectGroup, year, month, section, type));
    }

}
