package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationInformationData {
    private String organizationName;
    private String municipal;
    private String wardNo;
    private String street;
    private String tel;
    private String balTotal;
    private String panNumber;
}
