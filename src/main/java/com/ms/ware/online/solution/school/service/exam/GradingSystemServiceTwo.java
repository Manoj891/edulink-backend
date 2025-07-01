package com.ms.ware.online.solution.school.service.exam;


import com.ms.ware.online.solution.school.entity.exam.GradingSystemTwo;

public interface GradingSystemServiceTwo {

    public Object getAll();

    public Object save(GradingSystemTwo obj);

    public Object update(GradingSystemTwo obj,long id);

    public Object delete(String id);

}