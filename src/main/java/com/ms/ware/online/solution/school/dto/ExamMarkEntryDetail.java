package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamMarkEntryDetail {
    private long examRegId;
    private String examRollNo;
    private long studentRegNo;
    private float thOm;
    private float prOm;
    private String extraActivity;
}
