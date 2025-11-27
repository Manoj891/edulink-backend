package com.ms.ware.online.solution.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillingMasterReq {
    private String billDate;
    private long academicYear;
    private long program;
    private long classId;
    private Long subjectGroup;
    private String remark;
    private Long regNo;
    private Double payAmount;
    private String year;
    private String month;
    private String acCode;
    private String studentName;
    private String fathersName;
    private String mobileNo;
    private String address;

    private List<BillingDetailReq> obj;

    public List<BillingDetailReq> getList() {
        return obj;
    }

}