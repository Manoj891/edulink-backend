package com.ms.ware.online.solution.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingDetailReq {
    private String academicYear;
    private String program;
    private String classId;
    private long billId;
    private double amount;
    private String isExtra;
}
