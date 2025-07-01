
package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.EmployeeAttendance;
import com.ms.ware.online.solution.school.entity.student.StudentAttendance;
import com.ms.ware.online.solution.school.entity.utility.BiometricLog;

import java.util.List;
import java.util.Map;

public interface BiometricDataDao {

    List<BiometricLog> getAll(String hql);

    int save(BiometricLog obj);

    int save(StudentAttendance obj);

    int save(EmployeeAttendance obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
