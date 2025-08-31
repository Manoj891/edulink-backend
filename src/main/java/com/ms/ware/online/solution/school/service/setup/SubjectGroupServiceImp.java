/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.setup;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.setup.SubjectGroupDao;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SubjectGroupServiceImp implements SubjectGroupService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    SubjectGroupDao da;
    Message message = new Message();
    String msg = "", sql;
    int row;

    @Override
    public Object getAll() {
        sql = "from SubjectGroup";
        return da.getAll(sql);
    }

    @Override
    public Object save(SubjectGroup obj) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM subject_group";
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
    public Object update(SubjectGroup obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
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
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM subject_group WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object getAll(long id, Long program, Long classId) {
        return da.getAll("from SubjectGroupDetail where subjectGroup=" + id + " and program=ifnull(" + program + ",program)  and classId=ifnull(" + classId + ",classId) ");
    }
}
