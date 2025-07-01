package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class RollNumberUpdate {
    private long program;
    private long classId;
    private Long subjectGroup;
    private long academicYear;
    private String section;
    private List<RollNumberUpdateBuilder> obj;

    @Getter
    @Setter
    public static class RollNumberUpdateBuilder {
        private long regNo;
        private int rollNo;
    }
}
