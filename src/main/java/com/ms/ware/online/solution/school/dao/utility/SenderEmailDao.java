
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.SenderEmail;

import java.util.List;
import java.util.Map;

public interface SenderEmailDao {

     List<SenderEmail> getAll(String hql);

     int save(SenderEmail obj);

     int update(SenderEmail obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
