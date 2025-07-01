package com.ms.ware.online.solution.school.controller.swagger.setup;


import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.service.setup.ClassMasterService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/Setup/ClassMaster")
public class ClassMasterRestController {

    @Autowired
    private ClassMasterService service;

    @GetMapping
    public Object index() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String,Object>> getClassMaster(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getClassMaster(id));
    }

    @PostMapping
    public Object doSave(@RequestBody ClassMaster obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody ClassMaster obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
