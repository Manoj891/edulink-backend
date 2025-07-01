package com.ms.ware.online.solution.school.entity.setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
//import lombok.*;
//
//@Data

@Entity
@Table(name = "bus_master")
public class BusMaster implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "BUS_NO")
    private String busNo;
    @Column(name = "BUS_NAME")
    private String busName;
    @Column(name = "DRIVER_NAME")
    private String driverName;
    @Column(name = "MOBILE_NO")
    private String mobileNo;

    public BusMaster() {
    }

    public BusMaster(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"busNo\": \"" + busNo + "\",\"busName\": \"" + busName + "\",\"driverName\": \"" + driverName + "\",\"mobileNo\": \"" + mobileNo + "\"}";
    }
}
