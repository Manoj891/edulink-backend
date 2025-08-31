/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.exam.GradingSystemDao;
import com.ms.ware.online.solution.school.entity.exam.GradingSystemTwo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradingSystemServiceImpTwo implements GradingSystemServiceTwo {
    @Autowired
    private Message message;
    @Autowired
    private GradingSystemDao da;

    String msg = "", sql;
    int row;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Object getAll() {
        sql = "SELECT ID id,GRADE grade,GPA gpa,RANG_FROM rangFrom,REMARK remark,GET_BS_DATE(EFFECTIVE_DATE_FROM) effectiveDateFrom,IFNULL(GET_BS_DATE(EFFECTIVE_DATE_TO),'') effectiveDateTo FROM grading_system_two WHERE EFFECTIVE_DATE_TO is null ORDER BY RANG_FROM desc";
        List list = da.getRecord(sql);
        sql = "SELECT ID id,GRADE grade,GPA gpa,RANG_FROM rangFrom,REMARK remark,GET_BS_DATE(EFFECTIVE_DATE_FROM) effectiveDateFrom,IFNULL(GET_BS_DATE(EFFECTIVE_DATE_TO),'') effectiveDateTo FROM grading_system_two WHERE EFFECTIVE_DATE_TO is not null ORDER BY RANG_FROM desc";

        message.list = da.getRecord(sql);
        for (int i = 0; i < message.list.size(); i++) {
            list.add(message.list.get(i));
        }
        return list;
    }

    @Override
    public Object save(GradingSystemTwo obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            try {
                sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM grading_system_two";
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
    public Object update(GradingSystemTwo obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
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
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM grading_system_two WHERE ID IN (" + id + ")";
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
