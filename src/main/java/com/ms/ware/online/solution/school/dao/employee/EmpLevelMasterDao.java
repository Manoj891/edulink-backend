
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpLevelMaster;

import java.util.List;
import java.util.Map;

public interface EmpLevelMasterDao {

     List<EmpLevelMaster> getAll(String hql);

     int save(EmpLevelMaster obj);

     int update(EmpLevelMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
