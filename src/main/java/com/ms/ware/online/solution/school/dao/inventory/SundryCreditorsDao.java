
package com.ms.ware.online.solution.school.dao.inventory;

import com.ms.ware.online.solution.school.entity.inventory.SundryCreditors;

import java.util.List;
import java.util.Map;

public interface SundryCreditorsDao {

     List<SundryCreditors> getAll(String hql);

     int save(SundryCreditors obj);

     int update(SundryCreditors obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
