package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StuBillingMasterWav {
    private String remark;
    private String date;
    private long regNo;
    private List<StuBillingMasterWavDetail> obj;
}
