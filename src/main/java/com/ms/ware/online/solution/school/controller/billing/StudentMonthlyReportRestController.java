/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.billing;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Billing/Report")
public class StudentMonthlyReportRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private DB db;

    @PostMapping("/WavFee")
    public Object wavFee(@RequestParam String type, @RequestParam(required = false) Long regNo, @RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup) {

        return db.getRecord("SELECT M.BILL_TYPE AS billType, D.ACADEMIC_YEAR AS academicYear, P.NAME AS program, C.NAME AS className, G.NAME AS subjectGroup, S.STU_NAME AS stuName, S.ID AS regNo, ROUND(SUM(D.DR), 2) AS subAmount, ROUND(SUM(D.CR), 2) AS addAmount, B.NAME AS feeName, IFNULL(MAX(M.REMARK), '') AS remark, GET_BS_DATE(max(M.ENTER_DATE)) AS enterDate,MAX(M.ENTER_BY) AS enterBy FROM stu_billing_master M JOIN stu_billing_detail D ON M.BILL_NO = D.BILL_NO JOIN bill_master B ON D.BILL_ID = B.ID JOIN program_master P ON M.PROGRAM = P.ID JOIN class_master C ON D.CLASS_ID = C.ID JOIN student_info S ON D.REG_NO = S.ID JOIN subject_group G ON S.SUBJECT_GROUP = G.ID where M.BILL_TYPE in(" + type + ") and D.REG_NO=ifnull(" + regNo + ",D.REG_NO) AND D.ACADEMIC_YEAR=IFNULL(" + academicYear + ",D.ACADEMIC_YEAR) AND M.PROGRAM=IFNULL(" + program + ",M.PROGRAM) AND D.CLASS_ID=IFNULL(" + classId + ",D.CLASS_ID) AND M.SUBJECT_GROPU=IFNULL(" + subjectGroup + ",M.SUBJECT_GROPU)  GROUP BY S.ID, D.ACADEMIC_YEAR, P.NAME, C.NAME, G.NAME, S.STU_NAME, B.ID, B.NAME, M.BILL_TYPE ORDER BY stuName");

    }

    @GetMapping("/FeeWise")
    public Object feeWise(@RequestParam String reportType, @RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam(required = false) Long subjectGroup, @RequestParam(required = false) Long feeId, @RequestParam String dateFrom, @RequestParam String dateTo) {

        String sql = "";
        dateFrom = DateConverted.bsToAd(dateFrom);
        dateTo = DateConverted.bsToAd(dateTo);
        if (reportType.equalsIgnoreCase("Receipt")) {
            sql = "SELECT S.ID as id, D.ACADEMIC_YEAR academicYear, P.NAME as program, C.NAME AS className, G.NAME as subjectGroup, S.STU_NAME AS stuName, S.ID AS regNo, B.NAME feeName, SUM(D.CR) amount, SUM(D.DR) received FROM stu_billing_master M join stu_billing_detail D on M.BILL_NO = D.BILL_NO join bill_master B on D.BILL_ID = B.ID join program_master P on M.PROGRAM = P.ID join class_master C on D.CLASS_ID = C.ID join student_info S on D.REG_NO = S.ID join subject_group G on S.SUBJECT_GROUP = G.ID WHERE D.ACADEMIC_YEAR = IFNULL(" + academicYear + ", D.ACADEMIC_YEAR) AND M.PROGRAM = IFNULL(" + program + ", M.PROGRAM) AND D.CLASS_ID = IFNULL(" + classId + ", D.CLASS_ID) AND M.SUBJECT_GROPU = IFNULL(" + subjectGroup + ", M.SUBJECT_GROPU) AND D.BILL_ID = IFNULL("+feeId+", D.BILL_ID) AND M.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' group by S.ID, D.ACADEMIC_YEAR, P.id, C.id, G.id, B.id, P.NAME, C.NAME, G.NAME, S.STU_NAME, S.ID, B.NAME ORDER BY academicYear, program, className, subjectGroup, regNo, feeName";
        } else if (reportType.equalsIgnoreCase("Sum")) {
            sql = "SELECT '' roleNo,'' billNo,'' academicYear,'' program,'' AS className,'' subjectGroup,'' stuName,'' regNo,SUM(D.DR) amount,B.NAME feeName FROM stu_billing_master M,stu_billing_detail D,bill_master B,program_master P,class_master C WHERE M.BILL_NO=D.BILL_NO AND M.PROGRAM=P.ID AND D.CLASS_ID=C.ID AND D.BILL_ID=B.ID AND M.BILL_TYPE='DR'  AND D.ACADEMIC_YEAR=IFNULL(" + academicYear + ",D.ACADEMIC_YEAR) AND M.PROGRAM=IFNULL(" + program + ",M.PROGRAM) AND D.CLASS_ID=IFNULL(" + classId + ",D.CLASS_ID) AND M.SUBJECT_GROPU=IFNULL(" + subjectGroup + ",M.SUBJECT_GROPU) AND D.BILL_ID=IFNULL(" + feeId + ",D.BILL_ID) AND M.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND D.DR >0 GROUP BY D.BILL_ID ORDER BY academicYear,program,className,subjectGroup,regNo,feeName";
        } else {
            sql = "SELECT S.ROLL_NO roleNo,D.BILL_NO billNo,D.ACADEMIC_YEAR academicYear,P.NAME program,C.NAME AS className,G.NAME subjectGroup,S.STU_NAME stuName,S.ID regNo,D.DR amount,B.NAME feeName FROM stu_billing_master M,stu_billing_detail D,bill_master B,program_master P,class_master C,student_info S,subject_group G WHERE M.BILL_NO=D.BILL_NO AND M.PROGRAM=P.ID AND D.CLASS_ID=C.ID AND S.SUBJECT_GROUP=G.ID AND D.REG_NO=S.ID AND D.BILL_ID=B.ID AND M.BILL_TYPE='DR' AND D.ACADEMIC_YEAR=IFNULL(" + academicYear + ",D.ACADEMIC_YEAR) AND M.PROGRAM=IFNULL(" + program + ",M.PROGRAM) AND D.CLASS_ID=IFNULL(" + classId + ",D.CLASS_ID) AND M.SUBJECT_GROPU=IFNULL(" + subjectGroup + ",M.SUBJECT_GROPU) AND D.BILL_ID=IFNULL(" + feeId + ",D.BILL_ID) AND M.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND D.DR >0"
                    + " UNION SELECT '' roleNo,D.BILL_NO billNo,D.ACADEMIC_YEAR academicYear,P.NAME program,C.NAME AS className,G.NAME subjectGroup,M.STUDENT_NAME stuName,'NA' regNo,D.DR amount,B.NAME feeName FROM stu_billing_master M,stu_billing_detail D,bill_master B,program_master P,class_master C,subject_group G WHERE M.BILL_NO=D.BILL_NO AND M.PROGRAM=P.ID AND D.CLASS_ID=C.ID AND G.ID=M.SUBJECT_GROPU AND D.REG_NO IS NULL AND D.BILL_ID=B.ID AND M.BILL_TYPE='DR' AND D.ACADEMIC_YEAR=IFNULL(" + academicYear + ",D.ACADEMIC_YEAR) AND M.PROGRAM=IFNULL(" + program + ",M.PROGRAM) AND D.CLASS_ID=IFNULL(" + classId + ",D.CLASS_ID) AND M.SUBJECT_GROPU=IFNULL(" + subjectGroup + ",M.SUBJECT_GROPU) AND D.BILL_ID=IFNULL(" + feeId + ",D.BILL_ID) AND M.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' AND D.DR >0 "
                    + " ORDER BY academicYear,program,className,subjectGroup,regNo,feeName";
        }
        return db.getRecord(sql);
    }

    @GetMapping("/StudentMonthly")
    public Object index(@RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId) {

        String sql = "SELECT SUBSTR(D.PAYMENT_DATE_BS,1,7) AS month FROM school_class_session_bill_date D,school_class_session M WHERE  M.ID=D.MASTER_ID AND M.PROGRAM='" + program + "' AND M.CLASS_ID='" + classId + "' AND M.ACADEMIC_YEAR=" + academicYear + " ORDER BY month";
        List<Map<String, Object>> monthList = db.getRecord(sql);

        String month = "";
        for (Map<String, Object> map : monthList) {
            month = map.get("month").toString();
        }
        return month;
    }

    @DeleteMapping("/IndividualFee/{billNo}/{sn}")
    public String index(@PathVariable String billNo, @PathVariable int sn) {
        AuthenticatedUser user = facade.getAuthentication();
        if (!user.getUserName().equalsIgnoreCase("ADMIN")) {
            throw new CustomException("Permission denied");
        }

        String sql = "delete from stu_billing_detail where bill_no='" + billNo + "' and bill_sn='" + sn + "'";
        db.delete(sql);
        return "Success";
    }

    @PostMapping("/IndividualFee")
    public Object index(@RequestParam String regNo, @RequestParam String year, @RequestParam String month, @RequestParam String type) {
        String sql;

        month = month.isEmpty() ? "12" : month;
        if (type.contains("D")) {
            sql = "SELECT B.NAME billName,M.ENTER_DATE billDate,D.BILL_SN billSn, D.REG_NO,M.BILL_NO billNo,M.BILL_TYPE billType, D.DR dr,D.CR cr,D.ACADEMIC_YEAR academicYear,C.NAME className,P.NAME program,ifnull(M.REMARK,'') remark FROM stu_billing_detail D,stu_billing_master M,program_master P,class_master C,bill_master B WHERE D.BILL_ID=B.ID AND M.BILL_NO=D.BILL_NO AND D.PROGRAM=P.ID AND D.CLASS_ID=C.ID AND M.REG_NO='" + regNo + "' ORDER BY D.payment_date,billName,billType";
            return db.getMapRecord(sql);
        } else if (type.contains("B")) {
            sql = "select d.BILL_NO billNo,sum(CR) billAmount,sum(DR) paid, m.ENTER_DATE billDate,m.ENTER_BY entryBy from stu_billing_master m join stu_billing_detail d on m.BILL_NO = d.BILL_NO and d.REG_NO = '" + regNo + "' group by d.bill_no, m.ENTER_DATE, m.ENTER_BY order by billDate";
            return db.getMapRecord(sql);
        }

        Map<String, Object> map = new HashMap<>();
        sql = "SELECT STU_NAME studentName,FATHERS_NAME fathersName,MOBILE_NO mobileNo,CONCAT(DISTRICT,' ',MUNICIPAL,' ',WARD_NO,' ',TOL) AS address FROM student_info WHERE ID='" + regNo + "'";
        try {
            map.put("student", db.getRecord(sql).get(0));
        } catch (Exception e) {
            return new Message().respondWithError("Invalid Reg No!!");
        }
        String billId;
        float paidAmount;
        String paymentDate = "NULL";
        if (year.length() == 4 && month.length() == 2) {
            sql = "SELECT MAX(AD_DATE) paymentDate FROM ad_bs_calender WHERE BS_DATE LIKE '" + year + "-" + month + "%'";
            Map m = db.getRecord(sql).get(0);
            paymentDate = "'" + m.get("paymentDate").toString() + "'";
            map.put("monthTill", "Till " + year + " " + new Message().getMonthName(month));
        } else {
            map.put("monthTill", "");
        }
        sql = "SELECT D.CLASS_ID classId , D.ACADEMIC_YEAR year,ROUND(SUM(D.CR)-SUM(D.DR),2) AS remaining,C.NAME className,AY.YEAR academicYear,B.NAME billName,B.ID bill FROM stu_billing_master M,stu_billing_detail D,bill_master B,class_master C,academic_year AY WHERE M.BILL_NO=D.BILL_NO AND D.BILL_ID=B.ID AND D.CLASS_ID=C.ID AND D.ACADEMIC_YEAR=AY.ID AND M.REG_NO='" + regNo + "' AND D.PAYMENT_DATE<=IFNULL(" + paymentDate + ",D.PAYMENT_DATE)   GROUP BY D.ACADEMIC_YEAR,D.CLASS_ID,D.PROGRAM,D.BILL_ID ORDER BY D.ACADEMIC_YEAR,D.CLASS_ID,billName";
        List<Map<String, Object>> l = db.getRecord(sql);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> m;
        long classId, academicYear;
        for (Map<String, Object> m2 : l) {
            try {

                billId = m2.get("bill").toString();
                classId = Long.parseLong(m2.get("classId").toString());
                academicYear = Long.parseLong(m2.get("year").toString());
                sql = "SELECT ROUND(SUM(DR),2) paidAmount FROM stu_billing_detail D,stu_billing_master M WHERE M.BILL_NO=D.BILL_NO AND M.BILL_TYPE='DR' AND D.BILL_ID='" + billId + "' AND M.REG_NO='" + regNo + "' AND  D.ACADEMIC_YEAR ='" + academicYear + "' AND  D.CLASS_ID='" + classId + "' AND D.PAYMENT_DATE<=IFNULL(" + paymentDate + ",D.PAYMENT_DATE) ";
                try {
                    m = db.getRecord(sql).get(0);
                    paidAmount = Float.parseFloat(m.get("paidAmount").toString());
                } catch (Exception e) {
                    paidAmount = 0;
                }

                m2.put("paidAmount", paidAmount);
                sql = "SELECT ROUND(SUM(DR),2) wavAmount FROM stu_billing_detail D,stu_billing_master M WHERE M.BILL_NO=D.BILL_NO AND M.BILL_TYPE='WAV' AND D.BILL_ID='" + billId + "' AND M.REG_NO='" + regNo + "' AND  D.ACADEMIC_YEAR ='" + academicYear + "' AND  D.CLASS_ID='" + classId + "' AND D.PAYMENT_DATE<=IFNULL(" + paymentDate + ",D.PAYMENT_DATE) ";
                try {
                    m = db.getRecord(sql).get(0);
                    paidAmount = Float.parseFloat(m.get("wavAmount").toString());
                } catch (Exception e) {
                    paidAmount = 0;
                }
                m2.put("wavAmount", paidAmount);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            list.add(m2);
        }

        map.put("data", list);
        return map;

    }

    @GetMapping("/ClassWise")
    public Object classWiseReport(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam String opt, @RequestParam String year, @RequestParam String month, @RequestParam(defaultValue = "0") double amountFrom, @RequestParam(defaultValue = "0") double amountTo) {
        String sql;

        Map<String, Object> map = new HashMap<>();
        Message message = new Message();

        try {
            sql = "SELECT YEAR AS name FROM academic_year WHERE ID=" + academicYear;
            Map<String, Object> m = db.getRecord(sql).get(0);
            map.put("academicYear", m.get("name"));
        } catch (Exception e) {
            return new Message().respondWithError("Please provide Academic Year!!");
        }
        String remainingAmount;


        if (opt.equalsIgnoreCase("BETWEEN")) {
            remainingAmount = " HAVING  remaining between '" + amountFrom + "' and '" + amountTo + "' ";
        } else {
            remainingAmount = " HAVING  remaining " + opt + amountFrom + " ";
        }
        String paymentDate = "NULL";
        if (year.length() == 4 && month.length() == 2) {
            sql = "SELECT MAX(AD_DATE) paymentDate FROM ad_bs_calender WHERE BS_DATE LIKE '" + year + "-" + month + "%'";
            Map<String, Object> m = db.getRecord(sql).get(0);
            paymentDate = "'" + m.get("paymentDate").toString() + "'";
            map.put("monthTill", "Till " + year + " " + message.getMonthName(month));
        } else {
            map.put("monthTill", "");
        }
        sql = "SELECT SUM(D.CR) - SUM(D.DR) AS remaining, IFNULL(SUM(CASE WHEN M.BILL_TYPE = 'MNG' THEN D.DR - D.CR END), 0) AS managed, IFNULL(SUM(CASE WHEN M.BILL_TYPE = 'DR' AND is_extra = 'N' THEN D.DR END), 0) AS paidAmount, IFNULL(SUM(CASE WHEN M.BILL_TYPE = 'DR' AND is_extra = 'Y' THEN D.DR END), 0) AS extraPaidAmount, IFNULL(SUM(CASE WHEN M.BILL_TYPE = 'WAV' THEN D.DR - D.CR END), 0) AS wavAmount, C.NAME AS className, S.STU_NAME AS studentName, S.FATHERS_NAME AS fatherName, S.MOBILE_NO AS mobileNo, ROLL_NO AS rollNo, D.REG_NO AS regNo FROM stu_billing_master M JOIN stu_billing_detail D ON M.BILL_NO = D.BILL_NO JOIN student_info S ON D.reg_no = S.id LEFT JOIN class_master C ON D.class_id = C.id where D.academic_year = ifnull(" + academicYear + ", D.academic_year) and D.class_id = ifnull(" + classId + ", D.class_id) and D.PAYMENT_DATE <= " + paymentDate + " GROUP BY D.REG_NO, S.CLASS_ID, ROLL_NO, C.NAME, S.STU_NAME, S.FATHERS_NAME, S.MOBILE_NO  " + remainingAmount + " ORDER BY S.CLASS_ID, rollNo;";
        map.put("data", db.getRecord(sql));

        try {
            if (classId > 0) {
                sql = "SELECT NAME AS name FROM class_master WHERE ID=" + classId;
                Map<String, Object> m = db.getRecord(sql).get(0);
                map.put("classId", m.get("name"));
            }
        } catch (Exception e) {
            map.put("classId", "All");
        }
        return map;
    }

}
