package com.ms.ware.online.solution.school.service.employee;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.employee.TaxSlab;

public interface TaxSlabService {

    public ResponseEntity getAll(Long fiscalYear);

    public ResponseEntity save(TaxSlab obj);

    public ResponseEntity update(TaxSlab obj, long id);

    public ResponseEntity delete(String id);

}
