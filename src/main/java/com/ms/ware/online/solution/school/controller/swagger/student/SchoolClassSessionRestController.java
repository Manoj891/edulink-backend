package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.entity.student.SchoolClassSession;
import com.ms.ware.online.solution.school.service.student.SchoolClassSessionService;
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
@RequestMapping("api/Student/SchoolClassSession")
public class SchoolClassSessionRestController {

    @Autowired
    SchoolClassSessionService service;

    @GetMapping
    public Object index(@RequestParam(required = false) Long academicYear) {

        return service.getAll(academicYear);
    }

    @PostMapping
    public Object doSave(@RequestBody SchoolClassSession obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody SchoolClassSession obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
