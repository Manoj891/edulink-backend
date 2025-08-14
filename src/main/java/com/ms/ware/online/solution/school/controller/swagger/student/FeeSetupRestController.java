package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.dto.FeeSetupReq;
import com.ms.ware.online.solution.school.dto.OldStudent;
import com.ms.ware.online.solution.school.service.student.FeeSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Student/FeeSetup")
public class FeeSetupRestController {

    @Autowired
    private FeeSetupService service;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> index(@RequestParam(required = false) Long subjectGroup, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long academicYear) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(program, classId, academicYear, subjectGroup));
    }

    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody FeeSetupReq obj) throws IOException {
        service.save(obj);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Success\"}");
    }

    @PostMapping("/OldStudent")
    public Object doSave(@RequestBody OldStudent oldStudent) throws IOException {
        return service.save(oldStudent);
    }

    @PostMapping("/Copy")
    public Object doSave(@RequestParam Long program, @RequestParam Long classId, @RequestParam Long academicYear, @RequestParam Long programTo, @RequestParam Long classIdTo, @RequestParam Long academicYearTo, @RequestParam Long subjectGroup, @RequestParam Long subjectGroupTo) throws IOException {

        return service.copy(program, classId, academicYear, programTo, classIdTo, academicYearTo, subjectGroup, subjectGroupTo);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Success\"}");
    }
}
