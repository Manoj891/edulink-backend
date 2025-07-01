package com.ms.ware.online.solution.school.service.setup;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.setup.HostalTypeMaster;

public interface HostalTypeMasterService {

    public ResponseEntity getAll();

    public ResponseEntity save(HostalTypeMaster obj);

    public ResponseEntity update(HostalTypeMaster obj,long id);

    public ResponseEntity delete(String id);

}