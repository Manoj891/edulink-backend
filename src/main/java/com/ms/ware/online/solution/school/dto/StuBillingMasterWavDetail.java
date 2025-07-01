package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StuBillingMasterWavDetail {
    private long academicYear;
    private long classId;
    private long program;
    private long billId;
    private double amount;
}
