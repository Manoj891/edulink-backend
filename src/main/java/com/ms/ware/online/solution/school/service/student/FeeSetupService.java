package com.ms.ware.online.solution.school.service.student;

import com.ms.ware.online.solution.school.dto.FeeSetupReq;
import com.ms.ware.online.solution.school.dto.OldStudent;

import java.util.List;
import java.util.Map;

public interface FeeSetupService {

     List<Map<String, Object>> getAll(Long program, Long classId, Long academicYear, Long subjectGroup);

     void save(FeeSetupReq obj);

     Object copy( Long program, Long classId, Long academicYear, Long programTo, Long classIdTo, Long academicYearTo, Long subjectGroup,Long subjectGroupTo);


     void delete(String id);

     Object save(OldStudent jsonData);

}
