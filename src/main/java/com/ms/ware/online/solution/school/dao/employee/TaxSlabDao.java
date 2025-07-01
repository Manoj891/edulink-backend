
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.TaxSlab;

import java.util.List;
import java.util.Map;

public interface TaxSlabDao {

     List<TaxSlab> getAll(String hql);

     int save(TaxSlab obj);

     int update(TaxSlab obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
