/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.setup.HostelTypeMasterDao;
import com.ms.ware.online.solution.school.entity.setup.HostalTypeMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HostalTypeMasterServiceImp implements HostalTypeMasterService {

    @Autowired
    private HostelTypeMasterDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private Message message;
    @Override
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(da.getAll("from HostalTypeMaster"));
    }

    @Override
    public ResponseEntity save(HostalTypeMaster obj) {
        
        AuthenticatedUser td = facade.getAuthentication();
        String msg = "", sql;
        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM hostal_type_master";
                message.map = (Map) da.getRecord(sql).get(0);
                obj.setId(Long.parseLong(message.map.get("id").toString()));
            } catch (Exception e) {
                return ResponseEntity.status(200).body(message.respondWithError("connection error or invalid table name"));
            }
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
    public ResponseEntity update(HostalTypeMaster obj, long id) {
        
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "";
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            if (obj.getEffect().equalsIgnoreCase("Y")) {
                String sql = "UPDATE school_hostal SET MONTHLY_CHARGE='" + obj.getAmount() + "' WHERE END_DATE IS NULL AND HOSTEL_TYPE='" + obj.getId() + "'";
                row = da.delete(sql);
                if (row > 0) {
                    return ResponseEntity.status(200).body(message.respondWithMessage(row + " Row Effected Successfully!!"));
                }
            }
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
        
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "", sql;
        sql = "DELETE FROM hostal_type_master WHERE ID='" + id + "'";
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
