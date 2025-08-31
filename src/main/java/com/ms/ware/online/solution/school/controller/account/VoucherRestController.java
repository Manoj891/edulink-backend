package com.ms.ware.online.solution.school.controller.account;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.service.account.VoucherEntryService;
import com.ms.ware.online.solution.school.service.account.VoucherPost;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Account/")
public class VoucherRestController {

    @Autowired
    private VoucherEntryService service;
    @Autowired
    private DB db;
    @Autowired
    private AuthenticationFacade facade;



    @GetMapping("/Voucher/{voucherNo}")
    public Object index(@PathVariable String voucherNo) {
        DB db = new DB();
        String sql = "SELECT DR_AMT dr,CR_AMT cr,V.AC_CODE acCode,C.AC_NAME acName,IFNULL(PARTICULAR,'') particular FROM voucher_detail V,chart_of_account C WHERE C.AC_CODE=V.AC_CODE AND VOUCHER_NO='" + voucherNo + "'";
        return db.getRecord(sql);
    }

    @GetMapping("/VoucherData/{voucherNo}")
    public Object voucherData(@PathVariable String voucherNo) {
        DB db = new DB();
        Map map;
        String sql;
        sql = "SELECT IFNULL(GET_BS_DATE(APPROVE_DATE),'Pending') AS approveDate,IFNULL(APPROVE_BY,'') approveBy,VOUCHER_NO voucherNo,TOTAL_AMOUNT voucherAmount,IFNULL(CHEQUE_NO,'') chequeNo,IFNULL(NARRATION,'') narration,GET_BS_DATE(ENTER_DATE) AS enterDate,ENTER_BY enterBy FROM voucher WHERE VOUCHER_NO='" + voucherNo + "'";
        List list = db.getRecord(sql);
        if (list.isEmpty()) {
            return new Message().respondWithError("Invalid voucher no");
        }
        map = (Map) list.get(0);
        sql = "SELECT V.AC_CODE acCode,C.AC_NAME acName,V.DR_AMT dr,V.CR_AMT cr,IFNULL(V.PARTICULAR,'') particular,IFNULL(V.BILL_NO,'') billNo,IFNULL(V.CHEQUE_NO,'') chequeNo FROM voucher_detail V,chart_of_account C WHERE V.AC_CODE=C.AC_CODE AND VOUCHER_NO='" + voucherNo + "'";
        map.put("detail", db.getRecord(sql));
        return map;
    }

    @PutMapping("/Voucher/{date}/{voucherNo}")
    public Object postVoucher(@PathVariable String date, @PathVariable String voucherNo) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String vn[] = voucherNo.split(",");
        VoucherPost post = new VoucherPost();
        date = DateConverted.bsToAd(date);
        post.post(vn, td.getUserName(), date);
        return message.respondWithMessage(post.getMsg());
    }

    @PutMapping("/Voucher/{voucherNo}")
    public Object rejectVoucher(@PathVariable String voucherNo) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String sql, vn[] = voucherNo.split(",");
        int row = 0;
        DB db = new DB();
        for (int i = 0; i < vn.length; i++) {
            if (vn[i].length() > 3) {
                sql = "UPDATE voucher SET REJECT_DATE=NOW(),REJECT_BY='" + td.getUserName() + "' WHERE VOUCHER_NO='" + vn[i] + "' AND APPROVE_DATE IS NULL;";
                row += db.delete(sql);
            }
        }

        return message.respondWithMessage(row + " Voucher Rejected!!");
    }

    @PostMapping("/JournalVoucher")
    public Object journalVoucher(@Valid @RequestBody Voucher obj, @RequestParam(required = false) String voucherType) {
        return service.journalVoucher(obj, voucherType);
    }

    @PostMapping("/OpeningVoucher")
    public Object openingVoucher(@Valid @RequestBody Voucher obj) {
        return service.openingVoucher(obj);
    }

    @GetMapping("/VoucherEdit/{voucherNo}")
    public Object voucherEdit(@PathVariable String voucherNo) {
        return service.voucherEdit(voucherNo);
    }

    @PostMapping("/VoucherEdit")
    public Object voucherEdit(@Valid @RequestBody Voucher obj) {
        return service.voucherEdit(obj);
    }

    @PutMapping("/VoucherEdit/{voucherNo}")
    public String voucherUnApprove(@PathVariable String voucherNo) {
        return service.voucherUnApprove(voucherNo);
    }
}
