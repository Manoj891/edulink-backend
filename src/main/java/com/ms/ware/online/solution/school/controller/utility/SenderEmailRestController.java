package com.ms.ware.online.solution.school.controller.utility;


import com.ms.ware.online.solution.school.entity.utility.SenderEmail;
import com.ms.ware.online.solution.school.service.utility.SenderEmailService;

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
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("api/Utility/SenderEmail")
public class SenderEmailRestController {

    @Autowired
    SenderEmailService service;

    @GetMapping
    public ResponseEntity index() {

        return service.getAll();
    }

    @PostMapping
    public ResponseEntity doSave(@RequestBody SenderEmail obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity doUpdate(@RequestBody SenderEmail obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
