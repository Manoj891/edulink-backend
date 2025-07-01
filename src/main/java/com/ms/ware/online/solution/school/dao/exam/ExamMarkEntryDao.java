
package com.ms.ware.online.solution.school.dao.exam;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.exam.ExamMarkEntry;

public interface ExamMarkEntryDao {

     List<ExamMarkEntry> getAll(String hql);

     int save(ExamMarkEntry obj);

     int update(ExamMarkEntry obj);

     int delete(String sql);

     List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
