
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.BusStationMaster;

import java.util.List;
import java.util.Map;

public interface BusStationMasterDao {

    List<BusStationMaster> getAll(String hql);

    int save(BusStationMaster obj);

    int update(BusStationMaster obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
