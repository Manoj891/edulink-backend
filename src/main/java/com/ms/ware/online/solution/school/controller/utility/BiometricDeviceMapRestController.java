/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.utility;


import com.ms.ware.online.solution.school.entity.utility.BiometricDeviceMap;
import com.ms.ware.online.solution.school.service.utility.BiometricDeviceMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/utility/biometric-device-map")
public class BiometricDeviceMapRestController {

    @Autowired
    private BiometricDeviceMapService da;

    @GetMapping("/{userType}")
    public ResponseEntity<String> fatchData(@PathVariable String userType) {
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Success\"}");
    }

    @GetMapping
    public ResponseEntity<List<BiometricDeviceMap>> index(@RequestParam(required = false) String type) {
        return ResponseEntity.status(HttpStatus.OK).body(da.findAll(type));
    }


    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody BiometricDeviceMap obj) {
        da.save(obj);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Success\"}");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody BiometricDeviceMap obj) {
        da.update(obj);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Success\"}");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable String id) {
        da.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Success\"}");

    }

}
