package com.ms.ware.online.solution.school.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostHostelTransportation {
    private long id;
    private long group;
    private String name;
    private double hostelAmount;
    private double transportationAmount;

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\",\"name\":\"" + name + "\",\"hostelAmount\":\"" + hostelAmount + "\",\"transportationAmount\":\"" + transportationAmount + "\"}\n";
    }
}
