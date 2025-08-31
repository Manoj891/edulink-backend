package com.ms.ware.online.solution.school.controller.swagger.panel.student;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.PreviousEducationDao;
import com.ms.ware.online.solution.school.entity.student.PreviousEducation;
import com.ms.ware.online.solution.school.entity.student.PreviousEducationPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/panel/student/PreviousEducation")
public class StuPreviousEducationRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    PreviousEducationDao da;

    @GetMapping
    public Object index() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        return ResponseEntity.status(200).body(da.getAll("from PreviousEducation where regNo=" + td.getUserId()));
    }

    @PostMapping
    public ResponseEntity doSave(@RequestBody PreviousEducation obj) throws IOException {

        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        String msg = "";
        try {
            obj.setPk(new PreviousEducationPK(Long.parseLong(td.getUserId()), obj.getEducation()));
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

    @DeleteMapping("/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {

        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        int row;
        String msg = "", sql;
        sql = "DELETE FROM previous_education WHERE CONCAT(REG_NO,'-',EDUCATION)='" + id + "' AND REG_NO='" + td.getUserId() + "'";
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
