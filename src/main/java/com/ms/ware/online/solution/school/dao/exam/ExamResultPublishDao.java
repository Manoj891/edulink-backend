
package com.ms.ware.online.solution.school.dao.exam;

import com.ms.ware.online.solution.school.entity.exam.ExamResultPublish;

import java.util.List;
import java.util.Map;
public interface ExamResultPublishDao {

     List<ExamResultPublish> getAll(String hql);

     int save(ExamResultPublish obj);

     int update(ExamResultPublish obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
