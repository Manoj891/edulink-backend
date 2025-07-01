package com.ms.ware.online.solution.school.dao.exam;

import com.ms.ware.online.solution.school.entity.exam.GradingSystem;
import com.ms.ware.online.solution.school.entity.exam.GradingSystemTwo;
import com.ms.ware.online.solution.school.entity.exam.PercentageSystem;

import java.util.List;
import java.util.Map;

public interface GradingSystemDao {

     List<GradingSystem> getAll(String hql);

     int save(GradingSystem obj);

     int update(GradingSystem obj);

     int save(GradingSystemTwo obj);

     int update(GradingSystemTwo obj);

     int save(PercentageSystem obj);

     int update(PercentageSystem obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
