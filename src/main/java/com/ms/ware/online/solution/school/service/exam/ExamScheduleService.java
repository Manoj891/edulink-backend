package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.dto.ExamScheduleReq;

import java.util.List;
import java.util.Map;

public interface ExamScheduleService {
    List<Map<String, Object>> getAll(long exam);

    void save(ExamScheduleReq req);

    void update(ExamScheduleReq obj, long id);

    void delete(String id);

}