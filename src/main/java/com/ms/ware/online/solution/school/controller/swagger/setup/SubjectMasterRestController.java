package com.ms.ware.online.solution.school.controller.swagger.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;
import com.ms.ware.online.solution.school.service.setup.SubjectMasterService;
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
@RequestMapping("api/Setup/SubjectMaster")
public class SubjectMasterRestController {

    @Autowired
    SubjectMasterService service;

    @GetMapping
    public Object index() {

        return service.getAll();
    }

    @GetMapping("/{group}/{program}/{classId}")
    public Object index(@PathVariable Long group, @PathVariable Long program, @PathVariable Long classId) {
        return service.getAll(group, program, classId);
    }

    @PostMapping
    public Object doSave(@RequestBody SubjectMaster obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody SubjectMaster obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
