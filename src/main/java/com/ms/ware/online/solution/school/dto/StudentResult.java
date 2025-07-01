package com.ms.ware.online.solution.school.dto;

import lombok.Getter;
import lombok.Setter;
import com.ms.ware.online.solution.school.config.DateConverted;

import java.util.List;

@Getter
@Setter
public class StudentResult {

    private long id;
    private Integer absentDays;
    private String address;
    private String district;
    private String dobBs;
    private String examRollNo;
    private String rollNo;
    private String fathersName;
    private String mothersName;
    private String fullMark;
    private String gpa;
    private String grade;
    private List<Mark> mark;
    private String stuName;
    private String regNo;
    private Integer presentDays;
    private String municipal;
    private String province;
    private String remark;
    private String section;
    private String totalObtain;
    private String totalPrMark;
    private String totalThMark;
    private String wardNo;
    private String photo;

    public String getDobAd() {
        try {
            return DateConverted.bsToAd(dobBs);
        } catch (Exception e) {
            return "";
        }
    }

    @Setter
    @Getter
    public static class Mark {

        private String fullMark;
        private String gpa;
        private String grade;
        private String percent;
        private String phMark;
        private String prOm;
        private Float phGPA;
        private String prPercent;
        private String result;
        private String subjectName;
        private String thMark;
        private String thOm;
        private String activity;
        private Float thGPA;
        private String thPercent;
        private Float thPercentage;
        private Float prPercentage;

    }
}
