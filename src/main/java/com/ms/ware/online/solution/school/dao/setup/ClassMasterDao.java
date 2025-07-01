
package com.ms.ware.online.solution.school.dao.setup;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.setup.ClassMaster;

public interface ClassMasterDao {

     List<ClassMaster> getAll(String hql);

     int save(ClassMaster obj);

     int update(ClassMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
