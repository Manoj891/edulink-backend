/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.billing;

import com.ms.ware.online.solution.school.entity.account.FiscalYear;
import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stu_billing_master", uniqueConstraints = @UniqueConstraint(columnNames = {"BILL_SN", "FISCAL_YEAR", "BILL_TYPE"}, name = "UNIQUE_BILL_SN"))
public class StuBillingMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BILL_NO")
    private String billNo;
    @Column(name = "BILL_SN", nullable = false)
    private Integer billSn;
    @Index(columnNames = "index_stu_billing_master_program", name = "program")
    @Column(name = "PROGRAM", nullable = false)
    private long program;
    @Index(columnNames = "index_stu_billing_master_class_id", name = "class_id")
    @Column(name = "CLASS_ID", nullable = false)
    private long classId;
    @Index(columnNames = "index_stu_billing_master_academic_year", name = "academic_year")
    @Column(name = "academic_year", nullable = false)
    private long academicYear;
    @Index(columnNames = "index_stu_billing_master_fiscal_year", name = "fiscal_year")
    @Column(name = "fiscal_year", nullable = false)
    private long fiscalYear;
    @Index(columnNames = "index_stu_billing_master_subject_gropu", name = "subject_gropu")
    @Column(name = "subject_gropu", nullable = false)
    private Long subjectGropu;
    @Index(columnNames = "index_stu_billing_master_bill_type", name = "bill_type")
    @Column(name = "bill_type", nullable = false, columnDefinition = "VARCHAR(3)")
    private String billType;
    @Index(columnNames = "index_stu_billing_master_reg_no", name = "reg_no")
    @Column(name = "reg_no", nullable = true)
    private Long regNo;
    @Column(name = "STUDENT_NAME", nullable = true)
    private String studentName;
    @Column(name = "FATHER_NAME", nullable = true)
    private String fatherName;
    @Column(name = "MOBILE_NO", nullable = true)
    private String mobileNo;
    @Column(name = "ADDRESS", nullable = true)
    private String address;
    @Column(name = "ENTER_BY")
    private String enterBy;
    @Index(columnNames = "index_stu_billing_master_enter_date", name = "enter_date")
    @Column(name = "enter_date")
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Index(columnNames = "index_stu_billing_master_payment_till", name = "payment_till")
    @Column(name = "payment_till")
    @Temporal(TemporalType.DATE)
    private Date paymentTill;
    @Column(name = "APPROVE_BY", nullable = true)
    private String approveBy;
    @Column(name = "APPROVE_DATE", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date approveDate;
    @Column(name = "BILL_AMOUNT", nullable = true)
    private double billAmount;
    @Column(name = "AUTO_GENERATE", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String autoGenerate;
    @Column(name = "REMARK", nullable = true)
    private String remark;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "FISCAL_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private FiscalYear fiscalYearClass;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProgramMaster programMaster;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ClassMaster classMaster;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AcademicYear AcademicYearMaster;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "pk.billNo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StuBillingDetail> detail;
    @Column(name = "CREATE_AT", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt = new Date();
    @Column(name = "REFERENCE_ID", nullable = true)
    private String referenceId;


    public StuBillingMaster(String billNo) {
        this.billNo = billNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public Long getRegNo() {
        return regNo;
    }

    public void setRegNo(Long regNo) {
        this.regNo = regNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billNo != null ? billNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StuBillingMaster)) {
            return false;
        }
        StuBillingMaster other = (StuBillingMaster) object;
        if ((this.billNo == null && other.billNo != null) || (this.billNo != null && !this.billNo.equals(other.billNo))) {
            return false;
        }
        return true;
    }

    public Integer getBillSn() {
        return billSn;
    }

    public void setBillSn(Integer billSn) {
        this.billSn = billSn;
    }

    public long getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(long fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(String autoGenerate) {
        this.autoGenerate = autoGenerate;
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

    public Long getSubjectGropu() {
        return subjectGropu;
    }

    public void setSubjectGropu(Long subjectGropu) {
        this.subjectGropu = subjectGropu;
    }

    public List<StuBillingDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<StuBillingDetail> detail) {
        this.detail = detail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public String toString() {
        return "\n{\"billNo\": \"" + billNo + "\",\"billType\": \"" + billType + "\",\"fiscalYear\": \"" + fiscalYear + "\",\"regNo\": \"" + regNo + "\",\"studentName\": \"" + studentName + "\",\"fatherName\": \"" + fatherName + "\",\"mobileNo\": \"" + mobileNo + "\",\"address\": \"" + address + "\",\"enterBy\": \"" + enterBy + "\",\"enterDate\": \"" + enterDate + "\",\"approveBy\": \"" + approveBy + "\",\"approveDate\": \"" + approveDate + "\",\"referenceId\": \"" + referenceId + "\",\"detail\": \"" + detail + "\"}";

    }

}
