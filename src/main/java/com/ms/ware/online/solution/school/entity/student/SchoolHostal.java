package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.entity.setup.BillMaster;
import com.ms.ware.online.solution.school.entity.setup.HostalTypeMaster;
import lombok.Getter;
import lombok.Setter;
import com.ms.ware.online.solution.school.config.DateConverted;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "school_hostal")
public class SchoolHostal implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private java.util.Date endDate;
    @Column(name = "MONTHLY_CHARGE")
    private Float monthlyCharge;
    @Column(name = "REG_NO")
    private Long regNo;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private java.util.Date startDate;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "BILL_ID", updatable = false)
    private Long billId = (-2L);
    @Column(name = "HOSTEL_TYPE")
    private Long hostelType;

    @JoinColumn(name = "REG_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private StudentInfo studentInfo;

    @JoinColumn(name = "BILL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BillMaster BillMaster;

    @JoinColumn(name = "HOSTEL_TYPE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private HostalTypeMaster hostalTypeMaster;

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
        } catch (Exception e) {
        }
        return "";
    }

    public void setEndDate(String endDate) {
        try {
            if (endDate.length() == 10) {
                this.endDate = DateConverted.bsToAdDate(endDate);
                return;
            }
        } catch (Exception e) {
        }

        this.endDate = null;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"endDate\": \"" + endDate + "\",\"monthlyCharge\": \"" + monthlyCharge + "\",\"regNo\": \"" + regNo + "\",\"startDate\": \"" + startDate + "\",\"status\": \"" + status + "\",\"billId\": \"" + billId + "\"}";
    }
}
