package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamMarkEntryReq {
    private long exam;
    private long program;
    private long classId;
    private long subjectGroup;
    private long subject;
    private String enterDate;
    private List<ExamMarkEntryDetail> obj;
}
