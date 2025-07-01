package com.ms.ware.online.solution.school.entity.setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bus_station_master")
public class BusStationMaster implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CHARGE_AMOUNT")
    private Float chargeAmount;
    @Column(name = "EFFECT", columnDefinition = "VARCHAR(1)")
    private String effect;

    public BusStationMaster() {
    }

    public BusStationMaster(Long id) {
        this.id = id;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Float chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"name\": \"" + name + "\",\"chargeAmount\": \"" + chargeAmount + "\"}";
    }
}
