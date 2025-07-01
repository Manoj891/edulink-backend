package com.ms.ware.online.solution.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingDetailReq {
    private long academicYear;
    private long program;
    private long classId;
    private long billId;
    private double amount;
    private String isExtra;
}
