
package com.ms.ware.online.solution.school.dao.exam;

import com.ms.ware.online.solution.school.entity.exam.ExamStudentRegistration;

import java.util.List;
import java.util.Map;

public interface ExamStudentRegistrationDao {

     List<ExamStudentRegistration> getAll(String hql);

     int save(ExamStudentRegistration obj);

     int update(ExamStudentRegistration obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
