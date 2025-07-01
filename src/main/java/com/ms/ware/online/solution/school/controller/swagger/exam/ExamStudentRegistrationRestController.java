package com.ms.ware.online.solution.school.controller.swagger.exam;

import com.ms.ware.online.solution.school.dto.ExamStudentAttendance;
import com.ms.ware.online.solution.school.dto.ExamStudentRegistrationApprove;
import com.ms.ware.online.solution.school.service.exam.ExamStudentRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Exam/ExamStudentRegistration")
public class ExamStudentRegistrationRestController {

    @Autowired
    ExamStudentRegistrationService service;

    @GetMapping
    public Object getRecord(@RequestParam(required = false) Long program, @RequestParam Long classId, @RequestParam Long exam) {
        return service.getRecord(program, classId, exam);
    }

    @GetMapping("/{examId}/{regNo}")
    public Map<String, Object> getRegistration(@PathVariable long examId, @PathVariable long regNo) {
        return service.getRegistration(examId, regNo);
    }

    @PatchMapping("/{examId}/{regNo}/{program}/{classId}/{groupId}")
    public Map<String, Object> updateRegistration(@PathVariable long examId, @PathVariable long regNo, @PathVariable long program, @PathVariable long classId, @PathVariable long groupId) {
        return service.updateRegistration(examId, regNo, program, classId, groupId);
    }

    @PostMapping("entrance-card")
    public ResponseEntity<List<Map<String, Object>>> getEntranceCard(@RequestBody String regNos, @RequestParam long examId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntranceCard(regNos, examId));
    }

    @GetMapping("/Pending")
    public Object getPending(@RequestParam(required = false) Long program, @RequestParam Long classId, @RequestParam Long exam) {
        return service.getPending(program, classId, exam);
    }

    @GetMapping("/Approve")
    public Object getApprove(@RequestParam(required = false) Long program, @RequestParam Long classId, @RequestParam(defaultValue = "") String section, @RequestParam Long exam) {
        return service.getApprove(program, classId, exam, section);
    }

    @PostMapping
    public Object doSave(@RequestBody String jsonData) throws IOException {
        return service.save(jsonData);
    }

    @PutMapping
    public Object doApprove(@RequestBody ExamStudentRegistrationApprove jsonData) throws IOException {
        return service.approve(jsonData);
    }

    @PatchMapping
    public Object doStudentAttendance(@RequestBody List<ExamStudentAttendance> req) throws IOException {
        return service.doStudentAttendance(req);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
