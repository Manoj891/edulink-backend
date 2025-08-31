package com.ms.ware.online.solution.school.controller.billing;


import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Billing/Delete")
public class BillingDeleteRestController {
    @Autowired
    private DB db;
    @Autowired
    private Message message;

    @GetMapping
    public Object index(@RequestParam String dateFrom, @RequestParam String dateTo) {
        dateFrom = DateConverted.bsToAd(dateFrom);
        dateTo = DateConverted.bsToAd(dateTo);
        return db.getRecord("SELECT BILL_NO billNo,ENTER_BY enterBy,GET_BS_DATE(ENTER_DATE) enterDate,DELETE_BY deleteBy,GET_BS_DATE(DELETE_DATE) deleteDateBs,CONCAT(DELETE_DATE,'') deleteDate,BILL_AMOUNT billAmount,IFNULL(REASON,'') reason  FROM billing_delete_master WHERE ENTER_DATE BETWEEN '" + dateFrom + "' AND  '" + dateTo + "' ORDER BY DELETE_DATE DESC");
    }

    @GetMapping("/{billNo}")
    public Object index(@PathVariable String billNo) {


        String sql = "SELECT BILL_NO billNo,IFNULL(REG_NO,'N/A') regNo,GET_BS_DATE(B.ENTER_DATE) enterDate,B.ENTER_BY enterBy,STUDENT_NAME studentName,FATHER_NAME fatherName,MOBILE_NO mobileNo,ADDRESS address,P.NAME program,C.NAME 'class',IFNULL(B.REMARK,'') remark,'' rollNo FROM billing_delete_master B,program_master P,class_master C WHERE REG_NO IS NULL AND B.PROGRAM=P.ID AND B.CLASS_ID=C.ID AND BILL_NO='" + billNo + "' "
                + " UNION "
                + " SELECT BILL_NO billNo,IFNULL(REG_NO,'N/A') regNo,GET_BS_DATE(B.ENTER_DATE) enterDate,B.ENTER_BY enterBy,S.STU_NAME studentName,S.FATHERS_NAME fatherName,S.MOBILE_NO mobileNo,CONCAT(DISTRICT,' ',MUNICIPAL,' ',WARD_NO,' ',TOL) address,P.NAME program,C.NAME 'class',IFNULL(B.REMARK,'') remark,S.ROLL_NO rollNo  FROM billing_delete_master B,program_master P,class_master C,student_info S WHERE B.REG_NO=S.ID AND B.PROGRAM=P.ID AND B.CLASS_ID=C.ID AND BILL_NO='" + billNo + "'";
        List l = db.getRecord(sql);
        if (l.isEmpty()) {
            return message.respondWithError("Invalid Bill no");
        }
        Map map = (Map) l.get(0);
        sql = "SELECT D.DR amount,B.NAME name FROM billing_delete_detail D,bill_master B WHERE B.ID=D.BILL_ID AND BILL_NO='" + billNo + "'";
        map.put("bill", db.getRecord(sql));
        sql = "SELECT CREATE_AT AS createAt,IFNULL(ACADEMIC_YEAR,'') AS 'academicYear',IFNULL(CLASS_ID,'') AS 'classId',IFNULL(REG_NO,'') AS regNo FROM billing_delete_master WHERE BILL_NO='" + billNo + "'";
        Map m = db.getRecord(sql).get(0);
        String academicYear = m.get("academicYear").toString();
        String classId = m.get("classId").toString();
        String regNo = m.get("regNo").toString();
        String createAt = m.get("createAt").toString();
        double totalAmount = 0, paidAmount = 0, remaining = 0;
        if (regNo.length() > 3) {
            sql = " SELECT SUM(CR)-SUM(DR) AS remaining FROM billing_delete_detail D,billing_delete_master M WHERE D.BILL_NO=M.BILL_NO AND D.ACADEMIC_YEAR=" + academicYear + " AND D.CLASS_ID=" + classId + " AND D.REG_NO='" + regNo + "' AND CREATE_AT<='" + createAt + "' ";
            m = db.getRecord(sql).get(0);
            try {
                remaining = Float.parseFloat(m.get("remaining").toString());
            } catch (Exception e) {
                remaining = 0;
            }
            sql = " SELECT SUM(DR) AS paidAmount FROM billing_delete_detail D,billing_delete_master M WHERE D.BILL_NO=M.BILL_NO AND M.BILL_TYPE='DR' AND D.ACADEMIC_YEAR=" + academicYear + " AND D.CLASS_ID=" + classId + " AND D.REG_NO='" + regNo + "' AND CREATE_AT<='" + createAt + "' ";
            try {
                m = db.getRecord(sql).get(0);
                paidAmount = Float.parseFloat(m.get("paidAmount").toString());
            } catch (Exception e) {
                paidAmount = 0;
            }
            totalAmount = paidAmount + remaining;
            map.put("totalAmount", totalAmount);
            map.put("paidAmount", paidAmount);
            map.put("dueAmount", remaining);
        } else {
            map.put("totalAmount", "");
            map.put("paidAmount", "");
            map.put("dueAmount", "");
        }
        return map;
    }
}
