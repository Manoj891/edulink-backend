package com.ms.ware.online.solution.school.service.account;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.account.VoucherDao;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.account.VoucherDetail;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.HibernateUtilImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/account/pending")
public class PendingBillController {
    @Autowired
    private VoucherDao db;
    @Autowired
    private AuthenticationFacade facade;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(db.getRecord("SELECT VOUCHER_NO voucherNo,VOUCHER_TYPE type,TOTAL_AMOUNT voucherAmount,IFNULL(CHEQUE_NO,'') chequeNo,IFNULL(FEE_RECEIPT_NO,'') stuBillNo,IFNULL(NARRATION,'') narration,GET_BS_DATE(ENTER_DATE) AS enterDate,ENTER_BY enterBy FROM voucher WHERE APPROVE_DATE IS NULL AND REJECT_DATE IS NULL union select b.bill_no as voucherNo,'BRV' as type, b.bill_amount voucherAmount,'' as chequeNo,b.bill_no as stuBillNo,  concat(ifnull(b.reg_no, '') ,' ', b.remark,' ',ifnull(s.stu_name, b.student_name)) narration,GET_BS_DATE(b.enter_date) AS enterDate,b.enter_by as enterBy from stu_billing_master b left join voucher v on b.BILL_NO = v.FEE_RECEIPT_NO left join student_info s on b.REG_NO = s.ID where b.approve_date is null and BILL_TYPE = 'DR' and v.FEE_RECEIPT_NO is null limit 100"));
    }

    @PostMapping
    public List<String> postBill(@RequestBody List<AccountPost> req, @RequestParam String date) {
        AuthenticatedUser user = facade.getAuthentication();;
        String approveBy = user.getUserName();
        Date approveDate = DateConverted.bsToAdDate(date);
        String today = DateConverted.toString(approveDate);
        List<Map<String, Object>> l = db.getRecord("select id from fiscal_year where '" + today + "' between start_date and end_date");
        if (l.isEmpty()) throw new CustomException("Please define fiscal year for date of " + today);
        long fiscalYear = Long.parseLong(l.get(0).get("id").toString());
        List<String> successBill = new ArrayList<>();
        req.forEach(a -> {
            if (a.getVoucherType().equalsIgnoreCase("BRV")) {
                if (db.getRecord("select voucher_no from voucher where fee_receipt_no='" + a.getVoucherNo() + "'  or voucher_no='" + a.getVoucherNo() + "'").isEmpty()) {
                    successBill.add(billPost(a.getVoucherNo(), fiscalYear, approveBy, approveDate));
                } else {
                    successBill.add(post(a.getVoucherNo(), approveBy, today));
                }
            } else {
                successBill.add(post(a.getVoucherNo(), approveBy, today));
            }
        });

        return successBill;
    }

    private String post(String voucherNo, String approveBy, String today) {
        Session session = HibernateUtilImpl.getSession();
        Transaction tr = session.beginTransaction();
        try {
            session.createSQLQuery("insert into ledger (id, ac_code, dr_amt, cr_amt, particular, voucher_no, fee_receipt_no, cheque_no, narration, enter_date, enter_by, post_date, post_by) select d.id id,d.ac_code accode,d.dr_amt dramt,d.cr_amt cramt,d.particular,m.voucher_no,m.fee_receipt_no,d.cheque_no,m.narration,m.enter_date,m.enter_by, m.approve_date as post_date,'" + approveBy + "' post_by from voucher m join voucher_detail d on m.voucher_no=d.voucher_no left join ledger l on d.id = l.id where l.ID is null and m.voucher_no='" + voucherNo + "';\nUPDATE voucher SET APPROVE_BY='" + approveBy + "',APPROVE_DATE='" + today + "' WHERE VOUCHER_NO='" + voucherNo + "';").executeUpdate();
            tr.commit();
            session.close();
            return voucherNo;
        } catch (Exception e) {
            tr.rollback();
            session.close();
            throw new CustomException(e.getMessage());
        }

    }

    private String billPost(String billNo, long fiscalYear, String approveBy, Date approveDate) {
        String voucherType = "BRV";
        int voucherSn = Integer.parseInt(db.getRecord("select ifnull(max(voucher_sn),0)+1 as voucherSn from voucher where fiscal_year='" + fiscalYear + "' and voucher_type='" + voucherType + "'").get(0).get("voucherSn").toString());
        String voucherNo;
        if (voucherSn < 10) {
            voucherNo = fiscalYear + "0000" + voucherSn + voucherType;
        } else if (voucherSn < 100) {
            voucherNo = fiscalYear + "000" + voucherSn + voucherType;
        } else if (voucherSn < 1000) {
            voucherNo = fiscalYear + "00" + voucherSn + voucherType;
        } else if (voucherSn < 10000) {
            voucherNo = fiscalYear + "0" + voucherSn + voucherType;
        } else {
            voucherNo = fiscalYear + "" + voucherSn + voucherType;
        }
        String sql = "SELECT u.cash_account cashAcCode, ifnull(s.STU_NAME, m.STUDENT_NAME) studentName,m.enter_by enterBy,m.enter_date enterDate from stu_billing_master m join organization_user_info u on m.enter_by = u.login_id left join student_info s on m.reg_no = s.id where  bill_no = '" + billNo + "'";
        List<Map<String, Object>> list = db.getRecord(sql);
        if (list.isEmpty()) throw new CustomException("Invalid bill number " + billNo);
        String cashAcCode = list.get(0).get("cashAcCode").toString();
        String studentName = list.get(0).get("studentName").toString();
        String enterBy = list.get(0).get("enterBy").toString();
        Date enterDate = DateConverted.toDate(list.get(0).get("enterDate").toString());
        sql = "SELECT DR drAmount,ac_code acCode,b.NAME billName FROM stu_billing_detail d join bill_master b on b.id=d.bill_id and bill_no='" + billNo + "' and dr>0";
        List<VoucherDetail> detail = new ArrayList<>();
        AtomicReference<Double> totalAmount = new AtomicReference<>((double) 0);
        AtomicInteger sn = new AtomicInteger(1);
        db.getRecord(sql).forEach(d -> {
            double amount = Double.parseDouble(d.get("drAmount").toString());
            detail.add(VoucherDetail.builder()
                    .id(voucherNo + "-" + sn.get())
                    .voucherSn(sn.get())
                    .voucherNo(voucherNo)
                    .drAmt(0D)
                    .crAmt(amount)
                    .acCode(d.get("acCode").toString())
                    .particular(d.get("billName").toString() + " paid by " + studentName)
                    .billNo(billNo)
                    .build());
            totalAmount.updateAndGet(v -> v + amount);
            sn.getAndIncrement();
        });
        detail.add(VoucherDetail.builder()
                .id(voucherNo + "-" + sn.get())
                .voucherSn(sn.get())
                .voucherNo(voucherNo)
                .drAmt(totalAmount.get())
                .crAmt(0D)
                .acCode(cashAcCode)
                .particular(billNo + " paid by " + studentName)
                .billNo(billNo)
                .build());


        String narration = "Being cash Receive from " + studentName + ", Bill No :" + billNo + ".";
        Voucher obj = Voucher.builder()
                .voucherNo(voucherNo)
                .feeReceiptNo(billNo)
                .voucherSn(voucherSn)
                .voucherType(voucherType)
                .fiscalYear(fiscalYear)
                .narration(narration)
                .enterBy(enterBy)
                .enterDate(enterDate)
                .approveBy(approveBy)
                .approveDate(approveDate)
                .detail(detail)
                .build();

        db.save(obj);
        db.delete("insert into ledger (id, ac_code, dr_amt, cr_amt, particular, voucher_no, fee_receipt_no, cheque_no, narration, enter_date, enter_by, post_date, post_by) select id id,ac_code accode,dr_amt dramt,cr_amt cramt,particular,m.voucher_no,fee_receipt_no,d.cheque_no,m.narration,enter_date,enter_by, approve_date as post_date,'" + approveBy + "' post_by from voucher m join voucher_detail d on m.voucher_no=d.voucher_no and m.voucher_no='" + voucherNo + "'");
        return voucherNo;

    }
}
