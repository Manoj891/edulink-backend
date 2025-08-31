package com.ms.ware.online.solution.school.controller.swagger.setup;

import com.ms.ware.online.solution.school.entity.setup.BusStationTime;
import com.ms.ware.online.solution.school.service.setup.BusStationTimeService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.ms.ware.online.solution.school.config.DB;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Setup/BusStationTime")
public class BusStationTimeRestController {

    @Autowired
    BusStationTimeService service;
    @Autowired
    private DB db;
    @GetMapping
    public ResponseEntity index(@RequestParam Long bus) {

        return service.getAll(bus);
    }

    @GetMapping("/Master")
    public Object indexMaster( ) {

        Map map = new HashMap();
       
        map.put("bus", db.getRecord("SELECT ID id,CONCAT(BUS_NO, ' ',BUS_NAME) name FROM bus_master"));
        map.put("station", db.getRecord("SELECT ID id,NAME name FROM bus_station_master ORDER BY name"));
        return map;
    }

    @PostMapping
    public ResponseEntity doSave(@RequestBody BusStationTime obj) throws IOException {
        return service.save(obj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
