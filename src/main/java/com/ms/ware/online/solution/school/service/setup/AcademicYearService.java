package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.Section;

public interface AcademicYearService {

    Object getAll();

    Object save(AcademicYear obj);

    Object save(Section obj);


    Object update(AcademicYear obj, long id);

    Object delete(String id);

    Object getSection();

    Object deleteSection(String id);
}