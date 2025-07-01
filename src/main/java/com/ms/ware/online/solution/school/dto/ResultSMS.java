package com.ms.ware.online.solution.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultSMS {
    private List<Data> data;
    private String examName;
    private String className;

    @Getter
    @Setter
    public static class Data {
        private String grade;
        private String gpa;
        private String percentage;
        private long id;

    }
}
