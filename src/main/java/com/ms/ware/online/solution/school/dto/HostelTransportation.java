package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HostelTransportation {
    private long academicYear;
    private long classId;
    private long program;
    private long year;
    private String month;
    private List<Integer> monthData;
    private List<PostHostelTransportation> detail;

    @Override
    public String toString() {
        return "HostelTransportation{" +
                "academicYear=" + academicYear +
                ", classId=" + classId +
                ", program=" + program +
                ", year=" + year +
                ", month='" + month + '\'' +
                ", detail=" + detail +
                '}';
    }
}
