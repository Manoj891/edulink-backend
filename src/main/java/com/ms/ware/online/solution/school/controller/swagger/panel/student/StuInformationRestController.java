/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/panel/student/Information")
public class StuInformationRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    StudentInfoDao da;
    @Autowired
    private DB db;
    @GetMapping
    public Object index() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        return da.getAll("from StudentInfo where id='" + td.getUserId() + "'").get(0);
    }

    @GetMapping("/ClassYear")
    public Object indexClass() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        Map map = new HashMap();
        map.put("class", da.getRecord("SELECT C.ID id,C.NAME name FROM class_transfer T,class_master C WHERE T.CLASS_ID=C.ID AND T.STUDENT_ID='" + td.getUserId() + "'"));
        map.put("academicYear", da.getRecord("SELECT Y.ID id,Y.YEAR name FROM class_transfer T,academic_year Y WHERE T.ACADEMIC_YEAR=Y.ID AND T.STUDENT_ID='" + td.getUserId() + "'"));
        return map;
    }

    @PostMapping
    public Object doSave(@RequestBody StudentInfo obj) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String sql = "SELECT ID id,ACADEMIC_YEAR academicYear,IFNULL(PROGRAM,1) program,CLASS_ID classId,ROLL_NO rollNo,IFNULL(SECTION,'') section,IFNULL(SUBJECT_GROUP,1) subjectGroup,IFNULL(STATUS,'Y') status FROM student_info WHERE ID='" + td.getUserId() + "'";
        Map<String, Object> map =  da.getRecord(sql).get(0);
        obj.setId(Long.parseLong(map.get("id").toString()));
        obj.setAcademicYear(Long.parseLong(map.get("academicYear").toString()));
        obj.setProgram(Long.parseLong(map.get("program").toString()));
        obj.setClassId(Long.parseLong(map.get("classId").toString()));
        obj.setSubjectGroup(Long.parseLong(map.get("subjectGroup").toString()));
        obj.setRollNo(Integer.parseInt(map.get("rollNo").toString()));
        obj.setStatus(map.get("status").toString());
        obj.setSection(map.get("section").toString());
        int row = da.update(obj);
        String msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        }
        return message.respondWithError(msg);
    }

    @PostMapping("/Photo")
    public Object doSave(@RequestParam MultipartFile photo) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (photo.getSize() < 100) {
            return message.respondWithError("Please provide file");
        }
        String studentId = td.getUserId();
        String location = message.getFilepath(DatabaseName.getDocumentUrl());
        String filePath = DatabaseName.getDocumentUrl() + "Document/Student/" + studentId.substring(0, 2);
        File f = new File(location + filePath);
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        String fileName = filePath + "/" + studentId + ".png";
        System.out.println(location + fileName);

        f = new File(location + fileName);
        photo.transferTo(f);
        da.delete("UPDATE student_info SET PHOTO='" + fileName + "' WHERE ID='" + studentId + "'");
        return message.respondWithMessage("Success");

    }

    @GetMapping("/ChangePassword")
    public Object doChangePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String rePassword) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        if (!newPassword.equals(rePassword)) {
            return message.respondWithError("Re Password not match");
        }
        String sql = "SELECT STU_PASSWORD dbPassword,CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + oldPassword + "'))))) AS oldPassword FROM student_info WHERE ID='" + td.getUserId() + "'";
       
        List list = db.getRecord(sql);
        if (list.isEmpty()) {
            return message.respondWithError("invalid token");
        }
        Map map = (Map) list.get(0);
        String dbPassword = map.get("dbPassword").toString();
        oldPassword = map.get("oldPassword").toString();
        if (!dbPassword.equals(oldPassword)) {
            return message.respondWithError("Old Password not match");
        }
        sql = "UPDATE student_info SET STU_PASSWORD=CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + newPassword + "'))))) WHERE ID='" + td.getUserId() + "'";
        int row = db.save(sql);
        String msg = db.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

}
