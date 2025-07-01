package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.entity.employee.MonthlyAllowance;
import com.ms.ware.online.solution.school.entity.employee.RegularAllowance;
import com.ms.ware.online.solution.school.service.employee.MonthlyAllowanceService;

import java.io.IOException;

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
@RequestMapping("api/Employee/")
public class MonthlyAllowanceRestController {

    @Autowired
    MonthlyAllowanceService service;

    @GetMapping("MonthlyAllowance")
    public ResponseEntity index(@RequestParam(required = false) Long allowance, @RequestParam(required = false) Long empId, @RequestParam(required = false) Long year, @RequestParam(required = false) Long month) {
        return service.getAll(empId, year, month, allowance);
    }

    @PostMapping("MonthlyAllowance")
    public ResponseEntity doSave(@RequestBody MonthlyAllowance obj) throws IOException {
        return service.save(obj);
    }

    @DeleteMapping("MonthlyAllowance/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }

    @GetMapping("RegularAllowance")
    public ResponseEntity indexRegularAllowance(@RequestParam(required = false) Long allowance, @RequestParam(required = false) Long empId) {
        return service.getAll(empId, allowance);
    }

    @PostMapping("RegularAllowance")
    public ResponseEntity doSaveRegularAllowance(@RequestBody RegularAllowance obj) throws IOException {
        return service.save(obj);
    }

    @DeleteMapping("RegularAllowance/{id}")
    public ResponseEntity doDeleteRegularAllowance(@PathVariable String id) {
        return service.deleteRegularAllowance(id);
    }
}
