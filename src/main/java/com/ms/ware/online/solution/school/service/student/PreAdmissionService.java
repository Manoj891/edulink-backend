package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.entity.student.PreAdmission;


public interface PreAdmissionService {

    public Object getAll(Long academicYear,Long program,Long classId);

    public Object save(PreAdmission obj);

    public Object update(PreAdmission obj,long id);

    public Object delete(String id);

    public Object getRecord(long id);

}