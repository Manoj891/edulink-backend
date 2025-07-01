/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/panel/student/Homework")
public class HomeworkRestController {
    @Autowired
    private AuthenticationFacade facade;

    @GetMapping
    public Object homework(@RequestParam(required = false) String date) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            if (date.length() != 10) {
                date = "";
            }
        } catch (Exception e) {
            date = "";
        }
        if (date.length() != 10) {
            date = DateConverted.today();
            date = " AND HOMEWORK_DATE BETWEEN '" + DateConverted.toString(DateConverted.addDate(date, -30)) + "' AND '" + date + "'";
        } else {
            date = " AND HOMEWORK_DATE='" + DateConverted.bsToAd(date) + "' ";
        }
        String sql = "SELECT H.SUBJECT subject,(SELECT SM.NAME FROM subject_master SM where SM.ID=H.SUBJECT) AS subName,IFNULL((SELECT REMARK FROM student_homework SH WHERE SH.HOMEWORK=H.ID AND SH.STU_ID=" + td.getUserId() + "),'') remark,IFNULL((SELECT CHECK_DATE FROM student_homework SH WHERE SH.HOMEWORK=H.ID AND SH.STU_ID=" + td.getUserId() + "),'') checkDate,H.ID id,GET_BS_DATE(HOMEWORK_DATE) homeworkDate,HOMEWORK_TITLE title,HOME_WORK homework,H.ENTER_DATE enterDate,CONCAT(T.first_name,' ', ifnull(T,middle_name,''),' ', T.last_name) teacher,T.MOBILE mobileNo FROM teachers_homework H,student_info S,employee_info T WHERE H.ACADEMIC_YEAR=S.ACADEMIC_YEAR AND H.PROGRAM=S.PROGRAM AND H.SUBJECT_GROUP=S.SUBJECT_GROUP AND H.CLASS_ID=S.CLASS_ID AND H.TEACHER=T.ID " + date + " AND S.ID='" + td.getUserId() + "' ORDER BY HOMEWORK_DATE DESC";
        return new DB().getRecord(sql);
    }

    @GetMapping("/{id}")
    public Object homework(@PathVariable Long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        Map map;
        String sql = "SELECT IFNULL((SELECT ANSWER FROM student_homework SH WHERE SH.HOMEWORK=H.ID AND SH.STU_ID=" + td.getUserId() + "),'') answer,IFNULL((SELECT STU_FILE FROM student_homework SH WHERE SH.HOMEWORK=H.ID AND SH.STU_ID=" + td.getUserId() + "),'') stuFile,GET_BS_DATE(HOMEWORK_DATE) homeworkDate,HOMEWORK_TITLE title,HOME_WORK homework,H.ENTER_DATE enterDate, CONCAT(T.first_name,' ',T.last_name) teacher,T.mobile mobileNo,IFNULL(FILE_URL,'') fileUrl FROM teachers_homework H,student_info S,employee_info T WHERE H.PROGRAM=S.PROGRAM AND H.SUBJECT_GROUP=S.SUBJECT_GROUP AND H.CLASS_ID=S.CLASS_ID AND H.TEACHER=T.ID AND H.ID='" + id + "' ";
        try {
            map = new DB().getRecord(sql).get(0);
            String homework = map.get("homework").toString();
            homework = homework.replace("\n", "<br>");
            map.put("homework", homework);
            return map;
        } catch (Exception e) {
            return message.respondWithError("invalid homework");
        }
    }

    @GetMapping("/Completed")
    public Object index(@RequestParam Long academicYear, @RequestParam(required = false) String homeworkDate) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        DB db = new DB();
        try {
            if (homeworkDate.length() == 10) {
                homeworkDate = " AND HOMEWORK_DATE='" + homeworkDate + "'";
            } else {
                homeworkDate = "";
            }
        } catch (Exception e) {
            homeworkDate = "";
        }
        String sql = "SELECT S.ANSWER answer,GET_BS_DATE(S.CHECK_DATE) checkDate,IFNULL(S.REMARK,'') teachersRemark,S.STU_FILE answerFile,S.STU_FILE1 answerFile1,S.STU_FILE2 answerFile2,S.STU_FILE3 answerFile3,S.STU_FILE4 answerFile4,S.STU_FILE5 answerFile5,GET_BS_DATE(T.HOMEWORK_DATE) homeworkDate,T.HOME_WORK homework,T.FILE_URL homeworkFile FROM student_homework S,teachers_homework T WHERE S.HOMEWORK=T.ID AND STU_ID=" + td.getUserId() + " AND ACADEMIC_YEAR='" + academicYear + "' " + homeworkDate;
        return db.getRecord(sql);
    }

    @GetMapping("/Do")
    public Object doHomework(@RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        DB db = new DB();
        String sql = "SELECT GET_BS_DATE(HOMEWORK_DATE) homeworkDate,ACADEMIC_YEAR academicYear,H.ID id,H.CLASS_ID classId,H.PROGRAM program,H.SUBJECT_GROUP subjectGroup,H.SUBJECT subject,H.TEACHER teacher, GET_BS_DATE(H.ENTER_DATE) enterDate,IFNULL(H.FILE_URL,'') fileUrl,H.HOMEWORK_TITLE homeworkTitle,H.HOME_WORK homeWork,S.NAME subjectName,IFNULL((SELECT CONCAT(IFNULL(STU_FILE,''),' , ',IFNULL(STU_FILE1,''),' , ',IFNULL(STU_FILE2,''),' , ',IFNULL(STU_FILE3,''),' , ',IFNULL(STU_FILE4,''),' , ',IFNULL(STU_FILE5,'')) FROM student_homework SH WHERE SH.HOMEWORK=H.ID AND SH.STU_ID='" + td.getUserId() + "'),'') AS studentFile FROM teachers_homework H,subject_master S,ad_bs_calender C WHERE H.HOMEWORK_DATE=C.AD_DATE AND H.SUBJECT=S.ID AND ACADEMIC_YEAR =" + academicYear + " AND H.ID AND H.CLASS_ID='" + classId + "' AND H.PROGRAM='" + program + "' AND H.SUBJECT_GROUP='" + subjectGroup + "'";

        List list = db.getRecord(sql);
        List l = new ArrayList();
        List l1;
        String studentFile[];
        Map map;
        for (int i = 0; i < list.size(); i++) {
            map = (Map) list.get(i);
            l1 = new ArrayList();
            try {
                studentFile = map.get("studentFile").toString().split(",");
                for (int j = 0; j < studentFile.length; j++) {
                    if (studentFile[j].length() > 2) {
                        l1.add(studentFile[j].replace(" ", ""));
                    }
                }
            } catch (Exception e) {
            }
            map.put("studentFile", l1);
            l.add(map);
        }
        return l;
    }


}
