package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.dto.OldStudent;
import com.ms.ware.online.solution.school.entity.student.FeeSetup;

public interface FeeSetupService {

    public Object getAll(Long program, Long classId, Long academicYear,Long subjectGroup);

    public Object save(FeeSetup obj);

    public Object copy( Long program, Long classId, Long academicYear, Long programTo, Long classIdTo, Long academicYearTo, Long subjectGroup,Long subjectGroupTo);

    public Object update(FeeSetup obj, String id);

    public Object delete(String id);

    public Object save(OldStudent jsonData);

}
