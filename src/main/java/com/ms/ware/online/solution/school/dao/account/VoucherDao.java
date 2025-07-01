package com.ms.ware.online.solution.school.dao.account;

import com.ms.ware.online.solution.school.entity.account.Ledger;
import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.account.VoucherDelete;
import com.ms.ware.online.solution.school.entity.account.VoucherDetail;

public interface VoucherDao {

     List<Voucher> getAll(String hql);

     int save(Voucher obj);

     int save(VoucherDelete obj);

    
     int save(VoucherDetail obj);

     int save(Ledger obj);

     int update(Voucher obj);

     int delete(String sql);

     List<Map<String,Object>> getRecord(String sql);

     String getMsg();
}
