
package com.ms.ware.online.solution.school.dao.student;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.student.StudentTransportation;
import com.ms.ware.online.solution.school.entity.student.TransportationHostelBillGenerated;

public interface StudentTransportationDao {

    List<StudentTransportation> getAll(String hql);

    int save(StudentTransportation obj);

    int save(TransportationHostelBillGenerated obj);

    int update(StudentTransportation obj);


    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
