package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.dto.ExamScheduleReq;

import java.util.List;
import java.util.Map;

public interface ExamScheduleService {
    List<Map<String, Object>> getAll(long exam);

    void save(ExamScheduleReq req);

    void update(ExamScheduleReq obj, long id);

    void delete(String id);

    Map<String, Object> getEntranceCard(Long program, Long classId, Long group, Long exam, String section);

    Map<String, Object> getEntranceCard(String regNos, long examId, long program, long classId, long groupId);

}