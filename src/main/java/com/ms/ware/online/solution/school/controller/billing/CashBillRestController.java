/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.billing;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.account.CashBillDao;
import com.ms.ware.online.solution.school.entity.account.CashBill;
import com.ms.ware.online.solution.school.entity.account.CashBillDetail;
import com.ms.ware.online.solution.school.service.account.VoucherEntry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Billing/CashBill")
public class CashBillRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    CashBillDao dao;

    @GetMapping
    public Object index() {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        return dao.getAll("from CashBill where approveDate is null");
    }

    @GetMapping("/{billNo}")
    public Object index(@PathVariable String billNo) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        List list = dao.getAll("from CashBill where billNo='" + billNo + "'");
        if (list.isEmpty()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid Bill No"));
        }
        return list.get(0);
    }

    @PostMapping
    public Object doSave(@Valid @RequestBody CashBill obj) throws IOException {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String billNo;
        Map map;
        String enterDate = obj.getEnterDateAd();
        long fiscalYear;
        int billSn;
        String sql = "SELECT ID as id FROM fiscal_year WHERE '" + enterDate + "' BETWEEN START_DATE AND END_DATE";
        List list = dao.getRecord(sql);
        if (list.isEmpty()) {
            return message.respondWithError("Date in in fiscal year");
        }
        map = (Map) list.get(0);
        fiscalYear = Long.parseLong(map.get("id").toString());
        obj.setFiscalYear(fiscalYear);
        sql = "SELECT IFNULL(MAX(BILL_SN),0)+1 AS billSn FROM cash_bill WHERE FISCAL_YEAR='" + fiscalYear + "'";
        map = (Map) dao.getRecord(sql).get(0);
        billSn = Integer.parseInt(map.get("billSn").toString());
        if (billSn < 10) {
            billNo = fiscalYear + "00000" + billSn;
        } else if (billSn < 100) {
            billNo = fiscalYear + "0000" + billSn;
        } else if (billSn < 1000) {
            billNo = fiscalYear + "000" + billSn;
        } else if (billSn < 10000) {
            billNo = fiscalYear + "00" + billSn;
        } else if (billSn < 100000) {
            billNo = fiscalYear + "0" + billSn;
        } else {
            billNo = fiscalYear + "" + billSn;
        }
        obj.setBillNo(billNo);
        obj.setBillSn(billSn);
        obj.setFiscalYear(fiscalYear);
        obj.setEnterBy(td.getUserName());
        obj.setApproveDate(null);
        double totalAmount = 0;
        List<CashBillDetail> detail = obj.getDetail();
        String[] acCode = new String[detail.size() + 1], particular = new String[detail.size() + 1];
        double[] drAmount = new double[detail.size() + 1], crAmount = new double[detail.size() + 1];
        int i = 0;
        VoucherEntry ve = new VoucherEntry();
        for (i = 0; i < detail.size(); i++) {
            detail.get(i).setBillNo(billNo);
            detail.get(i).setBillSn(i + 1);
            detail.get(i).setId(billNo + "-" + (i + 1));
            totalAmount += detail.get(i).getAmount();
            acCode[i] = detail.get(i).getAcCode();
            particular[i] = detail.get(i).getParticular();
            drAmount[i] = 0;
            crAmount[i] = detail.get(i).getAmount();

        }
        acCode[i] = td.getCashAccount();
        particular[i] = "Being " + obj.getCashTransactionType() + " paid by " + obj.getCustomerName();
        drAmount[i] = totalAmount;
        crAmount[i] = 0;
        obj.setBillAmount(totalAmount);
        obj.setApproveBy(td.getUserName());
        obj.setApproveDate(enterDate);
        String msg = "";
        try {
            int row = dao.save(obj);
            msg = dao.getMsg();
            if (row > 0) {
                boolean veStatus = ve.save(fiscalYear, enterDate, td.getUserName(), "BRV", "", "", null, acCode, particular, drAmount, crAmount);
                return message.respondWithMessage("Success", billNo);
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }

    }

    @PutMapping("/{date}/{receiveAc}")
    public Object doApprove(@PathVariable String date, @PathVariable String receiveAc, @RequestBody List<String> list) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return ResponseEntity.status(200).body(message.respondWithError("invalid token"));
        }
        String dateAd = DateConverted.bsToAd(date);
        String userName = td.getUserName();
        String sql = "SELECT ID id FROM fiscal_year WHERE '" + dateAd + "' BETWEEN START_DATE AND END_DATE;";
        message.map = (Map) dao.getRecord(sql).get(0);
        long fiscalYear = Long.parseLong(message.map.get("id").toString());
        VoucherEntry ve = new VoucherEntry();
        List<CashBillDetail> detail = new ArrayList<>();
        CashBill cashBill = new CashBill();
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                cashBill = dao.getAll("from CashBill where billNo='" + list.get(i) + "'").get(0);
                detail = cashBill.getDetail();
            } catch (Exception e) {
                return ResponseEntity.status(200).body(message.respondWithError("invalid Bill No"));
            }

            String[] acCode = new String[detail.size() + 1], particular = new String[detail.size() + 1];
            double[] drAmount = new double[detail.size() + 1], crAmount = new double[detail.size() + 1];
            double totalAmount = 0;
            int j = 0;
            for (j = 0; j < detail.size(); j++) {
                acCode[j] = detail.get(j).getAcCode();
                particular[j] = detail.get(j).getParticular();
                drAmount[j] = detail.get(j).getAmount();
                crAmount[j] = 0;
                totalAmount = totalAmount + drAmount[j];
            }
            acCode[j] = receiveAc;
            particular[j] = "Being " + cashBill.getCashTransactionType() + " paid by " + cashBill.getCustomerName();
            drAmount[j] = 0;
            crAmount[j] = totalAmount;
            boolean veStatus = ve.save(fiscalYear, dateAd, userName, "BRV", "", "", null, acCode, particular, drAmount, crAmount);
            if (veStatus) {
                cashBill.setApproveBy(userName);
                cashBill.setApproveDate(dateAd);
                dao.save(cashBill);
                count++;
            }

        }
        return message.respondWithMessage(count + " Bill posted in account!!");
    }
}
