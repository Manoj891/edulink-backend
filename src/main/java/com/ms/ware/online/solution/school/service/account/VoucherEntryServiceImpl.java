/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.service.account;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.account.VoucherDao;
import com.ms.ware.online.solution.school.exception.PermissionDeniedException;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.account.VoucherDetail;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherEntryServiceImpl implements VoucherEntryService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    VoucherDao da;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public Object journalVoucher(Voucher obj, String voucherType) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid authorization");
        }

        String sql, msg = "";
        if (voucherType == null) {
            voucherType = "JVR";
        }
        Map map;
        List list;
        Date date = obj.getEnterDateAd();
        String enterDate = DateConverted.toString(date);
        sql = "SELECT ID id FROM fiscal_year WHERE '" + enterDate + "' BETWEEN START_DATE AND END_DATE;";
        list = da.getRecord(sql);
        if (list.isEmpty()) {
            msg = "Please define fiscal year of " + obj.getEnterDate();
            return message.respondWithError(msg);
        }
        map = (Map) list.get(0);
        long fiscalYear = Long.parseLong(map.get("id").toString());
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
        List<VoucherDetail> objList = obj.getDetail();
        int count = 0;
        for (int i = 0; i < objList.size(); i++) {
            if (objList.get(i).getAcCode().length() < 3) {
                continue;
            }
            totalDr += objList.get(i).getDrAmt();
            totalCr += objList.get(i).getCrAmt();
            objList.get(i).setId(voucherNo + "-" + (i + 1));
            objList.get(i).setVoucherNo(voucherNo);
            objList.get(i).setVoucherSn(i + 1);
            count++;
        }
        if (count < 2) {
            return message.respondWithError("Please provide at least 2 rows!");
        }
        totalDr = Double.parseDouble(df.format(totalDr));
        totalCr = Double.parseDouble(df.format(totalCr));
        if (totalCr != totalDr) {
            msg = "Dr Amount (" + totalDr + ") not equals Cr Amount (" + totalCr + ") ";
            return message.respondWithError(msg);
        }

        obj.setVoucherNo(voucherNo);
        obj.setVoucherSn(voucherSn);
        obj.setVoucherType(voucherType);
        obj.setFiscalYear(fiscalYear);
        obj.setTotalAmount(totalCr);
        obj.setFeeReceiptNo(null);
        obj.setChequeNo(null);
        obj.setEnterBy(td.getUserName());
//        int row =0;
        int row = da.save(obj);
        msg = da.getMsg();
        if (row == 0) {
            msg = msg.toLowerCase().replace("`", "");
            System.out.println(msg);
            if (msg.contains("foreign key (ac_code) references chart_of_account (ac_code)")) {
                msg = "Provided account code not valid!! Please check account code.";
            }
            return message.respondWithError(msg);
        }
        return message.respondWithMessage("Success", voucherNo);

    }

    @Override
    public Object voucherEdit(String voucherNo) {
        String sql = "SELECT VOUCHER_NO voucherNo,NARRATION narration,GET_BS_DATE(ENTER_DATE) enterDate,ifnull(APPROVE_DATE,'') approve_date FROM voucher WHERE VOUCHER_NO='" + voucherNo + "'";
        List list = da.getRecord(sql);
        if (list.isEmpty()) {
            return new Message().respondWithError("Invalid Voucher no!!");
        }
        Map map = (Map) list.get(0);
        sql = "SELECT ID id,C.AC_CODE acCode,C.AC_NAME acName,DR_AMT drAmt,CR_AMT crAmt,PARTICULAR particular,IFNULL(BILL_NO,'') billNo,IFNULL(CHEQUE_NO,'') chequeNo FROM voucher_detail V,chart_of_account C WHERE C.AC_CODE=V.AC_CODE AND VOUCHER_NO='" + voucherNo + "'";
        map.put("detail", da.getRecord(sql));
        return map;
    }

    @Override
    public Object voucherEdit(Voucher obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid authorization");
        }
        String voucherNo = obj.getVoucherNo();

        List<VoucherDetail> detail = obj.getDetail();
        Voucher objnew = da.getAll("from Voucher where voucherNo='" + voucherNo + "'").get(0);
        try {
            if (objnew.getApproveDate() != null) {
                return message.respondWithError("Can not edit after Approved!!");
            }

        } catch (Exception e) {
        }
        List<VoucherDetail> detailnew = objnew.getDetail();
        obj.setVoucherSn(objnew.getVoucherSn());
        obj.setVoucherType(objnew.getVoucherType());
        obj.setFiscalYear(objnew.getFiscalYear());
        obj.setEnterBy(td.getUserName());
        String id;
        double totalDr = 0, totalCr = 0;
        for (int i = 0; i < detail.size(); i++) {
            id = detail.get(i).getId();

            totalCr += detail.get(i).getCrAmt();
            totalDr += detail.get(i).getDrAmt();
            if (id.length() > 2) {
                for (int j = 0; j < detailnew.size(); j++) {
                    if (id.equalsIgnoreCase(detailnew.get(j).getId())) {
                        detail.get(i).setVoucherSn(i + 1);
                        detail.get(i).setVoucherNo(voucherNo);
                    }
                }
            } else {
                detail.get(i).setVoucherSn(i + 1);
                detail.get(i).setId(voucherNo + detail.get(i).getAcCode());
                detail.get(i).setVoucherNo(voucherNo);
            }
        }
        if (totalCr != totalDr) {
            return message.respondWithError("Dr Amount not equel Cr");
        }
        obj.setDetail(detail);
        obj.setTotalAmount(totalCr);
        int row = da.update(obj);
        String msg = da.getMsg();
        if (row == 0) {
            msg = msg.toLowerCase().replace("`", "");
            if (msg.contains("foreign key (ac_code) references chart_of_account (ac_code)")) {
                msg = "Provided account code not valid!! Please check account code.";
            }
            return message.respondWithError(msg);
        }
        String sql = "DELETE FROM voucher_detail WHERE VOUCHER_NO='" + voucherNo + "' AND CR_AMT=0 AND DR_AMT=0";
        da.delete(sql);
        return message.respondWithMessage("Success", voucherNo);
    }

    @Override
    public Object openingVoucher(Voucher obj) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid authorization");
        }
        List<VoucherDetail> objList = obj.getDetail();
        String sql, msg, voucherType = "OPN";
        Map<String, Object> map;
        List<Map<String, Object>> list;
        long fiscalYear = obj.getFiscalYear();
        String enterDate;
        sql = "SELECT START_DATE enterDate FROM fiscal_year WHERE ID='" + fiscalYear + "' ";
        list = da.getRecord(sql);
        if (list.isEmpty()) {
            msg = "Please define fiscal year of " + obj.getEnterDate();
            return message.respondWithError(msg);
        }
        map = list.get(0);
        enterDate = map.get("enterDate").toString();
        String voucherNo = fiscalYear + voucherType;

        List<Voucher> vList = da.getAll("from Voucher where voucherNo='" + voucherNo + "'");
        if (vList.size() == 1) {
            try {
                if (vList.get(0).getApproveDate() != null) {
                    return message.respondWithError("Opening already posted in ledger");
                }
            } catch (Exception ignored) {
            }
        }
        obj.setEnterDateAd(enterDate);
        obj.setVoucherNo(voucherNo);
        obj.setVoucherSn(0);
        obj.setVoucherType(voucherType);
        obj.setFiscalYear(fiscalYear);
        obj.setTotalAmount(0d);
        obj.setFeeReceiptNo(null);
        obj.setChequeNo(null);
        obj.setEnterBy(td.getUserName());
        obj.setDetail(null);
        int row = da.save(obj);


        VoucherDetail detail;
        for (VoucherDetail voucherDetail : objList) {
            if (voucherDetail.getAcCode().length() < 3) {
                continue;
            }
            detail = new VoucherDetail();
            detail.setId(voucherNo + "-" + voucherDetail.getAcCode());
            detail.setVoucherNo(voucherNo);
            sql = "SELECT IFNULL(MAX(`VOUCHER_SN`),0)+1 AS voucherSn FROM voucher_detail WHERE `VOUCHER_NO`='" + voucherNo + "'";
            map = da.getRecord(sql).get(0);
            int voucherSn = Integer.parseInt(map.get("voucherSn").toString());
            detail.setVoucherSn(voucherSn);
            detail.setAcCode(voucherDetail.getAcCode());
            detail.setCrAmt(voucherDetail.getCrAmt());
            detail.setDrAmt(voucherDetail.getDrAmt());
            detail.setParticular(voucherDetail.getParticular());
            da.save(detail);
        }
        da.delete("delete from ledger where VOUCHER_NO='" + voucherNo + "'");
        da.delete("INSERT INTO ledger (ID, AC_CODE, DR_AMT, CR_AMT, PARTICULAR, VOUCHER_NO, FEE_RECEIPT_NO, CHEQUE_NO, NARRATION, ENTER_DATE, ENTER_BY, POST_DATE, POST_BY) SELECT ID    AS id, AC_CODE  acCode, DR_AMT   drAmt, CR_AMT   crAmt, PARTICULAR, M.VOUCHER_NO, FEE_RECEIPT_NO, D.CHEQUE_NO, M.NARRATION, ENTER_DATE, ENTER_BY, now() AS POST_DATE, 'SYSTEM' POST_BY FROM voucher M join voucher_detail D on M.VOUCHER_NO = D.VOUCHER_NO WHERE M.VOUCHER_NO ='" + voucherNo + "'");
        msg = da.getMsg();
        if (row == 0) {
            msg = msg.toLowerCase().replace("`", "");

            if (msg.contains("foreign key (ac_code) references chart_of_account (ac_code)")) {
                msg = "Provided account code not valid!! Please check account code.";
            }
            return message.respondWithError(msg);
        }
        return message.respondWithMessage("Success", voucherNo);

    }

    @Override
    public String voucherUnApprove(String voucherNo) {
        Message message = new Message();
        AuthenticatedUser user = facade.getAuthentication();
        String sql = "select ifnull(voucher_un_approve,'N') voucher_un_approve from organization_user_info where login_id='" + user.getUserName() + "'";
        Map<String, Object> map = da.getRecord(sql).get(0);
        String status = map.get("voucher_un_approve").toString();
        if ("N".equalsIgnoreCase(status)) {
            throw new PermissionDeniedException();
        }
        sql = "delete from ledger where VOUCHER_NO='" + voucherNo + "'";
        da.delete(sql);
        sql = "update voucher set approve_by=null,approve_date=null,un_approve_by_date='" + user.getUserName() + " " + DateConverted.now() + "' where voucher_no='" + voucherNo + "'";
        da.delete(sql);
        return message.respondWithMessage("Success");
    }

}
