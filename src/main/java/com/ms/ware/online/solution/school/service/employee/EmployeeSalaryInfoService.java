package com.ms.ware.online.solution.school.service.employee;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.employee.EmployeeSalaryInfo;

public interface EmployeeSalaryInfoService {

    public ResponseEntity getAll();

    public ResponseEntity save(EmployeeSalaryInfo obj);

    public ResponseEntity update(EmployeeSalaryInfo obj,long id);

    public ResponseEntity delete(String id);

}