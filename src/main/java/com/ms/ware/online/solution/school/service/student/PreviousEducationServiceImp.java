/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.student;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.PreviousEducationDao;
import com.ms.ware.online.solution.school.entity.student.PreviousEducation;
import com.ms.ware.online.solution.school.entity.student.PreviousEducationPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PreviousEducationServiceImp implements PreviousEducationService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private PreviousEducationDao da;
    @Autowired
    private Message message;

    @Override
    public ResponseEntity<Map<String, Object>> getAll(Long regNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", da.getAll("from PreviousEducation where regNo=" + regNo));
        map.put("student", da.getRecord("select ifnull(photo,'') photo,ifnull(certificate01,'') certificate01,ifnull(certificate02,'') certificate02 from student_info where id=" + regNo).get(0));
        return ResponseEntity.status(200).body(map);
    }

    @Override
    public ResponseEntity save(PreviousEducation obj) {

        AuthenticatedUser td = facade.getAuthentication();
        String msg = "";
        try {
            obj.setPk(new PreviousEducationPK(obj.getRegNo(), obj.getEducation()));
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

        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "", sql;
        sql = "DELETE FROM previous_education WHERE CONCAT(REG_NO,'-',EDUCATION)='" + id + "'";
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
