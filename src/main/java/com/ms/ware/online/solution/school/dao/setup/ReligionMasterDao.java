
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.ReligionMaster;

import java.util.List;
import java.util.Map;

public interface ReligionMasterDao {

     List<ReligionMaster> getAll(String hql);

     int save(ReligionMaster obj);

     int update(ReligionMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
