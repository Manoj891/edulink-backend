package com.ms.ware.online.solution.school.controller.account;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.exception.CustomException;
import com.ms.ware.online.solution.school.model.HibernateUtil;
import com.ms.ware.online.solution.school.model.HibernateUtilImpl;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account/")
public class TrialBalanceController {
    private Session session;
    @Autowired
    private HibernateUtil util;
    @Autowired
    private DB db;


    @GetMapping("new-trial-balance")
    public ResponseEntity<List<TailBalance>> trailBalance(@RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam(defaultValue = "1") int level) {
        String acCode = db.getRecord("select reserves_and_surplus from organization_master").get(0).get("reserves_and_surplus").toString();
        dateFrom = DateConverted.bsToAd(dateFrom);
        dateTo = DateConverted.bsToAd(dateTo);
        String sql = "SELECT ifnull(SUM(opening), 0) AS opening, ifnull(SUM(debit), 0) AS debit, ifnull(SUM(credit), 0) AS credit, transact, ac_code acCode, ac_name as acName, level as ca  "
                + " from( SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level, transact from ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') WHERE enter_date < '" + dateFrom + "' GROUP BY a.ac_code "
                + " union SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level, transact from ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code WHERE enter_date < ' " + dateFrom + "' GROUP BY a.ac_code "
                + " union SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level, transact from ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') WHERE enter_date BETWEEN '" + dateFrom + "' AND ' " + dateTo + "' GROUP BY a.ac_code "
                + " union select 0 AS opening ,0 debit ,0 AS credit, ac_code,ac_name,level,transact from organization_master m join chart_of_account c on m.reserves_and_surplus = c.ac_code  "
                + " union SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level, transact FROM ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code WHERE enter_date BETWEEN ' " + dateFrom + "' AND ' " + dateTo + "' GROUP BY a.ac_code) "
                + " AS subquery_alias GROUP BY ac_code, ac_name, level, transact ORDER BY ac_code";
        return ResponseEntity.status(HttpStatus.OK).body(summary(sql, acCode));
    }


    private List<TailBalance> summary(String sql, String acCode) {
        DecimalFormat df = new DecimalFormat("#.##");
        session = util.getSession();
        AtomicReference<Double> pl = new AtomicReference<>((double) 0);
        List<TailBalanceRes> l = new ArrayList<>(getRecord(sql));
        session.close();
        l.stream().filter(t -> "3".equals(t.getAcCode()) || "4".equals(t.getAcCode())).collect(Collectors.toList()).forEach(d -> pl.updateAndGet(v -> v + d.getOpening()));
        List<TailBalance> list = new ArrayList<>();
        TailBalance total = TailBalance.builder().acName("Total").acCode("").transact("").openingDebit("0").openingCredit("0").debit("0").credit("0").ca(0).build();
        l.forEach(d -> {
            if (acCode.startsWith(d.getAcCode())) {
                d.setOpening(d.getOpening() + pl.get());
            } else if (d.getAcCode().startsWith("3") || d.getAcCode().startsWith("4")) {
                d.setOpening(0);
            }
            String credit = "0";
            String debit = "0";
            String openingDebit = "0";
            String openingCredit = "0";


            double bal = d.getDebit() - d.getCredit();
            if (bal >= 0) debit = df.format(bal);
            else credit = df.format(Math.abs(bal));
            if (d.getOpening() >= 0) openingDebit = df.format(d.getOpening());
            else openingCredit = df.format(Math.abs(d.getOpening()));
            bal = bal + d.getOpening();
            String totalBalance = (bal >= 0) ? df.format(bal) : "(" + df.format(Math.abs(bal)) + ")";

            list.add(TailBalance.builder()
                    .acCode(d.getAcCode())
                    .acName(d.getAcName())
                    .transact(d.getTransact())
                    .credit(credit)
                    .debit(debit)
                    .openingDebit(openingDebit)
                    .openingCredit(openingCredit)
                    .balance(totalBalance)
                    .ca(d.getCa()).build());


        });
        list.stream().
                filter(t -> t.getCa() == 1).
                collect(Collectors.toList()).
                forEach(d ->
                {
                    double openingCredit = Double.parseDouble(total.getOpeningCredit()) + Double.parseDouble(d.getOpeningCredit());
                    double openingDebit = Double.parseDouble(total.getOpeningDebit()) + Double.parseDouble(d.getOpeningDebit());
                    double debit = Double.parseDouble(total.getDebit()) + Double.parseDouble(d.getDebit());
                    double credit = Double.parseDouble(total.getCredit()) + Double.parseDouble(d.getCredit());
                    total.setOpeningCredit(df.format(openingCredit));
                    total.setOpeningDebit(df.format(openingDebit));
                    total.setDebit(df.format(debit));
                    total.setCredit(df.format(credit));
                    total.setBalance(df.format((openingDebit + debit) - (openingCredit + credit)));
                });


        list.add(total);
        return list;
    }

    public List getRecord(String sql) {
        try {
            return session.createSQLQuery(sql)
                    .setResultTransformer(Transformers.aliasToBean(TailBalanceRes.class)) // Map columns to DTO fields
                    .list();
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage());
        }

    }
}
