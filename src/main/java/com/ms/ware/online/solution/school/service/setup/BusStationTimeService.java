package com.ms.ware.online.solution.school.service.setup;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.setup.BusStationTime;

public interface BusStationTimeService {

    public ResponseEntity getAll(Long bus);

    public ResponseEntity save(BusStationTime obj);

    public ResponseEntity delete(String id);

}
