/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.employee;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.employee.EmployeeSalaryInfoDao;
import com.ms.ware.online.solution.school.entity.employee.EmployeeSalaryInfo;
import com.ms.ware.online.solution.school.config.Message;
import java.util.Map;

import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class EmployeeSalaryInfoServiceImp implements EmployeeSalaryInfoService {

    @Autowired
    EmployeeSalaryInfoDao da;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(da.getAll("from EmployeeSalaryInfo"));
    }

    @Override
    public ResponseEntity save(EmployeeSalaryInfo obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        pfCitValidation(obj, message);
        String msg = "", sql;
        try {
            long empId = obj.getEmpId().getId();
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM employee_salary_info";
                message.map = (Map) da.getRecord(sql).get(0);
                obj.setId(Long.parseLong(message.map.get("id").toString()));
            } catch (Exception e) {
                return ResponseEntity.status(200).body(message.respondWithError("connection error or invalid table name"));
            }
            obj.setStatus("Y");
            int row = da.save(obj);
            sql = "update employee_salary_info set EFFECTIVE_DATE_TO='" + DateConverted.bsToAd(obj.getEffectiveDateFrom()) + "' WHERE EFFECTIVE_DATE_TO IS NULL AND EMP_ID='" + empId + "' AND ID!=" + obj.getId();
            da.delete(sql);
            sql = "update employee_salary_info set STATUS='N' WHERE EMP_ID='" + empId + "' AND ID!=" + obj.getId();
            da.delete(sql);
            msg = da.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity update(EmployeeSalaryInfo obj, long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "";
        obj.setId(id);
        pfCitValidation(obj, message);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    @Override
    public ResponseEntity delete(String id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "", sql;
        sql = "DELETE FROM employee_salary_info WHERE ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    void pfCitValidation(EmployeeSalaryInfo obj, Message message) {

        if (obj.getCitType().equalsIgnoreCase("P")) {
            if (obj.getCit() > 10 || obj.getEmployerCit() > 10) {
                message.errorMessage("CIT not support >10 %");
            }
        } else {
            if (obj.getCit() > obj.getBasicSalary() || obj.getEmployerCit() > obj.getBasicSalary()) {
                message.errorMessage("CIT not support > " + obj.getBasicSalary());
            }
        }


        if (obj.getPfType().equalsIgnoreCase("P")) {
            if (obj.getPf() > 10 || obj.getEmployerPf() > 10) {
                message.errorMessage("PF not support >10 %");
            }
        } else {
            if (obj.getPf() > obj.getBasicSalary() || obj.getEmployerPf() > obj.getBasicSalary()) {
                message.errorMessage("PF not support > " + obj.getBasicSalary());
            }
        }

        if (obj.getEmployeesFundType().equalsIgnoreCase("P")) {
            if (obj.getEmployeesFund() > 10 || obj.getEmployerEmployeesFund() > 10) {
                message.errorMessage("Employees not support >10 %");
            }
        } else {
            if (obj.getEmployeesFund() > obj.getBasicSalary() || obj.getEmployerEmployeesFund() > obj.getBasicSalary()) {
                message.errorMessage("Employees not support > " + obj.getBasicSalary());
            }
        }
    }
}
