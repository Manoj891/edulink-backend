
package com.ms.ware.online.solution.school.dao.setup;

import java.util.Map;
import java.util.List;

import com.ms.ware.online.solution.school.entity.setup.BusMaster;

public interface BusMasterDao {

    List<BusMaster> getAll(String hql);

    int save(BusMaster obj);

    int update(BusMaster obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
