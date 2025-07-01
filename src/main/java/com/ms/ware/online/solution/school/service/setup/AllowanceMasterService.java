package com.ms.ware.online.solution.school.service.setup;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.setup.AllowanceMaster;

public interface AllowanceMasterService {

    public ResponseEntity getAll();

    public ResponseEntity save(AllowanceMaster obj);

    public ResponseEntity update(AllowanceMaster obj,long id);

    public ResponseEntity delete(String id);

}