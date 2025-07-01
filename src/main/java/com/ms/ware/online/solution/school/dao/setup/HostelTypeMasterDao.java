
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.HostalTypeMaster;

import java.util.List;
import java.util.Map;

public interface HostelTypeMasterDao {

     List<HostalTypeMaster> getAll(String hql);

     int save(HostalTypeMaster obj);

     int update(HostalTypeMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
