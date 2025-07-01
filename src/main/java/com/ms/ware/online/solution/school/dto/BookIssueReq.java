package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookIssueReq {
    private String issueDate;
    private String issueFor;
    private String issueForDay;
    private String regNo;
    private String staffId;
    private List<String> bookIds;
}
