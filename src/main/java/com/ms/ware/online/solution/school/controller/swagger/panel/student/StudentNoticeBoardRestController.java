/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.dao.utility.NoticeBoardDao;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel/student/NoticeBoard")
public class StudentNoticeBoardRestController {

    @Autowired
    NoticeBoardDao da;

    @GetMapping
    public Object dashboard(@RequestParam(required = false) String dateFrom, @RequestParam(required = false) String dateTo) {
        try {
            if (dateFrom == null) {
                dateFrom = "";
            }
        } catch (Exception e) {
            dateFrom = "";
        }
        try {
            if (dateTo == null) {
                dateTo = "";
            }
        } catch (Exception e) {
            dateTo = "";
        }
        if (dateFrom.length() == 10) {
            dateFrom = DateConverted.bsToAd(dateFrom);
        }
        if (dateTo.length() == 10) {
            dateTo = DateConverted.bsToAd(dateTo);
        }
        if (dateFrom.length() == 10 && dateTo.length() == 10) {
            return da.getAll("from NoticeBoard where enterDateAd between '" + dateFrom + "' AND '" + dateTo + "'");
        } else if (dateFrom.length() == 10) {
            return da.getAll("from NoticeBoard where enterDateAd= '" + dateFrom + "' ");
        }
        return da.getAll("from NoticeBoard where enterDateAd= '" + dateFrom + "' ");
    }

}
