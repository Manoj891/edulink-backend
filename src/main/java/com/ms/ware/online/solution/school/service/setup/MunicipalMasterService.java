package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.MunicipalMaster;

public interface MunicipalMasterService {

    public Object getAll(String district);

    public Object save(MunicipalMaster obj);

    public Object update(MunicipalMaster obj, long id);

    public Object delete(String id);

}
