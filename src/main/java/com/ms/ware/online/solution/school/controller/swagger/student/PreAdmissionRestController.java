package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.entity.student.PreAdmission;
import com.ms.ware.online.solution.school.service.student.PreAdmissionService;
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
@RequestMapping("api/Student/PreAdmission")
public class PreAdmissionRestController {

    @Autowired
    PreAdmissionService service;

    @GetMapping
    public Object index(@RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId) {

        return service.getAll(academicYear, program, classId);
    }

    @GetMapping("/{id}")
    public Object index(@PathVariable long id) {
        return service.getRecord(id);
    }

    @PostMapping
    public Object doSave(@RequestBody PreAdmission obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody PreAdmission obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
