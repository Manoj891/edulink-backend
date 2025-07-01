/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.setup;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.setup.MunicipalMasterDao;
import com.ms.ware.online.solution.school.entity.setup.MunicipalMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MunicipalMasterServiceImp implements MunicipalMasterService {

    @Autowired
    MunicipalMasterDao da;
    @Autowired
    private AuthenticationFacade facade;
    Message message = new Message();
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(String district) {
        return da.getAll("from MunicipalMaster where district='" + district + "'");
    }

    @Override
    public Object save(MunicipalMaster obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equalsIgnoreCase("ADM")) return message.respondWithError("invalid Access");

        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM municipal_master";
                message.map = (Map) da.getRecord(sql).get(0);
                obj.setId(Long.parseLong(message.map.get("id").toString()));
            } catch (Exception e) {
                return message.respondWithError("connection error or invalid table name");
            }
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(MunicipalMaster obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }else if (!td.getUserType().equalsIgnoreCase("ADM")) return message.respondWithError("invalid Access");
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object delete(String id) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }else if (!td.getUserType().equalsIgnoreCase("ADM")) return message.respondWithError("invalid Access");

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM municipal_master WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }
}
