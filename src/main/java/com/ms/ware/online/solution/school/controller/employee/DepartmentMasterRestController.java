package com.ms.ware.online.solution.school.controller.employee;


import com.ms.ware.online.solution.school.entity.employee.DepartmentMaster;
import com.ms.ware.online.solution.school.service.employee.DepartmentMasterService;
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
@RequestMapping("api/Employee/DepartmentMaster")
public class DepartmentMasterRestController {

    @Autowired
    DepartmentMasterService service;

    @GetMapping
    public ResponseEntity index() {

        return service.getAll();
    }

    @PostMapping
    public ResponseEntity doSave(@RequestBody DepartmentMaster obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity doUpdate(@RequestBody DepartmentMaster obj, @PathVariable long id) throws IOException {
        return service.update(obj,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
