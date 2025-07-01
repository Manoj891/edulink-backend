package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamStudentAttendance {
    private String remark;
    private String presentDays;
    private String symbolNo;
    private String boardRegNo;
    private String examRollNo;
    private String regNo;
}
