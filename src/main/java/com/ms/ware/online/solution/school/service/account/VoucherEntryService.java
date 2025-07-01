/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.service.account;

import com.ms.ware.online.solution.school.entity.account.Voucher;

public interface VoucherEntryService {

     Object journalVoucher(Voucher obj,String voucherType);

     Object voucherEdit(String voucherNo);

     Object voucherEdit(Voucher obj);

     Object openingVoucher(Voucher obj);

    String voucherUnApprove(String voucherNo);
}
