/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */

package com.ms.ware.online.solution.school.service.utility;


import com.ms.ware.online.solution.school.config.EmailService;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.SenderEmailDao;
import com.ms.ware.online.solution.school.entity.utility.SenderEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class SenderEmailServiceImp implements SenderEmailService {

    @Autowired
    SenderEmailDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private EmailService service;

    @Override
    public ResponseEntity getAll() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        } else if (!td.getUserName().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        return ResponseEntity.status(200).body(da.getAll("from SenderEmail"));
    }

    @Override
    public ResponseEntity save(SenderEmail obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();

        String msg = "", sql;
        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM sender_email";
                message.map = da.getRecord(sql).get(0);
                obj.setId(Long.parseLong(message.map.get("id").toString()));
            } catch (Exception e) {
                return ResponseEntity.status(200).body(message.respondWithError("connection error or invalid table name"));
            }
            int row = da.save(obj);
            service.init();
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
    public ResponseEntity update(SenderEmail obj, long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        } else if (!td.getUserName().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "";
        obj.setId(id);
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
        } else if (!td.getUserName().equalsIgnoreCase("ADMIN")) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "", sql;
        sql = "DELETE FROM sender_email WHERE ID='" + id + "'";
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
