package com.ms.ware.online.solution.school.service.setup;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.setup.BusStationMaster;

public interface BusStationMasterService {

    public ResponseEntity getAll();

    public ResponseEntity save(BusStationMaster obj);

    public ResponseEntity update(BusStationMaster obj,long id);

    public ResponseEntity delete(String id);

}