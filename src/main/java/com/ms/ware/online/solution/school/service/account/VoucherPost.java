package com.ms.ware.online.solution.school.service.account;

import com.ms.ware.online.solution.school.config.DB;

public class VoucherPost {

    String msg;

    public boolean post(String voucherNo[], String postedBy, String postedDate) {
        DB db = new DB();

        String sql;
        int row, count = 0;

        for (int i = 0; i < voucherNo.length; i++) {
            sql = "INSERT INTO ledger (ID, AC_CODE, DR_AMT, CR_AMT, PARTICULAR, VOUCHER_NO, FEE_RECEIPT_NO, CHEQUE_NO, NARRATION, ENTER_DATE, ENTER_BY, POST_DATE, POST_BY) \n"
                    + " SELECT ID id,AC_CODE acCode,DR_AMT drAmt,CR_AMT crAmt,PARTICULAR,M.VOUCHER_NO,FEE_RECEIPT_NO,D.CHEQUE_NO,M.NARRATION,ENTER_DATE,ENTER_BY,'" + postedDate + "' AS POST_DATE,'" + postedBy + "' POST_BY FROM voucher M,voucher_detail D WHERE M.VOUCHER_NO=D.VOUCHER_NO AND APPROVE_DATE IS NULL AND M.VOUCHER_NO='" + voucherNo[i] + "'";
            row = db.save(sql);
            msg = db.getMsg();
            System.out.println(row);
            if (row > 0) {
                sql = "UPDATE voucher SET APPROVE_BY='" + postedBy + "',APPROVE_DATE='" + postedDate + "' WHERE VOUCHER_NO='" + voucherNo[i] + "';";
                db.save(sql);
                count++;
            }
        }
        if (count > 0) {
            msg = count + " Record posted!!";
            return true;
        }
        return false;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
