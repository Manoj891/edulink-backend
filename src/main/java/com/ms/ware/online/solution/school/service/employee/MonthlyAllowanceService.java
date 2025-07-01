package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.entity.employee.RegularAllowance;
import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.employee.MonthlyAllowance;

public interface MonthlyAllowanceService {

    public ResponseEntity getAll(Long empId, Long year, Long month, Long allowance);

    public ResponseEntity getAll(Long empId, Long allowance);

    public ResponseEntity save(MonthlyAllowance obj);

    public ResponseEntity save(RegularAllowance obj);

    public ResponseEntity delete(String id);

    public ResponseEntity deleteRegularAllowance(String id);

}
