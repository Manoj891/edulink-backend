/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.dao.teacherpanel.TeachersHomeworkDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/TeacherPanel/UploadVideo/Stu")
public class StudentUploadVideoRestController {

    @Autowired
    TeachersHomeworkDao da;

    @GetMapping
    public Object index(@RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long subject) {

        return da.getAll("from UploadTeachersVideo where subjectGroup=ifnull(" + subjectGroup + ",subjectGroup) and program=ifnull(" + program + ",program) and classId=ifnull(" + classId + ",classId) and subject=IFNULL(" + subject + ",subject)");
    }
}
