package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamScheduleReq {
    private long examId;
    private long programId;
    private long classId;
    private long subjectId;
    private long subjectGroup;
    private String examDate;
    private String startTime;
    private String endTime;
}
