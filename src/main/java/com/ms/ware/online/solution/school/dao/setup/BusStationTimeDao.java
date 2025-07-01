
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.BusStationTime;

import java.util.List;
import java.util.Map;

public interface BusStationTimeDao {

    List<BusStationTime> getAll(String hql);

    int save(BusStationTime obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
