/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.dao.employee.EmployeeInfoDao;
import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import com.ms.ware.online.solution.school.entity.employee.OnlineVacancy;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Employee/")
public class EmployeeHireFireRestController {
    @Autowired
    private Message message;
    @Autowired
    private EmployeeInfoDao da;

    
    @PostMapping("Hire/{id}")
    public Object hire(@PathVariable Long id) {
        
        List<OnlineVacancy> vacancys = da.getOnlineVacancy("from OnlineVacancy where id=" + id);
        if (vacancys.isEmpty()) {
            return message.respondWithError("Record Not found");
        }
        OnlineVacancy v = vacancys.get(0);
        EmployeeInfo obj = new EmployeeInfo();
        String sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM employee_info";
        message.map = (Map) da.getRecord(sql).get(0);
        obj.setId(Long.parseLong(message.map.get("id").toString()));
        obj.setAlternativeEmail(v.getAlternativeEmail());
        obj.setAlternativeMobile(v.getAlternativeMobile());
        obj.setCitizenshipNo(v.getCitizenshipNo());
        obj.setCode("");
        obj.setDepartment(v.getDepartment());
        obj.setDistrict(v.getDistrict());
        obj.setDob(v.getDob());
        obj.setEmail(v.getEmail());
        obj.setEmergencyContactEmail(v.getEmergencyContactEmail());
        obj.setEmergencyContactNo(v.getEmergencyContactNo());
        obj.setEmergencyContactPerson(v.getEmergencyContactPerson());
        obj.setEmpLevel(v.getEmpLevel());
        obj.setEmpType(v.getEmpType());
        obj.setFirstName(v.getFirstName());
        obj.setGender(v.getGender());
        obj.setHouseNo(v.getHouseNo());
        obj.setJoinDate(new Date());
        obj.setLastName(v.getLastName());
        obj.setMaritalStatus(v.getMaritalStatus());
        obj.setMiddleName(v.getMiddleName());
        obj.setMunicipal(v.getMunicipal());
        obj.setPanNo(v.getPanNo());
        obj.setProvince(v.getProvince());
        obj.setSpecialization(v.getSpecialization());
        obj.setTemporaryAddress(v.getTemporaryAddress());
        obj.setWardNo(v.getWardNo());
        obj.setWorkingStatus("Y");
        obj.setMobile(v.getMobile());
        int row = da.save(obj);
        v.setHireFireDate(new Date());
        v.setHireFireStatus("Hire");
        da.update(v);
        String msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }
    
    @PostMapping("Fire/{id}")
    public Object fire(@PathVariable Long id) {
        
        List<OnlineVacancy> vacancys = da.getOnlineVacancy("from OnlineVacancy where id=" + id);
        if (vacancys.isEmpty()) {
            return message.respondWithError("Record Not found");
        }
        OnlineVacancy v = vacancys.get(0);
        v.setHireFireDate(new Date());
        v.setHireFireStatus("Fire");
        int row = da.update(v);
        if (row == 1) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        }
        return ResponseEntity.status(400).body(message.respondWithError(da.getMsg()));
    }
}
