package com.ms.ware.online.solution.school.controller.utility;


import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.entity.utility.OrganizationUserInfo;
import com.ms.ware.online.solution.school.service.utility.OrganizationUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Utility/OrganizationUserInfo")
public class OrganizationUserInfoRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    OrganizationUserInfoService service;

    @GetMapping
    public Object index() {
        return service.getAll();
    }

    @PatchMapping("/{id}")
    public Object resetPassword(@PathVariable long id, HttpServletRequest request) throws IOException {
        return service.resetPassword(id, request);
    }

    @GetMapping("/CashAccount")
    public Object cashAccount() {
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE TRANSACT='Y'  AND MGR_CODE=(SELECT CASH_ACCOUNT FROM organization_master WHERE ID=1)";
        return new DB().getRecord(sql);
    }

    @PostMapping
    public Object doSave(@RequestBody OrganizationUserInfo obj, HttpServletRequest request) throws IOException {
        return service.save(obj, request);
    }

    @PutMapping("/{id}")
    public Object doUpdate(@RequestBody OrganizationUserInfo obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @GetMapping("/ChangePassword")
    public Object doChangePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String rePassword) throws IOException {
        AuthenticatedUser td = facade.getAuthentication();;
        Message message = new Message();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        if (!newPassword.equals(rePassword)) {
            return message.respondWithError("Re Password not match");
        }
        String sql = "SELECT LOGIN_PASS dbPassword,CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + oldPassword + "'))))) AS oldPassword FROM organization_user_info WHERE ID='" + td.getUserId() + "'";
        DB db = new DB();
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
        sql = "UPDATE organization_user_info SET LOGIN_PASS=CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + newPassword + "'))))) WHERE ID='" + td.getUserId() + "'";
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

    @DeleteMapping("/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
