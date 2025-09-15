package com.ms.ware.online.solution.school.service.billing;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.SmsService;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.billing.StuBillingMasterDao;
import com.ms.ware.online.solution.school.dto.*;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.billing.*;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.service.account.VoucherEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class StuBillingMasterServiceImp implements StuBillingMasterService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private StuBillingMasterDao da;
    @Autowired
    private SmsService smsService;
    @Autowired
    private OrganizationInformation organizationInformation;
    @Autowired
    private VoucherEntry ve;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(String regNo, String year, String month) {
        String endDateAd;
        List<Map<String, Object>> ll;
        Map<String, Object> map;
        if (year.length() == 4 && month.length() == 2) {
            endDateAd = DateConverted.bsToAd(year + "-" + month + "-32");
            if (endDateAd == null || endDateAd.equalsIgnoreCase("invalid"))
                endDateAd = DateConverted.bsToAd(year + "-" + month + "-31");
            if (endDateAd == null || endDateAd.equalsIgnoreCase("invalid"))
                endDateAd = DateConverted.bsToAd(year + "-" + month + "-30");
            if (endDateAd == null || endDateAd.equalsIgnoreCase("invalid"))
                endDateAd = DateConverted.bsToAd(year + "-" + month + "-29");
            if (endDateAd == null || endDateAd.equalsIgnoreCase("invalid"))
                endDateAd = DateConverted.bsToAd(year + "-" + month + "-28");

        } else {
            sql = "SELECT SS.START_DATE startDateAd,SS.END_DATE endDateAd FROM student_info S,school_class_session SS WHERE SS.ACADEMIC_YEAR=S.ACADEMIC_YEAR AND SS.PROGRAM=S.PROGRAM AND SS.CLASS_ID=S.CLASS_ID AND S.ID='" + regNo + "'";
            ll = da.getRecord(sql);
            if (ll.isEmpty()) {
                return message.respondWithError("Please Define Education Session");
            }
            map = ll.get(0);
            endDateAd = map.get("endDateAd").toString();
        }
        sql = "SELECT ROUND(SUM(D.CR)-SUM(D.DR),2) AS amount,B.ID bill,D.PROGRAM program,D.CLASS_ID 'classId',D.ACADEMIC_YEAR AS academicYear,B.NAME billName,P.NAME AS programName,C.NAME AS className FROM stu_billing_master M,stu_billing_detail D,bill_master B,program_master P,class_master C WHERE M.BILL_NO=D.BILL_NO AND D.BILL_ID=B.ID AND D.PROGRAM=P.ID AND D.CLASS_ID=C.ID AND M.REG_NO='" + regNo + "' AND D.PAYMENT_DATE<='" + endDateAd + "'  GROUP BY D.ACADEMIC_YEAR,D.PROGRAM,D.CLASS_ID,D.BILL_ID HAVING amount>0";
        return da.getRecord(sql);

    }

    @Override
    public String save(BillingMasterReq req) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        String userName = td.getUserName();
        StuBillingMaster obj = new StuBillingMaster();
        List<StuBillingDetail> detail = new ArrayList<>();
        try {

            String stuName, className, mobileNo;
            Map<String, Object> map;
            String cashAccount = req.getAcCode();
            if (cashAccount.isEmpty()) {
                throw new CustomException("Please define cash Account of " + userName);
            }
            String bsdate = req.getBillDate();
            String dateAd = DateConverted.bsToAd(bsdate);
            if (dateAd == null || dateAd.length() != 10)
                throw new CustomException("Invalid Date " + bsdate);
            long billId, academicYear, program, classId, subjectGroup;
            String isExtra;
            long regNo = req.getRegNo();
            double amount, paidAmount = 0, payAmount = req.getPayAmount();
            String year = req.getYear();
            String month = req.getMonth();
            String paymentDate = DateConverted.bsToAd(year + "-" + month + "-32");
            if (paymentDate == null || paymentDate.equalsIgnoreCase("invalid"))
                paymentDate = DateConverted.bsToAd(year + "-" + month + "-31");
            if (paymentDate == null || paymentDate.equalsIgnoreCase("invalid"))
                paymentDate = DateConverted.bsToAd(year + "-" + month + "-30");
            if (paymentDate == null || paymentDate.equalsIgnoreCase("invalid"))
                paymentDate = DateConverted.bsToAd(year + "-" + month + "-29");
            if (paymentDate == null || paymentDate.equalsIgnoreCase("invalid"))
                paymentDate = DateConverted.bsToAd(year + "-" + month + "-28");

            String remark = req.getRemark();
            academicYear = req.getAcademicYear();
            program = req.getProgram();
            classId = req.getClassId();

            sql = "SELECT S.SUBJECT_GROUP subjectGroup,S.STU_NAME stuName,P.NAME programName,C.NAME className,ifnull(S.MOBILE_NO,'') mobileNo FROM student_info S,program_master P,class_master C WHERE S.PROGRAM=P.ID AND S.CLASS_ID=C.ID AND S.ID='" + regNo + "'";
            map = da.getRecord(sql).get(0);
            subjectGroup = Long.parseLong(map.get("subjectGroup").toString());
            stuName = map.get("stuName").toString();
            mobileNo = map.get("mobileNo").toString();
            className = map.get("className").toString();
            sql = "SELECT ID id FROM fiscal_year WHERE '" + dateAd + "' BETWEEN START_DATE AND END_DATE;";
            map = da.getRecord(sql).get(0);
            long fiscalYear = Long.parseLong(map.get("id").toString());
            sql = "SELECT IFNULL(max(BILL_SN),0)+1 AS billSn FROM stu_billing_master WHERE FISCAL_YEAR='" + fiscalYear + "' AND BILL_TYPE='DR'";
            map = da.getRecord(sql).get(0);
            int billSn = Integer.parseInt(map.get("billSn").toString());
            String billNo;
            if (billSn < 10) {
                billNo = fiscalYear + "000" + billSn;
            } else if (billSn < 100) {
                billNo = fiscalYear + "00" + billSn;
            } else if (billSn < 1000) {
                billNo = fiscalYear + "0" + billSn;
            } else {
                billNo = fiscalYear + "" + billSn;
            }

            obj.setEnterBy(userName);
            obj.setEnterDate(DateConverted.toDate(dateAd));
            obj.setPaymentTill(DateConverted.toDate(paymentDate));
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
            obj.setRemark(remark);
            billSn = 1;
            Date paidDate = new Date();
            for (BillingDetailReq d : req.getObj()) {
                amount = d.getAmount();
                paidAmount = paidAmount + amount;
                if (paidAmount > payAmount) {
                    break;
                }

                try {
                    academicYear = d.getAcademicYear();
                } catch (Exception e) {
                    academicYear = obj.getAcademicYear();
                }
                try {
                    program = d.getProgram();
                } catch (Exception e) {
                    program = obj.getProgram();
                }
                try {
                    classId = d.getClassId();
                } catch (Exception e) {
                    classId = obj.getClassId();
                }

                billId = d.getBillId();
                isExtra = d.getIsExtra();

                detail.add(new StuBillingDetail(billNo, billSn, regNo, academicYear, program, classId, billId, amount, 0, paidDate, isExtra));
                if (isExtra.equalsIgnoreCase("Y")) {
                    detail.add(new StuBillingDetail(billNo, billSn * (-1), regNo, academicYear, program, classId, billId, 0, amount, paidDate, isExtra));

                }

                billSn++;
            }
            obj.setDetail(detail);
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                if (smsService.isConfigured()) {
                    new Thread(() -> {
                        try {
                            String sname = stuName;
                            if (sname.contains(" ")) {
                                sname = sname.substring(0, sname.indexOf(" "));
                            }

                            String sms = "Dear " + sname + ",Class " + className + " fee deposited Rs." + payAmount + " on " + bsdate + " bill no " + obj.getBillNo() + ". Thank you.";
                            smsService.sendSMS(mobileNo, sms, userName);
                        } catch (Exception ignored) {
                        }
                    }).start();

                }

                return "{\"billNo\":\"" + billNo + "\"}";
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            throw new CustomException(msg);

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public Object wavFee(StuBillingMasterWav req) {

        AuthenticatedUser td = facade.getAuthentication();
        ;

        String userName = td.getUserName();
        List<Map<String, Object>> tempList;
        StuBillingMaster obj = new StuBillingMaster();
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        try {

            String date = DateConverted.bsToAd(req.getDate());

            long regNo = req.getRegNo();
            String remark = req.getRemark();
            double amount;

            long billId, academicYear, program, classId, subjectGroup;

            sql = "SELECT ACADEMIC_YEAR academicYear,PROGRAM program,CLASS_ID classId,SUBJECT_GROUP subjectGroup FROM student_info WHERE ID='" + regNo + "'";
            tempList = da.getRecord(sql);
            message.map = tempList.get(0);
            academicYear = Long.parseLong(message.map.get("academicYear").toString());
            program = Long.parseLong(message.map.get("program").toString());
            classId = Long.parseLong(message.map.get("classId").toString());
            subjectGroup = Long.parseLong(message.map.get("subjectGroup").toString());

            sql = "SELECT ID masterId,START_DATE startDate,END_DATE endDate,TOTAL_MONTH totalMonth FROM school_class_session WHERE ACADEMIC_YEAR='" + academicYear + "' AND PROGRAM='" + program + "' AND CLASS_ID='" + classId + "'";
            List<Map<String, Object>> list = da.getRecord(sql);
            if (list.isEmpty()) {
                msg = "Please define school class session";
                return false;
            }
            message.map = list.get(0);
            String paymentDate = message.map.get("startDate").toString();
            String[] dateArray = DateConverted.adToBs(paymentDate).split("-");
            int year = Integer.parseInt(dateArray[0]);
            int month = Integer.parseInt(dateArray[1]);
            int totalMonth = Integer.parseInt(message.map.get("totalMonth").toString());
            sql = "SELECT ID id FROM fiscal_year WHERE '" + date + "' BETWEEN START_DATE AND END_DATE;";
            tempList = da.getRecord(sql);
            if (tempList.isEmpty()) {
                return message.respondWithError("Please define fiscal Year");
            }
            message.map = tempList.get(0);
            long fiscalYear = Long.parseLong(message.map.get("id").toString());
            sql = "SELECT IFNULL(max(BILL_SN),0)+1 AS billSn FROM stu_billing_master WHERE FISCAL_YEAR='" + fiscalYear + "' AND BILL_TYPE='WAV'";
            message.map = da.getRecord(sql).get(0);
            int billSn = Integer.parseInt(message.map.get("billSn").toString());

            String billNo = "";
            if (billSn < 10) {
                billNo = fiscalYear + "WAV000" + billSn;
            } else if (billSn < 100) {
                billNo = fiscalYear + "WAV00" + billSn;
            } else if (billSn < 1000) {
                billNo = fiscalYear + "WAV0" + billSn;
            } else {
                billNo = fiscalYear + "WAV" + billSn;
            }
            obj.setEnterBy(userName);
            obj.setEnterDate(new Date());
            obj.setBillNo(billNo);
            obj.setBillSn(billSn);
            obj.setAcademicYear(academicYear);
            obj.setProgram(program);
            obj.setClassId(classId);
            obj.setSubjectGropu(subjectGroup);
            obj.setRegNo(regNo);
            obj.setRemark(remark);
            obj.setFiscalYear(fiscalYear);
            obj.setAutoGenerate("N");
            obj.setBillType("WAV");
            row = da.save(obj);
            sql = "";

            String aa, monthPayDate;
            double monthAmount;
            int index = 1;
            for (StuBillingMasterWavDetail data : req.getObj()) {

                amount = data.getAmount();
                academicYear = data.getAcademicYear();
                classId = data.getClassId();
                program = data.getProgram();
                billId = data.getBillId();
                String billTime;
                if (!(billId == (-1) || billId == (-2))) {
                    aa = "SELECT PAY_TIME 'time' FROM fee_setup WHERE ACADEMIC_YEAR='" + academicYear + "' AND PROGRAM='" + program + "' AND CLASS_ID='" + classId + "' AND SUBJECT_GROUP='" + subjectGroup + "' AND FEE_ID='" + billId + "'";
                    tempList = da.getRecord(aa);
                    if (tempList.isEmpty()) {
                        return message.respondWithError("Please define fee setup");
                    }
                    message.map = tempList.get(0);
                    billTime = message.map.get("time").toString();
                } else {
                    billTime = "1";
                }

                if (billTime.equalsIgnoreCase("1")) {
                    sql = sql + "INSERT INTO stu_billing_detail (BILL_NO, BILL_SN, ACADEMIC_YEAR, CLASS_ID, CR, DR, PAYMENT_DATE, PROGRAM, REG_NO, BILL_ID) VALUES ('" + billNo + "', " + index + ", " + academicYear + ", " + classId + ", 0, " + amount + ", '" + paymentDate + "', " + program + ", '" + regNo + "', " + billId + ");\n";
                } else {
                    monthAmount = amount / totalMonth;
                    for (int k = 0; k < totalMonth; k++) {
                        if (month <= 9) {
                            monthPayDate = DateConverted.bsToAd(year + "-0" + month + "-" + dateArray[2]);
                        } else {
                            monthPayDate = DateConverted.bsToAd(year + "-" + month + "-" + dateArray[2]);
                        }
                        sql = sql + "INSERT INTO stu_billing_detail (BILL_NO, BILL_SN, ACADEMIC_YEAR, CLASS_ID, CR, DR, PAYMENT_DATE, PROGRAM, REG_NO, BILL_ID) VALUES ('" + billNo + "', " + index + k + ", " + academicYear + ", " + classId + ", 0, " + monthAmount + ", '" + monthPayDate + "', " + program + ", '" + regNo + "', " + billId + ");\n";
                        month++;
                        if (month == 13) {
                            month = 1;
                            year++;
                        }
                    }

                }
                index++;
            }
            row = da.delete(sql);
            msg = da.getMsg();
            if (row > 0) {
                message.map = new HashMap<>();
                message.respondWithMessage("Success");
                message.map.put("message", "Success");
                message.map.put("billNo", billNo);
                return message.map;
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }

    }

    @Override
    public Object getAll(String billNo) {

        OrganizationInformationData data = organizationInformation.getData();

        DecimalFormat df = new DecimalFormat("#.##");
        sql = "SELECT BILL_NO billNo,IFNULL(REG_NO,'N/A') regNo,GET_BS_DATE(B.ENTER_DATE) enterDate,B.ENTER_BY enterBy,STUDENT_NAME studentName,FATHER_NAME fatherName,MOBILE_NO mobileNo,ADDRESS address,P.NAME program,C.NAME 'class',IFNULL(B.REMARK,'') remark,'' rollNo,G.`NAME` subjectGroup,'' as admissionYear FROM stu_billing_master B,program_master P,class_master C,subject_group G WHERE REG_NO IS NULL AND B.PROGRAM=P.ID AND B.CLASS_ID=C.ID AND G.`ID`=B.`SUBJECT_GROPU` AND BILL_NO='" + billNo + "' "
                + " UNION "
                + "SELECT BILL_NO billNo, IFNULL(REG_NO, 'N/A') regNo, GET_BS_DATE(B.ENTER_DATE) enterDate, B.ENTER_BY enterBy, S.STU_NAME studentName, S.fathers_name fatherName, S.MOBILE_NO mobileNo, CONCAT(DISTRICT, ' ', MUNICIPAL, ' ', WARD_NO, ' ', TOL) address, P.NAME program, case when B.class_id = S.class_id then CONCAT(C.NAME, ' ', section) else C.NAME end 'class', IFNULL(B.REMARK, '') remark, S.ROLL_NO rollNo, G.`NAME` subjectGroup,ifnull(S.admission_year,'') as admissionYear FROM stu_billing_master B join student_info S on B.REG_NO = S.ID join program_master P on B.PROGRAM = P.ID join class_master C on B.CLASS_ID = C.ID join subject_group G on B.`SUBJECT_GROPU` = G.`ID` WHERE BILL_NO='" + billNo + "'";
        List<Map<String, Object>> l = da.getRecord(sql);
        if (l.isEmpty()) {
            return message.respondWithError("Invalid Bill no");
        }
        Map<String, Object> map = l.get(0);
        sql = "SELECT D.DR amount,B.NAME name FROM stu_billing_detail D,bill_master B WHERE B.ID=D.BILL_ID AND BILL_NO='" + billNo + "'";
        List<Map<String, Object>> list = da.getRecord(sql);

        map.put("bill", list);
        sql = "SELECT ifnull(payment_till,ENTER_DATE) AS paymentTill,IFNULL(ACADEMIC_YEAR,'') AS 'academicYear',IFNULL(CLASS_ID,'') AS 'classId',IFNULL(REG_NO,'') AS regNo FROM stu_billing_master WHERE BILL_NO='" + billNo + "'";
        Map<String, Object> m = da.getRecord(sql).get(0);
        String academicYear = m.get("academicYear").toString();
        String classId = m.get("classId").toString();
        String paymentTill = m.get("paymentTill").toString();
        String regNo = m.get("regNo").toString();

        if (regNo.length() > 1) {
            if (data.getBalTotal().equalsIgnoreCase("D")) {
                sql = "SELECT ifnull(SUM(D.DR),0) debit, ifnull(SUM(D.CR),0) AS credit, BILL_TYPE billType FROM stu_billing_master M  join stu_billing_detail D ON M.BILL_NO = D.BILL_NO where M.REG_NO = '" + regNo + "' AND D.PAYMENT_DATE<='" + paymentTill + "' and D.ACADEMIC_YEAR=" + academicYear + " and D.CLASS_ID=" + classId + " group by BILL_TYPE";
            } else {
                sql = "SELECT ifnull(SUM(D.DR),0) debit, ifnull(SUM(D.CR),0) AS credit, BILL_TYPE billType FROM stu_billing_master M  join stu_billing_detail D ON M.BILL_NO = D.BILL_NO where M.REG_NO = '" + regNo + "' and D.ACADEMIC_YEAR=" + academicYear + " and D.CLASS_ID=" + classId + "  group by BILL_TYPE";
            }
            System.out.println(sql);
            AtomicReference<Double> dr = new AtomicReference<>((double) 0);
            AtomicReference<Double> cr = new AtomicReference<>((double) 0);
            AtomicReference<Double> wav = new AtomicReference<>((double) 0);
            AtomicReference<Double> nmg = new AtomicReference<>((double) 0);
            da.getRecord(sql).forEach(dd -> {
                double debit = Double.parseDouble(dd.get("debit").toString());
                double credit = Double.parseDouble(dd.get("credit").toString());
                String billType = dd.get("billType").toString();
                if (billType.equalsIgnoreCase("DR")) {
                    dr.set(dr.get() + debit);
                    cr.set(cr.get() + credit);
                } else if (billType.equalsIgnoreCase("CR") || billType.equalsIgnoreCase("OPN")) {
                    cr.set(cr.get() + credit);
                    dr.set(dr.get() + debit);
                } else if (billType.equalsIgnoreCase("WAV")) {
                    wav.set(debit - credit);
                } else if (billType.equalsIgnoreCase("MNG")) {
                    nmg.set(debit - credit);
                }
            });


            map.put("totalFee", df.format(cr.get()));
            map.put("totalWav", df.format(wav.get()));
            map.put("totalPaid", df.format(dr.get()));
            map.put("totalManage", df.format(nmg.get()));
            map.put("dueAmount", df.format(cr.get() - (dr.get() + wav.get()) - nmg.get()));

        } else {
            map.put("totalFee", 0);
            map.put("totalWav", 0);
            map.put("totalPaid", 0);
            map.put("dueAmount", 0);
            map.put("totalManage", 0);
        }
        map.put("info", data);
        return map;
    }


    @Override
    public Object findCreditBill(Long regN, Long roll, Long academicYear, Long program, Long classId, String year, String month) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        String userName = td.getUserName();
        List<Object> StuFeeList = new ArrayList<>();
        String dateBs = DateConverted.adToBs(new Date());
        String regNo, rollNo, calssName, studentName, fatherName, mobileNo, MMM = message.getMonthName(month);
        List<Map<String, Object>> stuList = da.getRecord("SELECT S.ID AS regNo, M.NAME 'calssName', STU_NAME studentName, FATHERS_NAME fatherName, MOBILE_NO mobileNo, C.ROLL_NO rollNo FROM student_info S join class_transfer C on S.ID = C.STUDENT_ID join class_master M on C.class_id = M.id WHERE C.ACADEMIC_YEAR = '" + academicYear + "' AND C.PROGRAM = IFNULL(" + program + ", C.PROGRAM) AND C.CLASS_ID = IFNULL(" + classId + ", C.CLASS_ID) AND C.STUDENT_ID = IFNULL(" + regN + ", C.STUDENT_ID) AND C.ROLL_NO = IFNULL(" + roll + ", C.ROLL_NO) AND (DROP_OUT is null or DROP_OUT != 'Y') ORDER BY studentName,rollNo");
        Map<String, Object> map;
        for (Map<String, Object> stu : stuList) {

            regNo = stu.get("regNo").toString();

            calssName = stu.get("calssName").toString();
            studentName = stu.get("studentName").toString();
            fatherName = stu.get("fatherName").toString();
            mobileNo = stu.get("mobileNo").toString();
            rollNo = stu.get("rollNo").toString();
            map = new HashMap<>();
            map.put("fee", getAll(regNo, year, month));
            map.put("mobileNo", mobileNo);
            map.put("fatherName", fatherName);
            map.put("studentName", studentName);
            map.put("calssName", calssName);
            map.put("regNo", regNo);
            map.put("year", year);
            map.put("month", MMM);
            map.put("rollNo", rollNo);
            map.put("dateBs", dateBs);
            map.put("printBy", userName);
            StuFeeList.add(map);
        }
        return StuFeeList;
    }

    @Override
    public Object manageCredit(String jsonData) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String userName = td.getUserName();
        String[] jsonDataArray = message.jsonDataToStringArray(jsonData);
        try {
            message.map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[0], new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
            long regNo = Long.parseLong(message.map.get("regNo").toString());
            String remark = message.map.get("remark").toString();
            String paymentDate = DateConverted.bsToAd(message.map.get("effectDate").toString());
            String sql = "SELECT ID id,ACADEMIC_YEAR academicYear,PROGRAM program,CLASS_ID classId,SUBJECT_GROUP subjectGroup FROM student_info WHERE ID='" + regNo + "'";
            Map map = da.getRecord(sql).get(0);
            long academicYear = Long.parseLong(map.get("academicYear").toString());
            long program = Long.parseLong(map.get("program").toString());
            long classId = Long.parseLong(map.get("classId").toString());
            long subjectGroup = Long.parseLong(map.get("subjectGroup").toString());

            long billId;
            double addAmount, subAmount;

            sql = "SELECT ID id FROM fiscal_year WHERE '" + paymentDate + "' BETWEEN START_DATE AND END_DATE;";
            message.map = da.getRecord(sql).get(0);
            long fiscalYear = Long.parseLong(message.map.get("id").toString());
            sql = "SELECT IFNULL(max(BILL_SN),0)+1 AS billSn FROM stu_billing_master WHERE FISCAL_YEAR='" + fiscalYear + "' AND BILL_TYPE='MNG'";
            message.map = da.getRecord(sql).get(0);
            int billSn = Integer.parseInt(message.map.get("billSn").toString());

            String billNo = "";
            if (billSn < 10) {
                billNo = "MNG" + fiscalYear + "000" + billSn;
            } else if (billSn < 100) {
                billNo = "MNG" + fiscalYear + "00" + billSn;
            } else if (billSn < 1000) {
                billNo = "MNG" + fiscalYear + "0" + billSn;
            } else {
                billNo = "MNG" + fiscalYear + "" + billSn;
            }
            StuBillingMaster obj = new StuBillingMaster();
            List<StuBillingDetail> detail = new ArrayList();
            obj.setEnterBy(userName);
            obj.setEnterDate(new Date());
            obj.setBillNo(billNo);
            obj.setBillSn(billSn);
            obj.setAcademicYear(academicYear);
            obj.setProgram(program);
            obj.setClassId(classId);
            obj.setSubjectGropu(subjectGroup);
            obj.setRegNo(regNo);
            obj.setFiscalYear(fiscalYear);
            obj.setBillAmount(0f);
            obj.setAutoGenerate("M");
            obj.setBillType("MNG");
            obj.setRemark(remark);
            message.list = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[1], new com.fasterxml.jackson.core.type.TypeReference<List>() {
            });

            for (int i = 0; i < message.list.size(); i++) {
                message.map = message.list.get(i);
                billId = Long.parseLong(message.map.get("billId").toString());
                addAmount = Float.parseFloat(message.map.get("addAmount").toString());
                subAmount = Float.parseFloat(message.map.get("subAmount").toString());
                academicYear = Long.parseLong(message.map.get("academicYear").toString());
                program = Long.parseLong(message.map.get("program").toString());
                classId = Long.parseLong(message.map.get("classId").toString());
                detail.add(new StuBillingDetail(billNo, (i + 1), regNo, academicYear, program, classId, billId, subAmount, addAmount, paymentDate, "M"));
            }
            obj.setDetail(detail);
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            }
            return message.respondWithError(msg);
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public String delete(String id, String reason) {
        AuthenticatedUser td = facade.getAuthentication();
        sql = "select voucher_no from voucher where FEE_RECEIPT_NO='" + id + "'";
        List<Map<String, Object>> l = da.getRecord(sql);
        l.forEach(m -> {
            String voucherNo = m.get("voucher_no").toString();
            List<StuBillingMaster> list = da.getAll("from StuBillingMaster where billNo='" + id + "'");

            BillingDeleteMaster obj = new BillingDeleteMaster();
            obj.setAcademicYear(list.get(0).getAcademicYear());
            obj.setAddress(list.get(0).getAddress());
            obj.setApproveBy(list.get(0).getApproveBy());
            obj.setApproveDate(list.get(0).getApproveDate());
            obj.setAutoGenerate(list.get(0).getAutoGenerate());
            obj.setBillAmount(list.get(0).getBillAmount());
            obj.setBillNo(list.get(0).getBillNo());
            obj.setBillSn(list.get(0).getBillSn());
            obj.setBillType(list.get(0).getBillType());
            obj.setClassId(list.get(0).getClassId());
            obj.setEnterBy(list.get(0).getEnterBy());
            obj.setEnterDate(list.get(0).getEnterDate());
            obj.setFatherName(list.get(0).getFatherName());
            obj.setFiscalYear(list.get(0).getFiscalYear());
            obj.setMobileNo(list.get(0).getMobileNo());
            obj.setProgram(list.get(0).getProgram());
            obj.setReferenceId(list.get(0).getReferenceId());
            obj.setRegNo(list.get(0).getRegNo());
            obj.setStudentName(list.get(0).getStudentName());
            obj.setSubjectGropu(list.get(0).getSubjectGropu());
            obj.setDeleteBy(td.getUserName());
            obj.setDeleteDate(DateConverted.now());
            obj.setReason(reason);
            List<StuBillingDetail> detail = list.get(0).getDetail();
            List<BillingDeleteDetail> details1 = new ArrayList<>();
            BillingDeleteDetail objd;
            for (StuBillingDetail stuBillingDetail : detail) {
                objd = new BillingDeleteDetail();
                objd.setAcademicYear(stuBillingDetail.getAcademicYear());
                objd.setClassId(stuBillingDetail.getClassId());
                objd.setCr(stuBillingDetail.getCr());
                objd.setDr(stuBillingDetail.getDr());
                objd.setInventoryIssue(stuBillingDetail.getInventoryIssue());
                objd.setInventoryIssueBy(stuBillingDetail.getInventoryIssueBy());
                objd.setInventoryIssueDate(stuBillingDetail.getInventoryIssueDate());
                objd.setIsExtra(stuBillingDetail.getIsExtra());
                objd.setPaymentDate(stuBillingDetail.getPaymentDate());
                objd.setProgram(stuBillingDetail.getProgram());
                objd.setRegNo(stuBillingDetail.getRegNo());
                objd.setBillId(stuBillingDetail.getBillIds());
                objd.setPk(new StuBillingDetailPK(obj.getBillNo(), stuBillingDetail.getPk().getBillSn()));
                details1.add(objd);
            }
            obj.setDetail(details1);
            row = da.save(obj, voucherNo);
            if (row == 0) {
                throw new CustomException("Bill Backup not Generated");
            }
        });
        return "{\"message\":\"Success\"}";
    }

    @Override
    public Object saveOthers(BillingMasterReq req) {

        AuthenticatedUser td = facade.getAuthentication();

        String userName = td.getUserName();
        String cashAccount = td.getCashAccount();
        if (cashAccount.isEmpty()) {
            return message.respondWithError("Please define cash Account of " + userName);
        }
        long billId, academicYear, program, classId, subjectGroup;
        StuBillingMaster obj = new StuBillingMaster();
        List<StuBillingDetail> detail = new ArrayList<>();
        try {

            String stuName, programName, className, fathersName, address, mobileNo;


            String dateAd = DateConverted.bsToAd(req.getBillDate());
            academicYear = req.getAcademicYear();
            program = req.getProgram();
            classId = req.getClassId();
            subjectGroup =req.getSubjectGroup();
            stuName =req.getStudentName();
            fathersName =req.getFathersName();
            address = req.getAddress();
            mobileNo = req.getMobileNo();
            double amount , payAmount =req.getPayAmount();
            String year =req.getYear();
            String month = req.getMonth();
            String remark = req.getRemark();
            String paymentDate = DateConverted.bsToAd(year + "-" + month + "-01");

            sql = "SELECT NAME programName FROM program_master WHERE ID='" + program + "' ";
            message.map = da.getRecord(sql).get(0);
            programName = message.map.get("programName").toString();
            sql = "SELECT NAME className FROM class_master WHERE ID='" + classId + "' ";
            message.map = da.getRecord(sql).get(0);
            className = message.map.get("className").toString();
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
            obj.setEnterBy(userName);
            obj.setEnterDate(DateConverted.toDate(dateAd));
            obj.setBillNo(billNo);
            obj.setBillSn(billSn);
            obj.setAcademicYear(academicYear);
            obj.setProgram(program);
            obj.setClassId(classId);
            obj.setSubjectGropu(subjectGroup);
            obj.setRegNo(null);
            obj.setFiscalYear(fiscalYear);
            obj.setBillAmount(payAmount);
            obj.setAutoGenerate("N");
            obj.setBillType("DR");
            obj.setRemark(remark);
            obj.setStudentName(stuName);
            obj.setFatherName(fathersName);
            obj.setAddress(address);
            obj.setMobileNo(mobileNo);
          
            billSn = 1;
            for (BillingDetailReq d : req.getList()) {

                amount = d.getAmount();
                if (amount <= 0) {
                    continue;
                }
                billId = d.getBillId();
                detail.add(new StuBillingDetail(billNo, billSn, null, academicYear, program, classId, billId, 0, amount, paymentDate, "Y"));

                detail.add(new StuBillingDetail(billNo, billSn * (-1), null, academicYear, program, classId, billId, amount, 0, paymentDate, "Y"));
                billSn++;
            }
            obj.setDetail(detail);
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {

                sql = "SELECT DR drAmount,AC_CODE acCode,B.NAME billName FROM stu_billing_detail D,bill_master B WHERE B.ID=D.BILL_ID AND BILL_NO='" + billNo + "' AND DR>0";
                message.list = da.getRecord(sql);
                int tt = message.list.size() + 1;
                double[] drAmount = new double[tt];
                double[] crAmount = new double[tt];
                String[] acCode = new String[tt];
                double totalAmount = 0;
                int i = 0;
                String particular[] = new String[tt];
                for (; i < message.list.size(); i++) {
                    message.map = message.list.get(i);
                    drAmount[i] = Float.parseFloat(message.map.get("drAmount").toString());
                    if (drAmount[i] <= 0) {
                        continue;
                    }
                    totalAmount = totalAmount + drAmount[i];
                    acCode[i] = message.map.get("acCode").toString();
                    particular[i] = "Being " + message.map.get("billName").toString() + " Paid by  " + stuName + " .";
                    crAmount[i] = 0;
                }
                drAmount[i] = 0;
                acCode[i] = cashAccount;
                particular[i] = "Being cash Receive from " + stuName + " ";
                crAmount[i] = totalAmount;

                String narration = "Being cash Receive from " + stuName + ", Class:" + className + ", Program:" + programName + ".";
                boolean veStatus = ve.save(fiscalYear, dateAd, userName, "BRV", narration, "", billNo, acCode, particular, crAmount, drAmount);
                if (veStatus) {
                    sql = "UPDATE stu_billing_master SET APPROVE_DATE='" + dateAd + "',APPROVE_BY='" + userName + "' WHERE BILL_NO='" + billNo + "'";
                    da.delete(sql);
                } else {
                    System.out.println(ve.getMsg());
                }
                message.map = new HashMap<>();
                message.map.put("message", "Success");
                message.map.put("billNo", billNo);
                return message.map;
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }
}
