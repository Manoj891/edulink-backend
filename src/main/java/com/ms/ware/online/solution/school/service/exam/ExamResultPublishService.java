package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.entity.exam.ExamResultPublish;

public interface ExamResultPublishService {

    public Object getAll();

    public Object save(ExamResultPublish obj);

    public Object update(ExamResultPublish obj);

    public Object delete(String id);

}
