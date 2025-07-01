package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.ReligionMaster;

public interface ReligionMasterService {

    public Object getAll();

    public Object save(ReligionMaster obj);

    public Object update(ReligionMaster obj, int id);

    public Object delete(String id);

}
