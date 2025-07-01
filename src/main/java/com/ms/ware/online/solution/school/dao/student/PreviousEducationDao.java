
package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.PreviousEducation;

import java.util.List;
import java.util.Map;
public interface PreviousEducationDao {

     List<PreviousEducation> getAll(String hql);

     int save(PreviousEducation obj);

 

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
