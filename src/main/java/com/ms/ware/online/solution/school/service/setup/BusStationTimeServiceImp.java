/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.setup.BusStationTimeDao;
import com.ms.ware.online.solution.school.entity.setup.BusStationTime;
import com.ms.ware.online.solution.school.entity.setup.BusStationTimePK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

@Service
public class BusStationTimeServiceImp implements BusStationTimeService {

    @Autowired
    BusStationTimeDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Override
    public ResponseEntity getAll(Long bus) {
        return ResponseEntity.status(200).body(da.getAll("from BusStationTime where bus=" + bus));
    }

    @Override
    public ResponseEntity save(BusStationTime obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String msg = "";
        try {
            obj.setPk(new BusStationTimePK(obj.getBus(), obj.getStation(), obj.getGoReturn()));
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
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "", sql;
        sql = "DELETE FROM bus_station_time WHERE CONCAT(BUS,'-',STATION)='" + id + "'";
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
