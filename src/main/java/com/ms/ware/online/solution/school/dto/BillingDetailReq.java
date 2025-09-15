package com.ms.ware.online.solution.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingDetailReq {
    private Long academicYear;
    private Long program;
    private Long classId;
    private long billId;
    private double amount;
    private String isExtra;
}
