package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.entity.student.CertificateData;

import java.util.List;
import java.util.Map;

public interface CertificateDataService {

    List<CertificateData> getAll(Long year, Long regNo);

    String save(CertificateData obj);

    String update(CertificateData obj, String id);

    String delete(String id);

    Map<String, Object> findOne(String id);

    Map<String, Object> getStudentInfo(String regNo);

    Map<String, Object> getInit();
}
