package com.ms.ware.online.solution.school.controller.billing;


import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.billing.StuBillingMasterDao;
import com.ms.ware.online.solution.school.dto.BillingMasterReq;
import com.ms.ware.online.solution.school.dto.StuBillingMasterWav;
import com.ms.ware.online.solution.school.service.account.VoucherEntry;
import com.ms.ware.online.solution.school.service.billing.StuBillingMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Billing/StuBillingMaster")
public class StuBillingMasterRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    StuBillingMasterService service;
    @Autowired
    StuBillingMasterDao da;
    @Autowired
    private DB db;
    @Autowired
    private Message message;
    @Autowired
    private  VoucherEntry ve ;
    @GetMapping
    public Object index(@RequestParam(required = false) String regNo, @RequestParam String year, @RequestParam String month) {
        return service.getAll(regNo, year, month);
    }

    @GetMapping("account")
    public List<Map<String, Object>> index() {
        String cashAccount = facade.getAuthentication().getCashAccount();
        return da.getRecord("select ac_code, ac_name from chart_of_account where TRANSACT = 'Y'  AND (AC_CODE='" + cashAccount + "' OR MGR_CODE ='1020202') order by ac_code");
    }

    @GetMapping("/CreditBill")
    public Object indexCreditBill(@RequestParam(required = false) Long regNo, @RequestParam(required = false) Long rollNo, @RequestParam Long academicYear, @RequestParam Long program, @RequestParam Long classId, @RequestParam String year, @RequestParam String month) {
        return service.findCreditBill(regNo, rollNo, academicYear, program, classId, year, month);
    }

    @GetMapping("/Report")
    public Object report(@RequestParam(required = false) Long academicYear, @RequestParam(required = false) Long program, @RequestParam(required = false) Long classId, @RequestParam String regNo, @RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String username) {
        String sql = "", dateRange = "";

        if (dateFrom.length() == 10 && dateTo.length() == 10) {
            dateRange = " AND B.ENTER_DATE BETWEEN '" + DateConverted.bsToAd(dateFrom) + "' AND '" + DateConverted.bsToAd(dateTo) + "'";
        } else if (dateFrom.length() == 10) {
            dateRange = " AND B.ENTER_DATE='" + DateConverted.bsToAd(dateFrom) + "'";
        } else if (dateTo.length() == 10) {
            dateRange = " AND B.ENTER_DATE ='" + DateConverted.bsToAd(dateTo) + "'";
        }
        if (!regNo.isEmpty()) {
            regNo = " AND IFNULL(REG_NO,'N/A')='" + regNo + "'";
        }
        if (username.length() > 5) {
            username = " AND B.ENTER_BY='" + username + "'";
        } else username = "";
        sql = "SELECT ROUND(IFNULL((SELECT SUM(DR) FROM stu_billing_detail D WHERE D.BILL_NO=B.BILL_NO),0),2) billAmount,GET_BS_DATE(B.ENTER_DATE) enterDate,B.ENTER_BY enterBy,B.ACADEMIC_YEAR academicYear,BILL_NO billNo,IFNULL(REG_NO,'N/A') regNo,STUDENT_NAME studentName,FATHER_NAME fatherName,MOBILE_NO mobileNo,ADDRESS address,P.NAME program,C.NAME 'class' FROM stu_billing_master B,program_master P,class_master C WHERE BILL_TYPE='DR' AND REG_NO IS NULL AND B.PROGRAM=P.ID AND B.CLASS_ID=C.ID AND B.ACADEMIC_YEAR=IFNULL(" + academicYear + ",B.ACADEMIC_YEAR) AND B.PROGRAM=IFNULL(" + program + ",B.PROGRAM) AND B.CLASS_ID=IFNULL(" + classId + ",B.CLASS_ID) " + regNo + dateRange + username
                + " UNION "
                + " SELECT ROUND(IFNULL((SELECT SUM(DR) FROM stu_billing_detail D WHERE D.BILL_NO=B.BILL_NO),0),2) billAmount,GET_BS_DATE(B.ENTER_DATE) enterDate,B.ENTER_BY enterBy,B.ACADEMIC_YEAR academicYear,BILL_NO billNo,IFNULL(REG_NO,'N/A') regNo,S.STU_NAME studentName,S.FATHERS_NAME fatherName,S.MOBILE_NO mobileNo,CONCAT(DISTRICT,' ',MUNICIPAL,' ',WARD_NO) address,P.NAME program,C.NAME 'class' FROM stu_billing_master B,program_master P,class_master C,student_info S WHERE BILL_TYPE='DR' AND B.REG_NO=S.ID AND B.PROGRAM=P.ID AND B.CLASS_ID=C.ID AND B.ACADEMIC_YEAR=IFNULL(" + academicYear + ",B.ACADEMIC_YEAR) AND B.PROGRAM=IFNULL(" + program + ",B.PROGRAM) AND B.CLASS_ID=IFNULL(" + classId + ",B.CLASS_ID) " + regNo + dateRange + username;
        return db.getRecord(sql);
    }

    @GetMapping("/{billNo}")
    public Object index(@PathVariable String billNo) {
        return service.getAll(billNo);
    }

    @GetMapping("/StudentByRollNo")
    public Object index(@RequestParam long program, @RequestParam long academicYear, @RequestParam long classId, @RequestParam long rollNo) {

        String sql = "SELECT ID AS regNo,STU_NAME AS stuName,FATHERS_NAME fatherName from student_info WHERE PROGRAM = " + program + " and ACADEMIC_YEAR = " + academicYear + " and CLASS_ID = " + classId + "  and ROLL_NO=" + rollNo;
        List l = db.getRecord(sql);
        if (l.isEmpty()) {
            return message.respondWithError("Roll No not found in system record!!");
        }
        return l.get(0);
    }

    @PostMapping
    public ResponseEntity<String> doSave(@RequestBody BillingMasterReq req) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(req));
    }

    @PostMapping("/Others")
    public Object doSaveOthers(@RequestBody BillingMasterReq req) {
        return service.saveOthers(req);
    }

    @PutMapping
    public Object wavFee(@RequestBody StuBillingMasterWav req) {
        return service.wavFee(req);
    }

    @PutMapping("/{billNo}")
    public Object doUpdate(@PathVariable String billNo, @RequestParam String date) throws IOException {
        String msg;
        
        date = DateConverted.bsToAd(date);
        String sql = "UPDATE stu_billing_master SET ENTER_DATE='" + date + "' WHERE BILL_NO='" + billNo + "'";

        int row = db.save(sql);
        msg = db.getMsg();
        if (row > 0) {
            sql = "UPDATE voucher SET ENTER_DATE='" + date + "' WHERE FEE_RECEIPT_NO='" + billNo + "'";
            db.save(sql);
            sql = "UPDATE ledger SET ENTER_DATE='" + date + "' WHERE VOUCHER_NO=(SELECT VOUCHER_NO FROM voucher WHERE FEE_RECEIPT_NO='" + billNo + "')";
            db.save(sql);
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        }
        return message.respondWithError(msg);
    }

    @PatchMapping("/{billNos}")
    public Object pendingBillApprove(@PathVariable String billNos, @RequestParam String cashAccount) throws IOException {

        
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String userName = td.getUserName();

        if (cashAccount.isEmpty()) {
            return message.respondWithError("Please define cash Account of " + userName);
        }
        List<String> billSuccess = new ArrayList<>();
        String[] bills = billNos.split(",");
        for (String billNo : bills) {


            String sql, regNo = "";
            if (billNo.length() < 5) {
                continue;
            }
            sql = "SELECT ENTER_DATE,FISCAL_YEAR FROM stu_billing_master  WHERE BILL_NO='" + billNo + "' AND APPROVE_DATE IS NULL";
            message.list = da.getRecord(sql);
            if (message.list.isEmpty()) {
                sql = "SELECT * FROM voucher WHERE FEE_RECEIPT_NO='" + billNo + "'";
                List<Map<String, Object>> l = da.getRecord(sql);
                if (l.isEmpty()) {
                    sql = "UPDATE stu_billing_master SET APPROVE_DATE=null WHERE BILL_NO='" + billNo + "' ";
                    da.delete(sql);
                    sql = "SELECT ENTER_DATE,FISCAL_YEAR FROM stu_billing_master  WHERE BILL_NO='" + billNo + "' AND APPROVE_DATE IS NULL";
                    message.list = da.getRecord(sql);
                    if (message.list.isEmpty()) {
                        return message.respondWithError("Invalid Bill Number");
                    }
                }
            }
            message.map = message.list.get(0);
            long fiscalYear = Long.parseLong(message.map.get("FISCAL_YEAR").toString());
            String billDate = message.map.get("ENTER_DATE").toString();
            sql = "SELECT DR drAmount,AC_CODE acCode,B.NAME billName,IFNULL(REG_NO,'') regNo FROM stu_billing_detail D,bill_master B WHERE B.ID=D.BILL_ID AND BILL_NO='" + billNo + "' AND DR>0 ";
            message.list = da.getRecord(sql);
            int tt = message.list.size() + 1;
            double drAmount[] = new double[tt];
            double crAmount[] = new double[tt];
            String acCode[] = new String[tt];
            double totalAmount = 0;
            int i = 0;
            String particular[] = new String[tt];
            for (; i < message.list.size(); i++) {
                message.map = (Map) message.list.get(i);
                drAmount[i] = Float.parseFloat(message.map.get("drAmount").toString());
                if (drAmount[i] <= 0) {
                    continue;
                }
                totalAmount = totalAmount + drAmount[i];
                acCode[i] = message.map.get("acCode").toString();
                regNo = message.map.get("regNo").toString();
                particular[i] = "Being " + message.map.get("billName").toString() + " Paid by  " + regNo + ".";
                crAmount[i] = 0;
            }
            drAmount[i] = 0;
            acCode[i] = cashAccount;
            particular[i] = "Being cash Receive from  (" + regNo + ")";
            crAmount[i] = totalAmount;

            String narration = "Being cash Receive from  (" + regNo + "), .";
            boolean veStatus = ve.save(fiscalYear, billDate, userName, "BRV", narration, "", billNo, acCode, particular, crAmount, drAmount);
            if (veStatus) {
                sql = "UPDATE stu_billing_master SET APPROVE_DATE='" + billDate + "',APPROVE_BY='" + userName + "' WHERE BILL_NO='" + billNo + "'";
                da.delete(sql);
            } else {
                System.out.println(ve.getMsg());
            }
            billSuccess.add(billNo);
        }
        message.map = new HashMap();
        message.map.put("message", "Success");
        message.map.put("billNos", billSuccess);
        return message.map;

    }

    @PatchMapping
    public Object doManageCredit(@RequestBody String jsonData) {
        return service.manageCredit(jsonData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> doDelete(@PathVariable String id, @RequestParam String reason) {
        return ResponseEntity.status(HttpStatus.OK).body(service.delete(id, reason));
    }
}
