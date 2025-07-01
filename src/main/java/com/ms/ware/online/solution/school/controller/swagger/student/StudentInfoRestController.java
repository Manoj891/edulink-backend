package com.ms.ware.online.solution.school.controller.swagger.student;

import com.ms.ware.online.solution.school.config.*;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dto.ClassTransferReq;
import com.ms.ware.online.solution.school.entity.SmsSendingReq;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.DatabaseName;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import com.ms.ware.online.solution.school.service.student.StudentInfoService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/Student/")
public class StudentInfoRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private StudentInfoService service;
    @Autowired
    private SmsService sms;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DB db;

    @GetMapping("/GenderWise")
    public Object GenderWise(@RequestParam Long academicYear, @RequestParam(required = false) Long program) {
        String sql = "SELECT ID id,NAME name FROM cast_ethnicity_master order by name";

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        List<Map<String, Object>> castEthnicity = db.getRecord(sql);

        sql = "SELECT PROGRAM program,CLASS_ID classId,ACADEMIC_YEAR academicYear,P.NAME programName,C.NAME className FROM student_info S,program_master P,class_master C WHERE S.PROGRAM=P.ID AND S.CLASS_ID=C.ID AND ACADEMIC_YEAR=" + academicYear + " AND PROGRAM=IFNULL(" + program + ",PROGRAM) GROUP BY ACADEMIC_YEAR,PROGRAM,CLASS_ID";
        for (Map<String, Object> m : db.getRecord(sql)) {
            String castSql = "SELECT ";
            for (int i = 0; i < castEthnicity.size(); i++) {
                castSql = castSql + "GET_MALE_FEMALE(" + m.get("academicYear") + "," + m.get("program") + "," + m.get("classId") + "," + castEthnicity.get(i).get("id") + ") AS 'cast" + i + "', ";
            }
            castSql = castSql + "0 AS ce FROM DUAL";
            List<Map<String, Object>> gender = new ArrayList<>();
            map = db.getRecord(castSql).get(0);
            for (int i = 0; i < castEthnicity.size(); i++) {
                String[] ss = map.get("cast" + i).toString().split(",");
                Map<String, Object> mapGender = new HashMap<>();
                mapGender.put("male", ss[0]);
                mapGender.put("female", ss[1]);
                gender.add(mapGender);
            }
            m.put("gender", gender);
            list.add(m);
        }

        map = new HashMap<>();
        map.put("head", castEthnicity);
        map.put("data", list);
        return map;
    }

    @PostMapping("/SendSMS/{option}")
    public Object sendSMS(@PathVariable int option, @RequestBody List<SmsSendingReq> req, HttpServletRequest request) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String username = td.getUserName();
        String context = DatabaseName.getDocumentUrl();
        List<Map<String, String>> success = new ArrayList<>();
        Map<String, String> m;
        if (option == 1) {

            for (SmsSendingReq r : req) {
                m = new HashMap<>();
                m.put("id", r.getId());
                if (sms.sendSMS(r.getMobileNo(), r.getMessage(), username) == 1) {
                    m.put("status", "Y");
                } else {
                    m.put("status", "N");
                }
                success.add(m);
            }
        } else {

            for (SmsSendingReq r : req) {
                m = new HashMap<>();
                m.put("id", r.getId());
                if (r.getEmail().length() < 8) {
                    m.put("status", "N");
                    success.add(m);
                    continue;
                }
                try {
                    if (emailService.sendmail(r.getEmail(), context, r.getMessage())) {
                        m.put("status", "Y");
                    } else {
                        m.put("status", "N");
                    }
                } catch (Exception e) {
                    m.put("status", "N");
                }
                success.add(m);
            }
        }
        return success;
    }

    @GetMapping("/StudentInfo/ClassTransfer")
    public Object classTransfer(@RequestParam(required = false) Long feeId, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long subjectGroup) {
        String feeCondition = "";
        if (feeId != null) {
            String sql = "SELECT S.ID AS id,STU_NAME stuName,S.DATE_OF_BIRTH dateOfBirth,S.FATHERS_NAME fathersName,S.MOBILE_NO mobileNo,C.NAME classId,G.NAME subjectGroup,S.ROLL_NO rollNo FROM stu_billing_master M,stu_billing_detail D,bill_master B,program_master P,class_master C,student_info S,subject_group G WHERE M.BILL_NO=D.BILL_NO AND M.PROGRAM=P.ID AND D.CLASS_ID=C.ID AND S.SUBJECT_GROUP=G.ID AND D.REG_NO=S.ID AND D.BILL_ID=B.ID AND M.BILL_TYPE='DR' AND D.ACADEMIC_YEAR=IFNULL(" + academicYear + ",D.ACADEMIC_YEAR) AND M.PROGRAM=IFNULL(" + program + ",M.PROGRAM) AND D.CLASS_ID=IFNULL(" + classId + ",D.CLASS_ID) AND M.SUBJECT_GROPU=IFNULL(" + subjectGroup + ",M.SUBJECT_GROPU) AND D.BILL_ID=IFNULL(" + feeId + ",D.BILL_ID) AND D.DR >0 ORDER BY S.ACADEMIC_YEAR,S.PROGRAM,S.CLASS_ID,S.SUBJECT_GROUP,S.ID,D.BILL_ID";
            return new DB().getRecord(sql);
        }
        String sql = "SELECT S.ID AS id,STU_NAME stuName,DATE_OF_BIRTH dateOfBirth,FATHERS_NAME fathersName,MOBILE_NO mobileNo,C.NAME classId,G.NAME subjectGroup,S.ROLL_NO rollNo FROM student_info S,class_master C,subject_group G WHERE S.CLASS_ID=C.ID AND S.SUBJECT_GROUP=G.ID AND ACADEMIC_YEAR=IFNULL(" + academicYear + ",ACADEMIC_YEAR) AND  PROGRAM=IFNULL(" + program + ",PROGRAM) AND CLASS_ID=IFNULL(" + classId + ",CLASS_ID) AND SUBJECT_GROUP=IFNULL(" + subjectGroup + ",SUBJECT_GROUP) " + feeCondition + " ORDER BY classId,subjectGroup,stuName";
        return new DB().getRecord(sql);
    }

    @GetMapping("/StudentInfo")
    public Object index(@RequestParam(required = false) Long regNo, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long subjectGroup, @RequestParam(defaultValue = "") String section) {
        return service.getAll(regNo, program, classId, academicYear, subjectGroup, section);
    }

    @GetMapping("/Dropout")
    public Object dropout(@RequestParam String drop, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long subjectGroup) {
        if (drop.equalsIgnoreCase("IN")) {
            return service.getDropout(drop, program, classId, academicYear, subjectGroup);
        }
        return service.studentReport(academicYear, program, classId, subjectGroup, null, "", "");

    }

    @PostMapping("/Dropout/{status}")
    public Object dropout(@PathVariable String status, @RequestBody String jsonData) {
        return service.doDropout(jsonData, status);
    }

    @GetMapping("/Find")
    public Object indexFind(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup) {
        return service.getAll(academicYear, program, classId, subjectGroup);
    }

    @GetMapping("/Report")
    public Object studentReport(@RequestParam(required = false) Long castEthnicity, @RequestParam String gender, @RequestParam String section, @RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup) {
        return service.studentReport(academicYear, program, classId, subjectGroup, castEthnicity, gender, section);
    }

    @GetMapping("/StudentInfo/By")
    public Object byNameRollNoRegNo(@RequestParam String name) {
        String dd[] = name.split(" ");
        name = " AND S.STU_NAME LIKE '" + dd[0] + "%'";
        if (dd.length > 1) {
            name = name + " AND S.STU_NAME LIKE '%" + dd[1] + "%'";
        }
        if (dd.length > 2) {
            name = name + " AND S.STU_NAME LIKE '%" + dd[2] + "'";
        }
        return new DB().getRecord("SELECT P.NAME program,C.NAME 'class',ACADEMIC_YEAR 'academicYear',S.ID regNo,STU_NAME name,FATHERS_NAME fatherName,ROLL_NO rollNo,IFNULL(S.MOBILE_NO,'') mobileNo,IFNULL(S.EMAIL,'') email FROM student_info S,program_master P,class_master C WHERE C.ID=S.CLASS_ID AND P.ID=S.PROGRAM " + name + " ORDER BY name LIMIT 200");
    }

    @GetMapping("/ByStuFathers")
    public Object byStuFathers(@RequestParam String name) {
        String fathersName = name;
        String[] dd = name.split(" ");
        name = "AND S.STU_NAME LIKE '" + dd[0] + "%'";
        if (dd.length > 1) {
            name = name + " AND S.STU_NAME LIKE '%" + dd[1] + "%'";
        }
        if (dd.length > 2) {
            name = name + " AND S.STU_NAME LIKE '%" + dd[2] + "'";
        }


        String sql = "SELECT P.NAME program,C.NAME 'class',ACADEMIC_YEAR 'academicYear',S.ID regNo,STU_NAME name,FATHERS_NAME fatherName,ROLL_NO rollNo,IFNULL(S.MOBILE_NO,'') mobileNo,IFNULL(S.EMAIL,'') email FROM student_info S,program_master P,class_master C WHERE C.ID=S.CLASS_ID AND P.ID=S.PROGRAM " + name + "  ORDER BY name ";
        List<Map<String, Object>> list = db.getRecord(sql);
        sql = "SELECT P.NAME program,C.NAME 'class',ACADEMIC_YEAR 'academicYear',S.ID regNo,STU_NAME name,FATHERS_NAME fatherName,ROLL_NO rollNo,IFNULL(S.MOBILE_NO,'') mobileNo,IFNULL(S.EMAIL,'') email FROM student_info S,program_master P,class_master C WHERE C.ID=S.CLASS_ID AND P.ID=S.PROGRAM AND (FATHERS_NAME LIKE '" + fathersName + "%' OR S.ID='" + fathersName + "') ORDER BY fatherName ";
        List<Map<String, Object>> l = db.getRecord(sql);
        list.addAll(l);
        return list;
    }

    @GetMapping("/StudentInfo/Byname")
    public Object index(@RequestParam String name) {
        return new DB().getRecord("SELECT PROGRAM program,CLASS_ID 'classId',ACADEMIC_YEAR 'academicYear',ID regNo ,SUBJECT_GROUP subjectGroup,STU_NAME name,ROLL_NO rollNo,FATHERS_NAME fatherName FROM student_info WHERE STU_NAME LIKE '%" + name + "%' OR ID='" + name + "' ORDER BY name");
    }

    @GetMapping("/StudentInfo/ByfathersName")
    public Object byfathersName(@RequestParam String name) {
        return new DB().getRecord("SELECT PROGRAM program,CLASS_ID 'classId',ACADEMIC_YEAR 'academicYear',ID regNo ,SUBJECT_GROUP subjectGroup,STU_NAME name,ROLL_NO rollNo,FATHERS_NAME fatherName FROM student_info WHERE (FATHERS_NAME LIKE '%" + name + "%' OR guardians_name like '%" + name + "%') ORDER BY fatherName");
    }

    @GetMapping("/StudentInfo/{regNo}")
    public Map<String, Object> byStudentRegNumber(@PathVariable Long regNo) {
        List<Map<String, Object>> l = new DB().getRecord("SELECT PROGRAM program,CLASS_ID 'classId',ACADEMIC_YEAR 'academicYear',ID regNo ,SUBJECT_GROUP subjectGroup,STU_NAME name,ROLL_NO rollNo,FATHERS_NAME fatherName FROM student_info WHERE ID = " + regNo + " ORDER BY fatherName");
        if (l.isEmpty()) {
            throw new CustomException("Invalid Regd No.");
        }
        return l.get(0);
    }

    @PostMapping("/StudentInfo")
    public Object doSave(@RequestBody StudentInfo obj) throws IOException {
        return service.save(obj);
    }

    @PostMapping("/ClassTransfer")
    public ResponseEntity<String> classTransfer(@RequestBody ClassTransferReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(service.classTransfer(req));
    }

    @PutMapping("/StudentInfo/{id}")
    public Object doUpdate(@RequestBody StudentInfo obj, @PathVariable long id) throws IOException {
        return service.update(obj, id);
    }

    @DeleteMapping("/StudentInfo/{id}")
    public Object doDelete(@PathVariable String id) {
        return service.delete(id);
    }

    @GetMapping("/IDCardPrint/{regNo}")
    public ResponseEntity<List<Map<String, Object>>> idCardPrint(@PathVariable String regNo) {
        return ResponseEntity.status(HttpStatus.OK).body(db.getRecord("SELECT IFNULL(P.NAME, '') as program, IFNULL(FATHERS_NAME, '') as fathersName, IFNULL(MOTHERS_NAME, '') as mothersName, CONCAT(IFNULL(MUNICIPAL, ''), '-', IFNULL(WARD_NO, '')) as address, IFNULL(PHOTO, '') as photo, IFNULL(S.ID, '') as regNo, IFNULL(UPPER(STU_NAME), '') as studentName, IFNULL(C.NAME, '') as className, IFNULL(A.YEAR, '') as academicYear, IFNULL(S.ROLL_NO, '') as rollNo, IFNULL(S.MOBILE_NO, '') as mobileNo, IFNULL(S.DATE_OF_BIRTH, '') as dob, g.NAME as subject,ifnull(S.section,'') as section FROM student_info S join class_master C on S.CLASS_ID = C.ID join academic_year A on S.ACADEMIC_YEAR = A.ID join program_master P on S.PROGRAM = P.ID join subject_group g on g.ID = S.SUBJECT_GROUP where S.ID IN(" + regNo + ")"));
    }

    @PostMapping("/Certificate-old")
    public Object certificate(@RequestBody Map<String, Object> req) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String regNo = req.get("regNo").toString();
        String division = req.get("division").toString();
        String receiveBy = req.get("receiveBy").toString();
        String nebTuRegNo = req.get("nebTuRegNo").toString();
        String transIssuedDate = req.get("transIssuedDate").toString();
        String transcriptNo = req.get("transcriptNo").toString();
        String issueDate = DateConverted.adToBs(new Date());

        Map map;
        String sql = "SELECT id FROM fiscal_year WHERE SYSDATE() BETWEEN `START_DATE` AND `END_DATE`";
        map = db.getRecord(sql).get(0);
        long fiscalYear = Long.parseLong(map.get("id").toString());
        sql = "SELECT IFNULL(MAX(sn),0)+1 sn FROM certificate_issue WHERE fiscal_year=" + fiscalYear;
        map = db.getRecord(sql).get(0);
        long sn = Long.parseLong(map.get("sn").toString());
        sql = "SELECT 'TU/NEB' AS affiliated,IFNULL(ESTABLISH_YEAR,'') establishYear,NAME name,EMAIL email,TEL telNo,URL url,PROVINCE province,DISTRICT district,MUNICIPAL municipal,WARD_NO wardNo,ADDRESS address FROM organization_master";
        map = db.getRecord(sql).get(0);
        sql = "SELECT IFNULL(P.NAME,'') program,IFNULL(FATHERS_NAME,'') fathersName,IFNULL(MOTHERS_NAME,'') mothersName,CONCAT(IFNULL(MUNICIPAL,''),'-',IFNULL(WARD_NO,''),', ',IFNULL(DISTRICT,'')) address,IFNULL(PHOTO,'') photo,IFNULL(S.ID,'') regNo,IFNULL(UPPER(STU_NAME),'') studentName,SYSDATE() tillDate,IFNULL(C.NAME,'') className,CONCAT('20',IFNULL(S.`ACADEMIC_YEAR`,'')) academicYear,IFNULL(S.ROLL_NO,'') rollNo,IFNULL(S.MOBILE_NO,'') mobileNo,IFNULL(S.DATE_OF_BIRTH,'') dob,G.NAME subject FROM student_info S,class_master C,program_master P,subject_group G WHERE  S.CLASS_ID=C.ID AND S.PROGRAM=P.ID AND S.SUBJECT_GROUP=G.ID AND S.ID=" + regNo;
        List<Map<String, Object>> list = new ArrayList<>();
        String level;
        for (Map m : db.getRecord(sql)) {
            String program = m.get("program").toString().toUpperCase();
            if (program.startsWith("B")) {
                level = "Bachelor";
            } else if (program.startsWith("M")) {
                level = "Master";
            } else if (program.contains("+2") || program.contains("NEB") || program.contains("PLUS")) {
                level = "+2";
            } else {
                level = m.get("className").toString();
            }
            sql = "INSERT INTO certificate_issue (reg_no, division, issue_by, issue_date, receive_by,neb_tu_reg_no,trans_issued_date,transcript_no,fiscal_year,sn) VALUES (" + regNo + ", '" + division + "', '" + td.getUserName() + "', '" + issueDate + "', '" + receiveBy + "','" + nebTuRegNo + "','" + transIssuedDate + "','" + transcriptNo + "','" + fiscalYear + "','" + sn + "')";
            Session session = HibernateUtil.getSession();
            try {
                Transaction tr = session.beginTransaction();
                session.createSQLQuery(sql).executeUpdate();
                tr.commit();
            } catch (Exception e) {
                sql = "SELECT division,neb_tu_reg_no,trans_issued_date,transcript_no,fiscal_year,sn FROM certificate_issue WHERE reg_no=" + regNo;
                map = db.getRecord(sql).get(0);
                nebTuRegNo = map.get("neb_tu_reg_no").toString();
                transIssuedDate = map.get("trans_issued_date").toString();
                transcriptNo = map.get("transcript_no").toString();
                division = map.get("division").toString();
                sn = Integer.parseInt(map.get("sn").toString());
            }
            session.close();
            m.put("level", level);
            m.put("issueDate", issueDate);
            m.put("userName", td.getUserName());
            m.put("division", division);
            m.put("transIssuedDate", transIssuedDate);
            m.put("transcriptNo", transcriptNo);
            m.put("nebTuRegNo", nebTuRegNo);
            m.put("sn", sn);
            list.add(m);
        }

        map.put("student", list);
        return map;
    }
}
