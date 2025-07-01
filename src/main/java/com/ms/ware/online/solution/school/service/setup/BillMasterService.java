package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.BillMaster;

public interface BillMasterService {

    public Object getAll();

    public Object account();

    public Object save(BillMaster obj);

    public Object update(BillMaster obj, long id);

    public Object delete(String id);

}
