
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;

import java.util.List;
import java.util.Map;

public interface SubjectGroupDao {

     List<SubjectGroup> getAll(String hql);

     int save(SubjectGroup obj);

     int update(SubjectGroup obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
