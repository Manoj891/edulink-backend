/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.account;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/Account/BankReconciliation")
public class BankReconciliationRestController {
    @Autowired
    private DB db;
    @GetMapping("/Bank")
    public Object getBank() {
       
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account  WHERE AC_CODE  LIKE '1020202%' AND TRANSACT='Y'";
        return db.getRecord(sql);
    }

    @GetMapping
    public Object getStatement(@RequestParam String bankAc, @RequestParam String dateTo, @RequestParam String dateFrom, @RequestParam String status) {
       
        if (bankAc.length() > 0) {
            bankAc = " AND L.AC_CODE='" + bankAc + "'";
        } else {
            bankAc = " AND L.AC_CODE  LIKE '1020202%'";
        }
        if (dateFrom.length() == 10 && dateTo.length() == 10) {
            dateFrom = DateConverted.bsToAd(dateFrom);
            dateTo = DateConverted.bsToAd(dateTo);
            dateFrom = " AND L.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "'";
        } else {
            dateFrom = "";
        }
        String sql = "SELECT IFNULL(BANK_RECONCILIATION_DATE,'') bankReconciliationDate,VOUCHER_NO voucherNo,CHEQUE_NO chequeNo,PARTICULAR particular,C.AC_CODE acCode,C.AC_NAME acName,L.DR_AMT drAmt,L.CR_AMT crAmt,GET_BS_DATE(L.ENTER_DATE) enterDate,GET_BS_DATE(L.POST_DATE) postDate,L.ID trId FROM chart_of_account C,ledger L WHERE C.AC_CODE=L.AC_CODE AND IFNULL(BANK_RECONCILIATION,'N')='" + status + "' " + bankAc + dateFrom + " ORDER BY C.AC_NAME,L.ENTER_DATE";
        return db.getRecord(sql);
    }

    @PutMapping("/{ids}")
    public Object doBankReconciliation(@PathVariable String ids, @RequestParam String date) {
       
        ids = "'" + ids.replace(",", "','") + "'";
        String sql = "UPDATE ledger SET BANK_RECONCILIATION='Y',BANK_RECONCILIATION_DATE='" + date + "' WHERE ID IN(" + ids + ");";
        int row = db.save(sql);
        return new Message().respondWithMessage(row + " Record Saved!!");
    }
}
