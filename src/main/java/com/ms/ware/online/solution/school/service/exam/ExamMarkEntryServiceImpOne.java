package com.ms.ware.online.solution.school.service.exam;


import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.exam.ExamMarkEntryDao;
import com.ms.ware.online.solution.school.dto.ExamMarkEntryReq;
import com.ms.ware.online.solution.school.dto.StudentResult;
import com.ms.ware.online.solution.school.entity.exam.ExamMarkEntry;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExamMarkEntryServiceImpOne {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    ExamMarkEntryDao da;
    Message message = new Message();


    public Object getAll(Long exam, Long program, Long classId, Long subjectGroup, Long subject) {
        String sql = "SELECT R.ID AS examRegNo,EXAM_ROLL_NO examRollNo,STUDENT_ID regNo,S.STU_NAME stuName,IFNULL((SELECT TH_OM FROM exam_mark_entry ME WHERE ME.EXAM_REG_ID=R.ID AND ME.SUBJECT=" + subject + "),0) AS thOm,IFNULL((SELECT PR_OM FROM exam_mark_entry ME WHERE ME.EXAM_REG_ID=R.ID AND ME.SUBJECT=" + subject + "),0) AS prOm FROM exam_student_registration R,student_info S WHERE R.STUDENT_ID=S.ID AND R.APPROVE_DATE IS NOT NULL AND R.EXAM=" + exam + " AND R.PROGRAM=" + program + " AND R.CLASS_ID=" + classId + " AND R.SUBJECT_GROUP=" + subjectGroup + " AND R.ID NOT IN(SELECT EXAM_REG_ID FROM exam_mark_entry ME WHERE ME.APPROVE_DATE IS NOT NULL AND ME.EXAM=" + exam + " AND ME.SUBJECT=" + subject + " ) ORDER BY examRollNo";
        return da.getRecord(sql);
    }


    public Object getApprove(Long exam, Long program, Long classId, Long subjectGroup, Long subject) {
        String sql = "SELECT CONCAT(ME.EXAM_REG_ID,'-',ME.SUBJECT) AS id,R.EXAM_ROLL_NO examRollNo,STUDENT_ID regNo,S.STU_NAME stuName,IFNULL(TH_OM,0) AS thOm,IFNULL(PR_OM,0) AS prOm FROM exam_student_registration R,student_info S,exam_mark_entry ME WHERE R.STUDENT_ID=S.ID AND ME.EXAM_REG_ID=R.ID AND ME.APPROVE_DATE IS NULL AND R.EXAM=" + exam + " AND R.PROGRAM=" + program + " AND R.CLASS_ID=" + classId + " AND R.SUBJECT_GROUP=" + subjectGroup + " AND ME.SUBJECT=" + subject + " ORDER BY examRollNo";
        return da.getRecord(sql);
    }


    public Object save(ExamMarkEntryReq req) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String enterBy = td.getUserName();
        return markEntry(req, enterBy);
    }

    public Object entryTeacher(ExamMarkEntryReq req) {
        AuthenticatedUser td = facade.getAuthentication();;

        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String enterBy = td.getUserName();
        return markEntry(req, enterBy);

    }

    private String markEntry(ExamMarkEntryReq req, String enterBy) {
        StringBuilder msg = new StringBuilder();
        AtomicInteger row = new AtomicInteger();
        try {

            long exam = req.getExam();
            long program = req.getProgram();
            long classId = req.getClassId();
            long subjectGroup = req.getSubjectGroup();
            long subject = req.getSubject();
            Date enterDate = DateConverted.bsToAdDate(req.getEnterDate());

            float thFm, prFm;
            String sql = "SELECT IFNULL(TH_FM,0) thFm,IFNULL(PR_FM,0) prFm FROM subject_group_detail WHERE PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND SUBJECT_GROUP=" + subjectGroup + " AND SUBJECT=" + subject;
            List<Map<String, Object>> l = da.getRecord(sql);
            if (l.isEmpty()) {
                return message.respondWithError("Invalid Subject");
            }

            thFm = Float.parseFloat(l.get(0).get("thFm").toString());
            prFm = Float.parseFloat(l.get(0).get("prFm").toString());


            req.getObj().forEach(d -> {
                if (d.getThOm() > thFm || d.getPrOm() > prFm) {
                    msg.append("Obtain mark must be less then full mark");
                    return;
                } else {

                    List<Map<String, Object>> list = da.getRecord("SELECT IFNULL(APPROVE_DATE,'') AS approveDate FROM exam_mark_entry WHERE EXAM_REG_ID='" + d.getExamRegId() + "' AND SUBJECT='" + subject + "'");
                    if (!list.isEmpty()) {
                        if (list.get(0).get("approveDate").toString().length() >= 10) {
                            return;
                        }
                    }
                }
                row.set(row.get() + da.save(new ExamMarkEntry(d.getExamRegId(), subject, exam, d.getExamRollNo(), d.getStudentRegNo(), d.getThOm(), d.getPrOm(), enterBy, enterDate, d.getExtraActivity())));
            });

            if (row.get() > 0) {
                return message.respondWithMessage(row + " Record Saved!!");
            } else if (msg.toString().contains("Duplicate entry")) {
                msg.append("This record already exist");
            }
            return message.respondWithError(msg.toString());

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }


    public Object doApprove(String jsonData) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String enterBy = td.getUserName();
        String jsonDataArray[] = message.jsonDataToStringArray(jsonData);
        int row = 0;
        String msg = "";
        try {
            message.map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[0], new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
            String enterDate = DateConverted.bsToAd(message.map.get("enterDate").toString());

            message.list = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[1], new com.fasterxml.jackson.core.type.TypeReference<List>() {
            });
            String id;
            for (int i = 0; i < message.list.size(); i++) {
                id = message.list.get(i).toString();
                String sql = "UPDATE exam_mark_entry SET APPROVE_BY='" + enterBy + "',APPROVE_DATE='" + enterDate + "' WHERE CONCAT(EXAM_REG_ID,'-',SUBJECT)='" + id + "'";
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


    public Object delete(String id) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String sql = "DELETE FROM exam_mark_entry WHERE CONCAT(EXAM_REG_ID,'-',SUBJECT)='" + id + "' AND APPROVE_DATE IS NULL";
        int row = da.delete(sql);
        String msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    float[] gpa, rangFrom;
    String[] grade, remark;

    //    
    public StudentResult getStudentResult(Map<String, Object> map, String[] subjectName, double totalFullMark, int noOfSubject, float[] thFm, float[] prFm, float[] totalFm) {

        String thprmark[];
        float thOm, prOm, total, percent, totalGPA, totalThObtainMark, totalPrObtainMark;

        totalGPA = 0;
        totalThObtainMark = 0;
        totalPrObtainMark = 0;
        StudentResult s = new StudentResult();

        s.setExamRollNo(map.get("examRollNo").toString());
        s.setStuName(map.get("stuName").toString());
        s.setRegNo(map.get("regNo").toString());
        s.setAddress(map.get("address").toString());
        s.setDobBs(map.get("dobBs").toString());
        s.setPresentDays(Integer.parseInt(map.get("presentDays").toString()));
        s.setAbsentDays(Integer.parseInt(map.get("absentDays").toString()));
        s.setRemark(map.get("remark").toString());
        s.setFathersName(map.get("fathersName").toString());
        s.setDistrict(map.get("DISTRICT").toString());
        s.setWardNo(map.get("WARD_NO").toString());
        s.setMunicipal(map.get("MUNICIPAL").toString());
        s.setProvince(map.get("PROVINCE").toString());
        System.out.println(map.get("section").toString());
        s.setSection(map.get("section").toString());
        List<StudentResult.Mark> markList = new ArrayList<>();
        StudentResult.Mark mark;

        for (int j = 0; j < noOfSubject; j++) {
            mark = new StudentResult.Mark();
            thprmark = map.get("sub" + j).toString().split(":");
            thOm = Float.parseFloat(thprmark[0]);
            prOm = Float.parseFloat(thprmark[1]);

            mark.setFullMark(df.format(thFm[j] + prFm[j]));
            mark.setPercent(df.format(((thOm + prOm) / (thFm[j] + prFm[j])) * 100));
            if (thFm[j] > 0) {
                if (thOm > 0) {
                    getGrading((thOm / thFm[j]) * 100);
                    mark.setThOm(resultGrade);
                    mark.setThMark(df.format(thOm));
                    mark.setThPercent(df.format((thOm / thFm[j]) * 100));
                } else {
                    mark.setThOm("F");
                    mark.setThPercent("0");
                    mark.setThMark("-");
                }
            } else {
                mark.setThOm("-");
                mark.setThPercent("-");
                mark.setThMark("-");
            }
            if (prFm[j] > 0) {
                if (prOm > 0) {
                    getGrading((prOm / prFm[j]) * 100);
                    mark.setPrOm(resultGrade);
                    mark.setPhMark(df.format(prOm));
                    mark.setPrPercent(df.format((prOm / prFm[j]) * 100));
                } else {
                    mark.setPrOm("-");
                    mark.setPhMark("F");
                    mark.setPrPercent("0");

                }
            } else {
                mark.setPrOm("-");
                mark.setPhMark("-");
                mark.setPrPercent("-");
            }
            totalThObtainMark += thOm;
            totalPrObtainMark += prOm;

            total = thOm + prOm;
            percent = (total / totalFm[j]) * 100;
            getGrading(percent);
            mark.setGrade(resultGrade);
            mark.setGpa(df.format(resultGPA));
            mark.setResult(resultRemark);
            System.out.println(totalGPA + " " + resultGPA);
            totalGPA = totalGPA + resultGPA;
//                }
            mark.setSubjectName(subjectName[j]);
            markList.add(mark);
        }

        s.setMark(markList);
        s.setFullMark(df.format(totalFullMark));
        s.setTotalThMark(df.format(totalThObtainMark));
        s.setTotalPrMark(df.format(totalPrObtainMark));
        s.setTotalObtain(df.format(((totalThObtainMark + totalPrObtainMark) / totalFullMark) * 100));


        s.setRemark(resultRemark);
//        if (failCount == 0) {
        totalGPA = totalGPA / noOfSubject;
        getGradingByGPS(totalGPA);
        s.setGpa(df.format(totalGPA));
        s.setGrade(resultGrade);
//        } else {
//            s.setGpa("-");
//            s.setGrade("F");
//        }


        return s;
    }


    public Object getReport(int system, Long exam, Long program, Long classId, Long subjectGroup, Long sId) {
        AuthenticatedUser td = facade.getAuthentication();;
        String studentId = "NULL";
        String type = td.getUserType();
        type = "ORG";
        if (td.getUserType().equalsIgnoreCase("STU")) {
            AuthenticatedUser dd = facade.getAuthentication();;
            type = "STU";
            studentId = dd.getUserId();
            String sql = "SELECT EXAM_ROLL_NO examRollNo,SUBJECT_GROUP subjectGroup,PROGRAM program,CLASS_ID classId FROM exam_student_registration WHERE EXAM='" + exam + "' AND STUDENT_ID='" + studentId + "'";
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
        String sql = "SELECT IFNULL(WORKING_DAYS,'') workingDays,IFNULL(PUBLISH_DATE,'" + DateConverted.today() + "') publishDate FROM exam_result_publish WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
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
            resultPublishDate = publishDate;
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
        List<Map<String, Object>> gradingSystem = da.getRecord(sql);
        if (gradingSystem.isEmpty()) {
            return message.respondWithError("Please define Result Rule.");
        }
        rangFrom = new float[gradingSystem.size()];
        gpa = new float[gradingSystem.size()];
        remark = new String[gradingSystem.size()];
        grade = new String[gradingSystem.size()];
        for (int i = 0; i < gradingSystem.size(); i++) {
            map = gradingSystem.get(i);
            rangFrom[i] = Float.parseFloat(map.get("rangFrom").toString());
            gpa[i] = Float.parseFloat(map.get("gpa").toString());
            remark[i] = map.get("remark").toString();
            grade[i] = map.get("grade").toString();
        }
        if (publishStatus) {
            sql = "SELECT SUBJECT_CODE subjectCode,SUBJECT_NAME subjectName,GROUP_NAME groupName,TH_FM thFm,TH_PM thPm,PR_FM prFm,PR_PM prPm,SUBJECT subject,CREDIT_HOUR credit FROM exam_result_publish_subject WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId + " AND SUBJECT_GROUP=" + subjectGroup + " ORDER BY SUBJECT_CODE";
        } else {
            sql = "SELECT D.SUBJECT_CODE subjectCode,S.NAME subjectName,M.NAME groupName,D.TH_FM thFm,D.TH_PM thPm,D.PR_FM prFm,D.PR_PM prPm,D.SUBJECT subject,CREDIT credit FROM subject_group_detail D,subject_group M,subject_master S WHERE M.ID=D.SUBJECT_GROUP AND D.SUBJECT=S.ID AND D.PROGRAM=" + program + " AND D.CLASS_ID=" + classId + " AND D.SUBJECT_GROUP=" + subjectGroup + " ORDER BY D.SUBJECT_CODE";
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
            message.map = subjectList.get(i);
            thFm[i] = Float.parseFloat(message.map.get("thFm").toString());
            prFm[i] = Float.parseFloat(message.map.get("prFm").toString());
            thPm[i] = Float.parseFloat(message.map.get("thPm").toString());
            prPm[i] = Float.parseFloat(message.map.get("prPm").toString());
            totalFm[i] = thFm[i] + prFm[i];
            totalFullMark = totalFullMark + totalFm[i];
            subjectName[i] = message.map.get("subjectName").toString();
            markSubject.append(",IFNULL((SELECT CONCAT(TH_OM,':',PR_OM) FROM exam_mark_entry ME WHERE ME.EXAM_REG_ID=R.ID AND ME.SUBJECT=").append(message.map.get("subject")).append("),'0:0') AS sub").append(i);
        }
        sql = "SELECT ifnull(section,'') section,roll_no rollNo,IFNULL(R.REMARK,'') AS remark,IFNULL(R.PRESENT_DAYS,'') AS presentDays,IFNULL(R.ABSENT_DAYS,'') AS absentDays,CONCAT(IFNULL(MUNICIPAL,''),' ',IFNULL(WARD_NO,''),', ',IFNULL(DISTRICT,'')) AS address,IFNULL(EXAM_ROLL_NO,'') examRollNo,IFNULL(UPPER(S.STU_NAME),'') stuName,IFNULL(UPPER(FATHERS_NAME),'') fathersName,IFNULL(DISTRICT,'') DISTRICT,IFNULL(WARD_NO,'') WARD_NO,IFNULL(MUNICIPAL,'') MUNICIPAL,IFNULL(PROVINCE,'') PROVINCE,IFNULL(DATE_OF_BIRTH,'') AS dobBs,S.ID regNo" + markSubject + " FROM exam_student_registration R,student_info S WHERE R.STUDENT_ID=S.ID AND R.STUDENT_ID=IFNULL(" + studentId + ",R.STUDENT_ID) AND R.EXAM=" + exam + " AND  R.PROGRAM=" + program + " AND  R.CLASS_ID=" + classId + " AND  R.SUBJECT_GROUP=" + subjectGroup + " and S.ID=IFNULL(" + sId + ",S.ID) ORDER BY stuName";
        List<Map<String, Object>> studentList = da.getRecord(sql);
        List<StudentResult> list = new ArrayList<>();

//        String examRollNo, fathersName, district, wardNo, municipal, province, regNo, stuName, address, dobBs, presentDays, absentDays, remark, thprmark[];
//        float thOm, prOm, total, percent, totalGPA, totalThObtainMark, totalPrObtainMark;
        float finalTotalFullMark = totalFullMark;
        studentList.forEach(map1 -> list.add(getStudentResult(map1, subjectName, finalTotalFullMark, noOfSubject, thFm, prFm, totalFm)));
//        for (int i = 0; i < studentList.size(); i++) {
//            totalGPA = 0;
//            totalThObtainMark = 0;
//            totalPrObtainMark = 0;
//            markList = new ArrayList();
//            message.map = (Map) studentList.get(i);
//            examRollNo = message.map.get("examRollNo").toString();
//            stuName = message.map.get("stuName").toString();
//            regNo = message.map.get("regNo").toString();
//            address = message.map.get("address").toString();
//            dobBs = message.map.get("dobBs").toString();
//            presentDays = message.map.get("presentDays").toString();
//            absentDays = message.map.get("absentDays").toString();
//            remark = message.map.get("remark").toString();
//            fathersName = message.map.get("fathersName").toString();
//            district = message.map.get("DISTRICT").toString();
//            wardNo = message.map.get("WARD_NO").toString();
//            municipal = message.map.get("MUNICIPAL").toString();
//            province = message.map.get("PROVINCE").toString();
//
//            failCount = 0;
//            for (int j = 0; j < noOfSubject; j++) {
//                thprmark = message.map.get("sub" + j).toString().split(":");
//                thOm = Float.parseFloat(thprmark[0]);
//                prOm = Float.parseFloat(thprmark[1]);
//                map = new HashMap();
//                map.put("fullMark", df.format(thFm[j] + prFm[j]));
//                map.put("percent", df.format(((thOm + prOm) / (thFm[j] + prFm[j])) * 100));
//                if (thFm[j] > 0) {
//                    if (thOm > 0) {
//                        getGrading((thOm / thFm[j]) * 100);
//                        map.put("thOm", resultGrade);
//                        map.put("thMark", df.format(thOm));
//                        map.put("thPercent", df.format((thOm / thFm[j]) * 100));
//                    } else {
//                        map.put("thOm", "F");
//                        map.put("thPercent", 0);
//                        map.put("thMark", "-");
//                    }
//                } else {
//                    map.put("thOm", "-");
//                    map.put("thPercent", "-");
//                    map.put("thMark", "-");
//                }
//                if (prFm[j] > 0) {
//                    if (prOm > 0) {
//                        getGrading((prOm / prFm[j]) * 100);
//                        map.put("prOm", resultGrade);
//                        map.put("phMark", df.format(prOm));
//                        map.put("prPercent", df.format((prOm / prFm[j]) * 100));
//                    } else {
//                        map.put("phMark", "-");
//                        map.put("prOm", "F");
//                        map.put("prPercent", 0);
//                    }
//                } else {
//                    map.put("prOm", "-");
//                    map.put("prPercent", "-");
//                    map.put("phMark", "-");
//                }
//                totalThObtainMark += thOm;
//                totalPrObtainMark += prOm;
//
////                if (thPm[j] > thOm || prPm[j] > prOm) {
////                    map.put("grade", "F");
////                    map.put("gpa", 0);
////                    map.put("result", "Fail");
//////                    failCount++;
////                } else {
//                total = thOm + prOm;
//                percent = (total / totalFm[j]) * 100;
//                getGrading(percent);
//                map.put("grade", resultGrade);
//                map.put("gpa", resultGPA);
//                map.put("result", resultRemark);
//                totalGPA = totalGPA + resultGPA;
////                }
//                map.put("subjectName", subjectName[j]);
//                markList.add(map);
//            }
//            map = new HashMap();
//            map.put("mark", markList);
//            map.put("examRollNo", examRollNo);
//            map.put("totaName", "MANOJ KUMAR");
//            map.put("stuName", stuName);
//            map.put("address", address);
//            map.put("dobBs", dobBs);
//            map.put("regNo", regNo);
//            map.put("fullMark", df.format(totalFullMark));
//            map.put("totalThMark", df.format(totalThObtainMark));
//            map.put("totalPrMark", df.format(totalPrObtainMark));
//            map.put("totalObtain", df.format(((totalThObtainMark + totalPrObtainMark) / totalFullMark) * 100));
//            map.put("dobBs", dobBs);
//            map.put("regNo", regNo);
//            map.put("presentDays", presentDays);
//            map.put("absentDays", absentDays);
//            map.put("remark", remark);
//            map.put("fathersName", fathersName);
//            map.put("district", district);
//            map.put("wardNo", wardNo);
//            map.put("municipal", municipal);
//            map.put("province", province);
//
//            if (failCount == 0) {
//
//                totalGPA = totalGPA / noOfSubject;
//                getGradingByGPS(totalGPA);
//                map.put("gpa", df.format(totalGPA));
//                map.put("grade", resultGrade);
//            } else {
//                map.put("gpa", "-");
//                map.put("grade", "F");
//            }
//            list.add(map);
//        }
        map = new HashMap();
        map.put("student", list);
        map.put("subject", subjectList);
        sql = "SELECT NAME 'name' FROM class_master WHERE ID=" + classId;
        map.put("class", da.getRecord(sql).get(0));
        sql = "SELECT NAME 'name' FROM program_master WHERE ID=" + program;
        map.put("program", da.getRecord(sql).get(0));
        sql = "SELECT EXAM_NAME 'name',ID FROM exam_master WHERE ID=" + exam;
        map.put("exam", da.getRecord(sql).get(0));
        map.put("gradingSystem", gradingSystem);
        map.put("publishDate", resultPublishDate);
        map.put("workingDays", workingDays);
        map.put("organization", da.getAll("from OrganizationMaster").get(0));

        long sn = 0;
        try {
            sn = Long.parseLong(da.getRecord("SELECT MAX(`SN`) sn FROM character_issue").get(0).get("sn").toString());
        } catch (Exception ignored) {
        }

        map.put("sn", sn);
        return map;
    }

    String resultGrade, resultRemark;
    float resultGPA;
    DecimalFormat df = new DecimalFormat("#.##");

    void getGrading(float percent) {
        if (percent < 0) {
            resultGrade = "-";
            resultRemark = "@";
            resultGPA = 0;
            return;
        }
        resultGrade = "F";
        resultRemark = "**";
        resultGPA = 0;
        for (int i = 0; i < rangFrom.length; i++) {
            if (percent >= rangFrom[i]) {
                resultGrade = grade[i];
                resultRemark = remark[i];
                resultGPA = gpa[i];
                break;
            }
        }
    }

    void getGradingByGPS(float obtainGPA) {
        if (obtainGPA < 0) {
            resultGrade = "-";
            resultRemark = "@";
            resultGPA = 0;
            return;
        }
        resultGrade = "F";
        resultRemark = "**";
        resultGPA = 0;
        for (int i = (gpa.length - 1); i >= 0; i--) {
            if (obtainGPA <= gpa[i]) {
                resultGrade = grade[i];
                resultRemark = remark[i];
                resultGPA = obtainGPA;
                break;
            }
        }
    }


    public Object markUpdate(Long exam, Long regNo) {
        String sql = "SELECT EXAM_REG_ID examRegNo,SUBJECT subject,S.NAME subjectName,TH_OM thOm,PR_OM phOm FROM exam_mark_entry E,subject_master S WHERE E.SUBJECT=S.ID and EXAM=" + exam + " AND STUDENT_REG_NO=" + regNo;
        return da.getRecord(sql);
    }


    public Object markUpdate(List<MarkUpdateReq> req) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equals("ADM")) {
            return message.respondWithError("invalid token");
        }
        String date = DateConverted.now();
        req.forEach(t -> da.delete("UPDATE exam_mark_entry SET TH_OM=" + t.getThOm() + ",PR_OM=" + t.getPhOm() + ",ENTER_BY='" + td.getUserName() + "',ENTER_DATE='" + date + "' where EXAM_REG_ID=" + t.getExamRegNo() + " AND SUBJECT=" + t.getSubject()));
        return message.respondWithMessage(" Record Updated!!");
    }


    public Object markUpload(HttpServletRequest request, MultipartFile markUpload, Long exam, String Authorization) throws IOException {
        AuthenticatedUser td = facade.getAuthentication();;
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
                String sql = "insert into mark_insert(REG_NO,SUBJECT_CODE,TH_OM,PR_OM,exam,POSTED,GROUP_ID,program,class_id) values (" + regNo + ",'" + subjectCode + "'," + th + "," + pr + "," + exam + ",'N',null,null,null);";
                count = count + da.delete(sql);
            } catch (Exception ignored) {
            }
        }

        return markUpdateConfig(exam);
    }


    public Object markUpload(Long exam) {
        String sql = "SELECT concat(exam,'-', reg_no, '-',subject_code) id,exam, reg_no, (SELECT S.STU_NAME FROM student_info S where S.ID = reg_no) stuName, subject_code, class_id, exam_reg_id, exam_roll_no, group_id, posted, program, sub_id, (SELECT S.NAME FROM subject_master S where S.ID = sub_id)  subName, th_om th, pr_om pr,(SELECT S.NAME FROM program_master S where S.ID = program) programName,(SELECT S.NAME FROM class_master S where S.ID = class_id) className, (SELECT S.NAME FROM subject_group S where S.ID = group_id) groupName FROM mark_insert WHERE POSTED = 'N' and exam=" + exam + " ORDER BY class_id,program,group_id";
        return da.getRecord(sql);
    }


    public Object markUpdateConfig(Long exam) {
        String sql = "update subject_group_detail set SUBJECT_CODE= replace(SUBJECT_CODE,' ','')";
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


    public Object markApprove(Long exam) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equals("ADM")) {
            return message.respondWithError("invalid token");
        }
        String userName = td.getUserName();
        String date = DateConverted.today();
        String sql = "SELECT exam, reg_no, subject_code, class_id, exam_reg_id, exam_roll_no, group_id, posted, pr_om, program, sub_id, th_om FROM mark_insert WHERE POSTED='N' and sub_id is not null and exam_reg_id is not null;";
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

}
