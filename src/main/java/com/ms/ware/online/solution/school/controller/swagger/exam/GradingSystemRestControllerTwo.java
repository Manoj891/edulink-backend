package com.ms.ware.online.solution.school.controller.swagger.exam;


import com.ms.ware.online.solution.school.entity.exam.GradingSystemTwo;

import com.ms.ware.online.solution.school.service.exam.GradingSystemServiceTwo;

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
@RequestMapping("api/Exam/GradingSystemTwo")
public class GradingSystemRestControllerTwo {

    @Autowired
    GradingSystemServiceTwo service;

    @GetMapping
    public Object index() {

        return service.getAll();
    }

    @PostMapping
    public Object doSave(@RequestBody GradingSystemTwo obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody GradingSystemTwo obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
