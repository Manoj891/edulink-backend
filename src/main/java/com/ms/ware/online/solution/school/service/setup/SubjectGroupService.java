package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;

public interface SubjectGroupService {

    public Object getAll();

    public Object getAll(long id,Long program, Long classId);

   

    public Object save(SubjectGroup obj);

    public Object update(SubjectGroup obj, long id);

    public Object delete(String id);

}
