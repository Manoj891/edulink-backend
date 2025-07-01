package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;

public interface ProgramMasterService {

    public Object getAll();

    public Object save(ProgramMaster obj);

    public Object update(ProgramMaster obj,long id);

    public Object delete(String id);

}