package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.entity.student.SchoolHostal;
import com.ms.ware.online.solution.school.service.student.SchoolHostalService;
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
@RequestMapping("api/Student/SchoolHostel")
public class SchoolHostalRestController {

    @Autowired
    SchoolHostalService service;

    @GetMapping
    public Object index(@RequestParam(required = false) Long regNo,@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long hostelType) {

        return service.getAll( regNo,academicYear, program, classId, hostelType);
    }

    @PostMapping
    public Object doSave(@RequestBody SchoolHostal obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody SchoolHostal obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
