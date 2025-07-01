package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradingSystemReq {
    private float gpa;
    private String grade;
    private float rangFrom;
    private String remark;

}
