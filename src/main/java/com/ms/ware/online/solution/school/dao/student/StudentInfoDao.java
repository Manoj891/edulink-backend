package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;
import com.ms.ware.online.solution.school.entity.student.*;

import java.util.List;
import java.util.Map;

public interface StudentInfoDao {

    List<StudentInfo> getAll(String hql);

    List<StudentImport> getStudentImport(String hql);

    int save(StudentInfo obj);

    int save(StudentImport obj);
    int update(StudentImport obj);
    int save(ClassTransfer obj);

    int save(StuBillingMaster obj);

    int update(StudentInfo obj);

    int save(StudentHomework obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();

    int save(Annex4bMaster obj);
}
