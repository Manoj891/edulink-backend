package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.SchoolClassSession;
import com.ms.ware.online.solution.school.entity.student.SchoolClassSessionBillDate;

import java.util.List;
import java.util.Map;

public interface SchoolClassSessionDao {

     List<SchoolClassSession> getAll(String hql);

     int save(SchoolClassSession obj);

     int save(SchoolClassSessionBillDate obj);

     int update(SchoolClassSession obj);

     int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

     String getMsg();
}
