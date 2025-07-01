/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpWorkingHour;
import com.ms.ware.online.solution.school.service.employee.EmpWorkingHourService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Employee/WorkingHour")
public class EmpWorkingHourRestController {

    @Autowired
    EmpWorkingHourService service;

    @GetMapping
    public Object index(@RequestParam(required = false) Long empId) {
        return service.index(empId);
    }

    @PostMapping
    public Object doSave(@RequestBody List<EmpWorkingHour> list) {
        return service.doSave(list);
    }
}
