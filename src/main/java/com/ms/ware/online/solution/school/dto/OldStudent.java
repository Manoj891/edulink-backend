package com.ms.ware.online.solution.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OldStudent {
    private long academicYear;
    private long classId;
    private List<Long> obj;
    private long program;
    private long subjectGroup;

}
