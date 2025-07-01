
package com.ms.ware.online.solution.school.dao.account;

import com.ms.ware.online.solution.school.entity.account.FiscalYear;

import java.util.List;
import java.util.Map;

public interface FiscalYearDao {

     List<FiscalYear> getAll(String hql);

     int save(FiscalYear obj);

     int update(FiscalYear obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
