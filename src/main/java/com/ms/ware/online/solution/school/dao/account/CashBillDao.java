package com.ms.ware.online.solution.school.dao.account;

import com.ms.ware.online.solution.school.entity.account.CashBill;
import com.ms.ware.online.solution.school.entity.account.CashBillDetail;

import java.util.List;
import java.util.Map;

public interface CashBillDao {

     List<CashBill> getAll(String hql);

     int save(CashBill obj);

     int save(CashBillDetail obj);

     int update(CashBill obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
