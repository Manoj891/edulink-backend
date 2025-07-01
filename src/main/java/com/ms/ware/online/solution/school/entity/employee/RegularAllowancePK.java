/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.employee;

import lombok.EqualsAndHashCode;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author MS
 */
@EqualsAndHashCode
@Embeddable
public class RegularAllowancePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "EMP_ID")
    private long empId;
    @Basic(optional = false)
    @Column(name = "ALLOWANCE")
    private long allowance;

    public RegularAllowancePK() {
    }

    public RegularAllowancePK(long empId, long allowance) {
        this.empId = empId;
        this.allowance = allowance;
    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public long getAllowance() {
        return allowance;
    }

    public void setAllowance(long allowance) {
        this.allowance = allowance;
    }


}
