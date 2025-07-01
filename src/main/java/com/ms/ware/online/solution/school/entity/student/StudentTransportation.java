package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.entity.setup.BillMaster;
import com.ms.ware.online.solution.school.entity.setup.BusStationMaster;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import com.ms.ware.online.solution.school.config.DateConverted;

@Getter
@Setter
@Entity
@Table(name = "student_transportation", uniqueConstraints = @UniqueConstraint(columnNames = {"REG_NO", "STATUS"}))
public class StudentTransportation implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "REG_NO", nullable = false)
    private Long regNo;
    @Column(name = "BILL_ID", updatable = false, nullable = false)
    private Long billId;
    @Column(name = "STATION", nullable = false)
    private Long station;
    @Column(name = "STATUS", length = 1, nullable = false)
    private String status;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private java.util.Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private java.util.Date endDate;
    @Column(name = "MONTHLY_CHARGE")
    private Float monthlyCharge;


    @JoinColumn(name = "REG_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private StudentInfo studentInfo;

    @JoinColumn(name = "BILL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BillMaster BillMaster;
    @JoinColumn(name = "STATION", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BusStationMaster busStationMaster;

    public String getStartDate() {
        return DateConverted.adToBs(startDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = DateConverted.bsToAdDate(startDate);
    }

    public String getEndDate() {
        try {
            if (endDate != null) {
                return DateConverted.adToBs(endDate);
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    public void setEndDate(String endDate) {
        try {
            if (endDate.length() == 10) {
                this.endDate = DateConverted.bsToAdDate(endDate);
                return;
            }
        } catch (Exception ignored) {
        }

        this.endDate = null;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"regNo\": \"" + regNo + "\",\"monthlyCharge\": \"" + monthlyCharge + "\",\"status\": \"" + status + "\",\"startDate\": \"" + startDate + "\",\"endDate\": \"" + endDate + "\"}";
    }
}
