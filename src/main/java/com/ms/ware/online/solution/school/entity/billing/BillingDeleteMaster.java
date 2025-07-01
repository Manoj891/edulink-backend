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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "billing_delete_master", uniqueConstraints = @UniqueConstraint(columnNames = {"BILL_SN", "FISCAL_YEAR", "BILL_TYPE"}, name = "UNIQUE_BILL_SN"))
public class BillingDeleteMaster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "BILL_NO")
    private String billNo;
    @Column(name = "BILL_SN", nullable = false)
    private Integer billSn;
    @Column(name = "PROGRAM", nullable = false)
    private long program;
    @Column(name = "CLASS_ID", nullable = false)
    private long classId;
    @Column(name = "ACADEMIC_YEAR", nullable = false)
    private long academicYear;
    @Column(name = "FISCAL_YEAR", nullable = false)
    private long fiscalYear;
    @Column(name = "SUBJECT_GROPU", nullable = false)
    private Long subjectGropu;
    @Column(name = "BILL_TYPE", nullable = false, columnDefinition = "VARCHAR(3)")
    private String billType;
    @Column(name = "REG_NO")
    private Long regNo;
    @Column(name = "STUDENT_NAME")
    private String studentName;
    @Column(name = "FATHER_NAME")
    private String fatherName;
    @Column(name = "MOBILE_NO")
    private String mobileNo;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ENTER_BY")
    private String enterBy;
    @Column(name = "ENTER_DATE")
    @Temporal(TemporalType.DATE)
    private Date enterDate;
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
    @JoinColumn(name = "FISCAL_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private FiscalYear fiscalYearClass;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProgramMaster programMaster;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ClassMaster classMaster;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AcademicYear academicYearMaster;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "pk.billNo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BillingDeleteDetail> detail;
    @Column(name = "CREATE_AT", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt = new Date();
    @Column(name = "REFERENCE_ID", nullable = true)
    private String referenceId;
    @Column(name = "DELETE_BY")
    private String deleteBy;
    @Column(name = "DELETE_DATE", columnDefinition = "DATETIME")
    private String deleteDate;
    @Column(name = "REASON", columnDefinition = "TEXT")
    private String reason;

    public BillingDeleteMaster() {
    }

    public BillingDeleteMaster(String billNo) {
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

//    public List<StuBillingDetail> getStuBillingDetailList() {
//        return stuBillingDetailList;
//    }
//
//    public void setStuBillingDetailList(List<StuBillingDetail> stuBillingDetailList) {
//        this.stuBillingDetailList = stuBillingDetailList;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billNo != null ? billNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BillingDeleteMaster)) {
            return false;
        }
        BillingDeleteMaster other = (BillingDeleteMaster) object;
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

    public List<BillingDeleteDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<BillingDeleteDetail> detail) {
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

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "\n{\"billNo\": \"" + billNo + "\",\"billType\": \"" + billType + "\",\"fiscalYear\": \"" + fiscalYear + "\",\"regNo\": \"" + regNo + "\",\"studentName\": \"" + studentName + "\",\"fatherName\": \"" + fatherName + "\",\"mobileNo\": \"" + mobileNo + "\",\"address\": \"" + address + "\",\"enterBy\": \"" + enterBy + "\",\"enterDate\": \"" + enterDate + "\",\"approveBy\": \"" + approveBy + "\",\"approveDate\": \"" + approveDate + "\",\"referenceId\": \"" + referenceId + "\",\"detail\": \"" + detail + "\"}";

    }

}
