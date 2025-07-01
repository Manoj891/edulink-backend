package com.ms.ware.online.solution.school.dao.inventory;

import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.inventory.InventoryLedger;

import java.util.List;
import java.util.Map;

public interface InventoryLedgerDao {

     List<InventoryLedger> getAll(String hql);

     int save(InventoryLedger obj);

     int save(Voucher obj);

     int update(InventoryLedger obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
