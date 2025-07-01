package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.entity.exam.PercentageSystem;


public interface PercentageSystemService {

    public Object getAll();

    public Object save(PercentageSystem obj);

    public Object update(PercentageSystem obj,long id);

    public Object delete(String id);

}