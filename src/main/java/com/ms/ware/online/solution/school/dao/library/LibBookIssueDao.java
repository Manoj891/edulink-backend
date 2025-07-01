
package com.ms.ware.online.solution.school.dao.library;

import com.ms.ware.online.solution.school.entity.library.LibBookIssue;

import java.util.List;
import java.util.Map;

public interface LibBookIssueDao {

     List<LibBookIssue> getAll(String hql);

     int save(LibBookIssue obj);

     int update(LibBookIssue obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
