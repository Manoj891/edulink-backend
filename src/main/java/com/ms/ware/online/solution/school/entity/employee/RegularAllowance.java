package com.ms.ware.online.solution.school.entity.employee;

import com.ms.ware.online.solution.school.entity.setup.AllowanceMaster;

import javax.persistence.*;

@Entity
@Table(name = "regular_allowance")
public class RegularAllowance {
    @EmbeddedId
    protected RegularAllowancePK pk;
    @Basic(optional = false)
    @Column(name = "EMP_ID", insertable = false, updatable = false)
    private long empId;
    @Basic(optional = false)
    @Column(name = "ALLOWANCE", insertable = false, updatable = false)
    private long allowance;
    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private String id;
    @JoinColumn(name = "ALLOWANCE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AllowanceMaster allowanceMaster;

    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeInfo employeeInfo;

    public RegularAllowancePK getPk() {
        return pk;
    }

    public void setPk(RegularAllowancePK pk) {
        this.pk = pk;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
