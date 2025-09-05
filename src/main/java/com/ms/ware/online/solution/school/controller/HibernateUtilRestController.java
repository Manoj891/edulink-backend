package com.ms.ware.online.solution.school.controller;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import com.ms.ware.online.solution.school.service.configure.ConfigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/api")
public class HibernateUtilRestController {
    @Autowired
    private DB db;
    @Autowired
    private ConfigureService configure;
    @Autowired
    private HibernateUtil util;
    @Autowired
    private Message message;

    @GetMapping("/function")
    public Object functionConfigure() {
        configure.functionConfigure();
        return message.respondWithMessage("Success");
    }


    @PostMapping("configure/district-municipal")
    public Object configureDistrictMunicipality() {
        configure.configureDistrictMunicipality();
        return message.respondWithMessage("Success");
    }

    @GetMapping("/hibernate-util")
    public Object get() {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT NOW()";
        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            throw new CustomException("Database connection error");
        } else {
            map.put("msg", "Success");
            return map;
        }

    }

    @PostMapping("/hibernate-util")
    public Object post() {
        Map<String, Object> map = new HashMap<>();
        util.init();
        String sql = "SELECT  NOW()";
        List<Map<String, Object>> l = db.getRecord(sql);
        if (l.isEmpty()) {
            throw new CustomException("Database connection error");
        } else {
            map.put("msg", "Success");
            return map;
        }
    }

}
