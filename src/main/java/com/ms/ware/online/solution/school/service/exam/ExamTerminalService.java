package com.ms.ware.online.solution.school.service.exam;

import com.ms.ware.online.solution.school.entity.exam.ExamTerminal;

public interface ExamTerminalService {
    Object getAll(Long academicYear);

    Object getAll();

    Object save(ExamTerminal obj);

    Object update(ExamTerminal obj, long id);

    Object delete(String id);

    Object markPush(long academicYear, long terminalExam, long program, long classId, long finalExam);

    Object getMark(long program, long classId, long finalExam);
}