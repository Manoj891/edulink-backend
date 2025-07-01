package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.entity.student.CertificateData;
import com.ms.ware.online.solution.school.service.student.CertificateDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Student/Certificate")
public class CertificateDataController {
    @Autowired
    private CertificateDataService service;

    @GetMapping("/para")
    public ResponseEntity<Map<String, Object>> getInit() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getInit());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getStudentInfo(@RequestParam String regNo) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getStudentInfo(regNo));
    }

    @GetMapping
    public ResponseEntity<List<CertificateData>> index(@RequestParam Long academicYear, @RequestParam(required = false) String regNo) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(academicYear, regNo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> index(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findOne(id));
    }

    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody CertificateData obj) {
        return ResponseEntity.status(HttpStatus.OK).body(service.save(obj));
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> doUpdate(@RequestBody CertificateData obj, @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(obj, id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(id));
    }

}
