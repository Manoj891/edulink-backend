package com.ms.ware.online.solution.school.controller.account;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Account/")
public class AccountReportRestController {

    @GetMapping("/DailyTransaction")
    public Object index(@RequestParam(defaultValue = "") String acCode, @RequestParam(defaultValue = "") String group, @RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam(defaultValue = "") String enterBy) {
        DB db = new DB();
        dateFrom = DateConverted.bsToAd(dateFrom);
        dateTo = DateConverted.bsToAd(dateTo);
        if (!enterBy.isEmpty()) {
            enterBy = " AND M.ENTER_BY='" + enterBy + "'";
        } else enterBy = "";

        if (!acCode.isEmpty()) {
            acCode = " AND C.AC_CODE='" + acCode + "'";
        } else acCode = "";

        if (!group.isEmpty()) {
            group = " AND C.AC_CODE LIKE '" + group + "%'";
        } else group = "";
        String sql = "SELECT D.VOUCHER_NO AS voucherNo,C.AC_NAME acName,C.AC_CODE acCode,ROUND(D.DR_AMT,2) AS dr,ROUND(D.CR_AMT,2) cr,D.PARTICULAR particular,M.ENTER_BY enterBy,GET_BS_DATE(M.ENTER_DATE) enterDate,IFNULL(GET_BS_DATE(APPROVE_DATE),'') approveDate,IFNULL(APPROVE_BY,'') approveBy FROM voucher M,voucher_detail D,chart_of_account C WHERE M.VOUCHER_NO=D.VOUCHER_NO AND C.AC_CODE=D.AC_CODE AND M.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' " + enterBy + acCode + group + " ORDER BY M.ENTER_DATE,D.VOUCHER_NO";
        return db.getRecord(sql);
    }

    @GetMapping("/AccountLedger")
    public Object accountLedger(@RequestParam(required = false) String acCode, @RequestParam String dateFrom, @RequestParam String dateTo) {
        DB db = new DB();

        Message msg = new Message();
        try {
            if (acCode.length() < 2) {
                return msg.respondWithError("Please provode Account");
            }
        } catch (Exception e) {
            return msg.respondWithError("Please provode Account");
        }
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String sql, dateToAd = DateConverted.bsToAd(dateTo);
        Map<String,Object> map = new HashMap<>();

        if (acCode.startsWith("1") || acCode.startsWith("2")) {
            sql = "SELECT '" + dateFrom + "' AS dateFrom, '" + dateTo + "' AS dateTo,AC_CODE acCode,AC_NAME acName,GET_OPENING_BALANCE(AC_CODE,'" + dateFromAd + "') AS opening FROM chart_of_account WHERE AC_CODE='" + acCode + "'";
        } else {
            sql = "SELECT '" + dateFrom + "' AS dateFrom, '" + dateTo + "' AS dateTo,AC_CODE acCode,AC_NAME acName,0 AS opening FROM chart_of_account WHERE AC_CODE='" + acCode + "'";
        }
        map.put("opening", db.getRecord(sql).get(0));

        sql = "SELECT ROUND(DR_AMT,2) dr,ROUND(CR_AMT,2) cr,PARTICULAR particular,GET_BS_DATE(ENTER_DATE) enterDate,VOUCHER_NO voucherNo,ifnull(CHEQUE_NO,'') chequeNo FROM ledger WHERE ENTER_DATE BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "' AND AC_CODE='" + acCode + "' ORDER BY ENTER_DATE,VOUCHER_NO";
        map.put("data", db.getRecord(sql));
        return map;
    }

    @GetMapping("/ProfitLoss")
    public Object profitLoss(@RequestParam Integer level, @RequestParam String dateFrom, @RequestParam String dateTo) {
        DB db = new DB();
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);
        String sql = "";
        double carryForward = 0;
        Map<String, Object> map = new HashMap<>();
        sql = "SELECT SUM(DR_AMT)-SUM(CR_AMT) carryForward FROM ledger where (AC_CODE like '3%' or AC_CODE like '4%') and ENTER_DATE BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "'";
        try {
            carryForward = Double.parseDouble(db.getRecord(sql).get(0).get("carryForward").toString());
        } catch (Exception ignored) {
        }
        sql = "SELECT AC_CODE acCode,AC_NAME acName,0 AS opening,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr,TRANSACT transact FROM chart_of_account  WHERE LEVEL<=" + level + " AND AC_CODE LIKE '3%' ORDER BY AC_CODE";
        map.put("income", db.getRecord(sql));
        sql = "SELECT AC_CODE acCode,AC_NAME acName,0 AS opening,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr,TRANSACT transact FROM chart_of_account  WHERE LEVEL<=" + level + " AND AC_CODE LIKE '4%' ORDER BY AC_CODE";
        map.put("expense", db.getRecord(sql));
        if (carryForward > 0)
            map.put("carryForward", df.format(carryForward) + " DR.");
        else if (carryForward < 0)
            map.put("carryForward", df.format(Math.abs(carryForward)) + " CR.");
        else map.put("carryForward", "0");
        map.put("dateFrom", dateFrom);
        map.put("dateTo", dateTo);
        return map;
    }

    @GetMapping("/BalanceSheet")
    public Object balanceSheet(@RequestParam String dateFrom, @RequestParam String dateTo) {
        DB db = new DB();
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);
        String sql;
        Map<String, Object> map;
        list = new ArrayList<>();
        double opening, dr, cr;
        sql = "SELECT GET_OPENING_BALANCE(AC_CODE,'" + dateFromAd + "') AS opening,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account  WHERE AC_CODE='1'";
        map = db.getRecord(sql).get(0);
        opening = Double.parseDouble(map.get("opening").toString());
        dr = Double.parseDouble(map.get("dr").toString());
        cr = Double.parseDouble(map.get("cr").toString());
        getMapVal(opening, dr, cr, "Assets", "1", "N");
        sql = "SELECT GET_OPENING_BALANCE(AC_CODE,'" + dateFromAd + "') AS opening,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account  WHERE AC_CODE ='2'";
        map = db.getRecord(sql).get(0);
        double liabilitiesOpening = Double.parseDouble(map.get("opening").toString());
        double liabilitiesDr = Double.parseDouble(map.get("dr").toString());
        double liabilitiesCr = Double.parseDouble(map.get("cr").toString());
        getMapVal(liabilitiesOpening, liabilitiesDr, liabilitiesCr, "Liabilities", "2", "Y");
        double plOpening = 0, plDr = 0, plCr = 0;
        sql = "SELECT SUM(DR_AMT)-SUM(CR_AMT) amount FROM ledger where (AC_CODE like '3%' or AC_CODE like '4%') and ENTER_DATE<'" + dateFromAd + "'";
        try {
            plOpening = Double.parseDouble(db.getRecord(sql).get(0).get("amount").toString());
        } catch (Exception ignored) {
        }
        sql = "SELECT SUM(DR_AMT)dr,SUM(CR_AMT) cr FROM ledger where (AC_CODE like '3%' or AC_CODE like '4%') and ENTER_DATE BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "'";
        try {
            map = db.getRecord(sql).get(0);
            plDr = Double.parseDouble(map.get("dr").toString());
            plCr = Double.parseDouble(map.get("cr").toString());
        } catch (Exception ignored) {
        }
        sql = "SELECT RESERVES_AND_SURPLUS acCode,AC_NAME acName FROM organization_master O,chart_of_account C where O.RESERVES_AND_SURPLUS=C.AC_CODE";
        map = db.getRecord(sql).get(0);
        getMapVal(plOpening, plDr, plCr, map.get("acName").toString(), map.get("acCode").toString(), "Y");
        getMapVal((liabilitiesOpening + plOpening), (liabilitiesDr + plDr), (liabilitiesCr + plCr), "Capital & Liabilities", "", "N");
        map = new HashMap<>();
        map.put("data", list);
        map.put("dateFrom", dateFrom);
        map.put("dateTo", dateTo);

        return map;
    }

    @GetMapping("/TrailBalance")
    public Object trailBalance(@RequestParam String dateFrom, @RequestParam String dateTo) {
        DB db = new DB();
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);
        String sql;
        Map<String, Object> map;
        list = new ArrayList<>();
        double opening, dr, cr, totalOpeningDr = 0, totalOpeningCr = 0, totalDr = 0, totalCr = 0, gTotalDr = 0, gTotalCr = 0;
        sql = "SELECT GET_OPENING_BALANCE(AC_CODE,'" + dateFromAd + "') AS opening,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account  WHERE AC_CODE='1'";
        map = db.getRecord(sql).get(0);
        opening = Double.parseDouble(map.get("opening").toString());
        dr = Double.parseDouble(map.get("dr").toString());
        cr = Double.parseDouble(map.get("cr").toString());
        if (opening > 0) totalOpeningDr = totalOpeningDr + opening;
        else totalOpeningCr = totalOpeningCr + Math.abs(opening);
        if ((dr - cr) > 0) totalDr = totalDr + (dr - cr);
        else totalCr = totalCr + Math.abs(dr - cr);
        if (opening + (dr - cr) > 0) gTotalDr = gTotalDr + opening + (dr - cr);
        else gTotalCr = gTotalCr + Math.abs(opening + dr - cr);

        getMapVal(opening, dr, cr, "Assets", "1", "N");
        sql = "SELECT GET_OPENING_BALANCE(AC_CODE,'" + dateFromAd + "') AS opening,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account  WHERE AC_CODE ='2'";
        map = db.getRecord(sql).get(0);
        double liabilitiesOpening = Double.parseDouble(map.get("opening").toString());
        double liabilitiesDr = Double.parseDouble(map.get("dr").toString());
        double liabilitiesCr = Double.parseDouble(map.get("cr").toString());
        getMapVal(liabilitiesOpening, liabilitiesDr, liabilitiesCr, "Liabilities", "2", "Y");
        double plOpening = 0;
        sql = "SELECT SUM(DR_AMT)-SUM(CR_AMT) amount FROM ledger where (AC_CODE like '3%' or AC_CODE like '4%') and ENTER_DATE<'" + dateFromAd + "'";
        try {
            plOpening = Double.parseDouble(db.getRecord(sql).get(0).get("amount").toString());
        } catch (Exception ignored) {
        }

        sql = "SELECT RESERVES_AND_SURPLUS acCode,AC_NAME acName FROM organization_master O,chart_of_account C where O.RESERVES_AND_SURPLUS=C.AC_CODE";
        map = db.getRecord(sql).get(0);
        getMapVal(plOpening, 0, 0, map.get("acName").toString(), map.get("acCode").toString(), "Y");

        if ((liabilitiesOpening + plOpening) > 0) totalOpeningDr = totalOpeningDr + (liabilitiesOpening + plOpening);
        else totalOpeningCr = totalOpeningCr + Math.abs((liabilitiesOpening + plOpening));
        if ((dr - cr) > 0) totalDr = totalDr + (liabilitiesDr - liabilitiesCr);
        else totalCr = totalCr + Math.abs(liabilitiesDr - liabilitiesCr);
        if ((liabilitiesOpening + plOpening) + (liabilitiesDr - liabilitiesCr) > 0)
            gTotalDr = gTotalDr + (liabilitiesOpening + plOpening) + (liabilitiesDr - liabilitiesCr);
        else gTotalCr = gTotalCr + Math.abs((liabilitiesOpening + plOpening) + (liabilitiesDr - liabilitiesCr));
        getMapVal((liabilitiesOpening + plOpening), liabilitiesDr, liabilitiesCr, "Capital & Liabilities", "", "N");

        sql = "SELECT GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account  WHERE AC_CODE='3'";
        map = db.getRecord(sql).get(0);
        dr = Double.parseDouble(map.get("dr").toString());
        cr = Double.parseDouble(map.get("cr").toString());
        getMapVal(0, dr, cr, "Income", "3", "N");

        if ((dr - cr) > 0) totalDr = totalDr + (dr - cr);
        else totalCr = totalCr + Math.abs(dr - cr);
        if ((dr - cr) > 0) gTotalDr = gTotalDr + (dr - cr);
        else gTotalCr = gTotalCr + Math.abs(dr - cr);

        sql = "SELECT GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account  WHERE AC_CODE='4'";
        map = db.getRecord(sql).get(0);
        dr = Double.parseDouble(map.get("dr").toString());
        cr = Double.parseDouble(map.get("cr").toString());
        if ((dr - cr) > 0) totalDr = totalDr + (dr - cr);
        else totalCr = totalCr + Math.abs(dr - cr);
        if ((dr - cr) > 0) gTotalDr = gTotalDr + (dr - cr);
        else gTotalCr = gTotalCr + Math.abs(dr - cr);
        getMapVal(0, dr, cr, "Expenses", "4", "N");
        map = new HashMap<>();
        map.put("acCode", "");
        map.put("acName", "Total");
        map.put("transact", "N");
        map.put("openingCr", df.format(totalOpeningCr));
        map.put("openingDr", df.format(totalOpeningDr));
        map.put("dr", df.format(totalDr));
        map.put("cr", df.format(totalCr));
        map.put("totalDr", df.format(gTotalDr));
        map.put("totalCr", df.format(gTotalCr));
        list.add(map);
        map = new HashMap<>();
        map.put("data", list);
        map.put("dateFrom", dateFrom);
        map.put("dateTo", dateTo);
        return map;
    }

    @GetMapping("/Assets")
    public ResponseEntity<List<Map<String, Object>>> getAssets(@RequestParam int level, @RequestParam String dateFrom, @RequestParam String dateTo) {
        list = new ArrayList<>();
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);
//        String sql = "SELECT TRANSACT transact,AC_CODE acCode,AC_NAME acName,GET_OPENING_BALANCE(AC_CODE,'" + dateFromAd + "') AS opening,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account WHERE AC_CODE LIKE '1%' AND LEVEL<=" + level + " ORDER BY AC_CODE";
        String sql = "SELECT ifnull(SUM(opening), 0) AS opening, ifnull(SUM(debit), 0) AS debit, ifnull(SUM(credit), 0)  AS credit,transact, ac_code, ac_name, level AS ca FROM (SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '1%' WHERE enter_date <  '" + dateFromAd + "' GROUP BY a.ac_code UNION SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '1%' WHERE enter_date BETWEEN  '" + dateFromAd + "' AND  '" + dateToAd + "' GROUP BY a.ac_code union SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '1%' WHERE enter_date <  '" + dateFromAd + "' GROUP BY a.ac_code UNION SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '1%' WHERE enter_date BETWEEN  '" + dateFromAd + "' AND  '" + dateToAd + "' GROUP BY a.ac_code) AS subquery_alias GROUP BY ac_code, ac_name, level,transact ORDER BY ac_code";
        new DB().getRecord(sql).forEach(map -> getMapVal(Double.parseDouble(map.get("opening").toString()), Double.parseDouble(map.get("debit").toString()), Double.parseDouble(map.get("credit").toString()), map.get("ac_name").toString(), map.get("ac_code").toString(), map.get("transact").toString()));
        return ResponseEntity.status(HttpStatus.OK).body(list);

    }

    @GetMapping("/Liabilities")
    public ResponseEntity<List<Map<String, Object>>> getLiabilities(@RequestParam int level, @RequestParam String dateFrom, @RequestParam String dateTo) {
        list = new ArrayList<>();
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);
        DB db = new DB();
        double plOpening = 0, plCr = 0, plDr = 0;
        String sql;
        sql = "SELECT SUM(DR_AMT)-SUM(CR_AMT) amount FROM ledger where (AC_CODE like '3%' or AC_CODE like '4%') and ENTER_DATE<'" + dateFromAd + "'";
        try {
            plOpening = Double.parseDouble(db.getRecord(sql).get(0).get("amount").toString());
        } catch (Exception ignored) {
        }
        sql = "SELECT SUM(DR_AMT)dr,SUM(CR_AMT) cr FROM ledger where (AC_CODE like '3%' or AC_CODE like '4%') and ENTER_DATE BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "'";
        try {
            Map<String, Object> map = db.getRecord(sql).get(0);
            plDr = Double.parseDouble(map.get("dr").toString());
            plCr = Double.parseDouble(map.get("cr").toString());
        } catch (Exception ignored) {
        }

        sql = "SELECT IFNULL(`RESERVES_AND_SURPLUS`,'') pl FROM organization_master WHERE `ID`=1";
        String plAcCode = db.getMapRecord(sql).get(0).get("pl").toString();
        sql = "SELECT ifnull(SUM(opening), 0) AS opening, ifnull(SUM(debit), 0) AS debit, ifnull(SUM(credit), 0)  AS credit,transact, ac_code, ac_name, level AS ca FROM (SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '2%' WHERE enter_date <  '" + dateFromAd + "' GROUP BY a.ac_code UNION SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '2%' WHERE enter_date BETWEEN  '" + dateFromAd + "' AND  '" + dateToAd + "' GROUP BY a.ac_code union SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '2%' WHERE enter_date <  '" + dateFromAd + "' GROUP BY a.ac_code UNION SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level,transact FROM ledger l  JOIN chart_of_account a ON LEVEL<=" + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '2%' WHERE enter_date BETWEEN  '" + dateFromAd + "' AND  '" + dateToAd + "' GROUP BY a.ac_code) AS subquery_alias GROUP BY ac_code, ac_name, level,transact ORDER BY ac_code";

        double finalPlOpening = plOpening;
        double finalPlDr = plDr;
        double finalPlCr = plCr;
        db.getRecord(sql).forEach(map -> {
            String transact = map.get("transact").toString();
            String acCode = map.get("ac_code").toString();
            String acName = map.get("ac_name").toString();
            double opening = Double.parseDouble(map.get("opening").toString());
            double dr = Double.parseDouble(map.get("debit").toString());
            double cr = Double.parseDouble(map.get("credit").toString());
            if (plAcCode.startsWith(acCode)) {
                getMapVal(opening + finalPlOpening, dr + finalPlDr, cr + finalPlCr, acName, acCode, transact);
            } else {
                getMapVal(opening, dr, cr, acName, acCode, transact);
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/Income")
    public ResponseEntity<List<Map<String, Object>>> getIncome(@RequestParam int level, @RequestParam String dateFrom, @RequestParam String dateTo) {
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);
//        String sql = "SELECT TRANSACT transact,AC_CODE acCode,AC_NAME acName,GET_DR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS dr,GET_CR_BALANCE(AC_CODE,'" + dateFromAd + "','" + dateToAd + "') AS cr FROM chart_of_account WHERE AC_CODE LIKE '3%' AND LEVEL<=" + level + " ORDER BY AC_CODE";
        String sql = "SELECT SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level, transact FROM ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '3%' WHERE enter_date BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "' GROUP BY ac_code, ac_name, level, transact union SELECT SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level, transact FROM ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '3%' WHERE enter_date BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "' GROUP BY ac_code, ac_name, level, transact ORDER BY ac_code";
        list = new ArrayList<>();
        new DB().getRecord(sql).forEach(map -> getMapVal(0, Double.parseDouble(map.get("debit").toString()), Double.parseDouble(map.get("credit").toString()), map.get("ac_name").toString(), map.get("ac_code").toString(), map.get("transact").toString()));
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/Expense")
    public ResponseEntity<List<Map<String, Object>>> getExpense(@RequestParam int level, @RequestParam String dateFrom, @RequestParam String dateTo) {
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);
        String sql = "SELECT SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level, transact FROM ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '4%' WHERE enter_date BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "' GROUP BY ac_code, ac_name, level, transact union SELECT SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level, transact FROM ledger l JOIN chart_of_account a ON LEVEL <= " + level + " and TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '4%' WHERE enter_date BETWEEN '" + dateFromAd + "' AND '" + dateToAd + "' GROUP BY ac_code, ac_name, level, transact ORDER BY ac_code";
        list = new ArrayList<>();
        new DB().getRecord(sql).forEach(map -> getMapVal(0, Double.parseDouble(map.get("debit").toString()), Double.parseDouble(map.get("credit").toString()), map.get("ac_name").toString(), map.get("ac_code").toString(), map.get("transact").toString()));
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/GroupReport")
    public ResponseEntity<List<Map<String, Object>>> getGroupReport(@RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String acCode) {
        String dateFromAd = DateConverted.bsToAd(dateFrom);
        String dateToAd = DateConverted.bsToAd(dateTo);

        String sql = "SELECT ifnull(SUM(opening), 0) AS opening, ifnull(SUM(debit), 0) AS debit, ifnull(SUM(credit), 0)  AS credit, ac_code, ac_name, level AS ca FROM (SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level FROM ledger l  JOIN chart_of_account a ON TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '" + acCode + "%' WHERE enter_date <  '" + dateFromAd + "' GROUP BY a.ac_code UNION SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level FROM ledger l  JOIN chart_of_account a ON TRANSACT = 'N' AND l.ac_code LIKE CONCAT(a.ac_code, '%') AND a.ac_code LIKE '" + acCode + "%' WHERE enter_date BETWEEN  '" + dateFromAd + "' AND  '" + dateToAd + "' GROUP BY a.ac_code union SELECT SUM(dr_amt) - SUM(cr_amt) AS opening, 0 AS debit, 0 AS credit, a.ac_code, a.ac_name, level FROM ledger l  JOIN chart_of_account a ON TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '" + acCode + "%' WHERE enter_date <  '" + dateFromAd + "' GROUP BY a.ac_code UNION SELECT 0 AS opening, SUM(dr_amt) AS debit, SUM(cr_amt) AS credit, a.ac_code, a.ac_name, level FROM ledger l  JOIN chart_of_account a ON TRANSACT = 'Y' AND l.ac_code = a.ac_code AND a.ac_code LIKE '" + acCode + "%' WHERE enter_date BETWEEN  '" + dateFromAd + "' AND  '" + dateToAd + "' GROUP BY a.ac_code) AS subquery_alias GROUP BY ac_code, ac_name, level ORDER BY ac_code";
        return ResponseEntity.status(HttpStatus.OK).body(new DB().getMapRecord(sql));
    }

    private void getMapVal(double opening, double dr, double cr, String name, String acCode, String transact) {
        if (opening != 0 || dr != 0 || cr != 0) {
            Map<String, Object> m = new HashMap<>();
            double total = dr - cr;
            double gTotal = (opening + dr) - cr;
            if (opening > 0.01) {
                m.put("openingDr", df.format(opening));
                m.put("openingCr", "-");
            } else if (opening < (-0.01)) {
                m.put("openingDr", "-");
                m.put("openingCr", df.format(Math.abs(opening)));
            } else {
                m.put("openingDr", "-");
                m.put("openingCr", "-");
            }
            if (total > 0.01) {
                m.put("dr", df.format(total));
                m.put("cr", "-");
            } else if (total < (-0.01)) {
                m.put("dr", "-");
                m.put("cr", df.format(Math.abs(total)));
            } else {
                m.put("dr", "-");
                m.put("cr", "-");
            }

            if (gTotal > 0.01) {
                m.put("totalDr", df.format(gTotal));
                m.put("totalCr", "-");
            } else if (gTotal < (-0.01)) {
                m.put("totalDr", "-");
                m.put("totalCr", df.format(Math.abs(gTotal)));
            } else {
                m.put("totalDr", "-");
                m.put("totalCr", "-");
            }
            m.put("acCode", acCode);
            m.put("acName", name);
            m.put("transact", transact);
            list.add(m);
        }
    }

    DecimalFormat df = new DecimalFormat("#.##");
    List<Map<String, Object>> list = new ArrayList<>();
}
