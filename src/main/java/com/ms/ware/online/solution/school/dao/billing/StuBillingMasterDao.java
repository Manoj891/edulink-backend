package com.ms.ware.online.solution.school.dao.billing;

import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.billing.BillingDeleteMaster;
import com.ms.ware.online.solution.school.entity.billing.StuBillingDetail;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;

public interface StuBillingMasterDao {

    List<Voucher> getVoucher(String hql);

    List<StuBillingMaster> getAll(String hql);

    int save(StuBillingMaster obj);

    int save(StuBillingDetail obj);

    int update(StuBillingMaster obj);

    int delete(Voucher obj);

    int delete(StuBillingMaster obj);

    int save(BillingDeleteMaster obj, String voucherNo);

    List<Map<String, Object>> getRecord(String sql);

    int delete(String sql);

    String getMsg();

}
