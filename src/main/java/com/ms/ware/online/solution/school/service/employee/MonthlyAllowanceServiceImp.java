/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.employee;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.employee.MonthlyAllowanceDao;
import com.ms.ware.online.solution.school.entity.employee.MonthlyAllowance;
import com.ms.ware.online.solution.school.entity.employee.MonthlyAllowancePK;
import com.ms.ware.online.solution.school.entity.employee.RegularAllowance;
import com.ms.ware.online.solution.school.entity.employee.RegularAllowancePK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MonthlyAllowanceServiceImp implements MonthlyAllowanceService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    MonthlyAllowanceDao da;

    @Override
    public ResponseEntity getAll(Long empId, Long year, Long month, Long allowance) {
        String MM = null;
        if (month == null) {
            MM = null;
        } else if (month < 10) {
            MM = "'0" + month + "'";
        } else {
            MM = "'" + month + "'";
        }
        return ResponseEntity.status(200).body(da.getAll("from MonthlyAllowance where empId=ifnull(" + empId + ",empId) and year=ifnull(" + year + ",year) and month=ifnull(" + MM + ",month) and allowance=ifnull(" + allowance + ",allowance) "));
    }

    @Override
    public ResponseEntity getAll(Long empId, Long allowance) {
        return ResponseEntity.status(200).body(da.getAll("from RegularAllowance where empId=ifnull(" + empId + ",empId)  and allowance=ifnull(" + allowance + ",allowance) "));

    }

    @Override
    public ResponseEntity save(MonthlyAllowance obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        String msg = "";
        try {
            int year = obj.getYear();
            int month = obj.getMonthVal();
            if (year > 2090 || year < 2075) {
                return ResponseEntity.status(200).body(message.respondWithError("Invalid Year"));
            }
            if (month > 12 || month < 1) {
                return ResponseEntity.status(200).body(message.respondWithError("Invalid Month"));
            }
            obj.setPk(new MonthlyAllowancePK(obj.getEmpId(), obj.getYear(), obj.getMonthVal(), obj.getAllowance()));
            obj.setId(UUID.randomUUID().toString());
            int row = da.save(obj);
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
    public ResponseEntity save(RegularAllowance obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        String msg = "";
        try {
            obj.setPk(new RegularAllowancePK(obj.getEmpId(), obj.getAllowance()));
            obj.setId(UUID.randomUUID().toString());
            int row = da.save(obj);
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
    public ResponseEntity delete(String id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "", sql;
        sql = "DELETE FROM monthly_allowance WHERE ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    @Override
    public ResponseEntity deleteRegularAllowance(String id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "", sql;

        sql = "DELETE FROM regular_allowance WHERE ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));

    }


}
