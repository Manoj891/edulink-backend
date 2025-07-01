package com.ms.ware.online.solution.school.entity.employee;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "leave_application")
public class LeaveApplication implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @NotNull
    @Column(name = "EMP_ID", nullable = false)
    private Long empId;
    @Column(name = "LEAVE_DATE_FROM", nullable = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date leaveDateFrom;
    @Column(name = "LEAVE_DATE_TO", nullable = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date leaveDateTo;
    @Column(name = "REASONS")
    private String reasons;

    @Column(name = "STATUS", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String status;
  @Column(name = "ENTER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enterDate;
    @Column(name = "ENTER_By")
    private String enterBy;

    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approveDate;
    @Column(name = "APPROVE_BY")
    private String approveBy;
    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private EmployeeInfo employeeInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getLeaveDateFrom() {
        return DateConverted.adToBs(leaveDateFrom);
    }

    public Date getLeaveDateFromAd() {
        return leaveDateFrom;
    }

    public void setLeaveDateFrom(String leaveDateFrom) {
        this.leaveDateFrom = DateConverted.bsToAdDate(leaveDateFrom);
    }

    public String getLeaveDateTo() {
        return DateConverted.adToBs(leaveDateTo);
    }

    public Date getLeaveDateToAd() {
        return leaveDateTo;
    }

    public void setLeaveDateTo(String leaveDateTo) {
        this.leaveDateTo = DateConverted.bsToAdDate(leaveDateTo);
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }

    public EmployeeInfo getEmployeeInfo() {
        return employeeInfo;
    }

    public void setEmployeeInfo(EmployeeInfo employeeInfo) {
        this.employeeInfo = employeeInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"empId\": \"" + empId + "\",\"leaveDateFrom\": \"" + leaveDateFrom + "\",\"leaveDateTo\": \"" + leaveDateTo + "\",\"reasons\": \"" + reasons + "\"}";
    }
}
