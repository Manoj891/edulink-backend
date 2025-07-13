package com.ms.ware.online.solution.school.controller.swagger.exam;


import com.ms.ware.online.solution.school.dto.CashSystem;
import com.ms.ware.online.solution.school.dto.ExamMarkEntryReq;
import com.ms.ware.online.solution.school.dto.GpaWise;
import com.ms.ware.online.solution.school.dto.SubjectWise;
import com.ms.ware.online.solution.school.service.exam.ExamMarkEntryService;

import java.io.IOException;
import java.util.List;

import com.ms.ware.online.solution.school.service.exam.MarkUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/Exam/ExamMark")
public class ExamMarkEntryRestController {

    @Autowired
    ExamMarkEntryService service;

    @GetMapping("/Entry")
    public Object index(@RequestParam Long exam, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long subject, @RequestParam String section, @RequestParam String order) {
        return service.getAll(exam, program, classId, subjectGroup, subject, section, order);
    }

    @PostMapping("/Upload")
    public Object markUpload(HttpServletRequest request, @RequestParam MultipartFile markUpload, @RequestParam Long exam) throws IOException {
        return service.markUpload(request, markUpload, exam);
    }

    @GetMapping("/Upload")
    public Object markUpload(@RequestParam Long exam) {
        return service.markUpload(exam);
    }

    @GetMapping("/update")
    public Object markUpdate(@RequestParam Long exam, @RequestParam Long regNo) {
        return service.markUpdate(exam, regNo);
    }

    @PatchMapping("/update")
    public Object markUpdateConfig(@RequestParam Long exam) {
        return service.markUpdateConfig(exam);
    }

    @PutMapping("/update")
    public Object markApprove(@RequestParam Long exam) {
        return service.markApprove(exam);
    }

    @PostMapping("/update")
    public Object markUpdate(@RequestBody List<MarkUpdateReq> req) {
        return service.markUpdate(req);
    }

    @GetMapping("/Approve")
    public Object getApprove(@RequestParam Long exam, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long subject) {
        return service.getApprove(exam, program, classId, subjectGroup, subject);
    }

    @GetMapping("/FinalReport")
    public Object getFinalReport(@RequestParam Integer system, @RequestParam(required = false) Long regNo, @RequestParam Long exam, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup) {
        return service.getFinalReport(system, exam, program, classId, subjectGroup, regNo);
    }

    @GetMapping("/Report")
    public Object getReport(@RequestParam Integer system, @RequestParam(defaultValue = "") String result, @RequestParam(required = false) Long regNo, @RequestParam Long exam, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam(defaultValue = "") String section) {
        return service.getReport(system, exam, program, classId, subjectGroup, regNo, section, result);
    }

    @GetMapping("/gpa-wise")
    public List<GpaWise> gpaWiseReport(@RequestParam Integer system, @RequestParam Long exam, @RequestParam Long program, @RequestParam Long subjectGroup) {
        return service.gpaWiseReport(system, exam, program, subjectGroup);
    }

    @GetMapping("/subject-wise")
    public List<SubjectWise> subjectWiseReport(@RequestParam Integer system, @RequestParam Long exam, @RequestParam Long classId, @RequestParam Long program, @RequestParam Long subjectGroup) {
        return service.subjectWiseReport(system, exam, program, classId, subjectGroup);
    }

    @GetMapping("/cash-report")
    public ResponseEntity<CashSystem> cashSystem(@RequestParam(required = false) Long regNo, @RequestParam Long exam, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam(defaultValue = "") String section) {
        return ResponseEntity.status(HttpStatus.OK).body(service.cashSystem(exam, program, classId, subjectGroup, regNo, section));
    }

    @PostMapping("/Entry")
    public Object doSave(@RequestBody ExamMarkEntryReq req) throws IOException {
        return service.save(req);
    }

    @PostMapping("/Approve")
    public Object doApprove(@RequestBody String jsonData) {
        return service.doApprove(jsonData);
    }

    @DeleteMapping("/Entry/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
