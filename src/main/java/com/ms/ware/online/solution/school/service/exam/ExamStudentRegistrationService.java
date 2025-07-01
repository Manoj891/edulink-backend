package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.dto.ExamStudentAttendance;
import com.ms.ware.online.solution.school.dto.ExamStudentRegistrationApprove;

import java.util.List;
import java.util.Map;

public interface ExamStudentRegistrationService {

    Object getRecord(Long program, Long classId, Long exam);

    Object getPending(Long program, Long classId, Long exam);

    Object getApprove(Long program, Long classId, Long exam, String section);

    Object save(String jsonData);

    Object approve(ExamStudentRegistrationApprove jsonData);

    Object delete(String id);

    Object doStudentAttendance(List<ExamStudentAttendance> req);

    List<Map<String, Object>> getEntranceCard(String regNos, long examId);

    Map<String, Object> getRegistration(long examId, long regNo);

    Map<String, Object> updateRegistration(long examId, long regNo, long program, long classId, long groupId);
}
