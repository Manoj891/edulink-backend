
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.MunicipalMaster;

import java.util.List;
import java.util.Map;

public interface MunicipalMasterDao {

     List<MunicipalMaster> getAll(String hql);

     int save(MunicipalMaster obj);

     int update(MunicipalMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
