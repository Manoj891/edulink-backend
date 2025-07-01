package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.entity.student.PreviousEducation;
import com.ms.ware.online.solution.school.service.student.PreviousEducationService;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Student/PreviousEducation")
public class PreviousEducationRestController {

    @Autowired
    PreviousEducationService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> index(@RequestParam Long regNo) {
        return service.getAll(regNo);
    }

    @PostMapping
    public ResponseEntity<Object> doSave(@RequestBody PreviousEducation obj) throws IOException {
        return service.save(obj);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
