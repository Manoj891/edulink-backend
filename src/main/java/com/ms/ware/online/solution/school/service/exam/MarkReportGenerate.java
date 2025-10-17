package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.exam.ExamMarkEntryDao;
import com.ms.ware.online.solution.school.dao.exam.ExamMarkEntryDaoImp;
import com.ms.ware.online.solution.school.dto.StudentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarkReportGenerate {
    @Autowired
    private Message message;
    @Autowired
    private ExamMarkEntryDao da;

    Object getFinalReport(int system, Long exam, Long program, Long classId, Long subjectGroup, Long sId) {
        String sql;
        sql = "SELECT  ifnull(T.final_percent, 0) percent,T.final_terminal terminal FROM exam_master M,exam_terminal T where M.TERMINAL = T.ID and M.ID=" + exam;
        List<Map<String, Object>> maps = da.getRecord(sql);
        if (maps.isEmpty()) {
            return message.respondWithError("Invalid Exam");
        }
        double finalTerminalPercent = Float.parseFloat(maps.get(0).get("percent").toString());
        if (!maps.get(0).get("terminal").equals("Y")) {
            return message.respondWithError("Not Final Terminal");
        }
        String publishDate, resultPublishDate, workingDays = "";
        boolean publishStatus;

        sql = "SELECT IFNULL(WORKING_DAYS,'0') workingDays,IFNULL(PUBLISH_DATE,'" + DateConverted.today() + "') publishDate FROM exam_result_publish WHERE EXAM=" + exam + " AND PROGRAM=" + program + " AND CLASS_ID=" + classId;
        maps = da.getRecord(sql);
        if (maps.isEmpty()) {
            publishDate = DateConverted.today();
            resultPublishDate = "Not Publish.";
            publishStatus = false;
        } else {
            workingDays = maps.get(0).get("workingDays").toString();
            publishDate = maps.get(0).get("publishDate").toString();
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
        this.rangFrom = new float[gradingSystem.size()];
        this.gpa = new float[gradingSystem.size()];
        this.remark = new String[gradingSystem.size()];
        this.grade = new String[gradingSystem.size()];
        Map<String, Object> m;
        for (int i = 0; i < gradingSystem.size(); i++) {
            m = gradingSystem.get(i);
            this.rangFrom[i] = Float.parseFloat(m.get("rangFrom").toString());
            this.gpa[i] = Float.parseFloat(m.get("gpa").toString());
            this.remark[i] = m.get("remark").toString();
            this.grade[i] = m.get("grade").toString();
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
        float[] thFm, prFm, totalFm;
        thFm = new float[noOfSubject];
        prFm = new float[noOfSubject];
        totalFm = new float[noOfSubject];
        for (int i = 0; i < noOfSubject; i++) {
            m = subjectList.get(i);
            thFm[i] = Float.parseFloat(m.get("thFm").toString());
            prFm[i] = Float.parseFloat(m.get("prFm").toString());
            totalFm[i] = thFm[i] + prFm[i];
            totalFullMark = totalFullMark + totalFm[i];
            subjectName[i] = m.get("subjectName").toString();
            markSubject.append(",IFNULL((SELECT CONCAT((TH_OM*").append(finalTerminalPercent).append("/100)+t1t+t2t+t3t,':',(PR_OM*").append(finalTerminalPercent).append("/100)+t1p+t2p+t3p) FROM exam_mark_entry ME WHERE ME.EXAM_REG_ID=R.ID AND ME.SUBJECT=").append(m.get("subject")).append("),'0:0') AS sub").append(i);
        }
        sql = "SELECT IFNULL(R.REMARK,'') AS remark,IFNULL(R.PRESENT_DAYS,'') AS presentDays,IFNULL(R.ABSENT_DAYS,'') AS absentDays,CONCAT(IFNULL(MUNICIPAL,''),' ',IFNULL(WARD_NO,''),', ',IFNULL(DISTRICT,'')) AS address,IFNULL(EXAM_ROLL_NO,'') examRollNo,IFNULL(UPPER(S.STU_NAME),'') stuName,IFNULL(UPPER(FATHERS_NAME),'') fathersName,IFNULL(DISTRICT,'') DISTRICT,IFNULL(WARD_NO,'') WARD_NO,IFNULL(MUNICIPAL,'') MUNICIPAL,IFNULL(PROVINCE,'') PROVINCE,IFNULL(DATE_OF_BIRTH,'') AS dobBs,S.ID regNo" + markSubject + " FROM exam_student_registration R,student_info S WHERE R.STUDENT_ID=S.ID AND R.STUDENT_ID=IFNULL(" + sId + ",R.STUDENT_ID) AND  R.PROGRAM=" + program + " AND  R.CLASS_ID=" + classId + " AND  R.SUBJECT_GROUP=" + subjectGroup + " ";
        List<Map<String, Object>> studentList = da.getRecord(sql + " and S.ID=IFNULL(" + sId + ",S.ID) AND R.EXAM=" + exam + " ORDER BY stuName");
        List<StudentResult> list = new ArrayList<>();

        float finalTotalFullMark1 = totalFullMark;
        studentList.forEach(map -> {
            String[] thprmark;
            float thOm, prOm, total, percent, totalGPA, totalThObtainMark, totalPrObtainMark;

            totalGPA = 0;
            totalThObtainMark = 0;
            totalPrObtainMark = 0;
            StudentResult s = new StudentResult();

            List<StudentResult.Mark> markList = new ArrayList<>();
            StudentResult.Mark mark;

            for (int j = 0; j < noOfSubject; j++) {
                mark = new StudentResult.Mark();
                thprmark = map.get("sub" + j).toString().split(":");
                thOm = Float.parseFloat(thprmark[0]);
                prOm = Float.parseFloat(thprmark[1]);
                mark.setFullMark(df.format(thFm[j] + prFm[j]));
                mark.setPercent(df.format(((thOm + prOm) / (thFm[j] + prFm[j])) * 100));

//                System.out.println(thOm + " " + prOm + " " + thFm[j] + " " + prFm[j] + " " + mark.getPercent());
                if (thFm[j] > 0) {
                    if (thOm > 0) {
                        getGrading((thOm / thFm[j]) * 100);
                        mark.setThOm(resultGrade);
                        mark.setThMark(df.format(thOm));
                        mark.setThPercentage((thOm / thFm[j]) * 100);
                        mark.setThPercent(df.format(mark.getThPercentage()));

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
                        mark.setPrPercentage((prOm / prFm[j]) * 100);
                        mark.setPrPercent(df.format(mark.getPrPercentage()));
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
//                System.out.println(total + " " + totalFm[j] + " " + percent);
                totalGPA = totalGPA + resultGPA;
                mark.setSubjectName(subjectName[j]);
                markList.add(mark);
            }

            s.setMark(markList);
            s.setFullMark(df.format(finalTotalFullMark1));
            s.setTotalThMark(df.format(totalThObtainMark));
            s.setTotalPrMark(df.format(totalPrObtainMark));
            s.setTotalObtain(df.format(((totalThObtainMark + totalPrObtainMark) / finalTotalFullMark1) * 100));
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

//            list.add(this.getStudentResult(map1, subjectName, finalTotalFullMark, noOfSubject, thFm, prFm, totalFm));
            list.add(s);
        });

        Map<String, Object> map = new HashMap();
        map.put("student", list);
        map.put("subject", subjectList);
        sql = "SELECT NAME 'name' FROM class_master WHERE ID=" + classId;
        map.put("class", da.getRecord(sql).get(0));
        sql = "SELECT NAME 'name' FROM program_master WHERE ID=" + program;
        map.put("program", da.getRecord(sql).get(0));
        sql = "SELECT EXAM_NAME 'name',ID,year_ad yearAd,ACADEMIC_YEAR 'academicYear' FROM exam_master WHERE ID=" + exam;
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

    public StudentResult getStudentResult(Map<String, Object> map, String[] subjectName, double totalFullMark, int noOfSubject, float[] thFm, float[] prFm, float[] thPm, float[] prPm, float[] totalFm, int system, String result) {

        String[] thprmark;
        float thOm, prOm, total, percent, totalGPA, totalThObtainMark, totalPrObtainMark;
        String activity;
        totalGPA = 0;
        totalThObtainMark = 0;
        totalPrObtainMark = 0;
        StudentResult s = new StudentResult();
        String boardSymbolNo = map.get("boardSymbolNo").toString().replace("null", "");
        s.setExamRollNo(boardSymbolNo.length() < 3 ? map.get("examRollNo").toString() : boardSymbolNo);
        s.setStuName(map.get("stuName").toString());
        String boardRegNo = map.get("boardRegNo").toString().replace("null", "");
        s.setRegNo(boardRegNo.length() < 3 ? map.get("regNo").toString() : boardRegNo);
        s.setId(Long.parseLong(map.get("regNo").toString()));
        s.setAddress(map.get("address").toString());
        s.setDobBs(map.get("dobBs").toString());
        s.setPresentDays(Integer.valueOf(map.get("presentDays").toString()));
        s.setAbsentDays(Integer.valueOf(map.get("absentDays").toString()));
        s.setRemark(map.get("remark").toString());
        s.setFathersName(map.get("fathersName").toString());
        s.setMothersName(map.get("mothersName").toString());
        s.setDistrict(map.get("DISTRICT").toString());
        s.setWardNo(map.get("WARD_NO").toString());
        s.setMunicipal(map.get("MUNICIPAL").toString());
        s.setProvince(map.get("PROVINCE").toString());
        s.setPhoto(map.get("photo").toString());
        s.setSection(map.get("section").toString());
        s.setRollNo(map.get("rollNo").toString());
        List<StudentResult.Mark> markList = new ArrayList<>();
        StudentResult.Mark mark;
        int failCount = 0;
        for (int j = 0; j < noOfSubject; j++) {
            mark = new StudentResult.Mark();
            thprmark = map.get("sub" + j).toString().split(":");
            thOm = Float.parseFloat(thprmark[0]);
            prOm = Float.parseFloat(thprmark[1]);
            try {
                activity = thprmark[2];
            } catch (Exception ignored) {
                activity = "-";
            }
            mark.setFullMark(df.format(thFm[j] + prFm[j]));
            mark.setPercent(df.format(((thOm + prOm) / (thFm[j] + prFm[j])) * 100));
            if (thFm[j] > 0) {
                if (thprmark[0].equalsIgnoreCase("-0.001")) {
                    mark.setThGPA(0F);
                    mark.setThOm("@");
                    mark.setThMark("0");
                    mark.setThPercentage(0F);
                    mark.setThPercent("0");
                } else if (thprmark[0].equalsIgnoreCase("-0.002")) {
                    mark.setThGPA(0F);
                    mark.setThOm("W");
                    mark.setThMark("0");
                    mark.setThPercentage(0F);
                    mark.setThPercent("0");
                } else if (thOm > 0) {
                    getGrading((thOm / thFm[j]) * 100);
                    mark.setThGPA(resultGPA);
                    mark.setThOm(resultGrade);
                    mark.setThMark(df.format(thOm));
                    mark.setThPercentage((thOm / thFm[j]) * 100);
                    mark.setThPercent(df.format(mark.getThPercentage()));
                    if (system == 3 && thOm < thPm[j]) {
                        failCount++;
                    } else if (system != 3 && resultGPA == 0) {
                        failCount++;
                    }
                } else {
                    mark.setThGPA(0F);
                    mark.setThOm("F");
                    mark.setThPercent("0");
                    mark.setThMark("-");
                    failCount++;
                }
            } else {
                mark.setThOm("-");
                mark.setThPercent("-");
                mark.setThMark("-");
                mark.setThGPA(0F);
            }

            if (prFm[j] > 0) {
                if (thprmark[1].equalsIgnoreCase("-0.001")) {
                    mark.setPhGPA(0F);
                    mark.setPrOm("@");
                    mark.setPhMark("0");
                    mark.setPrPercentage(0F);
                    mark.setPrPercent("0");
                } else if (thprmark[1].equalsIgnoreCase("-0.002")) {
                    mark.setPhGPA(0F);
                    mark.setPrOm("W");
                    mark.setPhMark("0");
                    mark.setPrPercentage(0F);
                    mark.setPrPercent("0");
                } else if (prOm > 0) {
                    getGrading((prOm / prFm[j]) * 100);
                    mark.setPhGPA(resultGPA);
                    mark.setPrOm(resultGrade);
                    mark.setPhMark(df.format(prOm));
                    mark.setPrPercentage((prOm / prFm[j]) * 100);
                    mark.setPrPercent(df.format(mark.getPrPercentage()));
                    if (system == 3 && prOm < prPm[j]) {
                        failCount++;
                    } else if (system != 3 && resultGPA == 0) {
                        failCount++;
                    }

                } else {
                    mark.setPrOm("-");
                    mark.setPhMark("F");
                    mark.setPrPercent("0");
                    mark.setPhGPA(0F);
                    failCount++;
                }
            } else {
                mark.setPrOm("-");
                mark.setPhMark("-");
                mark.setPrPercent("-");
                mark.setPhGPA(0F);
            }
            totalThObtainMark += thOm;
            totalPrObtainMark += prOm;

            total = thOm + prOm;
            percent = (total / totalFm[j]) * 100;
            getGrading(percent);

            mark.setGrade(resultGrade);
            if (system != 3 && resultGPA == 0) {
                mark.setGpa("-");
            } else {
                mark.setGpa(df.format(resultGPA));
            }
            if (thFm[j] > 0 && prFm[j] > 0) {
                if (thprmark[0].equalsIgnoreCase("-0.001") && thprmark[1].equalsIgnoreCase("-0.001")) {
                    mark.setResult("@");
                } else if (thprmark[0].equalsIgnoreCase("-0.002") && thprmark[1].equalsIgnoreCase("-0.002")) {
                    mark.setResult("W");
                } else {
                    mark.setResult(resultRemark);
                }
            } else if (thFm[j] > 0) {
                if (thprmark[0].equalsIgnoreCase("-0.001")) {
                    mark.setResult("@");
                } else if (thprmark[0].equalsIgnoreCase("-0.002")) {
                    mark.setResult("W");
                } else {
                    mark.setResult(resultRemark);
                }
            } else if (prFm[j] > 0) {
                if (thprmark[1].equalsIgnoreCase("-0.001")) {
                    mark.setResult("@");
                } else if (thprmark[1].equalsIgnoreCase("-0.002")) {
                    mark.setResult("W");
                } else {
                    mark.setResult(resultRemark);
                }
            }
            totalGPA = totalGPA + resultGPA;
            mark.setSubjectName(subjectName[j]);
            mark.setActivity(activity);
            markList.add(mark);
        }
        s.setMark(markList);
        s.setFullMark(df.format(totalFullMark));
        s.setTotalThMark(df.format(totalThObtainMark));
        s.setTotalPrMark(df.format(totalPrObtainMark));
        s.setTotalObtain(df.format(((totalThObtainMark + totalPrObtainMark) / totalFullMark) * 100));


        if (failCount == 0) {
            s.setRemark(resultRemark);
            if (system == 3) {
                float f = Float.parseFloat(s.getTotalObtain());
                getGrading(f);
                s.setGpa(df.format(f));
                s.setGrade(resultGrade);
            } else {
                totalGPA = totalGPA / noOfSubject;
                getGradingByGPS(totalGPA);
                s.setGpa(df.format(totalGPA));
                s.setGrade(resultGrade);
            }
        } else {
            s.setGpa("-");
            s.setGrade("F");
            s.setRemark("------------------------------");
        }
        return s;
    }

    String resultGrade, resultRemark;
    float resultGPA;
    DecimalFormat df = new DecimalFormat("#.##");

    void getGrading(float percent) {
        if (percent < 0) {
            resultGrade = "-";
            resultRemark = "@";
            resultGPA = 0;
        } else {
            resultGrade = "F";
            resultRemark = "**";
            resultGPA = 0;
            int i = 0;
            for (; i < rangFrom.length; i++) {
                if (percent >= rangFrom[i]) {
                    resultGrade = grade[i];
                    resultRemark = remark[i];
                    resultGPA = gpa[i];
                    break;
                }
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

    protected float[] gpa, rangFrom;
    protected String[] grade, remark;

}
