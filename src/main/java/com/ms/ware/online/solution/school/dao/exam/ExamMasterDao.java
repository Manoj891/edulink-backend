
package com.ms.ware.online.solution.school.dao.exam;

import com.ms.ware.online.solution.school.entity.exam.ExamMaster;

import java.util.List;
import java.util.Map;

public interface ExamMasterDao {

     List<ExamMaster> getAll(String hql);

     int save(ExamMaster obj);

     int update(ExamMaster obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
