package com.ms.ware.online.solution.school.service.employee;

import com.ms.ware.online.solution.school.dto.BiometricReq;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface BiometricDataService {
    List<Map<String, Object>> getAttendance(Long employee, String dateFrom, String dateTo);

    List<Map<String, Object>> getAll(String date);

    List<String> save(HttpServletRequest request, List<BiometricReq> obj);

}
