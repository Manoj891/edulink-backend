
package com.ms.ware.online.solution.school.dao.setup;

import java.util.Map;
import java.util.List;

import com.ms.ware.online.solution.school.entity.setup.CastEthnicityMaster;

public interface CastEthnicityMasterDao {

    List<CastEthnicityMaster> getAll(String hql);

    int save(CastEthnicityMaster obj);

    int update(CastEthnicityMaster obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
