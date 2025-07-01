/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.utility;


import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.NoticeBoardDao;
import com.ms.ware.online.solution.school.entity.utility.NoticeBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class NoticeBoardServiceImp implements NoticeBoardService {

    @Autowired
    NoticeBoardDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Override
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(da.getAll("from NoticeBoard order by enterDateAd desc"));
    }

    @Override
    public ResponseEntity save(NoticeBoard obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String msg = "", sql;
        String adDate = DateConverted.bsToAd(obj.getEnterDate());
        if (adDate.length() != 10) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid Date"));
        }
        int year = Integer.parseInt(obj.getEnterDate().substring(0, 4));
        try {
            sql = "SELECT ifnull(MAX(SN),0)+1 AS id FROM notice_board WHERE YEAR=" + year;
            message.map = (Map) da.getRecord(sql).get(0);
            int sn = Integer.parseInt(message.map.get("id").toString());
            if (sn < 10) {
                obj.setId(Long.parseLong(year + "000" + sn));
            }
            if (sn < 100) {
                obj.setId(Long.parseLong(year + "00" + sn));
            }
            if (sn < 1000) {
                obj.setId(Long.parseLong(year + "0" + sn));
            } else {
                obj.setId(Long.parseLong(year + "" + sn));
            }
            obj.setYear(year);
            obj.setSn(sn);
            obj.setEnterBy(td.getUserName());
            obj.setEnterDateAd(adDate);
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
    public ResponseEntity update(NoticeBoard obj, long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        int row;
        String msg = "";

        System.out.println(obj.getEnterDate());
        String adDate = DateConverted.bsToAd(obj.getEnterDate());
        if (adDate.length() != 10) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid Date"));
        }
        System.out.println(adDate);
        obj.setEnterDateAd(adDate);
        obj.setId(id);
        obj.setEnterBy(td.getUserName());
        row = da.update(obj);
        System.out.println(obj);
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
        sql = "DELETE FROM notice_board WHERE ID='" + id + "'";
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
