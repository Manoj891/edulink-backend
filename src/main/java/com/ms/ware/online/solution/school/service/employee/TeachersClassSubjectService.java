package com.ms.ware.online.solution.school.service.employee;

import org.springframework.http.ResponseEntity;
import com.ms.ware.online.solution.school.entity.employee.TeachersClassSubject;
import java.util.List;

public interface TeachersClassSubjectService {

      List<TeachersClassSubject> getAll(Long academicYear, Long program, Long classId);

     ResponseEntity save(TeachersClassSubject obj);

    ResponseEntity update(TeachersClassSubject obj, String id);

    ResponseEntity<String> delete(String id);

}
