package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashSystem {
    private String className;
    private String examName;
    private String publishedDate;
    private List<CashSystemStudent> student;
}
