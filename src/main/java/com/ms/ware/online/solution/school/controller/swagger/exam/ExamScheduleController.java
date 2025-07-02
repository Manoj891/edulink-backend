package com.ms.ware.online.solution.school.controller.swagger.exam;

import com.ms.ware.online.solution.school.dto.ExamScheduleReq;
import com.ms.ware.online.solution.school.service.exam.ExamScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Exam/ExamSchedule")
@RequiredArgsConstructor
public class ExamScheduleController {
    private final ExamScheduleService service;

    @GetMapping("/{exam}")
    public ResponseEntity<List<Map<String, Object>>> index(@PathVariable long exam) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(exam));
    }


    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody ExamScheduleReq obj) {
        service.save(obj);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"success\"}");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> doUpdate(@RequestBody ExamScheduleReq obj, @PathVariable long id) {
        service.update(obj, id);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"success\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"success\"}");
    }

    @GetMapping("entrance-card")
    public ResponseEntity<Map<String, Object>> getEntranceCard(@RequestParam Long group, @RequestParam Long program, @RequestParam Long classId, @RequestParam(defaultValue = "") String section, @RequestParam Long exam) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntranceCard(program, classId, group, exam, section));
    }

    @PostMapping("entrance-card")
    public ResponseEntity<Map<String, Object>> getEntranceCard(@RequestBody String regNos, @RequestParam long program, @RequestParam long classId, @RequestParam long groupId, @RequestParam long examId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getEntranceCard(regNos, examId, program, classId, groupId));
    }
}
