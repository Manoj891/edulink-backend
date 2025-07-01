/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.employee;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@EqualsAndHashCode
@Embeddable
public class EmpLeaveDetailPK implements Serializable {

    @Column(name = "LEAVE_DATE")
    @Temporal(TemporalType.DATE)
    private Date leaveDate;
    @Column(name = "EMP_ID")
    private Long empId;


    public EmpLeaveDetailPK() {
    }

    public EmpLeaveDetailPK(Date leaveDate, Long empId) {
        this.leaveDate = leaveDate;
        this.empId = empId;
  
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    
}
