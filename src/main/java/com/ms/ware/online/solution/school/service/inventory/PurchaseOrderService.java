package com.ms.ware.online.solution.school.service.inventory;

import com.ms.ware.online.solution.school.entity.inventory.PurchaseOrder;


public interface PurchaseOrderService {

    public Object getAll();

    public Object save(PurchaseOrder obj);

    public Object update(String jsonData);

    public Object delete(String id);

    public Object getAll(String orderNo);

    public Object getAllReport(Long sundryCreditors, String dateFrom, String dateTo);

    public Object opening(PurchaseOrder obj);

    public Object doReject(String jsonData);

}