package com.ms.ware.online.solution.school.controller.account;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.exception.PermissionDeniedException;
import com.ms.ware.online.solution.school.entity.account.Ledger;
import com.ms.ware.online.solution.school.entity.account.VoucherDetail;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Account/OpeningAdjustment")
public class OpeningAdjustmentController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private DB db;

    @GetMapping()
    public ResponseEntity<List<OpeningAdjustment>> openingAdjustment(@RequestParam String fiscalYear, @RequestParam String dateFrom) {
        dateFrom = DateConverted.bsToAd(dateFrom);

        String sql = "select ifnull(sum(opening), 0) as opening, ifnull(sum(adjusted), 0) as adjusted, transact, ac_code as acCode, ac_name AS acName, level AS ca from (\n" +
                "SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS adjusted, a.ac_code, a.ac_name, a.level, a.transact FROM ledger l JOIN chart_of_account a ON l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.level <= 6 AND a.transact = 'N' WHERE l.enter_date < '" + dateFrom + " ' and VOUCHER_NO != 'OPENING-" + fiscalYear + "' and (a.AC_CODE like '1%' or a.AC_CODE like '2%') GROUP BY a.ac_code\n" +
                "UNION\n" +
                "SELECT 0 AS opening, SUM(dr_amt) - SUM(cr_amt) AS adjusted, a.ac_code, a.ac_name, a.level, a.transact FROM ledger l JOIN chart_of_account a ON l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.level <= 6 AND a.transact = 'N' WHERE l.enter_date < '" + dateFrom + " ' and VOUCHER_NO = 'OPENING-" + fiscalYear + "' and (a.AC_CODE like '1%' or a.AC_CODE like '2%') GROUP BY a.ac_code\n" +
                "UNION\n" +
                "SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS adjusted, a.ac_code, a.ac_name, a.level, a.transact FROM ledger l JOIN chart_of_account a ON l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.level <= 6 AND a.transact = 'Y' WHERE l.enter_date < '" + dateFrom + " ' and VOUCHER_NO != 'OPENING-" + fiscalYear + "' and (a.AC_CODE like '1%' or a.AC_CODE like '2%') GROUP BY a.ac_code\n" +
                "UNION\n" +
                "SELECT 0 AS opening, SUM(dr_amt) - SUM(cr_amt) AS adjusted, a.ac_code, a.ac_name, a.level, a.transact FROM ledger l JOIN chart_of_account a ON l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.level <= 6 AND a.transact = 'Y' WHERE l.enter_date < '" + dateFrom + " ' and VOUCHER_NO = 'OPENING-" + fiscalYear + "' and (a.AC_CODE like '1%' or a.AC_CODE like '2%') GROUP BY a.ac_code\n" +
                ")\n" +
                "AS subquery_alias GROUP BY ac_code, ac_name, level, transact ORDER BY ac_code;";
        DecimalFormat df = new DecimalFormat("#.##");
        List<OpeningAdjustment> list = new LinkedList<>();
        Session session = HibernateUtil.getSession();
        List<OpeningAdjustmentRes> l = session.createSQLQuery(sql)
                .setResultTransformer(Transformers.aliasToBean(OpeningAdjustmentRes.class))
                .list();


        l.forEach(d -> {
            double debit = 0, credit = 0, adjustedCredit = 0, adjustedDebit = 0;
            if (d.getOpening() > 0) debit = d.getOpening();
            else credit = Math.abs(d.getOpening());
            if (d.getAdjusted() > 0) adjustedDebit = d.getAdjusted();
            else adjustedCredit = Math.abs(d.getAdjusted());
            double balance = (adjustedDebit + debit) - (adjustedCredit + credit);
            list.add(OpeningAdjustment.builder()
                    .acCode(d.getAcCode())
                    .acName(d.getAcName())
                    .transact(d.getTransact())
                    .ca(d.getCa())
                    .adjustedCredit(df.format(adjustedCredit))
                    .adjustedDebit(df.format(adjustedDebit))
                    .openingDebit(df.format(debit))
                    .openingCredit(df.format(credit))
                    .balance(df.format(balance))
                    .build());
        });
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }


    @PutMapping
    public ResponseEntity<String> postVoucher(@RequestParam String acCode, @RequestParam long fiscalYear, @RequestParam double debit, @RequestParam double credit) {

        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            throw new PermissionDeniedException();
        }
        String voucherNo = "OPENING-" + fiscalYear;
        String sql = "select subdate(start_date,1) as start_date from fiscal_year where ID=" + fiscalYear;
        Map<String, Object> map = db.getRecord(sql).get(0);
        String enterDate = map.get("start_date").toString();
        List<Map<String, Object>> list = db.getRecord("select * from voucher where voucher_no='" + voucherNo + "'");
        if (list.isEmpty()) {
            sql = "insert into voucher(voucher_no, approve_by, approve_date, enter_by, enter_date, fiscal_year,narration, total_amount, voucher_sn, voucher_type) value ('" + voucherNo + "','SYSTEM','" + enterDate + "','SYSTEM','" + enterDate + "'," + fiscalYear + ",'OPENING',0,0,'JVR')";
            db.delete(sql);
        }
        Date date = DateConverted.toDate(enterDate);
        sql = "select ifnull(max(voucher_sn),0)+1 as sn from voucher_detail where voucher_no='" + voucherNo + "'";
        int voucherSn = Integer.parseInt(db.getRecord(sql).get(0).get("sn").toString());
        Session session = HibernateUtil.getSession();
        Transaction tr = session.beginTransaction();
        sql = "select id from voucher_detail where voucher_no='" + voucherNo + "' and AC_CODE='" + acCode + "'";
        List<Map<String, Object>> data = db.getRecord(sql);
        String id = (data.isEmpty() ? (voucherNo + "-" + acCode) : data.get(0).get("id").toString());
        VoucherDetail detail = new VoucherDetail(id, voucherNo, voucherSn, acCode, "OPENING ADJUSTMENT", debit, credit);
        Ledger ledger = Ledger.builder().id(id).voucherNo(voucherNo).acCode(acCode).drAmt(debit).crAmt(credit).postBy(td.getUserName()).enterBy(td.getUserName()).enterDate(date).postDate(date).narration("OPENING ADJUSTMENT").particular("OPENING ADJUSTMENT")
                .voucherDetail(detail)
                .build();
        try {
            session.saveOrUpdate(detail);
            session.saveOrUpdate(ledger);
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
        }
        session.close();
        return ResponseEntity.status(200).body("{\"message\":\"Success\"}");
    }
}
