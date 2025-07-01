package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.DistrictMaster;

public interface DistrictMasterService {

    public Object getAll(String province);

    public Object save(DistrictMaster obj);

    public Object update(DistrictMaster obj, long id);

    public Object delete(String id);

}
