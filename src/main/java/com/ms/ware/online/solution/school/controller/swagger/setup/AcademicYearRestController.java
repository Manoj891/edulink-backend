package com.ms.ware.online.solution.school.controller.swagger.setup;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.Section;
import com.ms.ware.online.solution.school.service.setup.AcademicYearService;

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

@RestController
@RequestMapping("api/Setup/")
public class AcademicYearRestController {

    @Autowired
    AcademicYearService service;

    @GetMapping("AcademicYear")
    public Object index() {
        return service.getAll();
    }

    @GetMapping("Section")
    public Object getSection() {
        return service.getSection();
    }

    @PostMapping("/Section")
    public Object doSave(@RequestBody Section obj) throws IOException {
        return service.save(obj);
    }

    @DeleteMapping("Section/{id}")
    public Object doDeleteSection(@PathVariable String id) {
        return service.deleteSection(id);
    }

    @PostMapping("AcademicYear")
    public Object doSave(@RequestBody AcademicYear obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("AcademicYear/{id}")
    public Object doUpdate(@RequestBody AcademicYear obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("AcademicYear/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
