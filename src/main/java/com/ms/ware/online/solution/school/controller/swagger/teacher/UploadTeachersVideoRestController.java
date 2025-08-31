/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.teacher;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.teacherpanel.TeachersHomeworkDao;
import com.ms.ware.online.solution.school.entity.teacherpanel.UploadTeachersVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ms.ware.online.solution.school.config.Message;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/TeacherPanel/UploadVideo")
public class UploadTeachersVideoRestController {
    @Autowired
    private AuthenticationFacade facade;
   
    @Autowired
    TeachersHomeworkDao da;


    @GetMapping
    public Object index(@RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long subject) {
        Message message = new Message();
         AuthenticatedUser td = facade.getAuthentication();;
        return da.getAll("from UploadTeachersVideo where teacher='" + td.getUserId() + "' and academicYear=ifnull(" + academicYear + ",academicYear) and subjectGroup=ifnull(" + subjectGroup + ",subjectGroup) and program=ifnull(" + program + ",program) and classId=ifnull(" + classId + ",classId) and subject=IFNULL(" + subject + ",subject)");
    }

    @PostMapping
    public Object doSave(@RequestBody UploadTeachersVideo obj) throws IOException {
        Message message = new Message();
         AuthenticatedUser td = facade.getAuthentication();;
        String msg = "", sql;
        try {
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM upload_teachers_video";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            obj.setTeacher(Long.parseLong(td.getUserId()));
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

    @PutMapping
    public Object doUpdate(@RequestBody UploadTeachersVideo obj) throws IOException {
        System.out.println(obj);
        Message message = new Message();
         AuthenticatedUser td = facade.getAuthentication();;

        String msg = "";
        try {
            obj.setTeacher(Long.parseLong(td.getUserId()));
            int row = da.update(obj);
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
    public Object doDelete(@PathVariable Long id) {
        Message message = new Message();
         AuthenticatedUser td = facade.getAuthentication();;
        String msg = "";
        try {

            int row = da.delete("delete from upload_teachers_video where id='" + id + "' and TEACHER='" + td.getUserId() + "'");
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
}
