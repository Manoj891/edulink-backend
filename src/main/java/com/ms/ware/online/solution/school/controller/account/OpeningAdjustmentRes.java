package com.ms.ware.online.solution.school.controller.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpeningAdjustmentRes {
    private double opening;
    private double adjusted;
    private String acCode;
    private String acName;
    private String transact;
    private int ca;
}
