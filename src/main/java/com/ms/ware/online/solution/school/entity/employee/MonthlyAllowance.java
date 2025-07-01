/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.employee;

import com.ms.ware.online.solution.school.entity.setup.AllowanceMaster;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author MS
 */
@Entity
@Table(name = "monthly_allowance")
public class MonthlyAllowance implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MonthlyAllowancePK pk;
    @Basic(optional = false)
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "EMP_ID", insertable = false, updatable = false)
    private long empId;
    @Column(name = "YEAR", insertable = false, updatable = false)
    private Integer year;
    @Column(name = "MONTH", insertable = false, updatable = false)
    private Integer month;
    @Column(name = "ALLOWANCE", insertable = false, updatable = false)
    private long allowance;
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private String id;
    @JoinColumn(name = "ALLOWANCE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AllowanceMaster allowanceMaster;

    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeInfo employeeInfo;

    public MonthlyAllowance() {
    }

    public MonthlyAllowancePK getPk() {
        return pk;
    }

    public void setPk(MonthlyAllowancePK pk) {
        this.pk = pk;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public Integer getMonthVal() {
        return month;
    }

    public String getMonth() {
        if (month < 10)
            return "0" + month;
        else
            return "" + month;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" + "amount=" + amount + ", empId=" + empId + ", year=" + year + ", month=" + month + ", allowance=" + allowance + '}';
    }

}
