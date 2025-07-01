
package com.ms.ware.online.solution.school.dao.exam;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.exam.ExamTerminal;

public interface ExamTerminalDao {

     List<ExamTerminal> getAll(String hql);

     int save(ExamTerminal obj);

     int update(ExamTerminal obj);

     int delete(String sql);

     List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
