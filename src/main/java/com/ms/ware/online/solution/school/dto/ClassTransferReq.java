package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassTransferReq {
    private long program;
    private long classId;
    private long subjectGroup;
    private long academicYear;
    private List<Detail> obj;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Detail {
        private long regNo;
        private int rollNo;

    }
}
