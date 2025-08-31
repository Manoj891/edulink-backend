package com.ms.ware.online.solution.school.service.exam;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.exam.ExamMarkEntryDao;

import com.ms.ware.online.solution.school.dto.*;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.ware.online.solution.school.entity.exam.ExamMarkEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
public class ExamMarkEntryServiceImp implements ExamMarkEntryService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private ExamMarkEntryDao da;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(Long exam, Long program, Long classId, Long subjectGroup, Long subject, String section, String order) {
        if (!section.isEmpty()) section = "  AND S.SECTION='" + section + "'";
        return da.getRecord("SELECT R.ID AS examRegNo, R.EXAM_ROLL_NO AS examRollNo, R.STUDENT_ID AS regNo, S.STU_NAME AS stuName, IFNULL(ME.TH_OM, 0) AS thOm, IFNULL(ME.PR_OM, 0) AS prOm, ifnull(extra_activity,'') activity,ifnull(ME.approve_date,'') approve FROM exam_student_registration R JOIN student_info S ON R.STUDENT_ID = S.ID LEFT JOIN exam_mark_entry ME ON ME.EXAM_REG_ID = R.ID AND ME.SUBJECT = " + subject + " WHERE R.APPROVE_DATE IS NOT NULL AND R.EXAM = " + exam + " AND R.PROGRAM = " + program + " AND R.CLASS_ID = " + classId + " AND R.SUBJECT_GROUP =" + subjectGroup + section + " AND (DROP_OUT is null or DROP_OUT!='Y') ORDER BY " + order);
    }

    @Override
    public Object getApprove(Long exam, Long program, Long classId, Long subjectGroup, Long subject) {
        sql = "SELECT CONCAT(ME.EXAM_REG_ID,'-',ME.SUBJECT) AS id,R.EXAM_ROLL_NO examRollNo,STUDENT_ID regNo,S.STU_NAME stuName,IFNULL(TH_OM,0) AS thOm,IFNULL(PR_OM,0) AS prOm FROM exam_student_registration R,student_info S,exam_mark_entry ME WHERE R.STUDENT_ID=S.ID AND ME.EXAM_REG_ID=R.ID AND ME.APPROVE_DATE IS NULL AND R.EXAM=" + exam + " AND R.PROGRAM=" + program + " AND R.CLASS_ID=" + classId + " AND R.SUBJECT_GROUP=" + subjectGroup + " AND ME.SUBJECT=" + subject + " ORDER BY examRollNo";
        return da.getRecord(sql);
    }

    @Override
    public Object save(ExamMarkEntryReq req) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        return MarkEntry(req, td.getUserName());
    }

    private Object MarkEntry(ExamMarkEntryReq req, String enterBy) {
        row = 0;
        try {

            long exam = req.getExam();
            long program = req.getProgram();
            long classId = req.getClassId();
            long subjectGroup = req.getSubjectGroup();
            long subject = req.getSubject();
            Date enterDate = DateConverted.bsToAdDate(req.getEnterDate());
            long examRegId;

            float thOm, prOm, thFm, prFm;
            sql = "SELECT IFNULL(TH_FM,0) thFm,IFNULL(PR_FM,0) prFm FROM subject_group_detail WHERE PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND SUBJECT_GROUP=" + subjectGroup + " AND SUBJECT=" + subject;
            List<Map<String, Object>> l = da.getRecord(sql);
            if (l.isEmpty()) {
                return message.respondWithError("Invalid Subject");
            }
            Map<String, Object> m = l.get(0);
            thFm = Float.parseFloat(m.get("thFm").toString());
            prFm = Float.parseFloat(m.get("prFm").toString());
            if (thFm > 0) thFm = thFm + 0.01F;
            if (prFm > 0) prFm = prFm + 0.01F;

            List<Map<String, Object>> list;
            ExamMarkEntry obj;
            for (ExamMarkEntryDetail d : req.getObj()) {
                examRegId = d.getExamRegId();
                thOm = d.getThOm();
                prOm = d.getPrOm();

                if (thOm > thFm || prOm > prFm) {
                    msg = "Obtain mark must be less then full mark";
                    continue;
                } else {
                    sql = "SELECT IFNULL(APPROVE_DATE,'') AS approveDate FROM exam_mark_entry WHERE EXAM_REG_ID='" + examRegId + "' AND SUBJECT='" + subject + "'";
                    list = da.getRecord(sql);
                    if (!list.isEmpty()) {

                        if (list.get(0).get("approveDate").toString().length() >= 10) {
                            continue;
                        }
                    }
                }
                obj = new ExamMarkEntry(examRegId, subject, exam, d.getExamRollNo(), d.getStudentRegNo(), thOm, prOm, enterBy, enterDate, d.getExtraActivity());
                row = row + da.save(obj);
                msg = da.getMsg();
            }

            if (row > 0) {
                return message.respondWithMessage(row + " Record Saved!!");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object doApprove(String jsonData) {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String enterBy = td.getUserName();
        String[] jsonDataArray = message.jsonDataToStringArray(jsonData);
        row = 0;
        try {
            Map map = new ObjectMapper().readValue(jsonDataArray[0], new TypeReference<>() {
            });
            String enterDate = DateConverted.bsToAd(map.get("enterDate").toString());

            List list = new ObjectMapper().readValue(jsonDataArray[1], new TypeReference<List>() {
            });

            for (Object o : list) {
                sql = "UPDATE exam_mark_entry SET APPROVE_BY='" + enterBy + "',APPROVE_DATE='" + enterDate + "' WHERE CONCAT(EXAM_REG_ID,'-',SUBJECT)='" + o + "'";
                row = row + da.delete(sql);
                msg = da.getMsg();
            }

            if (row > 0) {
                return message.respondWithMessage(row + " Record Approved!!");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object delete(String id) {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        sql = "DELETE FROM exam_mark_entry WHERE CONCAT(EXAM_REG_ID,'-',SUBJECT)='" + id + "' AND APPROVE_DATE IS NULL";
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
    public Object getFinalReport(int system, Long exam, Long program, Long classId, Long subjectGroup, Long regNo) {
        MarkReportGenerate reportGenerate = new MarkReportGenerate();
        return reportGenerate.getFinalReport(system, exam, program, classId, subjectGroup, regNo);
    }

    @Override
    public Object getReport(int system, Long exam, Long program, Long classId, Long subjectGroup, Long sId, String section, String result) {
        MarkReportGenerate reportGenerate = new MarkReportGenerate();
        AuthenticatedUser td = facade.getAuthentication();
        ;
        String studentId = "NULL";
        String type;
        type = "ORG";
        if (td.getUserType().equalsIgnoreCase("STU")) {
            type = "STU";
            studentId = td.getUserId();
            sql = "SELECT EXAM_ROLL_NO examRollNo,SUBJECT_GROUP subjectGroup,PROGRAM program,CLASS_ID classId FROM exam_student_registration WHERE EXAM='" + exam + "' AND STUDENT_ID='" + studentId + "'";
            List<Map<String, Object>> ll = da.getRecord(sql);
            if (ll.isEmpty()) {
                return message.respondWithError("Exam not appppp token");
            }
            Map<String, Object> mm = ll.get(0);
            classId = Long.parseLong(mm.get("classId").toString());
            program = Long.parseLong(mm.get("program").toString());
            subjectGroup = Long.parseLong(mm.get("subjectGroup").toString());

        }

        String publishDate, resultPublishDate, workingDays = "";
        boolean publishStatus;
        Map<String, Object> map;
        sql = "SELECT IFNULL(WORKING_DAYS,'') workingDays,IFNULL(PUBLISH_DATE,'" + DateConverted.today() + "') publishDate FROM exam_result_publish WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
        List<Map<String, Object>> l = da.getRecord(sql);
        if (l.isEmpty()) {
            publishDate = DateConverted.today();
            resultPublishDate = "Not Publish.";
            if (type.equalsIgnoreCase("STU")) {
                return message.respondWithError("Result Not Published!!");
            }
            publishStatus = false;
        } else {
            map = l.get(0);
            workingDays = map.get("workingDays").toString();
            publishDate = map.get("publishDate").toString();
            resultPublishDate = DateConverted.adToBs(publishDate);
            publishStatus = true;
        }
        switch (system) {
            case 1:
                sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM grading_system WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                break;
            case 2:
                sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM grading_system_two WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                break;
            case 3:
                sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM percentage_system WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                break;
            default:
                return message.respondWithError("Invalid System");
        }

        List<GradingSystemReq> gradingSystem = da.getRecord(sql).stream()
                .map(m -> GradingSystemReq.builder()
                        .gpa(Float.parseFloat(m.get("gpa").toString()))
                        .grade(m.get("grade").toString())
                        .rangFrom(Float.parseFloat(m.get("rangFrom").toString()))
                        .remark(m.get("remark").toString())
                        .build())
                .collect(Collectors.toList());
        if (gradingSystem.isEmpty()) {
            return message.respondWithError("Please define Result Rule.");
        }

        reportGenerate.rangFrom = new float[gradingSystem.size()];
        reportGenerate.gpa = new float[gradingSystem.size()];
        reportGenerate.remark = new String[gradingSystem.size()];
        reportGenerate.grade = new String[gradingSystem.size()];
        for (int i = 0; i < gradingSystem.size(); i++) {
            GradingSystemReq m = gradingSystem.get(i);
            reportGenerate.rangFrom[i] = m.getRangFrom();
            reportGenerate.gpa[i] = m.getGpa();
            reportGenerate.remark[i] = m.getRemark();
            reportGenerate.grade[i] = m.getGrade();
        }
        if (publishStatus) {
            sql = "SELECT SUBJECT_CODE subjectCode,SUBJECT_NAME subjectName,GROUP_NAME groupName,TH_FM thFm,TH_PM thPm,PR_FM prFm,PR_PM prPm,SUBJECT subject,CREDIT_HOUR credit,ifnull(CREDIT_PH,0) creditPh,ifnull(SUBJECT_CODE_PH,'') codePh FROM exam_result_publish_subject WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND SUBJECT_GROUP=" + subjectGroup + " ORDER BY in_order ";
        } else {
            sql = "SELECT D.SUBJECT_CODE subjectCode,S.NAME subjectName,M.NAME groupName,D.TH_FM thFm,D.TH_PM thPm,D.PR_FM prFm,D.PR_PM prPm,D.SUBJECT subject,CREDIT credit,ifnull(CREDIT_PH,0) creditPh,ifnull(SUBJECT_CODE_PH,'') codePh FROM subject_group_detail D,subject_group M,subject_master S WHERE M.ID=D.SUBJECT_GROUP AND D.SUBJECT=S.ID AND D.PROGRAM=" + program + " AND D.CLASS_ID=" + classId + " AND D.SUBJECT_GROUP=" + subjectGroup + " ORDER BY in_order";
        }

        List<Map<String, Object>> subjectList = da.getRecord(sql);
        StringBuilder markSubject = new StringBuilder();
        int noOfSubject = subjectList.size();
        float totalFullMark = 0;
        String[] subjectName = new String[noOfSubject];
        float[] thFm, prFm, totalFm, thPm, prPm;
        thFm = new float[noOfSubject];
        prFm = new float[noOfSubject];
        thPm = new float[noOfSubject];
        prPm = new float[noOfSubject];
        totalFm = new float[noOfSubject];
        for (int i = 0; i < noOfSubject; i++) {
            Map<String, Object> m = subjectList.get(i);
            if (result.isEmpty() || result.equalsIgnoreCase("TH")) {
                thFm[i] = Float.parseFloat(m.get("thFm").toString());
                thPm[i] = Float.parseFloat(m.get("thPm").toString());
            } else {
                thFm[i] = 0;
                thPm[i] = 0;
            }
            if (result.isEmpty() || result.equalsIgnoreCase("PR")) {
                prFm[i] = Float.parseFloat(m.get("prFm").toString());
                prPm[i] = Float.parseFloat(m.get("prPm").toString());
            } else {
                prFm[i] = 0;
                prPm[i] = 0;
            }
            totalFm[i] = thFm[i] + prFm[i];
            totalFullMark = totalFullMark + totalFm[i];
            subjectName[i] = m.get("subjectName").toString();
            markSubject.append(",IFNULL((SELECT CONCAT(TH_OM, ':', PR_OM,':',ifnull(extra_activity,'-')) FROM exam_mark_entry ME WHERE ME.EXAM_REG_ID=R.ID AND ME.SUBJECT=").append(m.get("subject")).append("),'0:0:-') AS sub").append(i);
        }
        if (!section.isEmpty()) section = "  AND S.SECTION='" + section + "'";

        sql = "SELECT ifnull(section,'') section,ifnull(roll_no,'') rollNo,ifnull(PHOTO,'') photo,IFNULL(R.REMARK,'') AS remark,IFNULL(R.PRESENT_DAYS,'0') AS presentDays,IFNULL(R.ABSENT_DAYS,'0') AS absentDays,CONCAT(IFNULL(MUNICIPAL,''),' ',IFNULL(WARD_NO,''),', ',IFNULL(DISTRICT,'')) AS address,IFNULL(EXAM_ROLL_NO,'') examRollNo,IFNULL(UPPER(S.STU_NAME),'') stuName,IFNULL(UPPER(FATHERS_NAME),'') fathersName,ifnull(MOTHERS_NAME,'') mothersName,IFNULL(DISTRICT,'') DISTRICT,IFNULL(WARD_NO,'') WARD_NO,IFNULL(MUNICIPAL,'') MUNICIPAL,IFNULL(PROVINCE,'') PROVINCE,IFNULL(DATE_OF_BIRTH,'') AS dobBs,IFNULL(R.board_symbol_no,'') boardSymbolNo,ifnull(S.board_regd_no,'') boardRegNo,S.ID regNo" + markSubject + " FROM exam_student_registration R,student_info S WHERE R.STUDENT_ID=S.ID AND R.STUDENT_ID=IFNULL(" + studentId + ",R.STUDENT_ID) AND R.EXAM=" + exam + " AND  R.PROGRAM=" + program + " AND  R.CLASS_ID=" + classId + " AND  R.SUBJECT_GROUP=" + subjectGroup + section + " and S.ID=IFNULL(" + sId + ",S.ID) ORDER BY stuName";
        List<Map<String, Object>> studentList = da.getRecord(sql);
        List<StudentResult> list = new ArrayList<>();

        float finalTotalFullMark = totalFullMark;
        studentList.forEach(map1 -> list.add(reportGenerate.getStudentResult(map1, subjectName, finalTotalFullMark, noOfSubject, thFm, prFm, thPm, prPm, totalFm, system, result)));
        map = new HashMap<>();
        map.put("student", list);
        map.put("subject", subjectList);
        sql = "SELECT NAME 'name' FROM class_master WHERE ID=" + classId;
        map.put("class", da.getRecord(sql).get(0));
        sql = "SELECT NAME 'name' FROM program_master WHERE ID=" + program;
        map.put("program", da.getRecord(sql).get(0));
        sql = "SELECT EXAM_NAME 'name',e.ID,year_ad yearAd,ACADEMIC_YEAR 'academicYear',t.name terminal FROM exam_master e join exam_terminal t on e.terminal = t.id WHERE e.ID=" + exam;
        map.put("exam", da.getRecord(sql).get(0));
        map.put("gradingSystem", gradingSystem);
        map.put("publishDate", resultPublishDate);
        map.put("workingDays", workingDays);
        map.put("organization", da.getRecord("select ifnull(province,'') province,ifnull(ESTABLISH_YEAR,'') establishYear,ifnull(organization_name,name) name,concat(ifnull(MUNICIPAL,''),'-',ifnull(WARD_NO,''),', ',ifnull(DISTRICT,'')) address,ifnull(slogan,'') slogan from organization_master").get(0));

        long sn = 0;

        map.put("sn", sn);
        return map;
    }

    @Override
    public Object entryTeacher(ExamMarkEntryReq req) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equalsIgnoreCase("TCR"))
            return message.respondWithError("invalid token");
        return MarkEntry(req, td.getUserName());
    }

    @Override
    public Object markUpdate(Long exam, Long regNo) {
        String sql = "SELECT EXAM_REG_ID examRegNo,SUBJECT subject,S.NAME subjectName,TH_OM thOm,PR_OM phOm FROM exam_mark_entry E,subject_master S WHERE E.SUBJECT=S.ID and EXAM=" + exam + " AND STUDENT_REG_NO=" + regNo;
        return da.getRecord(sql);
    }

    @Override
    public Object markUpdate(List<MarkUpdateReq> req) {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equals("ADM")) {
            return message.respondWithError("invalid token");
        }
        String date = DateConverted.now();
        req.forEach(t -> da.delete("UPDATE exam_mark_entry SET TH_OM=" + t.getThOm() + ",PR_OM=" + t.getPhOm() + ",ENTER_BY='" + td.getUserName() + "',ENTER_DATE='" + date + "' where EXAM_REG_ID=" + t.getExamRegNo() + " AND SUBJECT=" + t.getSubject()));
        return message.respondWithMessage(" Record Updated!!");
    }

    @Override
    public Object markUpload(HttpServletRequest request, MultipartFile markUpload, Long exam) throws IOException {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equals("ADM")) {
            return message.respondWithError("invalid token");
        }
        File f;
        da.delete("delete from mark_insert where POSTED='N'");
        String location = message.getFilepath(DatabaseName.getDocumentUrl());
        f = new File(location + "/mark/");
        try {
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
        f = new File(location + "/mark/mark-sheet.xlsx");
        markUpload.transferTo(f);
        Workbook excel = new XSSFWorkbook(new FileInputStream(f));
        int count = 0;
        for (Row row : excel.getSheetAt(0)) {
            long regNo;
            String subjectCode;
            double th, pr;
            try {
                regNo = (long) row.getCell(0).getNumericCellValue();
                if (regNo <= 0) {
                    continue;
                }
                try {
                    subjectCode = row.getCell(1).getStringCellValue().replace(" ", "");
                } catch (Exception e) {
                    subjectCode = String.valueOf((long) row.getCell(1).getNumericCellValue()).replace(" ", "");
                }
                try {
                    th = row.getCell(2).getNumericCellValue();
                } catch (Exception e) {
                    th = 0;
                }
                try {
                    pr = row.getCell(3).getNumericCellValue();
                } catch (Exception e) {
                    pr = 0;
                }
                sql = "insert into mark_insert(REG_NO,SUBJECT_CODE,TH_OM,PR_OM,exam,POSTED,GROUP_ID,program,class_id) values (" + regNo + ",'" + subjectCode + "'," + th + "," + pr + "," + exam + ",'N',null,null,null);";
                count = count + da.delete(sql);
            } catch (Exception ignored) {
            }
        }

        return markUpdateConfig(exam);
    }

    @Override
    public Object markUpload(Long exam) {
        sql = "SELECT concat(exam,'-', reg_no, '-',subject_code) id,exam, reg_no, (SELECT S.STU_NAME FROM student_info S where S.ID = reg_no) stuName, subject_code, class_id, exam_reg_id, exam_roll_no, group_id, posted, program, sub_id, (SELECT S.NAME FROM subject_master S where S.ID = sub_id)  subName, th_om th, pr_om pr,(SELECT S.NAME FROM program_master S where S.ID = program) programName,(SELECT S.NAME FROM class_master S where S.ID = class_id) className, (SELECT S.NAME FROM subject_group S where S.ID = group_id) groupName FROM mark_insert WHERE POSTED = 'N' and exam=" + exam + " ORDER BY class_id,program,group_id";
        return da.getRecord(sql);
    }

    @Override
    public Object markUpdateConfig(Long exam) {
        sql = "update subject_group_detail set SUBJECT_CODE= replace(SUBJECT_CODE,' ','')";
        da.delete(sql);
        sql = "update mark_insert set subject_code= replace(subject_code,' ','')";
        da.delete(sql);
        sql = "update mark_insert I set I.program=(SELECT R.PROGRAM FROM exam_student_registration R where R.EXAM=I.exam and  R.STUDENT_ID=I.reg_no) where exam=" + exam + " and  I.program is null";
        da.delete(sql);
        sql = "update mark_insert I set I.class_id=(SELECT R.CLASS_ID FROM exam_student_registration R where R.EXAM=I.exam and  R.STUDENT_ID=I.reg_no) where exam=" + exam + " and  I.class_id is null";
        da.delete(sql);
        sql = "update mark_insert I set I.group_id=(SELECT R.SUBJECT_GROUP FROM exam_student_registration R where R.EXAM=I.exam and  R.STUDENT_ID=I.reg_no) where exam=" + exam + " and I.group_id is null";
        da.delete(sql);
        sql = "update mark_insert I set EXAM_REG_ID =(SELECT D.ID FROM exam_student_registration D where D.EXAM=" + exam + " and D.STUDENT_ID=I.REG_NO) where EXAM_REG_ID is null";
        da.delete(sql);
        sql = "update mark_insert I set EXAM_ROLL_NO =(SELECT D.EXAM_ROLL_NO FROM exam_student_registration D where D.STUDENT_ID=I.REG_NO AND D.EXAM=" + exam + ") where EXAM_ROLL_NO is null";
        da.delete(sql);
        sql = "update mark_insert I set SUB_ID =(SELECT D.SUBJECT  FROM subject_group_detail D where D.PROGRAM = I.program  and D.CLASS_ID = I.class_id   and D.SUBJECT_GROUP = I.group_id  and D.SUBJECT_CODE = I.subject_code limit 1) where SUB_ID is null";
        da.delete(sql);
        return markUpload(exam);
    }

    @Override
    public Object markApprove(Long exam) {

        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equals("ADM")) {
            return message.respondWithError("invalid token");
        }
        String userName = td.getUserName();
        String date = DateConverted.today();
        sql = "SELECT exam, reg_no, subject_code, class_id, exam_reg_id, exam_roll_no, group_id, posted, pr_om, program, sub_id, th_om FROM mark_insert WHERE POSTED='N' and sub_id is not null and exam_reg_id is not null;";
        for (Map<String, Object> map : da.getRecord(sql)) {
            try {
                sql = "insert into exam_mark_entry (approve_by, approve_date, enter_by, enter_date, exam, exam_roll_no, exam_reg_id, subject, student_reg_no, th_om,pr_om) values ('" + userName + "', '" + date + "', '" + userName + "', '" + date + "', " + exam + ", '" + map.get("exam_roll_no") + "', " + map.get("exam_reg_id") + ", " + map.get("sub_id") + ", " + map.get("reg_no") + ", " + map.get("th_om") + ", " + map.get("pr_om") + ")";
                if (da.delete(sql) == 1) {
                    sql = "update mark_insert set posted='Y' where exam='" + exam + "' and reg_no='" + map.get("reg_no") + "' and subject_code='" + map.get("subject_code") + "';";
                    da.delete(sql);
                } else if (da.getMsg().contains("Duplicate entry")) {
                    sql = "update mark_insert set posted='Y' where exam='" + exam + "' and reg_no='" + map.get("reg_no") + "' and subject_code='" + map.get("subject_code") + "';";
                    da.delete(sql);
                    System.out.println("Else Updated.");
                }

            } catch (Exception ignored) {
            }

        }
        return markUpload(exam);
    }

    @Override
    public CashSystem cashSystem(Long exam, Long program, Long classId, Long subjectGroup, Long regNo, String section) {
        if (!section.isEmpty()) section = "  AND S.SECTION='" + section + "'";
        String className = "", examName = "";
        sql = "select exam_name from exam_master where id=" + exam;
        for (Map<String, Object> stringObjectMap : da.getRecord(sql)) {
            examName = stringObjectMap.get("exam_name").toString();
        }
        sql = "select name from class_master where id=" + classId;
        for (Map<String, Object> stringObjectMap : da.getRecord(sql)) {
            className = stringObjectMap.get("name").toString();
        }
        sql = "SELECT R.ID AS examRegNo, R.EXAM_ROLL_NO AS examRollNo, R.STUDENT_ID AS regNo, S.STU_NAME AS stuName,ifnull(section,'') section FROM exam_student_registration R JOIN student_info S ON R.STUDENT_ID = S.ID where R.EXAM = " + exam + " AND R.PROGRAM = " + program + " AND R.CLASS_ID = " + classId + " AND R.SUBJECT_GROUP =" + subjectGroup + section + "  ORDER BY stuName,roll_no";
        List<CashSystemStudent> list = new ArrayList<>();
        da.getRecord(sql).forEach(d -> {
            String examRegNo = d.get("examRegNo").toString();
            List<CashSystemMark> mark = new ArrayList<>();
            da.getRecord("select s.name, m.extra_activity activity from exam_mark_entry m join subject_master s on m.subject = s.id where exam_reg_id=" + examRegNo + " order by s.name")
                    .forEach(m -> mark.add(CashSystemMark.builder().mark(m.get("activity").toString()).subject(m.get("name").toString()).build()));

            list.add(CashSystemStudent.builder()
                    .examRegNo(examRegNo)
                    .examRollNo(d.get("examRollNo").toString())
                    .regNo(d.get("regNo").toString())
                    .stuName(d.get("stuName").toString())
                    .section(d.get("section").toString())

                    .mark(mark)
                    .build());

        });
        CashSystem res = CashSystem.builder().examName(examName).className(className).student(list).publishedDate("Not Published").build();
        sql = "SELECT IFNULL(PUBLISH_DATE,'Not Published') publishDate FROM exam_result_publish WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
        da.getRecord(sql).forEach(d -> {
            String publishDate = d.get("publishDate").toString();
            if (publishDate == null || publishDate.isEmpty()) publishDate = "Not Published";
            res.setPublishedDate(publishDate);
        });


        return res;
    }

    @Override
    public List<GpaWise> gpaWiseReport(Integer system, Long exam, Long program, Long subjectGroup) {
        MarkReportGenerate reportGenerate = new MarkReportGenerate();
        List<GpaWise> gpaWises = new ArrayList<>();

        String publishDate;
        boolean publishStatus;
        Map<String, Object> map;
        for (int classId = 1; classId <= 15; classId++) {
            sql = "SELECT IFNULL(WORKING_DAYS,'') workingDays,IFNULL(PUBLISH_DATE,'" + DateConverted.today() + "') publishDate FROM exam_result_publish WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
            List<Map<String, Object>> l = da.getRecord(sql);
            if (l.isEmpty()) {
                publishDate = DateConverted.today();
                publishStatus = false;
            } else {
                map = l.get(0);
                publishDate = map.get("publishDate").toString();
                publishStatus = true;
            }
            switch (system) {
                case 1:
                    sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM grading_system WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                    break;
                case 2:
                    sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM grading_system_two WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                    break;
                case 3:
                    sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM percentage_system WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                    break;
                default:
                    throw new CustomException("Invalid System class " + classId);
            }

            List<GradingSystemReq> gradingSystem = da.getRecord(sql).stream()
                    .map(m -> GradingSystemReq.builder()
                            .gpa(Float.parseFloat(m.get("gpa").toString()))
                            .grade(m.get("grade").toString())
                            .rangFrom(Float.parseFloat(m.get("rangFrom").toString()))
                            .remark(m.get("remark").toString())
                            .build())
                    .collect(Collectors.toList());
            if (gradingSystem.isEmpty()) {
                throw new CustomException("Please define Result Rule. " + classId);
            }

            reportGenerate.rangFrom = new float[gradingSystem.size()];
            reportGenerate.gpa = new float[gradingSystem.size()];
            reportGenerate.remark = new String[gradingSystem.size()];
            reportGenerate.grade = new String[gradingSystem.size()];
            for (int i = 0; i < gradingSystem.size(); i++) {
                GradingSystemReq m = gradingSystem.get(i);
                reportGenerate.rangFrom[i] = m.getRangFrom();
                reportGenerate.gpa[i] = m.getGpa();
                reportGenerate.remark[i] = m.getRemark();
                reportGenerate.grade[i] = m.getGrade();
            }
            if (publishStatus) {
                sql = "SELECT SUBJECT_CODE subjectCode,SUBJECT_NAME subjectName,GROUP_NAME groupName,TH_FM thFm,TH_PM thPm,PR_FM prFm,PR_PM prPm,SUBJECT subject,CREDIT_HOUR credit,ifnull(CREDIT_PH,0) creditPh,ifnull(SUBJECT_CODE_PH,'') codePh FROM exam_result_publish_subject WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND SUBJECT_GROUP=" + subjectGroup + " ORDER BY in_order ";
            } else {
                sql = "SELECT D.SUBJECT_CODE subjectCode,S.NAME subjectName,M.NAME groupName,D.TH_FM thFm,D.TH_PM thPm,D.PR_FM prFm,D.PR_PM prPm,D.SUBJECT subject,CREDIT credit,ifnull(CREDIT_PH,0) creditPh,ifnull(SUBJECT_CODE_PH,'') codePh FROM subject_group_detail D,subject_group M,subject_master S WHERE M.ID=D.SUBJECT_GROUP AND D.SUBJECT=S.ID AND D.PROGRAM=" + program + " AND D.CLASS_ID=" + classId + " AND D.SUBJECT_GROUP=" + subjectGroup + " ORDER BY in_order";
            }

            List<Map<String, Object>> subjectList = da.getRecord(sql);
            StringBuilder markSubject = new StringBuilder();
            int noOfSubject = subjectList.size();
            float totalFullMark = 0;
            String[] subjectName = new String[noOfSubject];
            float[] thFm, prFm, totalFm, thPm, prPm;
            thFm = new float[noOfSubject];
            prFm = new float[noOfSubject];
            thPm = new float[noOfSubject];
            prPm = new float[noOfSubject];
            totalFm = new float[noOfSubject];
            for (int i = 0; i < noOfSubject; i++) {
                Map<String, Object> m = subjectList.get(i);
                thFm[i] = Float.parseFloat(m.get("thFm").toString());
                prFm[i] = Float.parseFloat(m.get("prFm").toString());
                thPm[i] = Float.parseFloat(m.get("thPm").toString());
                prPm[i] = Float.parseFloat(m.get("prPm").toString());
                totalFm[i] = thFm[i] + prFm[i];
                totalFullMark = totalFullMark + totalFm[i];
                subjectName[i] = m.get("subjectName").toString();
                markSubject.append(",IFNULL((SELECT CONCAT(TH_OM, ':', PR_OM,':',ifnull(extra_activity,'-')) FROM exam_mark_entry ME WHERE ME.EXAM_REG_ID=R.ID AND ME.SUBJECT=").append(m.get("subject")).append("),'0:0:-') AS sub").append(i);
            }


            sql = "SELECT ifnull(section,'') section,ifnull(roll_no,'') rollNo,ifnull(PHOTO,'') photo,IFNULL(R.REMARK,'') AS remark,IFNULL(R.PRESENT_DAYS,'0') AS presentDays,IFNULL(R.ABSENT_DAYS,'0') AS absentDays,CONCAT(IFNULL(MUNICIPAL,''),' ',IFNULL(WARD_NO,''),', ',IFNULL(DISTRICT,'')) AS address,IFNULL(EXAM_ROLL_NO,'') examRollNo,IFNULL(UPPER(S.STU_NAME),'') stuName,IFNULL(UPPER(FATHERS_NAME),'') fathersName,ifnull(MOTHERS_NAME,'') mothersName,IFNULL(DISTRICT,'') DISTRICT,IFNULL(WARD_NO,'') WARD_NO,IFNULL(MUNICIPAL,'') MUNICIPAL,IFNULL(PROVINCE,'') PROVINCE,IFNULL(DATE_OF_BIRTH,'') AS dobBs,IFNULL(R.board_symbol_no,'') boardSymbolNo,ifnull(S.board_regd_no,'') boardRegNo,S.ID regNo" + markSubject + " FROM exam_student_registration R,student_info S WHERE R.STUDENT_ID=S.ID AND R.EXAM=" + exam + " AND  R.PROGRAM=" + program + " AND  R.CLASS_ID=" + classId + " AND  R.SUBJECT_GROUP=" + subjectGroup + "  ORDER BY stuName";
            List<Map<String, Object>> studentList = da.getRecord(sql);
            List<StudentResult> list = new ArrayList<>();

            float finalTotalFullMark = totalFullMark;
            studentList.forEach(map1 -> list.add(reportGenerate.getStudentResult(map1, subjectName, finalTotalFullMark, noOfSubject, thFm, prFm, thPm, prPm, totalFm, system, "")));
            int ng = 0, gpa12 = 0, gpa16 = 0, gpa20 = 0, gpa24 = 0, gpa28 = 0, gpa32 = 0, gpa36 = 0, gpa40 = 0;
            double gpa;
            for (StudentResult d : list) {
                try {
                    if (d.getGpa().equalsIgnoreCase("-")) ng++;
                    else {
                        gpa = Double.parseDouble(d.getGpa());
                        if (gpa >= 4) gpa40++;
                        else if (gpa >= 3.6) gpa36++;
                        else if (gpa >= 3.2) gpa32++;
                        else if (gpa >= 2.8) gpa28++;
                        else if (gpa >= 2.4) gpa24++;
                        else if (gpa >= 2.0) gpa20++;
                        else if (gpa >= 1.6) gpa16++;
                        else if (gpa >= 1.2) gpa12++;
                        else ng++;
                    }
                } catch (Exception e) {
                    ng++;
                }
            }


            gpaWises.add(GpaWise.builder()
                    .className(da.getRecord("SELECT NAME 'name' FROM class_master WHERE ID=" + classId).get(0).get("name").toString())
                    .ng(ng).gpa12(gpa12).gpa16(gpa16).gpa20(gpa20).gpa24(gpa24).gpa28(gpa28).gpa32(gpa32).gpa36(gpa36).gpa40(gpa40)
                    .total(ng + gpa12 + gpa16 + gpa20 + gpa24 + gpa28 + gpa32 + gpa36 + gpa40)
                    .build());
        }


        return gpaWises;
    }

    @Override
    public List<SubjectWise> subjectWiseReport(Integer system, Long exam, Long program, Long classId, Long subjectGroup) {

        MarkReportGenerate reportGenerate = new MarkReportGenerate();
        List<SubjectWise> subjects = new ArrayList<>();

        String publishDate;
        boolean publishStatus;
        Map<String, Object> map;

        sql = "SELECT IFNULL(WORKING_DAYS,'') workingDays,IFNULL(PUBLISH_DATE,'" + DateConverted.today() + "') publishDate FROM exam_result_publish WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
        List<Map<String, Object>> l = da.getRecord(sql);
        if (l.isEmpty()) {
            publishDate = DateConverted.today();
            publishStatus = false;
        } else {
            map = l.get(0);
            publishDate = map.get("publishDate").toString();
            publishStatus = true;
        }
        switch (system) {
            case 1:
                sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM grading_system WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                break;
            case 2:
                sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM grading_system_two WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                break;
            case 3:
                sql = "SELECT RANG_FROM rangFrom,GRADE grade,GPA gpa,REMARK remark FROM percentage_system WHERE '" + publishDate + "' BETWEEN EFFECTIVE_DATE_FROM AND IFNULL(EFFECTIVE_DATE_TO,'" + publishDate + "') ORDER BY RANG_FROM DESC ";
                break;
            default:
                throw new CustomException("Invalid System class " + classId);
        }

        List<GradingSystemReq> gradingSystem = da.getRecord(sql).stream()
                .map(m -> GradingSystemReq.builder()
                        .gpa(Float.parseFloat(m.get("gpa").toString()))
                        .grade(m.get("grade").toString())
                        .rangFrom(Float.parseFloat(m.get("rangFrom").toString()))
                        .remark(m.get("remark").toString())
                        .build())
                .collect(Collectors.toList());
        if (gradingSystem.isEmpty()) {
            throw new CustomException("Please define Result Rule. " + classId);
        }

        reportGenerate.rangFrom = new float[gradingSystem.size()];
        reportGenerate.gpa = new float[gradingSystem.size()];
        reportGenerate.remark = new String[gradingSystem.size()];
        reportGenerate.grade = new String[gradingSystem.size()];
        for (int i = 0; i < gradingSystem.size(); i++) {
            GradingSystemReq m = gradingSystem.get(i);
            reportGenerate.rangFrom[i] = m.getRangFrom();
            reportGenerate.gpa[i] = m.getGpa();
            reportGenerate.remark[i] = m.getRemark();
            reportGenerate.grade[i] = m.getGrade();
        }
        if (publishStatus) {
            sql = "SELECT SUBJECT_CODE subjectCode,SUBJECT_NAME subjectName,GROUP_NAME groupName,TH_FM thFm,TH_PM thPm,PR_FM prFm,PR_PM prPm,SUBJECT subject,CREDIT_HOUR credit,ifnull(CREDIT_PH,0) creditPh,ifnull(SUBJECT_CODE_PH,'') codePh FROM exam_result_publish_subject WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND SUBJECT_GROUP=" + subjectGroup + " ORDER BY in_order ";
        } else {
            sql = "SELECT D.SUBJECT_CODE subjectCode,S.NAME subjectName,M.NAME groupName,D.TH_FM thFm,D.TH_PM thPm,D.PR_FM prFm,D.PR_PM prPm,D.SUBJECT subject,CREDIT credit,ifnull(CREDIT_PH,0) creditPh,ifnull(SUBJECT_CODE_PH,'') codePh FROM subject_group_detail D,subject_group M,subject_master S WHERE M.ID=D.SUBJECT_GROUP AND D.SUBJECT=S.ID AND D.PROGRAM=" + program + " AND D.CLASS_ID=" + classId + " AND D.SUBJECT_GROUP=" + subjectGroup + " ORDER BY in_order";
        }

        List<Map<String, Object>> subjectList = da.getRecord(sql);
        StringBuilder markSubject = new StringBuilder();
        int noOfSubject = subjectList.size();
        float totalFullMark = 0;
        String[] subjectName = new String[noOfSubject];
        float[] thFm, prFm, totalFm, thPm, prPm;
        thFm = new float[noOfSubject];
        prFm = new float[noOfSubject];
        thPm = new float[noOfSubject];
        prPm = new float[noOfSubject];
        totalFm = new float[noOfSubject];

        for (int i = 0; i < noOfSubject; i++) {
            Map<String, Object> m = subjectList.get(i);
            thFm[i] = Float.parseFloat(m.get("thFm").toString());
            prFm[i] = Float.parseFloat(m.get("prFm").toString());
            thPm[i] = Float.parseFloat(m.get("thPm").toString());
            prPm[i] = Float.parseFloat(m.get("prPm").toString());
            totalFm[i] = thFm[i] + prFm[i];
            totalFullMark = totalFullMark + totalFm[i];
            subjects.add(i, SubjectWise.builder().subject(m.get("subjectName").toString()).aplus(0).a(0).bplus(0).b(0).cplus(0).c(0).d(0).ng(0).build());
            subjectName[i] = m.get("subjectName").toString();
            markSubject.append(",IFNULL((SELECT CONCAT(TH_OM, ':', PR_OM,':',ifnull(extra_activity,'-')) FROM exam_mark_entry ME WHERE ME.EXAM_REG_ID=R.ID AND ME.SUBJECT=").append(m.get("subject")).append("),'0:0:-') AS sub").append(i);
        }


        sql = "SELECT ifnull(section,'') section,ifnull(roll_no,'') rollNo,ifnull(PHOTO,'') photo,IFNULL(R.REMARK,'') AS remark,IFNULL(R.PRESENT_DAYS,'0') AS presentDays,IFNULL(R.ABSENT_DAYS,'0') AS absentDays,CONCAT(IFNULL(MUNICIPAL,''),' ',IFNULL(WARD_NO,''),', ',IFNULL(DISTRICT,'')) AS address,IFNULL(EXAM_ROLL_NO,'') examRollNo,IFNULL(UPPER(S.STU_NAME),'') stuName,IFNULL(UPPER(FATHERS_NAME),'') fathersName,ifnull(MOTHERS_NAME,'') mothersName,IFNULL(DISTRICT,'') DISTRICT,IFNULL(WARD_NO,'') WARD_NO,IFNULL(MUNICIPAL,'') MUNICIPAL,IFNULL(PROVINCE,'') PROVINCE,IFNULL(DATE_OF_BIRTH,'') AS dobBs,IFNULL(R.board_symbol_no,'') boardSymbolNo,ifnull(S.board_regd_no,'') boardRegNo,S.ID regNo" + markSubject + " FROM exam_student_registration R,student_info S WHERE R.STUDENT_ID=S.ID AND R.EXAM=" + exam + " AND  R.PROGRAM=" + program + " AND  R.CLASS_ID=" + classId + " AND  R.SUBJECT_GROUP=" + subjectGroup + "  ORDER BY stuName";
        List<Map<String, Object>> studentList = da.getRecord(sql);
        List<StudentResult> list = new ArrayList<>();

        float finalTotalFullMark = totalFullMark;
        studentList.forEach(map1 -> list.add(reportGenerate.getStudentResult(map1, subjectName, finalTotalFullMark, noOfSubject, thFm, prFm, thPm, prPm, totalFm, system, "")));

        for (StudentResult d : list) {
            List<StudentResult.Mark> mark = d.getMark();
            for (int i = 0; i < mark.size(); i++) {
                String grade = mark.get(i).getGrade().trim();
                if (grade.equalsIgnoreCase("A+")) {
                    subjects.get(i).setAplus(subjects.get(i).getAplus() + 1);
                } else if (grade.equalsIgnoreCase("A")) {
                    subjects.get(i).setA(subjects.get(i).getA() + 1);
                } else if (grade.equalsIgnoreCase("B+")) {
                    subjects.get(i).setBplus(subjects.get(i).getBplus() + 1);
                } else if (grade.equalsIgnoreCase("B")) {
                    subjects.get(i).setBplus(subjects.get(i).getBplus() + 1);
                } else if (grade.equalsIgnoreCase("C+")) {
                    subjects.get(i).setCplus(subjects.get(i).getCplus() + 1);
                } else if (grade.equalsIgnoreCase("C")) {
                    subjects.get(i).setC(subjects.get(i).getC() + 1);
                } else if (grade.equalsIgnoreCase("D")) {
                    subjects.get(i).setD(subjects.get(i).getD() + 1);
                } else {
                    subjects.get(i).setNg(subjects.get(i).getNg() + 1);
                }
            }
        }


        return subjects;
    }

}
