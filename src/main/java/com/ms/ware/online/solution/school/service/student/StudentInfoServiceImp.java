/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.student;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ms.ware.online.solution.school.dto.ClassTransferReq;
import com.ms.ware.online.solution.school.entity.student.ClassTransfer;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.CreditBillAutoGenerate;
import com.ms.ware.online.solution.school.model.DatabaseName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class StudentInfoServiceImp implements StudentInfoService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private StudentInfoDao da;
    @Autowired
    private CreditBillAutoGenerate crBill;
    Message message = new Message();
    String msg = "", sql;
    int row;

    public void rollNumberSerialNumber() {
        for (Map<String, Object> m : da.getRecord("select academic_year, program, class_id, subject_group, section from student_info group by academic_year, program, class_id, subject_group, section ")) {
            String academic_year = m.get("academic_year").toString();
            String class_id = m.get("class_id").toString();
            String subject_group = m.get("subject_group").toString();
            String program = m.get("program").toString();
            String section = m.get("section").toString();
            String sql = "select id from student_info where academic_year=" + academic_year + " and program=" + program + " and class_id=" + class_id + " and subject_group=" + subject_group + " and section='" + section + "' order by stu_name";
            int rollNo = 1;
            for (Map<String, Object> d : da.getRecord(sql)) {
                String id = d.get("id").toString();
                da.delete("update class_transfer set roll_no = " + rollNo + ", section = '" + section + "', subject_group = " + subject_group + ", program = " + program + " where academic_year = " + academic_year + " and class_id = " + class_id + " and student_id = " + id + ";update  student_info set roll_no=" + rollNo + " where id=" + id + ";");
                System.out.println(academic_year + " " + program + " " + class_id + " " + subject_group + " " + section + " " + id + " " + rollNo);
                rollNo++;
            }
        }

    }

    @Override
    public Object getAll(Long regNo, Long program, Long classId, Long academicYear, Long subjectGroup, String section) {
        try {
            if (regNo > 0) {
                String sql = "SELECT ID AS id,ACADEMIC_YEAR AS academicYear,IFNULL(ADMISSION_YEAR,ACADEMIC_YEAR) AS admissionYear,ALTERNATIVE_MOBILE AS alternativeMobile,CAST_ETHNICITY AS castEthnicity,CITIZENSHIP AS citizenship,CLASS_ID AS classId,DATE_OF_BIRTH AS dateOfBirth,DISABILITY AS disability,DISTRICT AS district,DISTRICTT AS districtt,DROP_OUT AS dropOut,EMAIL AS email,ENTER_BY AS enterBy,ENTER_DATE AS enterDate,FATHERS_DESIGNATION AS fathersDesignation,FATHERS_MOBILE AS fathersMobile,FATHERS_NAME AS fathersName,FATHERS_OCCUPATION AS fathersOccupation,FATHERS_QUALIFICATION AS fathersQualification,GENDER AS gender,GUARDIANS_ADDRRESS AS guardiansAddrress,GUARDIANS_MOBILE AS guardiansMobile,GUARDIANS_NAME AS guardiansName,GUARDIANS_RELATION AS guardiansRelation,MARITAL_STATUS AS maritalStatus,MOBILE_NO AS mobileNo,MOTHERS_DESIGNATION AS mothersDesignation,MOTHERS_MOBILE AS mothersMobile,MOTHERS_NAME AS mothersName,MOTHERS_OCCUPATION AS mothersOccupation,MOTHERS_QUALIFICATION AS mothersQualification,MUNICIPAL AS municipal,MUNICIPALT AS municipalt,PHOTO AS photo,PRE_ADMISSION AS preAdmission,PRE_SCHOOL AS preSchool,PROGRAM AS program,PROVINCE AS province,PROVINCET AS provincet,RELIGION AS religion,ROLL_NO AS rollNo,SECTION AS section,SN AS sn,STATUS AS status,STU_NAME AS stuName,STU_PASSWORD AS stuPassword,SUBJECT_GROUP AS subjectGroup,TOL AS tol,TOLT AS tolt,WARD_NO AS wardNo,WARD_NOT AS wardNot,IFNULL(link_student,'') linkStudent,reg_no_link regNoLink,board_regd_no boardRegdNo FROM student_info where id=ifnull(" + regNo + ",id)";
                return da.getRecord(sql);
            }
        } catch (Exception ignored) {
        }
        if (!section.isEmpty()) section = " and SECTION='" + section + "'";
        else section = "";
        String sql = "SELECT ID AS id,ACADEMIC_YEAR AS academicYear,IFNULL(ADMISSION_YEAR,ACADEMIC_YEAR) AS admissionYear,ALTERNATIVE_MOBILE AS alternativeMobile,CAST_ETHNICITY AS castEthnicity,CITIZENSHIP AS citizenship,CLASS_ID AS classId,DATE_OF_BIRTH AS dateOfBirth,DISABILITY AS disability,DISTRICT AS district,DISTRICTT AS districtt,DROP_OUT AS dropOut,EMAIL AS email,ENTER_BY AS enterBy,ENTER_DATE AS enterDate,FATHERS_DESIGNATION AS fathersDesignation,FATHERS_MOBILE AS fathersMobile,FATHERS_NAME AS fathersName,FATHERS_OCCUPATION AS fathersOccupation,FATHERS_QUALIFICATION AS fathersQualification,GENDER AS gender,GUARDIANS_ADDRRESS AS guardiansAddrress,GUARDIANS_MOBILE AS guardiansMobile,GUARDIANS_NAME AS guardiansName,GUARDIANS_RELATION AS guardiansRelation,MARITAL_STATUS AS maritalStatus,MOBILE_NO AS mobileNo,MOTHERS_DESIGNATION AS mothersDesignation,MOTHERS_MOBILE AS mothersMobile,MOTHERS_NAME AS mothersName,MOTHERS_OCCUPATION AS mothersOccupation,MOTHERS_QUALIFICATION AS mothersQualification,MUNICIPAL AS municipal,MUNICIPALT AS municipalt,PHOTO AS photo,PRE_ADMISSION AS preAdmission,PRE_SCHOOL AS preSchool,PROGRAM AS program,PROVINCE AS province,PROVINCET AS provincet,RELIGION AS religion,ROLL_NO AS rollNo,SECTION AS section,SN AS sn,STATUS AS status,STU_NAME AS stuName,STU_PASSWORD AS stuPassword,SUBJECT_GROUP AS subjectGroup,TOL AS tol,TOLT AS tolt,WARD_NO AS wardNo,WARD_NOT AS wardNot,IFNULL(link_student,'') linkStudent,reg_no_link regNoLink,board_regd_no boardRegdNo FROM  student_info where academic_Year=ifnull(" + academicYear + ",academic_Year) and program=ifnull(" + program + ",program) and class_Id=ifnull(" + classId + ",class_Id) and subject_Group=ifnull(" + subjectGroup + ",subject_Group) " + section + " order by program,classId,stuName";
        return da.getRecord(sql);
    }

    @Override
    public Object save(StudentInfo obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            long program = obj.getProgram();
            long classId = obj.getClassId();
            long academicYear = obj.getAcademicYear();

            sql = "SELECT ifnull(MAX(SN),0)+1 AS id FROM student_info WHERE concat(id,'') like '" + academicYear + "%'";
            message.map = da.getRecord(sql).get(0);
            int sn = Integer.parseInt(message.map.get("id").toString());
            obj.setSn(sn);
            long id;
            if (sn < 10) {
                id = Long.parseLong(academicYear + "000" + sn);
            } else if (sn < 100) {
                id = Long.parseLong(academicYear + "00" + sn);
            } else if (sn < 1000) {
                id = Long.parseLong(academicYear + "0" + sn);
            } else {
                id = Long.parseLong(academicYear + "" + sn);
            }
            if (message.isNull(obj.getRollNo())) {
                sql = "SELECT ifnull(MAX(ROLL_NO),0)+1 AS rollNo FROM student_info WHERE ACADEMIC_YEAR=" + academicYear + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
                message.map = da.getRecord(sql).get(0);
                int rollNo = Integer.parseInt(message.map.get("rollNo").toString());
                obj.setRollNo(rollNo);
            }

            obj.setId(id);
            obj.setEnterBy(td.getUserName());
            obj.setEnterDateAD(new Date());
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                ClassTransfer ct = new ClassTransfer(obj.getId(), obj.getAcademicYear(), obj.getProgram(), obj.getClassId(), obj.getSubjectGroup(), obj.getRollNo(), obj.getSection());
                da.save(ct);

                long subjectGroup = obj.getSubjectGroup();
                if (!crBill.generate(academicYear, program, classId, subjectGroup, id, td.getUserName())) {
                    sql = "DELETE FROM class_transfer WHERE STUDENT_ID='" + id + "';";
                    da.delete(sql);
                    sql = "DELETE FROM student_info where ID=" + id;
                    da.delete(sql);
                    return message.respondWithError(crBill.getMsg());
                }

                return message.respondWithMessage("Success", String.valueOf(id));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(StudentInfo obj, long id) {
        System.out.println(obj.getBoardRegdNo());
        if (obj.getLinkStudent() == null) {
            obj.setRegNoLink(null);
        } else if (!obj.getLinkStudent().equalsIgnoreCase("on")) {
            obj.setRegNoLink(null);
        }


        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg().toLowerCase().replace("", "");
        if (row > 0) {
            sql = "SELECT ROLL_NO FROM class_transfer WHERE ACADEMIC_YEAR='" + obj.getAcademicYear() + "' AND STUDENT_ID='" + obj.getId() + "'";
            message.list = da.getRecord(sql);
            if (message.list.isEmpty()) {
                sql = "INSERT INTO class_transfer(ACADEMIC_YEAR,PROGRAM,CLASS_ID,SUBJECT_GROUP,SECTION,ROLL_NO,STUDENT_ID) VALUES(" + obj.getAcademicYear() + "," + obj.getProgram() + "," + obj.getClassId() + ",'" + obj.getSubjectGroup() + "','" + obj.getSection() + "','" + obj.getRollNo() + "','" + obj.getId() + "');";
                da.delete(sql);
            } else {
                sql = "UPDATE class_transfer SET CLASS_ID='" + obj.getClassId() + "',PROGRAM='" + obj.getProgram() + "',ROLL_NO='" + obj.getRollNo() + "',SECTION='" + obj.getSection() + "',SUBJECT_GROUP='" + obj.getSubjectGroup() + "' WHERE ACADEMIC_YEAR='" + obj.getAcademicYear() + "' AND STUDENT_ID='" + obj.getId() + "'";
                da.delete(sql);
            }
            return message.respondWithMessage("Success", String.valueOf(id));
        } else if (msg.contains("duplicate entry")) {
            if (msg.contains("roll_no")) {
                msg = "Roll No " + obj.getRollNo() + " already exist. Please Change Roll No.";
            } else {
                msg = "This record already exist";
            }
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object delete(String id) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        sql = "SELECT M.REG_NO,D.BILL_NO,M.BILL_TYPE FROM stu_billing_detail D,stu_billing_master M  WHERE M.BILL_NO=D.BILL_NO AND M.BILL_TYPE='DR' AND M.REG_NO='" + id + "'";
        List<Map<String, Object>> list = da.getRecord(sql);
        if (!list.isEmpty()) {
            return message.respondWithError("Can not Delete after Bill Collected.");
        }
        sql = "DELETE FROM stu_billing_detail WHERE BILL_NO IN(SELECT BILL_NO FROM stu_billing_master WHERE REG_NO='" + id + "');";
        da.delete(sql);
        sql = "DELETE FROM previous_education WHERE REG_NO='" + id + "';";
        da.delete(sql);
        sql = "DELETE FROM stu_billing_master WHERE REG_NO='" + id + "';";
        da.delete(sql);
        sql = "DELETE FROM class_transfer WHERE STUDENT_ID='" + id + "';";
        da.delete(sql);
        sql = "DELETE FROM student_info WHERE ID =" + id;
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object getAll(Long academicYear, Long program, Long classId, Long subjectGroup) {
        sql = "SELECT s.ID id,s.STU_NAME stuName,s.MOBILE_NO mobileNo,s.FATHERS_NAME fatherName,c.ROLL_NO rollNo FROM student_info s join class_transfer c on s.ID = c.STUDENT_ID WHERE c.ACADEMIC_YEAR=IFNULL(" + academicYear + ",c.ACADEMIC_YEAR) AND c.PROGRAM=IFNULL(" + program + ",c.PROGRAM) AND c.CLASS_ID=IFNULL(" + classId + ",c.CLASS_ID) AND c.SUBJECT_GROUP=IFNULL(" + subjectGroup + ",c.SUBJECT_GROUP) AND ifnull(DROP_OUT,'')!='Y' ORDER BY stuName,rollNo";
        return da.getRecord(sql);
    }

    @Override
    public String classTransfer(ClassTransferReq req) {
        AuthenticatedUser td = facade.getAuthentication();
        long program = req.getProgram();
        long classId = req.getClassId();
        long subjectGroup = req.getSubjectGroup();
        long academicYear = req.getAcademicYear();

        AtomicInteger count = new AtomicInteger();
        req.getObj().forEach(detail -> {
            try {

                sql = "update student_info set academic_year=" + academicYear + ",program=" + program + ",class_id=" + classId + ",subject_group=" + subjectGroup + ",roll_no=" + detail.getRollNo() + " where id=" + detail.getRegNo();
                row = da.delete(sql);
                msg = da.getMsg();
                if (row > 0) {
                    crBill.generate(academicYear, program, classId, subjectGroup, detail.getRegNo(), td.getUserName());
                    da.save(new ClassTransfer(detail.getRegNo(), academicYear, program, classId, subjectGroup, detail.getRollNo(), ""));
                    count.getAndIncrement();
                } else if (msg.contains("Duplicate entry")) {
                    msg = "This record already exist";
                }
            } catch (Exception ignored) {
            }
        });
        return message.respondWithMessage(count.get() + " Record Transfer!!");

    }

    @Override
    public Object studentReport(Long academicYear, Long program, Long classId, Long subjectGroup, Long castEthnicity, String gender, String section) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> m;
        try {
            if (academicYear > 0) {
                sql = "SELECT YEAR AS name FROM academic_year WHERE ID=" + academicYear;
                m = da.getRecord(sql).get(0);
                map.put("academicYear", m.get("name"));
            } else {
                map.put("academicYear", "All");
            }
        } catch (Exception e) {
            map.put("academicYear", "All");
        }
        try {
            if (program > 0) {
                sql = "SELECT NAME AS name FROM program_master WHERE ID=" + program;
                m = da.getRecord(sql).get(0);
                map.put("program", m.get("name"));
            } else {
                map.put("program", "All");
            }
        } catch (Exception e) {
            map.put("program", "All");
        }
        try {
            if (classId > 0) {
                sql = "SELECT NAME AS name FROM class_master WHERE ID=" + classId;
                m = da.getRecord(sql).get(0);
                map.put("classId", m.get("name"));
            } else {
                map.put("classId", "All");
            }
        } catch (Exception e) {
            map.put("classId", "All");
        }
        try {
            if (subjectGroup > 0) {
                sql = "SELECT NAME AS name FROM subject_group WHERE ID=" + subjectGroup;
                m = da.getRecord(sql).get(0);
                map.put("subjectGroup", m.get("name"));
            } else {
                map.put("subjectGroup", "All");
            }
        } catch (Exception e) {
            map.put("subjectGroup", "All");
        }
        try {
            if (castEthnicity > 0) {
                sql = "SELECT NAME AS name FROM cast_ethnicity_master WHERE ID=" + castEthnicity;
                m = da.getRecord(sql).get(0);
                map.put("castEthnicity", m.get("name"));
            }
        } catch (Exception e) {
            map.put("castEthnicity", "All");
        }
        map.put("gender", "All");
        if (!gender.isEmpty()) {
            map.put("gender", gender.equalsIgnoreCase("M") ? "Male" : "Female");
            gender = " AND GENDER='" + gender + "'";
        }
        if (!section.isEmpty()) section = " AND CT.section='" + section + "'";
        else section = "";

        map.put("data", da.getRecord("SELECT ifnull(CM.NAME, '') castEthnicity, ifnull(RM.NAME, '') as religion, SI.GENDER as gender, PM.NAME AS program, G.NAME 'group', AY.YEAR academicYear, CM.NAME className, IFNULL(SI.STU_NAME, '') studentName, SI.GUARDIANS_NAME guardiansName, SI.MOTHERS_NAME mothersName, SI.GUARDIANS_RELATION guardiansRelation, IFNULL(FATHERS_MOBILE, '') fathersMobile, IFNULL(SI.FATHERS_NAME, '') fathersName, IFNULL(SI.DATE_OF_BIRTH, '') dob, IFNULL(SI.MOBILE_NO, '') mobileNo, IFNULL(PROVINCE, '') as province, IFNULL(DISTRICT, '') as district, IFNULL(MUNICIPAL, '') as municipal, IFNULL(WARD_NO, '') wardNo, IFNULL(TOL, '') as tol, IFNULL(CT.ROLL_NO, '') rollNo, CT.STUDENT_ID regNo, ifnull(CT.SECTION, '') as section, ifnull(SI.EMAIL, '') as email FROM class_transfer CT join student_info SI on CT.student_id = SI.id join class_master CM on CT.class_id = CM.id join program_master PM on CT.program = PM.id join academic_year AY on CT.academic_year = AY.id join subject_group G on G.ID = SI.SUBJECT_GROUP left join religion_master RM on RM.ID = RELIGION left join cast_ethnicity_master CE on CE.ID = CAST_ETHNICITY WHERE CT.ACADEMIC_YEAR = IFNULL( " + academicYear + ", CT.ACADEMIC_YEAR) AND CT.PROGRAM = IFNULL( " + program + ", CT.PROGRAM) AND CT.CLASS_ID = IFNULL( " + classId + ", CT.CLASS_ID) AND SI.SUBJECT_GROUP = IFNULL( " + subjectGroup + ", SI.SUBJECT_GROUP) AND IFNULL(SI.CAST_ETHNICITY, '') = IFNULL( " + castEthnicity + ", IFNULL(SI.CAST_ETHNICITY, ''))  " + section + "  " + gender + " AND IFNULL(DROP_OUT,'')!='Y' ORDER BY studentName,rollNo"));
        return map;
    }

    @Override
    public Object save(HttpServletRequest request, long studentId, MultipartFile photo, MultipartFile certificate1, MultipartFile certificate2) {
        File f;
        try {
            String regNo = String.valueOf(studentId);
            String location = message.getFilepath(DatabaseName.getDocumentUrl());
            String filePath = "/Student/" + regNo.substring(0, 2);
            f = new File(location + filePath);
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                return message.respondWithError(e.getMessage());
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = df.format(new Date());
            Map<String, Object> map = da.getRecord("select ifnull(photo,'') photo,ifnull(certificate01,'') certificate01,ifnull(certificate02,'') certificate02 from student_info where id=" + regNo).get(0);
            String photoUrl = map.get("photo").toString();
            String certificate01 = map.get("certificate01").toString();
            String certificate02 = map.get("certificate02").toString();

            if (photo != null && photo.getSize() > 100) {
                photoUrl = filePath + "/" + studentId + time + ".png";
                photo.transferTo(new File(location + photoUrl));
            }
            if (certificate1 != null && certificate1.getSize() > 100) {
                certificate01 = filePath + "/" + regNo + time + "certificate01.png";
                certificate1.transferTo(new File(location + certificate01));
            }

            if (certificate2 != null && certificate2.getSize() > 100) {
                certificate02 = filePath + "/" + regNo + time + "certificate02.png";
                certificate2.transferTo(new File(location + certificate02));
            }
            da.delete("update student_info set photo='" + photoUrl + "',certificate01='" + certificate01 + "',certificate02='" + certificate02 + "' where id='" + studentId + "'");
            return message.respondWithMessage("Success");
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object getDropout(String drop, Long program, Long classId, Long academicYear, Long subjectGroup) {
        if (drop.equalsIgnoreCase("IN")) {
            drop = " and ifnull(dropOut,'N')='Y'";
        } else if (drop.equalsIgnoreCase("OUT")) {
            drop = " and ifnull(dropOut,'N')!='Y'";
        } else {
            return message.respondWithError("Please provide drop OUT/IN");
        }
        return da.getAll("from StudentInfo where academicYear=ifnull(" + academicYear + ",academicYear) and program=ifnull(" + program + ",program) and classId=ifnull(" + classId + ",classId) and subjectGroup=ifnull(" + subjectGroup + ",subjectGroup) " + drop + "  order by program,classId,stuName");
    }

    @Override
    public Object doDropout(String jsonData, String status) {
        try {
            List list = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<List>() {
            });
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                sql = "UPDATE student_info SET DROP_OUT='" + status + "' WHERE ID='" + list.get(i) + "'";
                count = count + da.delete(sql);
            }
            return message.respondWithMessage("Success");
        } catch (JsonProcessingException e) {
            return message.respondWithError(e.getMessage());
        }
    }
}
