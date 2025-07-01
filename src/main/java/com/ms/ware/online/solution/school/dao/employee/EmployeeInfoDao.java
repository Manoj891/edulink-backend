package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpWorkingHour;
import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import com.ms.ware.online.solution.school.entity.employee.OnlineVacancy;

public interface EmployeeInfoDao {

    List<EmployeeInfo> getAll(String hql);

    List<OnlineVacancy> getOnlineVacancy(String hql);

    List<EmpWorkingHour> getEmpWorkingHour(String hql);

    int save(EmpWorkingHour obj);

    int save(EmployeeInfo obj);

    int save(OnlineVacancy obj);

    int update(OnlineVacancy obj);

    int update(EmployeeInfo obj);

    int delete(String sql);

    List<Map<String,Object>> getRecord(String sql);

    String getMsg();
}
