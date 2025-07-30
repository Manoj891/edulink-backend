package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.dto.BiometricData;
import com.ms.ware.online.solution.school.dto.BiometricReq;
import com.ms.ware.online.solution.school.service.employee.BiometricDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class BiometricDataRestController {

    @Autowired
    private BiometricDataService service;

    @GetMapping("api/Employee/BiometricData")
    public ResponseEntity<List<Map<String, Object>>> getAttendance(@RequestParam(required = false) Long employee, @RequestParam String year, @RequestParam String month) {
        return ResponseEntity.status(200).body(service.getAttendance(employee, year, month));
    }

    @GetMapping("/api/Employee/BiometricData/{date}")
    public ResponseEntity<List<Map<String, Object>>> index(@PathVariable String date) {
        return ResponseEntity.status(200).body(service.getAll(date));
    }

    @GetMapping("public/api/biometric-data")
    public ResponseEntity<String> getMap() {
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @PostMapping("public/api/biometric-data")
    public ResponseEntity<List<String>> doSave( @RequestBody BiometricData obj) {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(obj));
    }

}
