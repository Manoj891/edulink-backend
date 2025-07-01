package com.ms.ware.online.solution.school.service.account;

import com.ms.ware.online.solution.school.entity.account.FiscalYear;

public interface FiscalYearService {

    public Object getAll();

    public Object save(FiscalYear obj);

    public Object update(FiscalYear obj,long id);

    public Object delete(String id);

}