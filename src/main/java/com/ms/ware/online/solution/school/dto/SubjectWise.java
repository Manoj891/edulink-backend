package com.ms.ware.online.solution.school.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectWise {
    private String subject;
    private int aplus;
    private int a;
    private int bplus;
    private int b;
    private int cplus;
    private int c;
    private int d;
    private int ng;

    public int getTotal() {
        return aplus + a + bplus + b + cplus + c + d + ng;
    }

    @Override
    public String toString() {
        return "\n{\"subject\":\"" + subject + "\",\"aplus\":\"" + aplus + "\",\"a\":\"" + a + "\",\"bplus\":\"" + bplus + "\",\"b\":\"" + b + "\",\"cplus\":\"" + cplus + "\",\"c\":\"" + c + "\",\"d\":\"" + d + "\",\"ng\":\"" + ng + "\"}";
    }
}
