
package com.ms.ware.online.solution.school.dao.utility;

import com.ms.ware.online.solution.school.entity.utility.NoticeBoard;

import java.util.List;
import java.util.Map;
public interface NoticeBoardDao {

     Object getAll(String hql);

     int save(NoticeBoard obj);

     int update(NoticeBoard obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
