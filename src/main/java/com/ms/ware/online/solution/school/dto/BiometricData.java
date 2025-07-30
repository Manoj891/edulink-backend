package com.ms.ware.online.solution.school.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiometricData {
    private int branch;
    private String password;
    private List<BiometricReq> data;
}
