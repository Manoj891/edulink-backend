package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpLeaveDetail;
import com.ms.ware.online.solution.school.entity.employee.LeaveApplication;
import com.ms.ware.online.solution.school.service.employee.LeaveApplicationService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Employee/LeaveApplication")
public class LeaveApplicationRestController {

    @Autowired
    private LeaveApplicationService service;

    @GetMapping("/report")
    public ResponseEntity report(@RequestParam Long empId, @RequestParam String leaveDateFrom, @RequestParam String leaveDateTo) {

        return service.report(empId, leaveDateFrom, leaveDateTo);
    }

    @GetMapping
    public ResponseEntity index() {

        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity index(@PathVariable Long id) {

        return service.getAll(id);
    }

    @PostMapping
    public ResponseEntity doSave(@RequestBody LeaveApplication obj) throws IOException {
        return service.save(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity doUpdate(@RequestBody LeaveApplication obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity leaveApprove(@RequestBody List<EmpLeaveDetail> obj, @PathVariable long id) throws IOException {
        return service.leaveApprove(obj, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
