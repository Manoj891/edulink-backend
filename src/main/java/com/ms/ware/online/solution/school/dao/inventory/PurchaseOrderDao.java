package com.ms.ware.online.solution.school.dao.inventory;

import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrder;
import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrderDetail;

import java.util.List;
import java.util.Map;
public interface PurchaseOrderDao {

     List<PurchaseOrder> getAll(String hql);

     int save(PurchaseOrder obj);

     int save(PurchaseOrderDetail obj);

     int update(PurchaseOrder obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
