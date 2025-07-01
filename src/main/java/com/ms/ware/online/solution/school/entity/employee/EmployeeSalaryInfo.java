package com.ms.ware.online.solution.school.entity.employee;

import java.util.Date;

import com.ms.ware.online.solution.school.config.DateConverted;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "employee_salary_info")
public class EmployeeSalaryInfo implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "EMP_ID", insertable = false, updatable = false)
    private Long employeeId;
    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EmployeeInfo empId;
    @Column(name = "BASIC_SALARY")
    private Double basicSalary;
    @Column(name = "LEVEL_UPGRADE", columnDefinition = "FLOAT(8,2) default 0")
    private double levelUpgrade;
    @Column(name = "GRADE_UPGRADE", columnDefinition = "FLOAT(8,2) default 0")
    private double gradeUpgrade;
    @Column(name = "PERSONAL_INSURANCE")
    private Double personalInsurance;
    @Column(name = "HEALTH_INSURANCE")
    private Double healthInsurance;
    @Column(name = "CIT")
    private Double cit;
    @Column(name = "CIT_TYPE", columnDefinition = "VARCHAR(1) DEFAULT 'P'")
    private String citType;
    @Column(name = "EMPLOYER_CIT")
    private Double employerCit;
    @Column(name = "CIT_AC_NO")
    private String citAcNo;
    @Column(name = "PF")
    private Double pf;
    @Column(name = "PF_TYPE", columnDefinition = "VARCHAR(1) DEFAULT 'P'")
    private String pfType;
    @Column(name = "EMPLOYER_PF")
    private Double employerPf;
    @Column(name = "PF_AC_NO")
    private String pfAcNo;
    @Column(name = "BANK_NAME")
    private String bankName;
    @Column(name = "ACCOUNT_NO")
    private String accountNo;
    @Column(name = "STATUS")
    private String status = "Y";
    @Column(name = "EFFECTIVE_DATE_FROM", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date effectiveDateFrom;
    @Column(name = "EFFECTIVE_DATE_TO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date effectiveDateTo;
    @Column(name = "EMPLOYEES_FUND", columnDefinition = "Double(7,2) default 0")
    private Double employeesFund;
    @Column(name = "EMPLOYER_EMPLOYEES_FUND", columnDefinition = "Double(7,2) default 0")
    private Double employerEmployeesFund;
    @Column(name = "EMPLOYEES_FUND_TYPE", columnDefinition = "VARCHAR(1) DEFAULT NULL")
    private String employeesFundType;

    public Long getId() {
        return id;
    }

    public String getEffectiveDateFrom() {
        return DateConverted.adToBs(effectiveDateFrom);
    }

    public String getCitType() {
        return citType;
    }

    public void setCitType(String citType) {
        this.citType = citType;
    }

    public String getPfType() {
        return pfType;
    }

    public void setPfType(String pfType) {
        this.pfType = pfType;
    }

    public Date getEffectiveDateFromAd() {
        return effectiveDateFrom;
    }

    public void setEffectiveDateFrom(String effectiveDateFrom) {
        this.effectiveDateFrom = DateConverted.bsToAdDate(effectiveDateFrom);
    }

    public void setEffectiveDateFrom(Date effectiveDateFrom) {
        this.effectiveDateFrom = effectiveDateFrom;
    }

    public String getEffectiveDateTo() {
        try {
            if (effectiveDateTo == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return DateConverted.adToBs(effectiveDateTo);
    }

    public void setEffectiveDateToAd(Date effectiveDateTo) {
        this.effectiveDateTo = effectiveDateTo;
    }

    public void setEffectiveDateTo(String effectiveDateTo) {
        this.effectiveDateTo = DateConverted.bsToAdDate(effectiveDateTo);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getLevelUpgrade() {
        return levelUpgrade;
    }

    public void setLevelUpgrade(float levelUpgrade) {
        this.levelUpgrade = levelUpgrade;
    }

    public Double getGradeUpgrade() {
        return gradeUpgrade;
    }

    public void setGradeUpgrade(float gradeUpgrade) {
        this.gradeUpgrade = gradeUpgrade;
    }

    public Double getPersonalInsurance() {
        if (personalInsurance == null) return 0d;
        return personalInsurance;
    }

    public void setPersonalInsurance(Double personalInsurance) {
        this.personalInsurance = personalInsurance;
    }

    public Double getHealthInsurance() {
        if (healthInsurance == null) return 0d;
        return healthInsurance;
    }

    public void setHealthInsurance(Double healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    public Double getCit() {
        if (cit == null) return 0d;
        return cit;
    }

    public void setCit(Double cit) {
        this.cit = cit;
    }

    public String getCitAcNo() {
        return citAcNo;
    }

    public void setCitAcNo(String citAcNo) {
        this.citAcNo = citAcNo;
    }

    public Double getPf() {
        if (pf == null) return 0d;
        return pf;
    }

    public void setPf(Double pf) {
        this.pf = pf;
    }

    public String getPfAcNo() {
        return pfAcNo;
    }

    public void setPfAcNo(String pfAcNo) {
        this.pfAcNo = pfAcNo;
    }

    public EmployeeInfo getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = new EmployeeInfo(empId);
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getEmployeesFund() {
        if (employeesFund == null) return 0d;
        return employeesFund;
    }

    public void setEmployeesFund(Double employeesFund) {
        this.employeesFund = employeesFund;
    }

    public Double getEmployerCit() {
        if (employerCit == null) return 0d;
        return employerCit;
    }

    public void setEmployerCit(Double employerCit) {
        this.employerCit = employerCit;
    }

    public Double getEmployerPf() {
        if (employerPf == null) return 0d;
        return employerPf;
    }

    public void setEmployerPf(Double employerPf) {
        this.employerPf = employerPf;
    }

    public Double getEmployerEmployeesFund() {
        if (employerEmployeesFund == null) return 0d;
        return employerEmployeesFund;
    }

    public void setEmployerEmployeesFund(Double employerEmployeesFund) {
        this.employerEmployeesFund = employerEmployeesFund;
    }

    public String getEmployeesFundType() {
        return employeesFundType;
    }

    public void setEmployeesFundType(String employeesFundType) {
        this.employeesFundType = employeesFundType;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", empId=" + empId + ", basicSalary=" + basicSalary + ", levelUpgrade=" + levelUpgrade + ", gradeUpgrade=" + gradeUpgrade + ", personalInsurance=" + personalInsurance + ", healthInsurance=" + healthInsurance + ", cit=" + cit + ", citType=" + citType + ", employerCit=" + employerCit + ", citAcNo=" + citAcNo + ", pf=" + pf + ", pfType=" + pfType + ", employerPf=" + employerPf + ", pfAcNo=" + pfAcNo + ", bankName=" + bankName + ", accountNo=" + accountNo + ", status=" + status + ", effectiveDateFrom=" + effectiveDateFrom + ", effectiveDateTo=" + effectiveDateTo + ", employeesFund=" + employeesFund + ", employerEmployeesFund=" + employerEmployeesFund + ", employeesFundType=" + employeesFundType + "}";
    }

}
