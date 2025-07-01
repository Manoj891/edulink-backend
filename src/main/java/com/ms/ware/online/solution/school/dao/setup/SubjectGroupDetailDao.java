
package com.ms.ware.online.solution.school.dao.setup;

import com.ms.ware.online.solution.school.entity.setup.SubjectGroupDetail;

import java.util.List;
import java.util.Map;

public interface SubjectGroupDetailDao {

     List<SubjectGroupDetail> getAll(String hql);

     int save(SubjectGroupDetail obj);

     int update(SubjectGroupDetail obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
