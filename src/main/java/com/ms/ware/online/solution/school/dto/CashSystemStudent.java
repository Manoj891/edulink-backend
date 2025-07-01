package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashSystemStudent {
    private String examRegNo;
    private String examRollNo;
    private String regNo;
    private String stuName;
    private String section;

    private List<CashSystemMark> mark;
}
