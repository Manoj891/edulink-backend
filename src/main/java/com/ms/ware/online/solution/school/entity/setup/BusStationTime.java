package com.ms.ware.online.solution.school.entity.setup;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bus_station_time")
public class BusStationTime implements java.io.Serializable {

    @EmbeddedId
    protected BusStationTimePK pk;
    @Column(name = "BUS", insertable = false, updatable = false)
    private Long bus;
    @Column(name = "STATION", insertable = false, updatable = false)
    private Long station;
    @Column(name = "GO_RETURN", insertable = false, updatable = false)
    private String goReturn;
    @Column(name = "ARRIVAL_TIME", columnDefinition = "TIME")
    private String arrivalTime;
    @Column(name = "DEPARTURE_TIME", columnDefinition = "TIME")
    private String departureTime;
    @JoinColumn(name = "BUS", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BusMaster busMaster;
    @JoinColumn(name = "STATION", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private BusStationMaster busStationMaster;

    public void setPk(BusStationTimePK pk) {
        this.pk = pk;
    }

    public Long getBus() {
        return bus;
    }

    public void setBus(Long bus) {
        this.bus = bus;
    }

    public Long getStation() {
        return station;
    }

    public void setStation(Long station) {
        this.station = station;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    
    public BusStationMaster getBusStationMaster() {
        return busStationMaster;
    }

   

    public String getGoReturn() {
        return goReturn;
    }

    public void setGoReturn(String goReturn) {
        this.goReturn = goReturn;
    }

    @Override
    public String toString() {
        return "\n{\"bus\": \"" + bus + "\",\"station\": \"" + station + "\",\"arrivalTime\": \"" + arrivalTime + "\",\"departureTime\": \"" + departureTime + "\"}";
    }
}
