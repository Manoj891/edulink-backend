package com.ms.ware.online.solution.school.dao.exam;

import com.ms.ware.online.solution.school.entity.exam.ExamSchedule;

import java.util.List;

public interface ExamScheduleDao {

    List<ExamSchedule> getAll(String hql);

    int save(ExamSchedule obj);

    int update(ExamSchedule obj);

    int delete(String sql);

    String getMsg();
}
