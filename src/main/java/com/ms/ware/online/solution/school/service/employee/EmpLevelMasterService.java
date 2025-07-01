package com.ms.ware.online.solution.school.service.employee;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.employee.EmpLevelMaster;

public interface EmpLevelMasterService {

    public ResponseEntity getAll();

    public ResponseEntity save(EmpLevelMaster obj);

    public ResponseEntity update(EmpLevelMaster obj,long id);

    public ResponseEntity delete(String id);

}