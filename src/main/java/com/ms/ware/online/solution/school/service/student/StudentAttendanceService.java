package com.ms.ware.online.solution.school.service.student;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.student.StudentAttendance;

import java.util.List;
import java.util.Map;

public interface StudentAttendanceService {

    List<Map<String, Object>> getAll(Long acadeicYear, Long program, Long classId, Long subjectGroup, String date);

    ResponseEntity save(List<StudentAttendance> List);

    ResponseEntity update(StudentAttendance obj);

    ResponseEntity delete(String id);

    ResponseEntity saveTeacher(List<StudentAttendance> list);

    List<Map<String, Object>> getAttendance(Long academicYear, Long program, Long classId, Long subjectGroup, String year, String month, String section, String type);
}
