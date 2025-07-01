package com.ms.ware.online.solution.school.controller.account;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TailBalanceRes {
    private String acCode;
    private String acName;
    private String transact;

    private double credit;
    private double debit;
    private double opening;
    private int ca;

    @Override
    public String toString() {
        return "{" +
                "acCode='" + acCode +
                ", acName='" + acName +
                ", ca=" + ca +
                ", credit=" + credit +
                ", debit=" + debit +
                ", opening=" + opening +
                ", transact='" + transact +
                '}';
    }
}
