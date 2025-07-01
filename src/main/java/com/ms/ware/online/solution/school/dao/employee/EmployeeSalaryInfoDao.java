
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.EmployeeSalaryInfo;

import java.util.List;
import java.util.Map;
public interface EmployeeSalaryInfoDao {

     List<EmployeeSalaryInfo> getAll(String hql);

     int save(EmployeeSalaryInfo obj);

     int update(EmployeeSalaryInfo obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
