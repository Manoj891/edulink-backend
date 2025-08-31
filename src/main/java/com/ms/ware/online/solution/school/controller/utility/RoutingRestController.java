/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.utility;


import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.RoutingDao;
import com.ms.ware.online.solution.school.entity.utility.Routing;
import com.ms.ware.online.solution.school.entity.utility.RoutingPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/Utility/Routing")
public class RoutingRestController {
    @Autowired
    private DB db;
    @Autowired
    private RoutingDao da;
    @Autowired
    private Message message;
    @Autowired
    private AuthenticationFacade facade;

    @GetMapping
    public Object index() {

        return da.getAll("from Routing");
    }

    @GetMapping("/Teacher")
    public Object findTeacher(@RequestParam Long academicYear, @RequestParam Long subjectGroup, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subject) {
        return db.getRecord("SELECT CONCAT(I.first_name,' ',last_name,'')  name,T.TEACHER id FROM teachers_class_subject T,employee_info I WHERE T.TEACHER=I.ID AND ACADEMIC_YEAR='" + academicYear + "' AND SUBJECT_GROUP='" + subjectGroup + "' AND PROGRAM='" + program + "' AND CLASS_ID='" + classId + "' AND SUBJECT='" + subject + "'");
    }

    @PostMapping
    public Object doSave(@RequestBody Routing obj) throws IOException {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        int row;
        String msg = "";
        try {
            obj.setPk(new RoutingPK(obj.getAcademicYear().getId(), obj.getSubjectGroup().getId(), obj.getProgram().getId(), obj.getClassId().getId(), obj.getSection(), obj.getSubject().getId(), obj.getTeacher().getId()));
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


    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        int row;
        String msg = "";
        try {
            String sql = "DELETE FROM routing WHERE CONCAT(ACADEMIC_YEAR,'-',SUBJECT_GROUP,'-',PROGRAM,'-',CLASS_ID,'-',SECTION,'-',SUBJECT,'-',TEACHER)='" + id + "';";
            row = da.delete(sql);
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

}
