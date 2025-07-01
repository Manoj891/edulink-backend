package com.ms.ware.online.solution.school.service.setup;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.setup.BusMaster;

public interface BusMasterService {

    public ResponseEntity getAll();

    public ResponseEntity save(BusMaster obj);

    public ResponseEntity update(BusMaster obj,long id);

    public ResponseEntity delete(String id);

}