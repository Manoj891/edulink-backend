package com.ms.ware.online.solution.school.service.employee;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.employee.DepartmentMaster;

public interface DepartmentMasterService {

    public ResponseEntity getAll();

    public ResponseEntity save(DepartmentMaster obj);

    public ResponseEntity update(DepartmentMaster obj,long id);

    public ResponseEntity delete(String id);

}