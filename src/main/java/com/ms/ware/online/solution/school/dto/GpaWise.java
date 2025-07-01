package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GpaWise {
    private int ng = 0, gpa12 = 0, gpa16 = 0, gpa20 = 0, gpa24 = 0, gpa28 = 0, gpa32 = 0, gpa36 = 0, gpa40 = 0,total=0;
    private String className;

    @Override
    public String toString() {
        return "\n{" +
                "ng=" + ng +
                ", gpa12=" + gpa12 +
                ", gpa16=" + gpa16 +
                ", gpa20=" + gpa20 +
                ", gpa24=" + gpa24 +
                ", gpa28=" + gpa28 +
                ", gpa32=" + gpa32 +
                ", gpa36=" + gpa36 +
                ", gpa40=" + gpa40 +   ", total=" + total +
                ", className=" + className + "}";
    }
}
