package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.dto.OldStudent;
import com.ms.ware.online.solution.school.entity.student.FeeSetup;
import com.ms.ware.online.solution.school.service.student.FeeSetupService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Student/FeeSetup")
public class FeeSetupRestController {

    @Autowired
    FeeSetupService service;

    @GetMapping
    public Object index(@RequestParam(required = false) Long subjectGroup, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long academicYear) {

        return service.getAll(program, classId, academicYear, subjectGroup);
    }

    @PostMapping
    public Object doSave(@RequestBody FeeSetup obj) throws IOException {
        return service.save(obj);
    }

    @PostMapping("/OldStudent")
    public Object doSave(@RequestBody OldStudent oldStudent) throws IOException {
        return service.save(oldStudent);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody FeeSetup obj, @PathVariable String id) throws IOException {
        return service.update(obj, id);
    }

    @PostMapping("/Copy")
    public Object doSave(@RequestParam Long program, @RequestParam Long classId, @RequestParam Long academicYear, @RequestParam Long programTo, @RequestParam Long classIdTo, @RequestParam Long academicYearTo, @RequestParam Long subjectGroup, @RequestParam Long subjectGroupTo) throws IOException {

        return service.copy(program, classId, academicYear, programTo, classIdTo, academicYearTo, subjectGroup, subjectGroupTo);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
