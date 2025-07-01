
package com.ms.ware.online.solution.school.dao.library;

import com.ms.ware.online.solution.school.entity.library.LibBookType;

import java.util.List;
import java.util.Map;
public interface LibBookTypeDao {

     List<LibBookType> getAll(String hql);

     int save(LibBookType obj);

     int update(LibBookType obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
