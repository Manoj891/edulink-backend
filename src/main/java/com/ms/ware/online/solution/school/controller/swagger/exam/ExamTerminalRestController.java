package com.ms.ware.online.solution.school.controller.swagger.exam;


import com.ms.ware.online.solution.school.entity.exam.ExamTerminal;
import com.ms.ware.online.solution.school.service.exam.ExamTerminalService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/Exam/ExamTerminal")
public class ExamTerminalRestController {

    @Autowired
    ExamTerminalService service;

    @GetMapping("/{academicYear}")
    public Object index(@PathVariable Long academicYear) {
        return service.getAll(academicYear);
    }

    @GetMapping
    public Object index() {
        return service.getAll();
    }

    @GetMapping("mark")
    public Object getMark(@RequestParam long program, @RequestParam long classId, @RequestParam long exam) throws IOException {
        return service.getMark(program, classId, exam);
    }

    @PostMapping("mark")
    public Object markPush(@RequestParam long academicYear, @RequestParam long terminalExam, @RequestParam long program, @RequestParam long classId, @RequestParam long finalExam) throws IOException {
        return service.markPush(academicYear, terminalExam, program, classId, finalExam);
    }

    @PostMapping
    public Object doSave(@RequestBody ExamTerminal obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody ExamTerminal obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
