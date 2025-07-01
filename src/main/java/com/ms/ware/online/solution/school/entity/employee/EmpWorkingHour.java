package com.ms.ware.online.solution.school.entity.employee;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "emp_working_hour")
public class EmpWorkingHour implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmpWorkingHourPK pk;
    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EmployeeInfo employeeInfo;
    @Column(name = "EMP_ID", insertable = false, updatable = false)
    private Long empId;
    @Column(name = "WORKING_DAY", insertable = false, updatable = false)
    private String workingDay;
    @Column(name = "IN_TIME", columnDefinition = "VARCHAR(12)")
    private String inTime;
    @Column(name = "OUT_TIME", columnDefinition = "VARCHAR(12)")
    private String outTime;
    @Column(name = "LATE_IN_TIME", columnDefinition = "VARCHAR(12)")
    private String lateInTime;
    @Column(name = "LATE_OUT_TIME", columnDefinition = "VARCHAR(12)")
    private String lateOutTime;

    public void setPk(EmpWorkingHourPK pk) {
        this.pk = pk;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getLateInTime() {
        return lateInTime;
    }

    public void setLateInTime(String lateInTime) {
        this.lateInTime = lateInTime;
    }

    public String getLateOutTime() {
        return lateOutTime;
    }

    public void setLateOutTime(String lateOutTime) {
        this.lateOutTime = lateOutTime;
    }

}
