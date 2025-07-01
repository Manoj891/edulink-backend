
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;

import java.util.List;
import java.util.Map;
public interface ProgramMasterDao {

     List<ProgramMaster> getAll(String hql);

     int save(ProgramMaster obj);

     int update(ProgramMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
