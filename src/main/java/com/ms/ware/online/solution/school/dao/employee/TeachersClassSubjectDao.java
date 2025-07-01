package com.ms.ware.online.solution.school.dao.employee;

import com.ms.ware.online.solution.school.entity.employee.TeachersClassSubject;

import java.util.List;
import java.util.Map;

public interface TeachersClassSubjectDao {

    
     List<TeachersClassSubject> getAll(String hql);

     int save(TeachersClassSubject obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
