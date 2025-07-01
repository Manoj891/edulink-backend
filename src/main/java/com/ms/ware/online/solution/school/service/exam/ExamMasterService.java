package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.entity.exam.ExamMaster;

public interface ExamMasterService {

    public Object getAll();

    public Object save(ExamMaster obj);

    public Object update(ExamMaster obj,long id);

    public Object delete(String id);

}