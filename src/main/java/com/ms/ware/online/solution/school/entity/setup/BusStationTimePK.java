/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.setup;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
@Setter
@Getter
public class BusStationTimePK implements Serializable {

    @Column(name = "BUS")
    private Long bus;
    @Column(name = "STATION")
    private Long station;
    @Column(name = "GO_RETURN")
    private String goReturn;

    public BusStationTimePK() {
    }

    public BusStationTimePK(Long bus, Long station, String goReturn) {
        this.bus = bus;
        this.station = station;
        this.goReturn = goReturn;
    }
}
