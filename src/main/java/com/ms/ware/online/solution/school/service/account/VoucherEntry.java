/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.service.account;

import com.ms.ware.online.solution.school.dao.account.VoucherDao;
import com.ms.ware.online.solution.school.dao.account.VoucherDaoImp;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.account.VoucherDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VoucherEntry {

    String msg = "";
    String voucherNo;
    @Autowired
    private VoucherDao da;

    public boolean save(String enterDate, String enterBy, String voucherType, String narration, String chequeNo, String feeReceiptNo, String acCode[], String particular[], double drAmount[], double crAmount[]) {

        Voucher obj = new Voucher();
        List<VoucherDetail> l = new ArrayList();
        String sql;
        Map map;
        List list;
        sql = "SELECT ID id FROM fiscal_year WHERE '" + enterDate + "' BETWEEN START_DATE AND END_DATE;";
        list = da.getRecord(sql);
        if (list.isEmpty()) {
            msg = "Please define fiscal year of " + enterDate;
            return false;
        }
        map = (Map) list.get(0);
        long fiscalYear = Long.parseLong(map.get("id").toString());
        sql = "SELECT IFNULL(max(VOUCHER_SN),0)+1 AS voucherSn FROM voucher WHERE FISCAL_YEAR='" + fiscalYear + "' AND VOUCHER_TYPE='" + voucherType + "'";
        map = da.getRecord(sql).get(0);
        int voucherSn = Integer.parseInt(map.get("voucherSn").toString());
        String voucherNo = "";
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
        double totalDr = 0, totalCr = 0;
        for (int i = 0; i < drAmount.length; i++) {
            totalDr += drAmount[i];
        }
        for (int i = 0; i < crAmount.length; i++) {
            totalCr += crAmount[i];
        }
        if (totalCr != totalDr) {
            msg = "Dr Amount (" + totalDr + ") not equals Cr Amount (" + totalCr + ") ";
            return false;
        }

        obj.setVoucherNo(voucherNo);
        obj.setVoucherSn(voucherSn);
        obj.setVoucherType(voucherType);
        obj.setFiscalYear(fiscalYear);
        obj.setNarration(narration);
        obj.setTotalAmount(totalCr);
        obj.setFeeReceiptNo(feeReceiptNo);
        obj.setChequeNo(chequeNo);
        obj.setEnterBy(enterBy);
        obj.setEnterDateAd(enterDate);
        VoucherDetail objd;

        for (int i = 0; i < drAmount.length; i++) {
            objd = new VoucherDetail();
            objd.setId(voucherNo + "-" + (i + 1));
            objd.setVoucherNo(voucherNo);
            objd.setVoucherSn(i + 1);
            objd.setAcCode(acCode[i]);
            objd.setParticular(particular[i]);
            objd.setCrAmt(crAmount[i]);
            objd.setDrAmt(drAmount[i]);
            l.add(objd);
        }
        obj.setDetail(l);
        System.out.println(obj);
        int row = da.save(obj);
        if (row == 0) {
            msg = da.getMsg();
            sql = "DELETE FROM voucher_detail WHERE `VOUCHER_NO`='" + voucherNo + "';\n"
                    + "DELETE FROM voucher WHERE `VOUCHER_NO`='" + voucherNo + "';";
            da.delete(sql);
            return false;
        }
        return true;

    }

    public boolean save(long fiscalYear, String enterDate, String enterBy, String voucherType, String narration, String chequeNo, String feeReceiptNo, String acCode[], String particular[], double drAmount[], double crAmount[]) {
        VoucherDao da = new VoucherDaoImp();
        Voucher obj = new Voucher();

        String sql;
        Map map;
        sql = "SELECT IFNULL(max(VOUCHER_SN),0)+1 AS voucherSn FROM voucher WHERE FISCAL_YEAR='" + fiscalYear + "' AND VOUCHER_TYPE='" + voucherType + "'";
        map = (Map) da.getRecord(sql).get(0);
        int voucherSn = Integer.parseInt(map.get("voucherSn").toString());
        String voucherNo = "";
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
        double totalDr = 0, totalCr = 0;
        for (int i = 0; i < drAmount.length; i++) {
            totalDr += drAmount[i];
        }
        for (int i = 0; i < crAmount.length; i++) {
            totalCr += crAmount[i];
        }
        if (totalCr != totalDr) {
            msg = "Dr Amount (" + totalDr + ") not equals Cr Amount (" + totalCr + ") ";
            return false;
        }

        obj.setVoucherNo(voucherNo);
        obj.setVoucherSn(voucherSn);
        obj.setVoucherType(voucherType);
        obj.setFiscalYear(fiscalYear);
        obj.setNarration(narration);
        obj.setTotalAmount(totalCr);
        obj.setFeeReceiptNo(feeReceiptNo);
        obj.setChequeNo(chequeNo);
        obj.setEnterBy(enterBy);
        obj.setEnterDateAd(enterDate);
        VoucherDetail objd;
        List<VoucherDetail> list = new ArrayList();

        for (int i = 0; i < drAmount.length; i++) {
            objd = new VoucherDetail();
            objd.setId(voucherNo + "-" + (i + 1));
            objd.setVoucherNo(voucherNo);
            objd.setBillNo(feeReceiptNo);
            objd.setVoucherSn(i + 1);
            objd.setAcCode(acCode[i]);
            objd.setParticular(particular[i]);
            objd.setCrAmt(crAmount[i]);
            objd.setDrAmt(drAmount[i]);
            list.add(objd);
        }
        obj.setDetail(list);
        int row = da.save(obj);
        if (row == 0) {
            msg = da.getMsg();
            sql = "DELETE FROM voucher_detail WHERE `VOUCHER_NO`='" + voucherNo + "';\n"
                    + "DELETE FROM voucher WHERE `VOUCHER_NO`='" + voucherNo + "';";
            da.delete(sql);
            return false;
        }
        setVoucherNo(voucherNo);
        return true;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

}
