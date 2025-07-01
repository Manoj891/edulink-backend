
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.Notes;

import java.util.List;
import java.util.Map;

public interface NotesDao {

     List<Notes> getAll(String hql);

     int save(Notes obj);

     int update(Notes obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
