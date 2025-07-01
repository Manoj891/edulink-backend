package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.entity.setup.BillMaster;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
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
import javax.persistence.UniqueConstraint;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "pre_admission", uniqueConstraints = @UniqueConstraint(columnNames = {"STU_NAME", "FATHER_NAME", "MOTHER_NAME", "DATE_OF_BIRTH", "PROVINCE", "DISTRICT", "MUNICIPAL"}))
public class PreAdmission  {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "STU_NAME", nullable = false)
    private String stuName;
    @Column(name = "FATHER_NAME", nullable = false)
    private String fatherName;
    @Column(name = "MOTHER_NAME", nullable = false)
    private String motherName;
    @Column(name = "DATE_OF_BIRTH", columnDefinition = "VARCHAR(15)", nullable = false)
    private String dateOfBirth;

    @Column(name = "ENTER_DATE", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Column(name = "ENTER_BY", nullable = false, updatable = false)

    private String enterBy;
    @Column(name = "PROVINCE", nullable = false)
    private Integer province;
    @Column(name = "DISTRICT", nullable = false)
    private String district;
    @Column(name = "MUNICIPAL", nullable = false)
    private String municipal;
    @Column(name = "WARD_NO")
    private String wardNo;
    @Column(name = "TOL")
    private String tol;
    @Column(name = "MOBILE_NO", nullable = false)
    private String mobileNo;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "STATUS", updatable = false, columnDefinition = "VARCHAR(1)")
    private String status;
    @Column(name = "BILL_AMOUNT", updatable = false)
    private Float billAmount;
    @Column(name = "BILL_ID", updatable = false)
    private Long billId;
    @Column(name = "BILL_NO")
    private String billNo;
    @Column(name = "PROGRAM")
    private Long program;
    @Column(name = "CLASS_ID")
    private Long classId;
    @Column(name = "ACADEMIC_YEAR")
    private Long academicYear;
    @Column(name = "SUBJECT_GROUP")
    private Long subjectGroup;

    @JoinColumn(name = "BILL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BillMaster feeId;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramMaster programMaster;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ClassMaster classMaster;

    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubjectGroup group;

    public Long getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Long academicYear) {
        this.academicYear = academicYear;
    }

    public Long getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = program;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMunicipal() {
        return municipal;
    }

    public void setMunicipal(String municipal) {
        this.municipal = municipal;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Float billAmount) {
        this.billAmount = billAmount;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getEnterDate() {
        return DateConverted.adToBs(enterDate);
    }

    public Date getEnterDateAd() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = DateConverted.bsToAdDate(enterDate);
    }

    public String getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"stuName\": \"" + stuName + "\",\"fatherName\": \"" + fatherName + "\",\"motherName\": \"" + motherName + "\",\"dateOfBirth\": \"" + dateOfBirth + "\",\"province\": \"" + province + "\",\"district\": \"" + district + "\",\"municipal\": \"" + municipal + "\",\"wardNo\": \"" + wardNo + "\",\"tol\": \"" + tol + "\",\"mobileNo\": \"" + mobileNo + "\",\"email\": \"" + email + "\",\"status\": \"" + status + "\",\"billAmount\": \"" + billAmount + "\",\"billId\": \"" + billId + "\"}";
    }
}
