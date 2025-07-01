package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BiometricReq {
    private String id;
    private String time;
    private String date;
    private long userId;

    @Override
    public String toString() {
        return "\n{\"id\":\"" + id + "\",\"time\":\"" + time + "\",\"date\":\"" + date + "\",\"userId\":\"" + userId + "\"}";
    }
}
