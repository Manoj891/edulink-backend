package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.entity.student.StudentTransportation;

public interface StudentTransportationService {

     Object getAll(Long academicYear, Long program, Long classId, Long station,Long regNo);

     Object save(StudentTransportation obj);

     Object update(StudentTransportation obj, long id);

     Object delete(String id);

}
