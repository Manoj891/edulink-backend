package com.ms.ware.online.solution.school.controller.account;


import com.ms.ware.online.solution.school.entity.account.FiscalYear;
import com.ms.ware.online.solution.school.service.account.FiscalYearService;
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
@RequestMapping("api/Account/FiscalYear")
public class FiscalYearRestController {

    @Autowired
    FiscalYearService service;

    @GetMapping
    public Object index() {

        return service.getAll();
    }

    @PostMapping
    public Object doSave(@RequestBody FiscalYear obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody FiscalYear obj, @PathVariable long id) throws IOException {
        return service.update(obj,id);
    }

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
