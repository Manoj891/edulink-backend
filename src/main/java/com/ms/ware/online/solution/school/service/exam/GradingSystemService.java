package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.entity.exam.GradingSystem;

public interface GradingSystemService {

    public Object getAll();

    public Object save(GradingSystem obj);

    public Object update(GradingSystem obj,long id);

    public Object delete(String id);

}