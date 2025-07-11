/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.setup.BusStationMasterDao;
import com.ms.ware.online.solution.school.entity.setup.BusStationMaster;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class BusStationMasterServiceImp implements BusStationMasterService {

    @Autowired
    private BusStationMasterDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Override
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(da.getAll("from BusStationMaster"));
    }

    @Override
    public ResponseEntity save(BusStationMaster obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String msg = "", sql;
        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM bus_station_master";
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
    public ResponseEntity update(BusStationMaster obj, long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "";
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            if (obj.getEffect().equalsIgnoreCase("Y")) {
                String sql = "UPDATE student_transportation SET MONTHLY_CHARGE='" + obj.getChargeAmount() + "' WHERE END_DATE IS NULL AND STATION='" + obj.getId() + "'";
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
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "", sql;
        sql = "DELETE FROM bus_station_master WHERE ID='" + id + "'";
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
