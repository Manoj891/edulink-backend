package com.ms.ware.online.solution.school.service.inventory;

import com.ms.ware.online.solution.school.entity.inventory.InventoryLedger;

public interface InventoryLedgerService {

    public Object getAll();

    public Object save(InventoryLedger obj);

    public Object update(InventoryLedger obj,long id);

    public Object delete(String id);

}