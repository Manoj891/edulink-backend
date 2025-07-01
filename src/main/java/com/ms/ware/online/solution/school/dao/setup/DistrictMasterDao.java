
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.DistrictMaster;

import java.util.List;
import java.util.Map;

public interface DistrictMasterDao {

     List<DistrictMaster> getAll(String hql);

     int save(DistrictMaster obj);

     int update(DistrictMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
