package com.ms.ware.online.solution.school.service.inventory;

import com.ms.ware.online.solution.school.entity.inventory.SundryCreditors;

public interface SundryCreditorsService {

    public Object getAll();

    public Object save(SundryCreditors obj);

    public Object update(SundryCreditors obj,long id);

    public Object delete(String id);

}