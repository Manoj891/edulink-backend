/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.dao.utility.NoticeBoardDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel/student/Calendar")
public class StudentCalendarRestController {

    @Autowired
    NoticeBoardDao da;

    @GetMapping
    public Object calendar(@RequestParam int year) {
        return da.getAll("from AdBsCalender where bsDate like '" + year + "%'");
    }

}
