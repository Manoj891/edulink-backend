package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.config.EmailService;
import com.ms.ware.online.solution.school.config.security.JwtHelper;
import com.ms.ware.online.solution.school.dto.StudentLoginReq;
import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/api/")
public class StudentLoginRestController {
    @Autowired
    private JwtHelper JwtHelper;
    @Autowired
    private DB db;
    @Autowired
    private EmailService es;

    @PostMapping("/student-login")
    public ResponseEntity<Map<String, Object>> doLogin(@RequestBody StudentLoginReq obj, HttpServletRequest request) {
        Map<String, Object> m, map = new HashMap<>();
        String userName = obj.getUsername().replace(" ", "");
        String userPassword = obj.getPassword().replace(" ", "");
        String sql = "SELECT PROGRAM program,CLASS_ID classId,SUBJECT_GROUP subjectGroup,ACADEMIC_YEAR academicYear,IFNULL(SECTION,'') session,CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + userPassword + "'))))) userPassword,IFNULL(STU_PASSWORD,'') stuPassword,ID stuId,STU_NAME stuName  FROM student_info WHERE ID='" + userName + "'";

        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            map.put("error", "invalid student id!!");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        m = l.get(0);

        String password = m.get("stuPassword").toString();
        if (password.isEmpty()) {
            map.put("error", "Password not generated!!");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        String stuName = m.get("stuName").toString();
        String stuId = m.get("stuId").toString();
        userPassword = m.get("userPassword").toString();
        if (!userPassword.equalsIgnoreCase(password)) {
            map.put("error", "invalid password!!");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }

        String token = JwtHelper.create(stuId, stuName, "STU", "", DatabaseName.getDocumentUrl(), "STU");
        map.put("program", m.get("program"));
        map.put("classId", m.get("classId"));
        map.put("subjectGroup", m.get("subjectGroup"));
        map.put("academicYear", m.get("academicYear"));
        map.put("session", m.get("session"));
        map.put("message", "Success");
        map.put("token", token);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @PostMapping("/password-generate")
    public ResponseEntity<String> studentPassword(HttpServletRequest request, @RequestParam String email, @RequestParam String stuId) {
        String serverAddress = request.getRequestURL().toString();
        String sql = "SELECT STU_NAME stuName,IFNULL(EMAIL,'') email FROM student_info WHERE ID='" + stuId + "' ";

        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) throw new CustomException("invalid student id!!");
        String dbEmail = l.get(0).get("email").toString();
        String stuName = l.get(0).get("stuName").toString();
        if (email.isEmpty()) {
            throw new CustomException("email not define please contact in school administration!!");
        } else if (!email.equalsIgnoreCase(dbEmail)) {
            throw new CustomException("invalid E-mail address!!");
        } else if (!email.contains(".") || !email.contains("@")) {
            throw new CustomException("invalid E-mail address!!");
        }
        String pass = String.valueOf(Math.random());
        pass = pass.substring(3, 10);


        try {
            String subject = "Password generated!";
            String body = "Dear " + stuName + "<br> Your Login ID : " + stuId + " and password : " + pass + "<br> Login Address " + serverAddress;
            es.sendmail(email, subject, body);
            sql = "UPDATE student_info SET STU_PASSWORD=CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + pass + "'))))) WHERE ID='" + stuId + "'";
            db.save(sql);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\":\"Your Login id and password send in your e-mail!!\"}");
    }
}
