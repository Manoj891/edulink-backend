
package com.ms.ware.online.solution.school.dao.setup;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.Section;

public interface AcademicYearDao {

    List<AcademicYear> getAll(String hql);

    int save(AcademicYear obj);

    int save(Section obj);

    int update(AcademicYear obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
