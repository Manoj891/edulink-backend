/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.employee;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author MS
 */
@Embeddable
@EqualsAndHashCode
public class MonthlyAllowancePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "EMP_ID")
    private long empId;
    @Basic(optional = false)
    @Column(name = "YEAR")
    private Integer year;
    @Basic(optional = false)
    @Column(name = "MONTH")
    private Integer month;
    @Basic(optional = false)
    @Column(name = "ALLOWANCE")
    private long allowance;

    public MonthlyAllowancePK() {
    }

    public MonthlyAllowancePK(long empId, Integer year, Integer month, long allowance) {
        this.empId = empId;
        this.year = year;
        this.month = month;
        this.allowance = allowance;
    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public long getAllowance() {
        return allowance;
    }

    public void setAllowance(long allowance) {
        this.allowance = allowance;
    }

    
}
