/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.employee;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "emp_leave_detail")
public class EmpLeaveDetail implements Serializable {

    @EmbeddedId
    protected EmpLeaveDetailPK pk;

    @Column(name = "LEAVE_DATE", insertable = false, updatable = false)
    private String leaveDate;
    @Column(name = "EMP_ID", insertable = false, updatable = false)
    private Long empId;

    @Column(name = "LEAVE_ID")
    private Long leaveId;
    @Column(name = "PAY_TYPE")
    private String payType;

    @JoinColumn(name = "LEAVE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LeaveApplication empLeave;
    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeInfo employeeInfo;

    public EmpLeaveDetail() {
    }

    public EmpLeaveDetailPK getPk() {
        return pk;
    }

    public void setPk(EmpLeaveDetailPK pk) {
        this.pk = pk;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId) {
        this.leaveId = leaveId;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

}
