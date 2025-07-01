package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.CastEthnicityMaster;


public interface CastEthnicityMasterService {

    public Object getAll();

    public Object save(CastEthnicityMaster obj);

    public Object update(CastEthnicityMaster obj,int id);

    public Object delete(String id);

}