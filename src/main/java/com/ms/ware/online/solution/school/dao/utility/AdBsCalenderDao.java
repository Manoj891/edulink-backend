
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.AdBsCalender;

import java.util.List;
import java.util.Map;

public interface AdBsCalenderDao {

     List<AdBsCalender> getAll(String hql);

     int save(AdBsCalender obj);

     int update(AdBsCalender obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
