package com.ms.ware.online.solution.school.controller.account;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OpeningAdjustment {
    private String acCode;
    private String acName;
    private String transact;
    private String adjustedCredit;
    private String adjustedDebit;
    private String openingDebit;
    private String openingCredit;
    private String balance;
    private int ca;


}
