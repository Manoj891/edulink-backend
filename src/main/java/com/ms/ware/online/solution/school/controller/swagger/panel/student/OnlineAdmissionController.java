package com.ms.ware.online.solution.school.controller.swagger.panel.student;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.OnlineAdmissionDao;
import com.ms.ware.online.solution.school.entity.student.OnlineAdmission;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

@RestController
@RequestMapping("/public/api/")
public class OnlineAdmissionController {
    @Autowired
    private OnlineAdmissionDao da;
    @Autowired
    private AuthenticationFacade facade;

    @PostMapping("/Student/OnlineAdmission")
    public Object onlineAdmission(@ModelAttribute OnlineAdmission obj, @RequestParam MultipartFile photo, HttpServletRequest request) {
        Message message = new Message();
        String msg;
        int row, sn;
        long academicYear = obj.getAcademicYear();
        try {
            try {
                String sql = "SELECT ifnull(MAX(SN),0)+1 AS sn FROM online_admission where ACADEMIC_YEAR='" + academicYear + "'";
                message.map = new DB().getRecord(sql).get(0);
                sn = Integer.parseInt(message.map.get("sn").toString());
                if (sn < 10) {
                    obj.setId(Long.parseLong(academicYear + "000" + sn));
                } else if (sn < 100) {
                    obj.setId(Long.parseLong(academicYear + "00" + sn));
                } else if (sn < 1000) {
                    obj.setId(Long.parseLong(academicYear + "0" + sn));
                } else {
                    obj.setId(Long.parseLong(academicYear + "" + sn));
                }

            } catch (Exception e) {
                return message.respondWithError("connection error or invalid table name", e.getMessage());
            }
            try {
                if (photo.getSize() >= 100) {

                    File f;
                    String location = message.getFilepath(DatabaseName.getDocumentUrl());
                    f = new File(location + "/OnlineAdmission/" + academicYear + "/");
                    try {
                        if (!f.exists()) {
                            f.mkdirs();
                        }
                    } catch (Exception e) {

                    }
                    String fileName = "/OnlineAdmission/" + academicYear + "/" + obj.getId() + ".png";
                    f = new File(location + fileName);
                    System.out.println(f.getAbsolutePath());
                    photo.transferTo(f);
                    obj.setStudentPhoto(fileName);
                }
            } catch (Exception e) {

            }

            obj.setEnterDate(new Date());
            obj.setStatus("PENDING");
            obj.setSn(sn);
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @GetMapping("/Student/OnlineAdmission")
    public Object onlineAdmission(@RequestParam Long academicYear, @RequestParam Long subjectGroup, @RequestParam Long program, @RequestParam Long classId, HttpServletRequest request) {
        String sql = "SELECT ACADEMIC_YEAR academicYear,P.NAME program,C.NAME className,A.ID id,STU_NAME name,FATHERS_NAME fathersName,MOBILE_NO mobileNo,EMAIL email,CONCAT(MUNICIPAL,'-',WARD_NO,' ',DISTRICT) AS address,STUDENT_PHOTO photo,GET_BS_DATE(ENTER_DATE) enterDate FROM online_admission A,class_master C,program_master P WHERE A.CLASS_ID=C.ID AND A.PROGRAM=P.ID AND STATUS='PENDING' AND ACADEMIC_YEAR=IFNULL(" + academicYear + ",ACADEMIC_YEAR) AND SUBJECT_GROUP=IFNULL(" + subjectGroup + ",SUBJECT_GROUP) AND PROGRAM=IFNULL(" + program + ",PROGRAM) AND CLASS_ID=IFNULL(" + classId + ",CLASS_ID) ORDER BY ENTER_DATE";
        return new DB().getRecord(sql);
    }

    @PutMapping("/Student/OnlineAdmission/{id}")
    public Object onlineAdmissionApprove(@PathVariable long id) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        DB db = new DB();
        String msg, sql;
        String academicYear, program, classId;
        long stuId;
        sql = "SELECT ACADEMIC_YEAR academicYear,PROGRAM program,CLASS_ID classId FROM online_admission WHERE ID='" + id + "'";
        message.map = db.getRecord(sql).get(0);
        academicYear = message.map.get("academicYear").toString();
        program = message.map.get("program").toString();
        classId = message.map.get("classId").toString();

        sql = "SELECT ifnull(MAX(SN),0)+1 AS id FROM student_info WHERE ACADEMIC_YEAR=" + academicYear;
        message.map = db.getRecord(sql).get(0);
        int sn = Integer.parseInt(message.map.get("id").toString());

        if (sn < 10) {
            stuId = Long.parseLong(academicYear + program + classId + "000" + sn);
        } else if (sn < 100) {
            stuId = Long.parseLong(academicYear + program + classId + "00" + sn);
        } else if (sn < 1000) {
            stuId = Long.parseLong(academicYear + program + classId + "0" + sn);
        } else {
            stuId = Long.parseLong(academicYear + program + classId + "" + sn);
        }

        sql = "SELECT ifnull(MAX(ROLL_NO),0)+1 AS rollNo FROM student_info WHERE ACADEMIC_YEAR=" + academicYear + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
        message.map = db.getRecord(sql).get(0);
        String rollNo = message.map.get("rollNo").toString();
        sql = "INSERT INTO student_info (ID, ROLL_NO,ADMISSION_YEAR, ACADEMIC_YEAR,ALTERNATIVE_MOBILE, CAST_ETHNICITY, CITIZENSHIP, CLASS_ID, DATE_OF_BIRTH, DISABILITY,DISTRICT, DISTRICTT, DROP_OUT, EMAIL, ENTER_DATE, FATHERS_DESIGNATION, FATHERS_MOBILE, FATHERS_NAME, FATHERS_OCCUPATION, FATHERS_QUALIFICATION, GENDER, GUARDIANS_ADDRRESS, GUARDIANS_MOBILE, GUARDIANS_NAME, GUARDIANS_RELATION, MARITAL_STATUS, MOBILE_NO, MOTHERS_DESIGNATION, MOTHERS_MOBILE, MOTHERS_NAME, MOTHERS_OCCUPATION, MOTHERS_QUALIFICATION, MUNICIPAL, MUNICIPALT, PRE_SCHOOL, PROGRAM, PROVINCE, PROVINCET, RELIGION, SN, STATUS, STU_NAME, PHOTO, SUBJECT_GROUP, TOL, TOLT, WARD_NO, WARD_NOT,ENTER_BY,SECTION) SELECT '" + stuId + "','" + rollNo + "', ACADEMIC_YEAR admissionYear,ACADEMIC_YEAR, ALTERNATIVE_MOBILE, CAST_ETHNICITY, CITIZENSHIP, CLASS_ID, DATE_OF_BIRTH, DISABILITY, DISTRICT, DISTRICTT, 'N', EMAIL, SYSDATE(), FATHERS_DESIGNATION, FATHERS_MOBILE, FATHERS_NAME, FATHERS_OCCUPATION, FATHERS_QUALIFICATION, GENDER, GUARDIANS_ADDRRESS, GUARDIANS_MOBILE, GUARDIANS_NAME, GUARDIANS_RELATION, MARITAL_STATUS, MOBILE_NO, MOTHERS_DESIGNATION, MOTHERS_MOBILE, MOTHERS_NAME, MOTHERS_OCCUPATION, MOTHERS_QUALIFICATION, MUNICIPAL, MUNICIPALT, PRE_SCHOOL, PROGRAM, PROVINCE, PROVINCET, RELIGION, '" + sn + "', 'Y', STU_NAME, STUDENT_PHOTO, SUBJECT_GROUP, TOL, TOLT, WARD_NO, WARD_NOT,'" + td.getUserName() + "' AS enterBy,'' AS SECTION FROM online_admission WHERE ID='" + id + "';";
        int row = db.save(sql);
        msg = da.getMsg();
        if (row > 0) {
            sql = "INSERT INTO class_transfer(ACADEMIC_YEAR,STUDENT_ID,CLASS_ID,PROGRAM,ROLL_NO,SECTION,SUBJECT_GROUP) SELECT ACADEMIC_YEAR,ID,CLASS_ID,PROGRAM,ROLL_NO,SECTION,SUBJECT_GROUP FROM student_info WHERE `ID`='" + stuId + "'";
            db.save(sql);
            sql = "UPDATE online_admission SET STATUS='APPROVED' WHERE ID='" + id + "';";
            db.save(sql);
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }


    @DeleteMapping("/Student/OnlineAdmission/{id}")
    public Object onlineAdmissionReject(@PathVariable long id, HttpServletRequest request) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String msg;
        String sql = "UPDATE online_admission SET STATUS='REJECT' WHERE ID='" + id + "'";
        int row = new DB().delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }
}
