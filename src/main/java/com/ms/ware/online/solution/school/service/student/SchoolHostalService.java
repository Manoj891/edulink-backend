package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.entity.student.SchoolHostal;

public interface SchoolHostalService {

     Object getAll(Long regNo,Long academicYear, Long program, Long classId, Long hostelType);

     Object save(SchoolHostal obj);

     Object update(SchoolHostal obj, long id);

     Object delete(String id);

}
