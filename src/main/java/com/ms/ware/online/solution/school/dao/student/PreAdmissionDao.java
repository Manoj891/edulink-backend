
package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.PreAdmission;

import java.util.List;
import java.util.Map;

public interface PreAdmissionDao {

     List<PreAdmission> getAll(String hql);

     int save(PreAdmission obj);

     int update(PreAdmission obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
