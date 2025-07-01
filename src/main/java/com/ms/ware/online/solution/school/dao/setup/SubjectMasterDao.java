
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;

import java.util.List;
import java.util.Map;

public interface SubjectMasterDao {

     List<SubjectMaster> getAll(String hql);

     int save(SubjectMaster obj);

     int update(SubjectMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
