package com.ms.ware.online.solution.school.dao.student;

import com.ms.ware.online.solution.school.entity.student.CertificateData;

import java.util.List;
import java.util.Map;

public interface CertificateDataDao {
    List<CertificateData> getAll(String hql);

    int save(CertificateData obj);

    int update(CertificateData obj);

    int delete(String sql);

    List<Map<String, Object>> getRecord(String sql);

    String getMsg();
}
