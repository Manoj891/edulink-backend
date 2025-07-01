package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.EmpLeaveDetail;
import com.ms.ware.online.solution.school.entity.employee.LeaveApplication;

import java.util.List;
import java.util.Map;

public interface LeaveApplicationDao {

     List<LeaveApplication> getAll(String hql);

     int save(LeaveApplication obj);

     int save(List<EmpLeaveDetail> obj);

     int save(EmpLeaveDetail obj);

     int update(LeaveApplication obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
