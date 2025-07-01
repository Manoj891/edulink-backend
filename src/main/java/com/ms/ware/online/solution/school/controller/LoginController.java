package com.ms.ware.online.solution.school.controller;

import com.ms.ware.online.solution.school.config.*;
import com.ms.ware.online.solution.school.config.security.JwtHelper;
import com.ms.ware.online.solution.school.dto.LoginReq;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/api/")
public class LoginController {
    @Autowired
    private DB db;
    @Autowired
    private JwtHelper td;
    @Autowired
    private EmailService e;
    private final ApplicationStatus AS = new ApplicationStatus();

    @PostMapping("login")
    public Object doLogin(@RequestBody LoginReq req) {
        String userName = req.getUsername();
        String userPassword = req.getPassword();
        Map<String, Object> m, map = new HashMap<>();
        long day = AS.get();
        if (day < 0) {
            throw new CustomException("Your Application Expired.");
        }
        String sql = "SELECT IFNULL(CASH_ACCOUNT,'') cashAccount,ID id,LOGIN_ID loginId,LOGIN_PASS password,CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + userPassword + "'))))) userPassword,USER_TYPE userType,STATUS status FROM organization_user_info WHERE (LOGIN_ID='" + userName + "' OR EMAIL='" + userName + "')";

        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            if (userName.equalsIgnoreCase("ADMIN")) {
                sql = "INSERT INTO organization_user_info (ID, EMAIL, LOGIN_ID, LOGIN_PASS, MOBILE, STATUS, USER_TYPE, CASH_ACCOUNT, EMP_NAME) VALUES (1, 'ADMIN', 'ADMIN', CONCAT('*', UPPER(SHA1(UNHEX(SHA1('ADMIN'))))), 'ADMIN', 'Y', 'ADM', NULL, 'ADMIN');";
                db.save(sql);
                throw new CustomException("Try Again");
            } else {
                throw new CustomException("Invalid credentials!!");
            }

        }
        m = l.get(0);
        String status = m.get("status").toString();
        if (!status.equalsIgnoreCase("Y")) {
            throw new CustomException("user login id expired!");
        }
        String password = m.get("password").toString();
        userPassword = m.get("userPassword").toString();
        if (!userPassword.equalsIgnoreCase(password)) {
            throw new CustomException("Invalid credentials!!");
        }
        String userType = m.get("userType").toString();
        String id = m.get("id").toString();
        String loginId = m.get("loginId").toString();
        String cashAccount = m.get("cashAccount").toString();
        map.put("token", td.create(id, loginId, userType, cashAccount, DatabaseName.getDocumentUrl(), "ROLE_ORG"));
        String today = DateConverted.today();
        map.put("badate", DateConverted.adToBs(today));
        try {
            sql = "select start_date from fiscal_year where '" + today + "' between START_DATE and END_DATE";
            map.put("fyStart", DateConverted.adToBs(db.getRecord(sql).get(0).get("start_date").toString()));
        } catch (Exception e) {
            map.put("fyStart", DateConverted.adToBs(today));
        }
        return map;
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody String jsonData) {
        Message message = new Message();
        try {
            Map<String, Object> map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
            String loginId = map.get("loginId").toString();
            String email = map.get("email").toString();
            String sql = "SELECT ID id,EMP_NAME empName,LOGIN_ID loginId,STATUS status,EMAIL email FROM organization_user_info WHERE (LOGIN_ID='" + loginId + "' AND EMAIL='" + email + "')";

            List<Map<String, Object>> l = db.getRecord(sql);
            if (l.isEmpty()) {
                return message.respondWithError("Invalid login or email!!");
            }
            map = l.get(0);
            String status = map.get("status").toString();
            if (!status.equalsIgnoreCase("Y")) {
                return message.respondWithError("Login not active!!");
            }
            String empName = map.get("empName").toString();
            String id = map.get("id").toString();
            loginId = map.get("loginId").toString();
            email = map.get("email").toString();

            String password = String.valueOf(Math.random()).substring(3, 9);
            String body = "Dear " + empName + ",<br/> Your login id " + loginId + " and login password " + password + "<br/>" + "Please login and change password!!";
            if (e.sendmail(email, "Password generated from info web nepal!.", body)) {
                sql = "UPDATE organization_user_info SET LOGIN_PASS=CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + password + "'))))) WHERE ID='" + id + "'";
                db.delete(sql);
                return message.respondWithMessage("Success!!");
            }
            return message.respondWithError(e.getMsg());
        } catch (Exception ex) {
            return message.respondWithError(ex.getMessage());

        }
    }
}
