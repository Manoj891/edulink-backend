/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.swagger.student;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.StudentInfoDao;
import com.ms.ware.online.solution.school.entity.student.Annex4bDetail;
import com.ms.ware.online.solution.school.entity.student.Annex4bMaster;
import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/ugc/")
public class Annex4BRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    StudentInfoDao da;
    @Autowired
    private DB db;
    @Autowired
    private Message message;
    @GetMapping("Data")
    public Object data(@RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long academicYear) {

       
        Map m, map = new HashMap();
        String sql;
        try {
            sql = "SELECT YEAR academicYear FROM academic_year WHERE ID=" + academicYear;
            m = (Map) db.getRecord(sql).get(0);
            map.put("academicYear", m.get("academicYear"));
        } catch (Exception e) {
            map.put("academicYear", "All");
        }
        try {
            sql = "SELECT NAME program FROM program_master WHERE ID=" + program;
            m = (Map) db.getRecord(sql).get(0);
            map.put("program", m.get("program"));
        } catch (Exception e) {
            map.put("program", "All");
        }
        try {
            sql = "SELECT NAME AS 'class' FROM class_master WHERE ID=" + classId;
            m = (Map) db.getRecord(sql).get(0);
            map.put("class", m.get("class"));
        } catch (Exception e) {
            map.put("class", "All");
        }
        try {
            sql = "SELECT NAME AS subjectGroup FROM subject_group WHERE ID=" + classId;
            m = (Map) db.getRecord(sql).get(0);
            map.put("subjectGroup", m.get("subjectGroup"));
        } catch (Exception e) {
            map.put("subjectGroup", "All");
        }
        sql = "SELECT IFNULL(PHOTO,'') photo,IFNULL(DATE_OF_BIRTH,'') dobBs,IFNULL(MOTHERS_NAME,'') mothersName,IFNULL(GUARDIANS_RELATION,'') relation,CONCAT(MUNICIPAL,'-',WARD_NO,', ',DISTRICT) address,IFNULL(MOTHERS_OCCUPATION,'') motherOccupation,IFNULL(FATHERS_MOBILE,'') fathersMobileNo,"
                + "IFNULL(GUARDIANS_ADDRRESS,'') guardianAddress,IFNULL(ADMISSION_YEAR,S.ACADEMIC_YEAR) admissionYear,"
                + "MARITAL_STATUS maritalStatus,STUDENT_ID regNo,STU_NAME name,GENDER gender,IFNULL(MARITAL_STATUS,'') marritalStatus,IFNULL(CAST_ETHNICITY,'') ethnicity,IFNULL(MOBILE_NO,'') mobileNo,IFNULL(EMAIL,'') email,FATHERS_NAME fatherName,IFNULL(FATHERS_OCCUPATION,'') occupation,IFNULL(FATHERS_MOBILE,'') parentTelephone,"
                + "IFNULL(GUARDIANS_NAME,'') guardianName,IFNULL(GUARDIANS_MOBILE,'') guardianTelephone,'' boardRegNo,'' boardName,1 faculty,1 level,T.ACADEMIC_YEAR admisionYear,1 AS class FROM student_info S,class_transfer T WHERE S.ID=T.STUDENT_ID AND T.CLASS_ID=ifnull(" + classId + ",T.CLASS_ID) AND T.ACADEMIC_YEAR=ifnull(" + academicYear + ",T.ACADEMIC_YEAR) AND T.PROGRAM=ifnull(" + program + " ,T.PROGRAM) AND T.SUBJECT_GROUP=ifnull(" + subjectGroup + ",T.SUBJECT_GROUP) ORDER BY STU_NAME";
        map.put("data", db.getRecord(sql));
        return map;
    }

    @GetMapping("Annex4B")
    public Object annex4B(@RequestParam(required = false) Long regNo, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long academicYear) {
       
        Map map, subMap, m = new HashMap();
        String masterId;
        String sql;
        try {
            if (regNo > 0) {
                try {
                    sql = "SELECT `ACADEMIC_YEAR`,`PROGRAM`,`CLASS_ID`,`SUBJECT_GROUP` FROM student_info WHERE `ID`=" + regNo;
                    map = (Map) db.getRecord(sql).get(0);
                    program = Long.parseLong(map.get("PROGRAM").toString());
                    classId = Long.parseLong(map.get("CLASS_ID").toString());
                    academicYear = Long.parseLong(map.get("ACADEMIC_YEAR").toString());
                    subjectGroup = Long.parseLong(map.get("SUBJECT_GROUP").toString());
                } catch (Exception e) {
                    return message.respondWithError("Invalid Reg No!!");
                }
            } else {
                regNo = null;
            }
        } catch (Exception e) {
            regNo = null;
        }
        try {
            sql = "SELECT YEAR academicYear FROM academic_year WHERE ID=" + academicYear;
            map = (Map) db.getRecord(sql).get(0);
            m.put("academicYear", map.get("academicYear"));
        } catch (Exception e) {
            m.put("academicYear", "All");
        }
        try {
            sql = "SELECT NAME program FROM program_master WHERE ID=" + program;
            map = (Map) db.getRecord(sql).get(0);
            m.put("program", map.get("program"));
        } catch (Exception e) {
            m.put("program", "All");
        }
        try {
            sql = "SELECT NAME AS 'class' FROM class_master WHERE ID=" + classId;
            map = (Map) db.getRecord(sql).get(0);
            m.put("class", map.get("class"));
        } catch (Exception e) {
            m.put("class", "All");
        }
        try {
            sql = "SELECT NAME AS subjectGroup FROM subject_group WHERE ID=" + classId;
            map = (Map) db.getRecord(sql).get(0);
            m.put("subjectGroup", map.get("subjectGroup"));
        } catch (Exception e) {
            m.put("subjectGroup", "All");
        }

        sql = "SELECT G.SUBJECT_CODE code,M.NAME name,M.ID id FROM subject_group_detail G,subject_master M WHERE G.SUBJECT=M.ID AND PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND SUBJECT_GROUP=" + subjectGroup;
        List subList, subDataList, l, list = db.getRecord(sql);
        String subjectId[] = new String[list.size()];
        String subjectName[] = new String[list.size()];
        String subjectCode[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            map = (Map) list.get(i);
            subjectCode[i] = map.get("code").toString();
            subjectName[i] = map.get("name").toString();
            subjectId[i] = map.get("id").toString();
        }
        sql = "SELECT T.CLASS_ID classId,I.ID regNo,STU_NAME name,T.ROLL_NO rollNo,FATHER_NAME fathersName,MOBILE_NO mobileNo,IFNULL(PHOTO,'') photo  FROM student_info I,class_transfer T WHERE I.ID=T.STUDENT_ID AND  T.ACADEMIC_YEAR=" + academicYear + " AND T.PROGRAM='" + program + "' AND T.CLASS_ID='" + classId + "' AND T.SUBJECT_GROUP='" + subjectGroup + "' AND I.ID=IFNULL(" + regNo + ",I.ID) ORDER BY name";
        l = db.getRecord(sql);

        list = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            subDataList = new ArrayList();
            map = (Map) l.get(i);
            regNo = Long.parseLong(map.get("regNo").toString());
            classId = Long.parseLong(map.get("classId").toString());
            sql = "SELECT ID masterId,SEME_YEAR_NAME semesterName,YEAR1 year1,YEAR2 year2,YEAR3 year3,YEAR4 year4,YEAR5 year5,YEAR6 year6,YEAR7 year7,IFNULL(PASS_PERCENT,'') passPercent FROM annex4b_master WHERE REG_NO='" + regNo + "' AND SEME_YEAR='" + classId + "'";
            subList = db.getRecord(sql);
            if (!subList.isEmpty()) {
                subMap = (Map) subList.get(0);
                masterId = subMap.get("masterId").toString();
                map.put("subMaster", subMap);
            } else {
                sql = "SELECT '' masterId,'' semesterName,'' year1,'' year2,'' year3,'' year4,'' year5,'' year6,'' year7,'' passPercent FROM DUAL";
                map.put("subMaster", db.getRecord(sql).get(0));
                masterId = "0";
            }
            for (int j = 0; j < subjectId.length; j++) {
                if (!masterId.equalsIgnoreCase("0")) {
                    sql = "SELECT '" + subjectId[j] + "' AS subject,'" + subjectName[j] + "' subjectName,'" + subjectCode[j] + "' subjectCode,IFNULL(MO1,'') mo1,IFNULL(REM1,'') rem1,IFNULL(MO2,'') mo2,IFNULL(REM2,'') rem2,IFNULL(MO3,'') mo3,IFNULL(REM3,'') rem3,IFNULL(MO4,'') mo4,IFNULL(REM4,'') rem4,IFNULL(MO5,'') mo5,IFNULL(REM5,'') rem5,IFNULL(MO6,'') mo6,IFNULL(REM6,'') rem6,IFNULL(MO7,'') mo7,IFNULL(REM7,'') rem7,IFNULL(REMARK,'') remark FROM annex4b_detail WHERE MASTER_ID='" + masterId + "' AND SUBJECT='" + subjectId[j] + "'";
                    subList = db.getRecord(sql);
                    if (!subList.isEmpty()) {
                        subMap = (Map) subList.get(0);
                    } else {
                        sql = "SELECT '" + subjectId[j] + "' AS subject,'" + subjectName[j] + "' subjectName,'" + subjectCode[j] + "' subjectCode,'' mo1,'' rem1,'' mo2,'' rem2,'' mo3,'' rem3,'' mo4,'' rem4,'' mo5,'' rem5,'' mo6,'' rem6,'' mo7,'' rem7,'' remark FROM DUAL";
                        subMap = (Map) db.getRecord(sql).get(0);
                    }
                } else {
                    sql = "SELECT '" + subjectId[j] + "' AS subject,'" + subjectName[j] + "' subjectName,'" + subjectCode[j] + "' subjectCode,'' mo1,'' rem1,'' mo2,'' rem2,'' mo3,'' rem3,'' mo4,'' rem4,'' mo5,'' rem5,'' mo6,'' rem6,'' mo7,'' rem7,'' remark FROM DUAL";
                    subMap = (Map) db.getRecord(sql).get(0);
                }
                subDataList.add(subMap);
            }
            map.put("subject", subDataList);
            list.add(map);
        }
        m.put("student", list);
        return m;
    }

    @PostMapping("Annex4B")
    public Object annex4B(@RequestParam Long regNo, @RequestParam String passPercent, @RequestParam Integer classId, @RequestParam String semesterName, @RequestParam String year1, @RequestParam String year2, @RequestParam String year3, @RequestParam String year4, @RequestParam String year5, @RequestParam String year6, @RequestParam String year7,
            @RequestParam String[] mo1, @RequestParam String[] mo2, @RequestParam String[] mo3, @RequestParam String[] mo4, @RequestParam String[] mo5, @RequestParam String[] mo6, @RequestParam String[] mo7,
            @RequestParam String[] rem1, @RequestParam String[] rem2, @RequestParam String[] rem3, @RequestParam String[] rem4, @RequestParam String[] rem5, @RequestParam String[] rem6, @RequestParam String[] rem7,
            @RequestParam Long[] subject, @RequestParam String[] remark) {
        
        AuthenticatedUser tv = facade.getAuthentication();;
        if (tv.isStatus()) {
            return message.respondWithError("invalid token");
        }
        List<Annex4bDetail> list = new ArrayList();
        Annex4bMaster obj = new Annex4bMaster(regNo, classId, semesterName, year1, year2, year3, year4, year5, year6, year7, passPercent);
        for (int i = 0; i < subject.length; i++) {
            list.add(new Annex4bDetail(obj.getId(), subject[i], mo1[i], mo2[i], mo3[i], mo4[i], mo5[i], mo6[i], mo7[i], rem1[i], rem2[i], rem3[i], rem4[i], rem5[i], rem6[i], rem7[i], remark[i]));
        }
        obj.setDetail(list);

        String msg;   
        int row = da.save(obj);
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else {
            msg = da.getMsg();
            if (msg.contains("PRIMARY")) {
                msg = "This Record Alredy Exist";
            }
            return message.respondWithError(msg);
        }
    }

    @GetMapping("Annex4B/Report")
    public Object annex4BReport(@RequestParam(required = false) Long regNo, @RequestParam Long program, @RequestParam Long classId, @RequestParam Long subjectGroup, @RequestParam Long academicYear) {
       
        Map map, subMap;
        String masterId;
        String sql;
        List subList, subDataList, l, list;
try {
            if (regNo > 0) {
                try {
                    sql = "SELECT `ACADEMIC_YEAR`,`PROGRAM`,`CLASS_ID`,`SUBJECT_GROUP` FROM student_info WHERE `ID`=" + regNo;
                    map = (Map) db.getRecord(sql).get(0);
                    program = Long.parseLong(map.get("PROGRAM").toString());
                    classId = Long.parseLong(map.get("CLASS_ID").toString());
                    academicYear = Long.parseLong(map.get("ACADEMIC_YEAR").toString());
                    subjectGroup = Long.parseLong(map.get("SUBJECT_GROUP").toString());
                } catch (Exception e) {
                    return message.respondWithError("Invalid Reg No!!");
                }
            } else {
                regNo = null;
            }
        } catch (Exception e) {
            regNo = null;
        }
        sql = "SELECT IFNULL(PHOTO,'') photo ,'' level,STUDENT_ID regNo,STU_NAME name,IFNULL(T.ROLL_NO,'') rollNo,IFNULL(FATHER_NAME,'') fathersName,IFNULL(MOBILE_NO,'') mobileNo,CONCAT(IFNULL(MUNICIPAL,''),'-',IFNULL(WARD_NO,''),', ',IFNULL(DISTRICT,'')) address,IFNULL(DATE_OF_BIRTH,'') dob,IFNULL(GENDER,'') gender,IFNULL(MARITAL_STATUS,'') maritalStatus ,CONCAT(IFNULL(RELIGION,''),' ',IFNULL(CAST_ETHNICITY,'')) ethnicity,IFNULL(EMAIL,'') email,IFNULL(FATHERS_OCCUPATION,'') fathersOccupation,IFNULL(FATHERS_MOBILE,'') fathersMobileNo ,IFNULL(MOTHER_NAME,'') mothersName,IFNULL(MOTHERS_OCCUPATION,'') mothersOccupation ,IFNULL(MOTHERS_MOBILE,'') mothersMobileNo,IFNULL(GUARDIANS_NAME,'') guradianName,IFNULL(GUARDIANS_MOBILE,'') guradianMobileNo,IFNULL(GUARDIANS_RELATION,'') guradianRelation,'' program,IFNULL(ADMISSION_YEAR,'') academicYear,'' boardName,'' boardRegNo FROM student_info I,class_transfer T WHERE I.ID=T.STUDENT_ID AND  T.ACADEMIC_YEAR='" + academicYear + "' AND T.PROGRAM='" + program + "' AND T.CLASS_ID='" + classId + "' AND T.SUBJECT_GROUP='" + subjectGroup + "' AND STUDENT_ID=IFNULL("+regNo+",STUDENT_ID)";
        l = db.getRecord(sql);

        list = new ArrayList();
        for (int i = 0; i < l.size(); i++) {
            subDataList = new ArrayList();
            map = (Map) l.get(i);
            regNo = Long.parseLong(map.get("regNo").toString());
            sql = "SELECT ID masterId,SEME_YEAR_NAME semesterName,IFNULL(PASS_PERCENT,'Pass % NA ') passPercent,YEAR1 year1,YEAR2 year2,YEAR3 year3,YEAR4 year4,YEAR5 year5,YEAR6 year6,YEAR7 year7 FROM annex4b_master WHERE REG_NO='" + regNo + "' ORDER BY SEME_YEAR";
            subList = db.getRecord(sql);
            if (subList.isEmpty()) {
                continue;
            }
            for (int j = 0; j < subList.size(); j++) {
                subMap = (Map) subList.get(j);
                masterId = subMap.get("masterId").toString();
                sql = "SELECT D.SUBJECT AS subject,S.NAME subjectName,IFNULL(REMARK,'NA') remark,IFNULL(MO1,'') mo1,IFNULL(REM1,'') rem1,IFNULL(MO2,'') mo2,IFNULL(REM2,'') rem2,IFNULL(MO3,'') mo3,IFNULL(REM3,'') rem3,IFNULL(MO4,'') mo4,IFNULL(REM4,'') rem4,IFNULL(MO5,'') mo5,IFNULL(REM5,'') rem5,IFNULL(MO6,'') mo6,IFNULL(REM6,'') rem6,IFNULL(MO7,'') mo7,IFNULL(REM7,'') rem7 FROM annex4b_detail D,subject_master S WHERE D.SUBJECT=S.ID AND MASTER_ID='" + masterId + "' ";
                subMap.put("subjectList", db.getRecord(sql));
                subDataList.add(subMap);
            }
            map.put("subject", subDataList);
            sql = "SELECT EDUCATION education,BOARD board,PASSED_YEAR passedYear,PERCENTAGE percent,RESULT divGrade FROM previous_education WHERE REG_NO='" + regNo + "'";
            map.put("education", db.getRecord(sql));

            list.add(map);
        }

        return list;
    }
}
