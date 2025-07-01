
package com.ms.ware.online.solution.school.controller.billing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.billing.StuBillingMasterDao;
import com.ms.ware.online.solution.school.entity.billing.StuBillingDetail;
import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;
import com.ms.ware.online.solution.school.model.DatabaseName;
import com.ms.ware.online.solution.school.service.billing.StuBillingMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/Payment/Billing/")
public class DueBalanceRestController {

    @Autowired
    StuBillingMasterDao da;
    @Autowired
    StuBillingMasterService tva;

    @PostMapping("Due")
    public Object getDue(HttpServletRequest request, @RequestBody String jsonData) {
        System.gc();
        Map map;
        String schoolCode = "", regNo = "", year = "", month = "";
        Message message = new Message();
        try {
            map = new ObjectMapper().readValue(jsonData, new TypeReference<Map<String, String>>() {
            });
            String token = map.get("token").toString();
            schoolCode = map.get("schoolCode").toString();
            regNo = map.get("regNo").toString();
            year = map.get("year").toString();
            month = map.get("month").toString();

            if (!token.equalsIgnoreCase("infoweb@onlinepayment" + schoolCode + ".msware9@gmail.com")) {
                return message.respondWithError("Invalid Authorization");
            }
        } catch (Exception e) {
            return message.respondWithError("Missing parameter!!");
        }

        String dbStatus = callApi(request.getServerPort(), DatabaseName.getDocumentUrl(), "GET");
        if (!dbStatus.contains("{\"msg\":\"Success\"}")) {
            dbStatus = callApi(request.getServerPort(), DatabaseName.getDocumentUrl(), "POST");
            if (!dbStatus.contains("{\"msg\":\"Success\"}")) {
                return message.respondWithError("School server connection error!!");
            }
        }

        String endDateAd = "", startDateAd = "", sql;
        List ll, list;

        sql = "SELECT S.ID regNo,S.STU_NAME stuName,S.MOBILE_NO mobileNo,S.FATHERS_NAME fathersName,C.NAME 'class',P.NAME program FROM student_info S,class_master C,program_master P WHERE S.CLASS_ID=C.ID AND S.PROGRAM=P.ID AND S.ID='" + regNo + "'";
        list = da.getRecord(sql);
        if (list.isEmpty()) {
            return message.respondWithError("Invalid Student Regd. no (" + regNo + ")");
        }
        Map mapData = (Map) list.get(0);

        if (year.length() == 4 && month.length() == 2) {
            sql = "SELECT COUNT(AD_DATE)  AS totalDay FROM ad_bs_calender WHERE BS_DATE LIKE '" + year + "-" + month + "%'";
            map = (Map) da.getRecord(sql).get(0);
            int totalDay = Integer.parseInt(map.get("totalDay").toString());
            endDateAd = DateConverted.bsToAd(year + "-" + month + "-" + totalDay);
            startDateAd = DateConverted.bsToAd(year + "-" + month + "-01");
            if (startDateAd.length() != 10) {
                return message.respondWithError("year " + year + " month " + month + " not list in school calendar!!");
            }
        } else {
            sql = "SELECT SS.START_DATE startDateAd,SS.END_DATE endDateAd FROM student_info S,school_class_session SS WHERE SS.ACADEMIC_YEAR=S.ACADEMIC_YEAR AND SS.PROGRAM=S.PROGRAM AND SS.CLASS_ID=S.CLASS_ID AND S.ID='" + regNo + "'";
            ll = da.getRecord(sql);
            if (ll.isEmpty()) {
                endDateAd = DateConverted.today();
                startDateAd = DateConverted.toString(DateConverted.addDate(endDateAd, (-30)));
            } else {
                map = (Map) ll.get(0);
                endDateAd = map.get("endDateAd").toString();
                startDateAd = map.get("startDateAd").toString();
            }
        }
        mapData.put("dueAmount", new Message().getFeeDueAmount(regNo, startDateAd, endDateAd));
        return message.respondWithMessage("Success", mapData);

    }

    @PostMapping("Payment")
    public Object doPayment(@RequestBody String jsonData) {
        Map map;
        long regNo;
        String sql, endDateAd, startDateAd, schoolCode = "", referenceId, year = "", month = "", paymentFromName = "";
        double payAmount = 0;
        Message message = new Message();
        try {
            map = new ObjectMapper().readValue(jsonData, new TypeReference<>() {
            });
            String token = map.get("token").toString();
            schoolCode = map.get("schoolCode").toString();
            regNo = Long.parseLong(map.get("regNo").toString());
            payAmount = Float.parseFloat(map.get("payAmount").toString());
            month = map.get("month").toString();
            year = map.get("year").toString();
            referenceId = map.get("referenceId").toString();
            paymentFromName = map.get("paymentFromName").toString();
            if (!token.equalsIgnoreCase("infoweb@onlinepayment" + schoolCode + ".msware9@gmail.com")) {
                return message.respondWithError("Invalid Authorization");
            }
        } catch (Exception e) {
            return message.respondWithError("Missing parameter!!");
        }
        List ll;
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println(year + " " + month);
        if (year.length() == 4 && month.length() == 2) {
            sql = "SELECT COUNT(AD_DATE)  AS totalDay FROM ad_bs_calender WHERE BS_DATE LIKE '" + year + "-" + month + "%'";
            map = da.getRecord(sql).get(0);
            int totalDay = Integer.parseInt(map.get("totalDay").toString());
            endDateAd = DateConverted.bsToAd(year + "-" + month + "-" + totalDay);
            startDateAd = DateConverted.bsToAd(year + "-" + month + "-01");
        } else {
            sql = "SELECT SS.START_DATE startDateAd,SS.END_DATE endDateAd FROM student_info S,school_class_session SS WHERE SS.ACADEMIC_YEAR=S.ACADEMIC_YEAR AND SS.PROGRAM=S.PROGRAM AND SS.CLASS_ID=S.CLASS_ID AND S.ID='" + regNo + "'";
            ll = da.getRecord(sql);
            if (ll.isEmpty()) {
                endDateAd = DateConverted.today();
                startDateAd = DateConverted.toString(DateConverted.addDate(endDateAd, (-30)));
            } else {
                map = (Map) ll.get(0);
                endDateAd = map.get("endDateAd").toString();
                startDateAd = map.get("startDateAd").toString();
            }
        }
        String paymentDate = "";
        sql = "SELECT SS.START_DATE paymentDate,SS.END_DATE endDateAd FROM student_info S,school_class_session SS WHERE SS.ACADEMIC_YEAR=S.ACADEMIC_YEAR AND SS.PROGRAM=S.PROGRAM AND SS.CLASS_ID=S.CLASS_ID AND S.ID='" + regNo + "'";
        ll = da.getRecord(sql);
        if (ll.isEmpty()) {
            paymentDate = DateConverted.today();
        } else {
            map = (Map) ll.get(0);
            paymentDate = map.get("paymentDate").toString();
        }

        sql = "SELECT ROUND(SUM(D.CR)-SUM(D.DR),2) AS amount,D.BILL_ID bill,D.PROGRAM program,D.CLASS_ID 'classId',D.ACADEMIC_YEAR AS academicYear FROM stu_billing_master M,stu_billing_detail D WHERE M.BILL_NO=D.BILL_NO AND M.REG_NO='" + regNo + "' AND D.PAYMENT_DATE<='" + endDateAd + "'  GROUP BY D.ACADEMIC_YEAR,D.PROGRAM,D.CLASS_ID,D.BILL_ID HAVING amount>0 ORDER BY amount DESC";
        List list = da.getRecord(sql);
        int chargeDay;
        String chargeStartDate;
        long classId, academicYear, program;
        double pardayCharge;
        sql = "SELECT START_DATE startDate,MONTHLY_CHARGE monthlyCharge,S.ACADEMIC_YEAR academicYear,S.PROGRAM program,S.CLASS_ID AS classId FROM student_transportation T,student_info S WHERE T.REG_NO=S.ID AND T.REG_NO='" + regNo + "' AND '" + startDateAd + "'>=START_DATE AND (END_DATE IS NULL OR  '" + startDateAd + "'<=END_DATE)";
        ll = da.getRecord(sql);
        if (!ll.isEmpty()) {
            map = (Map) ll.get(0);
            chargeStartDate = map.get("startDate").toString();
            academicYear = Long.parseLong(map.get("academicYear").toString());
            program = Long.parseLong(map.get("program").toString());
            classId = Long.parseLong(map.get("classId").toString());
            pardayCharge = Float.parseFloat(map.get("monthlyCharge").toString()) / 30;
            sql = "SELECT DATEDIFF('" + endDateAd + "',IFNULL(((SELECT MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-1))),'" + chargeStartDate + "')) AS chargeDay,IFNULL((SELECT  MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-1)),'" + chargeStartDate + "') AS startDate FROM DUAL";
            map = da.getRecord(sql).get(0);
            chargeDay = Integer.parseInt(map.get("chargeDay").toString());
            chargeStartDate = map.get("startDate").toString();
            String chargeStartDateBs = DateConverted.adToBs(chargeStartDate);
            if (chargeDay > 0) {
                map = new HashMap();
                map.put("bill", "-1");
                String dd = chargeStartDateBs.substring(8, 10);
                if (dd.equalsIgnoreCase("01")) {
                    chargeDay = Math.round(chargeDay / 30);
                    map.put("amount", df.format(chargeDay * (pardayCharge * 30)));
                } else {
                    map.put("amount", df.format(chargeDay * pardayCharge));
                }
                map.put("academicYear", academicYear);
                map.put("classId", classId);
                map.put("program", program);
                list.add(map);
            }
        }
        sql = "SELECT START_DATE startDate,MONTHLY_CHARGE monthlyCharge,S.ACADEMIC_YEAR academicYear,S.PROGRAM program,S.CLASS_ID AS classId FROM school_hostal T,student_info S WHERE T.REG_NO=S.ID AND T.REG_NO='" + regNo + "' AND '" + startDateAd + "'>=START_DATE AND (END_DATE IS NULL OR  '" + startDateAd + "'<=END_DATE)";
        ll = da.getRecord(sql);
        if (!ll.isEmpty()) {
            map = (Map) ll.get(0);
            chargeStartDate = map.get("startDate").toString();
            academicYear = Long.parseLong(map.get("academicYear").toString());
            program = Long.parseLong(map.get("program").toString());
            classId = Long.parseLong(map.get("classId").toString());
            pardayCharge = Float.parseFloat(map.get("monthlyCharge").toString()) / 30;
            sql = "SELECT DATEDIFF('" + endDateAd + "',IFNULL(((SELECT MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-2))),'" + chargeStartDate + "')) AS chargeDay,IFNULL((SELECT  MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-2)),'" + chargeStartDate + "') AS startDate FROM DUAL";
            map = da.getRecord(sql).get(0);
            chargeDay = Integer.parseInt(map.get("chargeDay").toString());
            chargeStartDate = map.get("startDate").toString();
            String chargeStartDateBs = DateConverted.adToBs(chargeStartDate);
            if (chargeDay > 0) {
                map = new HashMap();
                map.put("bill", "-2");
                String dd = chargeStartDateBs.substring(8, 10);
                if (dd.equalsIgnoreCase("01")) {
                    chargeDay = Math.round(chargeDay / 30);
                    map.put("amount", df.format(chargeDay * (pardayCharge * 30)));
                } else {
                    map.put("amount", df.format(chargeDay * pardayCharge));
                }
                map.put("academicYear", academicYear);
                map.put("classId", classId);
                map.put("program", program);
                list.add(map);
            }
        }
        long[] billId, vClassId, vProgram, vAcademicYear;
        long subjectGroup;
        double amount[];
        billId = new long[list.size()];
        amount = new double[list.size()];
        vClassId = new long[list.size()];
        vProgram = new long[list.size()];
        vAcademicYear = new long[list.size()];
        double totalAmount = 0, setPayAmount = 0;
        int i = 0;

        while (i < list.size()) {
            map = (Map) list.get(i);
            billId[i] = Long.parseLong(map.get("bill").toString());
            amount[i] = Float.parseFloat(map.get("amount").toString());
            vClassId[i] = Long.parseLong(map.get("classId").toString());
            vProgram[i] = Long.parseLong(map.get("program").toString());
            vAcademicYear[i] = Long.parseLong(map.get("academicYear").toString());
            totalAmount = totalAmount + amount[i];
            if (totalAmount > payAmount) {
                amount[i] = payAmount - setPayAmount;
                i++;
                break;
            }
            setPayAmount = setPayAmount + amount[i];
            i++;
        }
        String dateAd = DateConverted.today();
        sql = "SELECT ACADEMIC_YEAR academicYear,PROGRAM program,CLASS_ID classId,SUBJECT_GROUP subjectGroup FROM student_info WHERE ID='" + regNo + "'";
        message.map = da.getRecord(sql).get(0);
        academicYear = Long.parseLong(message.map.get("academicYear").toString());
        program = Long.parseLong(message.map.get("program").toString());
        classId = Long.parseLong(message.map.get("classId").toString());
        subjectGroup = Long.parseLong(message.map.get("subjectGroup").toString());
        sql = "SELECT ID id FROM fiscal_year WHERE '" + dateAd + "' BETWEEN START_DATE AND END_DATE;";
        message.map = da.getRecord(sql).get(0);
        long fiscalYear = Long.parseLong(message.map.get("id").toString());
        sql = "SELECT IFNULL(max(BILL_SN),0)+1 AS billSn FROM stu_billing_master WHERE FISCAL_YEAR='" + fiscalYear + "' AND BILL_TYPE='DR'";
        message.map = da.getRecord(sql).get(0);
        int billSn = Integer.parseInt(message.map.get("billSn").toString());
        String billNo = "";
        if (billSn < 10) {
            billNo = fiscalYear + "000" + billSn;
        } else if (billSn < 100) {
            billNo = fiscalYear + "00" + billSn;
        } else if (billSn < 1000) {
            billNo = fiscalYear + "0" + billSn;
        } else {
            billNo = fiscalYear + "" + billSn;
        }

        StuBillingMaster obj = new StuBillingMaster();
        List<StuBillingDetail> detail = new ArrayList();
        obj.setEnterBy(paymentFromName);
        obj.setEnterDate(new Date());
        obj.setBillNo(billNo);
        obj.setBillSn(billSn);
        obj.setAcademicYear(academicYear);
        obj.setProgram(program);
        obj.setClassId(classId);
        obj.setSubjectGropu(subjectGroup);
        obj.setRegNo(regNo);
        obj.setFiscalYear(fiscalYear);
        obj.setBillAmount(payAmount);
        obj.setAutoGenerate("N");
        obj.setBillType("DR");
        obj.setRemark("Payment from " + paymentFromName);
        double paidAmount = 0;
        billSn = 1;

        for (int j = 0; j < i; j++) {
            paidAmount = paidAmount + amount[j];
            detail.add(new StuBillingDetail(billNo, billSn, regNo, academicYear, program, classId, billId[j], amount[j], 0, paymentDate, "N"));
            billSn++;
        }
        if (payAmount > paidAmount) {
            detail.add(new StuBillingDetail(billNo, billSn, regNo, academicYear, program, classId, -3, (payAmount - paidAmount), 0, paymentDate, "N"));
        }
        obj.setDetail(detail);
        obj.setReferenceId(referenceId);
        int row = da.save(obj);
        String msg = da.getMsg();
        map = new HashMap();
        if (row > 0) {
            map.put("message", "Success");
            map.put("billNo", billNo);
            return message.respondWithMessage("Success", map);
        }
        return message.respondWithError(msg);

    }

    public String callApi(int port, String context, String method) {
        String status = "";
        String requestUrl = "http://localhost:" + port + context + "/public/api/hibernate-util";
        try {
            URL obj = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            status = response.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return status;
    }

    @PostMapping("PaymentVerification")
    public Object paymentVerification(HttpServletRequest request, @RequestBody String jsonData) {
        System.gc();
        Map map;
        String schoolCode = "", regNo = "", ReferenceID = "";
        Message message = new Message();
        try {
            map = new ObjectMapper().readValue(jsonData, new TypeReference<>() {
            });
            String token = map.get("token").toString();
            schoolCode = map.get("schoolCode").toString();
            regNo = map.get("regNo").toString();
            ReferenceID = map.get("ReferenceID").toString();

            if (!token.equalsIgnoreCase("infoweb@onlinepayment" + schoolCode + ".msware9@gmail.com")) {
                return message.respondWithError("Invalid Authorization");
            }
        } catch (Exception e) {
            return message.respondWithError("Missing parameter!!");
        }

        String dbStatus = callApi(request.getServerPort(), DatabaseName.getDocumentUrl(), "GET");
        if (!dbStatus.contains("{\"msg\":\"Success\"}")) {
            dbStatus = callApi(request.getServerPort(), DatabaseName.getDocumentUrl(), "POST");
            if (!dbStatus.contains("{\"msg\":\"Success\"}")) {
                return message.respondWithError("School server connection error!!");
            }
        }
        DB db = new DB();
        List l = db.getRecord("SELECT BILL_NO billNo,BILL_AMOUNT billAmount FROM stu_billing_master WHERE REG_NO='" + regNo + "' AND REFERENCE_ID='" + ReferenceID + "'");
        System.out.println(l);
        if (l.isEmpty()) {
            map = new HashMap();
            map.put("timestamp", new Date());

            map.put("error", "server response");
            map.put("status", 404);
            l = db.getRecord("SELECT * FROM student_info WHERE ID='" + regNo + "'");
            if (l.isEmpty()) {
                map.put("message", "Invalid Reg No. !!");
                return map;
            }
            map.put("message", "Invalid ReferenceID !!");
            return map;
        }
        return message.respondWithMessage("Success", l.get(0));

    }
}

