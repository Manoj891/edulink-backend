/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.billing;

import com.ms.ware.online.solution.school.entity.setup.BillMaster;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ms.ware.online.solution.school.entity.student.StudentInfo;
import lombok.*;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.hibernate.annotations.Index;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stu_billing_detail")
public class StuBillingDetail implements Serializable {

    @EmbeddedId
    protected StuBillingDetailPK pk;
    @Column(name = "REG_NO")
    private Long regNo;
    @Index(columnNames = "index_stu_billing_detail_academic_year", name = "academic_year")
    @Column(name = "academic_year")
    private long academicYear;
    @Index(columnNames = "index_stu_billing_detail_program", name = "program")
    @Column(name = "program")
    private long program;
    @Index(columnNames = "index_stu_billing_detail_class_id", name = "class_id")
    @Column(name = "class_id")
    private long classId;
    @Column(name = "BILL_ID")
    private long billId;
    @Column(name = "DR")
    private double dr;
    @Column(name = "CR")
    private double cr;
    @Index(columnNames = "index_stu_billing_detail_payment_date", name = "payment_date")
    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "IS_EXTRA", columnDefinition = "VARCHAR(1)")
    private String isExtra;
    @Column(name = "INVENTORY_ISSUE", columnDefinition = "VARCHAR(1)")
    private String inventoryIssue;
    @Column(name = "INVENTORY_ISSUE_BY", columnDefinition = "VARCHAR(30)")
    private String inventoryIssueBy;
    @Column(name = "INVENTORY_ISSUE_DATE", columnDefinition = "VARCHAR(10)")
    private String inventoryIssueDate;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "BILL_NO", referencedColumnName = "BILL_NO", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StuBillingMaster billingMaster;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "BILL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BillMaster billMaster;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "REG_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentInfo studentInfo;


    public StuBillingDetail(String billNo, int billSn, Long regNo, long academicYear, long program, long classId, long billId, double dr, double cr, String paymentDate, String isExtra) {
        this.pk = new StuBillingDetailPK(billNo, billSn);
        this.regNo = regNo;
        this.academicYear = academicYear;
        this.program = program;
        this.classId = classId;
        this.billId = billId;
        this.dr = dr;
        this.cr = cr;
        this.paymentDate = DateConverted.toDate(paymentDate);
        this.isExtra = isExtra;
    }

    public StuBillingDetail(String billNo, int billSn, Long regNo, long academicYear, long program, long classId, long billId, double dr, double cr, Date paymentDate, String isExtra) {
        this.pk = new StuBillingDetailPK(billNo, billSn);
        this.regNo = regNo;
        this.academicYear = academicYear;
        this.program = program;
        this.classId = classId;
        this.billId = billId;
        this.dr = dr;
        this.cr = cr;
        this.paymentDate = paymentDate;
        this.isExtra = isExtra;
    }

    public void setPk(StuBillingDetailPK pk) {
        this.pk = pk;
    }

    public Long getRegNo() {
        return regNo;
    }

    public void setRegNo(Long regNo) {
        this.regNo = regNo;
    }

    public long getProgram() {
        return program;
    }

    public void setProgram(long program) {
        this.program = program;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(long academicYear) {
        this.academicYear = academicYear;
    }

    public double getDr() {
        return dr;
    }

    public void setDr(double dr) {
        this.dr = dr;
    }

    public double getCr() {
        return cr;
    }

    public void setCr(double cr) {
        this.cr = cr;
    }

    public int getBillSn() {
        return pk.getBillSn();
    }

    public long getBillIds() {
        return billId;
    }

    public String getBillId() {
        return pk.getBillNo();
    }

    public StuBillingDetailPK getPk() {
        return pk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.pk);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StuBillingDetail other = (StuBillingDetail) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getIsExtra() {
        return isExtra;
    }

    public void setIsExtra(String isExtra) {
        this.isExtra = isExtra;
    }

    public String getInventoryIssue() {
        return inventoryIssue;
    }

    public void setInventoryIssue(String inventoryIssue) {
        this.inventoryIssue = inventoryIssue;
    }

    public String getInventoryIssueBy() {
        return inventoryIssueBy;
    }

    public void setInventoryIssueBy(String inventoryIssueBy) {
        this.inventoryIssueBy = inventoryIssueBy;
    }

    public String getInventoryIssueDate() {
        return inventoryIssueDate;
    }

    public void setInventoryIssueDate(String inventoryIssueDate) {
        this.inventoryIssueDate = inventoryIssueDate;
    }

    @Override
    public String toString() {
        return "\n{\"regNo\":\"" + regNo + "\",\"academicYear\":\"" + academicYear + "\",\"program\":\"" + program + "\",\"classId\":\"" + classId + "\",\"billId\":\"" + billId + "\",\"dr\":\"" + dr + "\",\"cr\":\"" + cr + "\",\"paymentDate\":\"" + paymentDate + "\",\"isExtra\":\"" + isExtra + "\",\"billId\":\"" + billId + "\",\"billSn\":\"" + pk.getBillSn() + "\"}";
    }

}
