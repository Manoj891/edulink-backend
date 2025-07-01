package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.entity.student.SchoolClassSession;


public interface SchoolClassSessionService {

    public Object getAll(Long academicYear);

    public Object save(SchoolClassSession obj);

    public Object update(SchoolClassSession obj,long id);

    public Object delete(String id);

}