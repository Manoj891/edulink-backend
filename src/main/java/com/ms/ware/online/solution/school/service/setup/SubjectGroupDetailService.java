package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectGroupDetail;

public interface SubjectGroupDetailService {

    public Object getAll();

    public Object save(SubjectGroupDetail obj);

    public Object update(SubjectGroupDetail obj,long id);

    public Object delete(String id);

}