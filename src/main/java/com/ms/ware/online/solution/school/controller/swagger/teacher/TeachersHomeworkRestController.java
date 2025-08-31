package com.ms.ware.online.solution.school.controller.swagger.teacher;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.entity.student.StudentHomework;
import com.ms.ware.online.solution.school.entity.teacherpanel.TeachersHomework;
import com.ms.ware.online.solution.school.service.teacherpanel.TeachersHomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("api/TeacherPanel/TeachersHomework")
public class TeachersHomeworkRestController {
    @Autowired
    private DB db;
    @Autowired
    TeachersHomeworkService service;
    @Autowired
    private AuthenticationFacade facade;
    @GetMapping
    public ResponseEntity index(@RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup) {
        return service.getAll(academicYear, program, classId, subjectGroup);
    }

    @GetMapping("/Subject")
    public Object subject(@RequestParam(defaultValue = "0") Long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup) {
         AuthenticatedUser td = facade.getAuthentication();;;
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(new Message().respondWithError("invalid token"));
        }
        String sql = "SELECT S.ID id,S.NAME name FROM teachers_class_subject T,subject_master S WHERE T.SUBJECT=S.ID AND ACADEMIC_YEAR='" + academicYear + "' AND PROGRAM='" + program + "' AND CLASS_ID='" + classId + "' AND SUBJECT_GROUP='" + subjectGroup + "' AND TEACHER=" + td.getUserId();
        return db.getRecord(sql);
    }

    @GetMapping("/Subject/{academicYear}")
    public Object subject(@PathVariable long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup) {
         AuthenticatedUser td = facade.getAuthentication();;;
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(new Message().respondWithError("invalid token"));
        }
        return db.getRecord("SELECT S.ID id,S.NAME name FROM teachers_class_subject T,subject_master S WHERE  T.SUBJECT=S.ID AND ACADEMIC_YEAR='" + academicYear + "' AND T.SUBJECT_GROUP=" + subjectGroup + " AND T.PROGRAM='" + program + "' AND T.CLASS_ID='" + classId + "' AND T.TEACHER=" + td.getUserId());
    }

    /*
    date=" + date + "&classId=" + classId + "&program=" + program + "&subjectGroup=" + subjectGroup
     */
    @GetMapping("/HomeworkCheck")
    public Object homeworkCheck(@RequestParam String date, @RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long subject) {
         AuthenticatedUser td = facade.getAuthentication();;;
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(new Message().respondWithError("invalid token"));
        }

        if (date.length() != 10) {
            date = DateConverted.today();
            date = " AND Q.HOMEWORK_DATE BETWEEN '" + DateConverted.toString(DateConverted.addDate(date, -30)) + "' AND '" + date + "'";
        } else {
            date = " AND Q.HOMEWORK_DATE='" + DateConverted.bsToAd(date) + "' ";
        }
        String sql = "SELECT GET_BS_DATE(Q.HOMEWORK_DATE) homeworkDate,S.ID regNo,Q.ID question,S.STU_NAME name,S.ROLL_NO rollNo,IFNULL(SH.ANSWER,'') answer,Q.HOMEWORK_TITLE title,Q.HOME_WORK homework,Q.SUBJECT subject,TEACHER teacher,IFNULL(SH.REMARK,'') remark,IFNULL(GET_BS_DATE(SH.CHECK_DATE),'') checkDate,IFNULL(Q.FILE_URL,'') questionFile,IFNULL(SH.STU_FILE,'') answerFile,IFNULL(SH.STU_FILE1,'') answerFile1,IFNULL(SH.STU_FILE2,'') answerFile2,IFNULL(SH.STU_FILE3,'') answerFile3,IFNULL(SH.STU_FILE4,'') answerFile4,IFNULL(SH.STU_FILE5,'') answerFile5 FROM teachers_homework Q, student_info S LEFT JOIN student_homework SH ON SH.STU_ID=S.ID WHERE IFNULL(SH.HOMEWORK,Q.ID)=Q.ID AND S.ACADEMIC_YEAR=" + academicYear + " AND S.PROGRAM=" + program + " AND S.SUBJECT_GROUP=" + subjectGroup + " AND S.CLASS_ID=" + classId + " AND Q.SUBJECT=" + subject + " AND Q.TEACHER=" + td.getUserId() + " " + date + " ORDER BY homeworkDate,checkDate DESC,rollNo";
        return db.getRecord(sql);

    }

    @PostMapping("/HomeworkCheck")
    public Object doSave(@RequestBody StudentHomework obj) throws IOException {
        Message message = new Message();
         AuthenticatedUser td = facade.getAuthentication();;
        String msg = "", remark = obj.getRemark();
        long homeworkId = obj.getHomework();
        String checkDate = obj.getCheckDateAd();
        long regNo = obj.getStuId();
       
        String sql;
        int row = 0;
        if (checkDate.length() == 10) {
            sql = "UPDATE student_homework SET REMARK=?,CHECK_DATE='" + checkDate + "' WHERE HOMEWORK=" + homeworkId + " AND STU_ID=" + regNo;
            row = db.save(sql, new String[]{remark});
        } else {
            sql = "UPDATE student_homework SET REMARK=? WHERE HOMEWORK=" + homeworkId + " AND STU_ID=" + regNo;
            row = db.save(sql, new String[]{remark});
            if (row == 0) {
                sql = "INSERT INTO student_homework(HOMEWORK,STU_ID,ANSWER,REMARK) VALUES(" + homeworkId + "," + regNo + ",'',?);";
                row = db.save(sql, new String[]{remark});
            }
        }

        try {
            msg = db.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

    @PostMapping
    public Object doSave(HttpServletRequest request, @RequestParam(required = false) MultipartFile photo, @ModelAttribute TeachersHomework obj) throws IOException {
        return service.save(request, obj, photo);
    }

    @PutMapping("/{id}")
    public Object doUpdate(HttpServletRequest request, @RequestParam(required = false) MultipartFile photo, @ModelAttribute TeachersHomework obj, @PathVariable long id) throws IOException {
        return service.update(request, obj, id, photo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity doDelete(@PathVariable String id) {
        return service.delete(id);
    }
}
