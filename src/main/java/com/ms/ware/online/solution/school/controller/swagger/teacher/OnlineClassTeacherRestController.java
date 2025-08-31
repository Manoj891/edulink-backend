/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.teacher;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.entity.teacherpanel.OnlineClass;
import com.ms.ware.online.solution.school.entity.teacherpanel.OnlineClassPK;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.io.IOException;

@RestController
@RequestMapping("api/Student/OnlineClass")
public class OnlineClassTeacherRestController {
    @Autowired
    private AuthenticationFacade facade;
    @GetMapping
    public Object getOnlineClass(@RequestParam(required = false) Long acadeicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        
        String sql = "SELECT ACADEMIC_YEAR academicYear,CLASS_ID classId,PROGRAM program,SUBJECT_GROUP subjectGroup,SUBJECT subject,LINK link,START_TIME startTime,END_TIME endTime,S.NAME subjectName,C.NAME className FROM online_class O,subject_master S ,class_master C WHERE O.SUBJECT=S.ID AND C.ID=O.CLASS_ID AND ACADEMIC_YEAR=IFNULL(" + acadeicYear + ",ACADEMIC_YEAR) AND CLASS_ID=IFNULL(" + classId + ",CLASS_ID) AND PROGRAM=IFNULL(" + program + ",PROGRAM) AND SUBJECT_GROUP=IFNULL(" + subjectGroup + ",SUBJECT_GROUP) AND TEACHER='" + td.getUserId() + "'";
        return new DB().getRecord(sql);
    }

    @PostMapping
    public Object doSave(@RequestBody OnlineClass obj) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        
        String msg = "";
        int row;
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        msg = "";
        row = 1;
        try {
            Long academicYear = obj.getAcademicYear().getId();
            Long program = obj.getProgram().getId();
            Long classId = obj.getClassId().getId();
            Long subjectGroup = obj.getSubjectGroup().getId();
            Long subject = obj.getSubject().getId();
            obj.setPk(new OnlineClassPK(academicYear, program, classId, subjectGroup, subject));
            obj.setTeacher(Long.parseLong(td.getUserId()));
            session.saveOrUpdate(obj);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
              session.close();
            throw new PersistenceException();
     
        }
        try {
            session.close();
        } catch (HibernateException e) {
        }
        if (row == 1) {
            return message.respondWithMessage("Success");
        }
        return message.respondWithError(msg);
    }
}
