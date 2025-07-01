package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;

public interface SubjectMasterService {

    public Object getAll();

    public Object save(SubjectMaster obj);

    public Object update(SubjectMaster obj,long id);

    public Object delete(String id);

    public Object getAll(long group, long program, long classId);

}