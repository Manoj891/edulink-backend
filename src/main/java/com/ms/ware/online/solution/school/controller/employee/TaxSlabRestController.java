package com.ms.ware.online.solution.school.controller.employee;


import com.ms.ware.online.solution.school.entity.employee.TaxSlab;
import com.ms.ware.online.solution.school.service.employee.TaxSlabService;
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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/Employee/TaxSlab")
public class TaxSlabRestController {

    @Autowired
    TaxSlabService service;

    @GetMapping
    public ResponseEntity index(@RequestParam Long fiscalYear) {

        return service.getAll(fiscalYear);
    }

    @PostMapping
    public ResponseEntity doSave(@RequestBody TaxSlab obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity doUpdate(@RequestBody TaxSlab obj, @PathVariable long id) throws IOException {
        return service.update(obj,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
