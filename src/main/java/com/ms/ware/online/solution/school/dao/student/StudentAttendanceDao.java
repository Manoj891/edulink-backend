
package com.ms.ware.online.solution.school.dao.student;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.student.StudentAttendance;

public interface StudentAttendanceDao {

     List<StudentAttendance> getAll(String hql);

     int save(StudentAttendance obj);

     int update(StudentAttendance obj);

     int delete(String sql);

     List<Map<String,Object>> getRecord(String sql);

     String getMsg();
}
