package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import com.ms.ware.online.solution.school.service.employee.EmployeeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/Employee/EmployeeInfo")
public class EmployeeInfoRestController {
    @Autowired
    private DB db;
    @Autowired
    private EmployeeInfoService service;

    @GetMapping
    public ResponseEntity<List<EmployeeInfo>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }

    @GetMapping("/teacher")
    public ResponseEntity<?> findTeacher() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findTeacher());
    }

    @GetMapping("/find")
    public ResponseEntity<List<Map<String, Object>>> find() {
        String sql = "SELECT id,CONCAT(first_name,' ',middle_name,' ',last_name) AS name,mobile from employee_info  order by name";
        return ResponseEntity.status(HttpStatus.OK).body(db.getRecord(sql));
    }

    @GetMapping("/IdCard")
    public ResponseEntity<List<Map<String, Object>>> idCard() {

        String sql = "SELECT ID id,CONCAT(first_name,' ',middle_name,' ',last_name) name,CODE code FROM employee_info";
        return ResponseEntity.status(200).body(db.getRecord(sql));
    }

    @GetMapping("/IdCard/{ids}")
    public ResponseEntity<Map<String, Object>> idCard(@PathVariable String ids) {
       
        String sql = "SELECT `NAME` name,`MUNICIPAL` municipal,`DISTRICT` district,`WARD_NO` wardNo,`TEL` tel,address FROM organization_master";
        Map<String, Object> map = db.getRecord(sql).get(0);
        sql = "SELECT E.mobile,E.dob,CONCAT(first_name,' ',middle_name,' ',last_name) As empName,code,D.`NAME` department,IFNULL(E.PHOTO,'') photo,L.`NAME` AS empLevel FROM employee_info E,department_master D,emp_level_master L  WHERE E.department=D.`ID` AND E.emp_level=L.`ID` AND E.ID IN(" + ids + ")";
        map.put("data", db.getRecord(sql));
        return ResponseEntity.status(200).body(map);
    }

    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody EmployeeInfo obj) throws IOException {
        service.save(obj);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"success\"}");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> doUpdate(@RequestBody EmployeeInfo obj, @PathVariable long id) throws IOException {
        service.update(obj, id);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"success\"}");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> doDeviceIdMap(@RequestParam Long biometricCompanyId, @RequestParam Long biometricEmpId, @PathVariable long id) throws IOException {
        service.doDeviceIdMap(id, biometricCompanyId, biometricEmpId);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"success\"}");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"success\"}");
    }
}
