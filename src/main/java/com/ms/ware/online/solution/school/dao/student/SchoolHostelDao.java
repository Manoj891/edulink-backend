
package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.SchoolHostal;

import java.util.List;
import java.util.Map;

public interface SchoolHostelDao {

     List<SchoolHostal> getAll(String hql);

     int save(SchoolHostal obj);

     int update(SchoolHostal obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
