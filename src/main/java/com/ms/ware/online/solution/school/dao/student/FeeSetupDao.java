package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.FeeSetup;

import java.util.List;
import java.util.Map;
public interface FeeSetupDao {

     List<FeeSetup> getAll(String hql);

     int saveOrUpdate(FeeSetup obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
