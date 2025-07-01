package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.MonthlyAllowance;
import com.ms.ware.online.solution.school.entity.employee.RegularAllowance;

import java.util.List;
import java.util.Map;
public interface MonthlyAllowanceDao {

     List<MonthlyAllowance> getAll(String hql);

     int save(MonthlyAllowance obj);

     int save(RegularAllowance obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
