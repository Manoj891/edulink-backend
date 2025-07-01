package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.dto.ClassTransferReq;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface StudentInfoService {

    Object getAll(Long regNo, Long program, Long classId, Long academicYear, Long subjectGroup, String section);

    Object save(StudentInfo obj);

    Object update(StudentInfo obj, long id);

    Object delete(String id);

    Object getAll(Long academicYear, Long program, Long classId, Long subjectGroup);

    String classTransfer(ClassTransferReq req);

    Object studentReport(Long academicYear, Long program, Long classId, Long subjectGroup, Long castEthnicity, String gender, String section);

    Object save(HttpServletRequest request, long studentId, MultipartFile photo, MultipartFile certificate1, MultipartFile certificate2);

    Object getDropout(String drop, Long program, Long classId, Long academicYear, Long subjectGroup);

    Object doDropout(String jsonData, String status);

}
