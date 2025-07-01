package com.ms.ware.online.solution.school.controller.swagger.teacher;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.EmailService;
import com.ms.ware.online.solution.school.config.security.JwtHelper;
import com.ms.ware.online.solution.school.dto.StudentLoginReq;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/api/")
public class TeacherLoginRestController {
    @Autowired
    private JwtHelper JwtHelper;
    @Autowired
    private EmailService es;
    @Autowired
    private DB db;
    @PostMapping("/teacher-login")
    public ResponseEntity<String> teacherLogin(@RequestBody StudentLoginReq obj) {

        String username = obj.getUsername().replace(" ", "");
        String password = obj.getPassword().replace(" ", "");
        Map<String, Object> m;

        String sql = "SELECT concat('*', upper(sha1(unhex(sha1('" + password + "'))))) userPassword,ifnull(login_password,'') password,id as id,concat(first_name,' ',last_name) teacherName from employee_info where emp_type='Teaching' and email='" + username + "'";

        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            throw new CustomException("Invalid Teacher's id!!");
        }
        m = l.get(0);
        password = m.get("password").toString();
        if (password.isEmpty()) {
            throw new CustomException("Password not generated!!");
        }
        String userPassword = m.get("userPassword").toString();
        if (!userPassword.equalsIgnoreCase(password)) {
            throw new CustomException("Invalid password!!");
        }
        String token = JwtHelper.create(m.get("id").toString(), m.get("teacherName").toString(), "TCR", "", DatabaseName.getDocumentUrl(), "TCR");
        return ResponseEntity.status(HttpStatus.OK).body("{\"token\":\"" + token + "\"}");
    }


    @PostMapping("/teacher-password")
    public Object teacherPassword(@RequestBody String email) {
        Map<String, Object> m, map = new HashMap<>();
        String sql = "SELECT CONCAT(first_name,' ',last_name)  stuName,IFNULL(EMAIL,'') email FROM employee_info WHERE EMAIL='" + email + "' ";

        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            map.put("error", "Invalid Teacher's ID!!");
            return map;
        }
        m = l.get(0);

        String dbEmail = m.get("email").toString();
        String stuName = m.get("stuName").toString();
        if (email.isEmpty()) {
            map.put("error", "Email not Define Please Contact in Organization Administration!!");
            return map;
        } else if (!email.equalsIgnoreCase(dbEmail)) {
            map.put("error", "Invalid E-mail Address!!");
            return map;
        } else if (!email.contains(".") || !email.contains("@")) {
            map.put("error", "Invalid E-mail Address!!");
            return map;
        }
        String pass = String.valueOf(Math.random());
        pass = pass.substring(3, 10);


        try {
            String subject = "Password generated!";
            String body = "Dear " + stuName + "<br> Your Login ID : " + email + " and password : " + pass;
            es.sendmail(email, subject, body);
            map.put("message", "Your Login id and password send in your e-mail!!");
            sql = "UPDATE employee_info SET LOGIN_PASSWORD=CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + pass + "'))))) WHERE EMAIL='" + email + "'";
            db.save(sql);
            return map;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }

    }
}
