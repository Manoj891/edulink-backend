package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeSetupReq {
    private long academicYear;
    private long program;
    private long classId;
    private long subjectGroup;
    private long feeId;
    private float amount;
    private int payTime;
    private String feeMonth;
}
