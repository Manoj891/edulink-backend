package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.BillMaster;

public interface BillMasterService {

     Object getAll();

     Object account();

     Object save(BillMaster obj);

     Object update(BillMaster obj, long id);

     Object delete(String id);

    Object getAllRecord();
}
