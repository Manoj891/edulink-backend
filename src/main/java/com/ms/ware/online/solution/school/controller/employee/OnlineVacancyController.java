package com.ms.ware.online.solution.school.controller.employee;

import com.ms.ware.online.solution.school.config.EmailService;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.employee.EmployeeInfoDao;
import com.ms.ware.online.solution.school.entity.employee.OnlineVacancy;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/api/")
public class OnlineVacancyController {
    @Autowired
    private EmployeeInfoDao da;
    @Autowired
    private EmailService es;

    @GetMapping("/Online/Vacancy")
    public Object onlineVacancy(@RequestParam String email, @RequestParam String code) {
        List<OnlineVacancy> list = da.getOnlineVacancy("from OnlineVacancy where email='" + email + "' and code='" + code + "'");
        if (list.isEmpty()) {
            return list;
        }
        OnlineVacancy onlineVacancy = list.get(0);
        try {
            if (!onlineVacancy.getCv().contains(".")) {
                onlineVacancy.setCv("");
            }
        } catch (Exception e) {
            onlineVacancy.setCv("");
        }
        try {
            if (!onlineVacancy.getPhoto().contains(".")) {
                onlineVacancy.setPhoto("");
            }
        } catch (Exception e) {
            onlineVacancy.setPhoto("");
        }
        list = new ArrayList<>();
        list.add(onlineVacancy);
        return list;
    }


    @GetMapping("/Online/Vacancy/find")
    public Object onlineVacancyFind(HttpServletRequest request) {
        List<OnlineVacancy> list = da.getOnlineVacancy("from OnlineVacancy where hireFireDate is null order by id desc");
        if (list.isEmpty()) {
            return list;
        }
        for (int i = 0; i < list.size(); i++) {
            OnlineVacancy onlineVacancy = list.get(i);
            try {
                if (!onlineVacancy.getCv().contains(".")) {
                    onlineVacancy.setCv("");
                } else {
                    onlineVacancy.setCv(DatabaseName.getDocumentUrl() + "Document" + onlineVacancy.getCv());
                }
            } catch (Exception e) {
                onlineVacancy.setCv("");
            }
            try {
                if (!onlineVacancy.getPhoto().contains(".")) {
                    onlineVacancy.setPhoto("");
                } else {
                    onlineVacancy.setPhoto(DatabaseName.getDocumentUrl() + "Document" + onlineVacancy.getPhoto());
                }
            } catch (Exception e) {
                onlineVacancy.setPhoto("");
            }
            list.remove(i);
            list.add(i, onlineVacancy);
        }
        return list;
    }


    @PostMapping("/Online/Vacancy")
    public Object onlineVacancySave(HttpServletRequest request, @RequestBody OnlineVacancy obj) {
        Message message = new Message();
        String msg, sql;
        try {

            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM online_vacancy";
            message.map = da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            String pass = String.valueOf(Math.random());
            pass = pass.substring(3, 8);
            obj.setCode(pass);
            if (obj.getPanNo().isEmpty()) {
                obj.setPanNo(pass);
            }
            int row = da.save(obj);
            msg = da.getMsg();
            if (row == 1) {

                String serverAddress = request.getRequestURL().toString();
                serverAddress = serverAddress.substring(0, serverAddress.lastIndexOf("/"));
                String subject = "Online Application.";
                String body = "Dear " + obj.getFirstName() + "<br> your application has been sentto administrative department.<br>"
                        + "Your Code : " + pass;
                es.sendmail(obj.getEmail(), subject, body);
                return ResponseEntity.status(200).body(message.respondWithMessage("Success", obj));
            } else if (msg.contains("Duplicate entry")) {

                msg = "This record already exist";
            }

            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }


    @PutMapping("/Online/Vacancy")
    public Object onlineVacancyUpdate(@RequestBody OnlineVacancy obj) {

        Message message = new Message();

        String msg = "";
        try {
            int row = da.update(obj);
            msg = da.getMsg();
            if (row == 1) {
                String sql = "SELECT CODE AS code,IFNULL(PHOTO,'') photo,IFNULL(CV,'') cv FROM online_vacancy where id=" + obj.getId();
                message.map = (Map) da.getRecord(sql).get(0);
                obj.setCode(message.map.get("code").toString());
                obj.setPhoto(message.map.get("photo").toString());
                obj.setCv(message.map.get("cv").toString());
                return ResponseEntity.status(200).body(message.respondWithMessage("Success", obj));
            } else if (msg.contains("Duplicate entry")) {

                msg = "This record already exist";
            }

            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

}
