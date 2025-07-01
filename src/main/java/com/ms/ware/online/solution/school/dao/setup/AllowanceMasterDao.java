
package com.ms.ware.online.solution.school.dao.setup;
import java.util.Map;
import java.util.List;
import com.ms.ware.online.solution.school.entity.setup.AllowanceMaster;

public interface AllowanceMasterDao {

     List<AllowanceMaster> getAll(String hql);

     int save(AllowanceMaster obj);

     int update(AllowanceMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
