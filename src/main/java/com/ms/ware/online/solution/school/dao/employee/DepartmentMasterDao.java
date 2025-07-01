
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.DepartmentMaster;

import java.util.List;
import java.util.Map;
public interface DepartmentMasterDao {

     List<DepartmentMaster> getAll(String hql);

     int save(DepartmentMaster obj);

     int update(DepartmentMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
