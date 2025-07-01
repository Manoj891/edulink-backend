package com.ms.ware.online.solution.school.entity.employee;


import com.ms.ware.online.solution.school.entity.account.FiscalYear;
import com.ms.ware.online.solution.school.entity.account.Voucher;

import java.util.Date;
import javax.persistence.*;


@Entity
@Table(name = "emp_monthly_salary")
public class EmpMonthlySalary {
    public EmpMonthlySalary() {
    }

    public EmpMonthlySalary(EmpMonthlySalaryPK pk) {
        this.pk = pk;
    }

    @Column(name = "EMP_ID", insertable = false, updatable = false)
    private long empId;
    @Column(name = "YEAR", insertable = false, updatable = false)
    private int year;
    @Column(name = "MONTH", insertable = false, updatable = false)
    private int month;
    @EmbeddedId
    private EmpMonthlySalaryPK pk;
    @Column(name = "FISCAL_YEAR")
    private long fiscalYear;

    @Column(name = "FISCAL_YEAR_MONTH")
    private int fiscalYearMonth;
    @Column(name = "BASIC_SALARY")
    private double basicSalary;
    @Column(name = "LEVEL_UPGRADE")
    private double levelUpgrade;
    @Column(name = "GRADE_UPGRADE")
    private double gradeUpgrade;
    @Column(name = "REGULAR_ALLOWANCE")
    private double regularAllowance;
    @Column(name = "NON_REGULAR_ALLOWANCE")
    private double nonRegularAllowance;
    @Column(name = "TOTAL_SALARY")
    private double totalSalary;
    @Column(name = "EMPLOYERS_FUND")
    private double employersFund;
    @Column(name = "EMPLOYERS_PF")
    private double employersPf;
    @Column(name = "EMPLOYERS_CIT")
    private double employersCit;

    @Column(name = "TOTAL_EARNING")
    private double totalEarning;


    @Column(name = "PROJECTING_SALARY")
    private double projectingSalary;

    @Column(name = "PROJECTED_PFCIT")
    private double projectedPFCIT;

    @Column(name = "EMPLOYEES_PF")
    private double employeesPf;
    @Column(name = "EMPLOYEES_CIT")
    private double employeesCit;
    @Column(name = "EMPLOYEES_FUND")
    private double employeesFund;
    @Column(name = "INSURANCE")
    private double insurance;


    @Column(name = "LEAVE_DEDUCTION_DAY")
    private double leaveDeductionDay;
    @Column(name = "LEAVE_DEDUCTION_AMOUNT")
    private double leaveDeductionAmount;
    @Column(name = "OVERTIME_ALLOWANCE")
    private double overtimeAllowance;


    @Column(name = "TAXABLE_SALARY")
    private double taxableSalary;

    @Column(name = "sst_payable")
    private double sstPayable;
    @Column(name = "income_tax_payable")
    private double incomeTaxPayable;

    @Column(name = "NET_PAYABLE")
    private double netPayable;

    @Column(name = "TOTAL_DAY_OF_MONTH", columnDefinition = "INT")
    private int totalDayOfMonth;

    @Column(name = "ENTER_BY", updatable = false)
    private String enterBy;
    @Column(name = "ENTER_DATE", updatable = false)
    @Temporal(TemporalType.DATE)
    private Date enterDate;

    @Column(name = "APPROVE_BY", insertable = false)
    private String approveBy;
    @Column(name = "APPROVE_DATE", insertable = false)
    @Temporal(TemporalType.DATE)
    private Date approveDate;

    @Column(name = "BANK_DEPOSIT_AMOUNT")
    private double bankDepositAmount;
    @Column(name = "BANK_DEPOSIT_DATE", columnDefinition = "DATE DEFAULT NULL")
    @Temporal(TemporalType.DATE)
    private Date bankDepositDate;
    @Column(name = "VOUCHER_NO")
    private String voucherNo;
    @JoinColumn(name = "VOUCHER_NO", referencedColumnName = "VOUCHER_NO", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Voucher voucher;
    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EmployeeInfo employee;
    @JoinColumn(name = "FISCAL_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FiscalYear fy;

    @Override
    public String toString() {
        return "{\"pk\":\"" + pk + "\",\"fiscalYear\":\"" + fiscalYear + "\",\"fiscalYearMonth\":\"" + fiscalYearMonth + "\",\"basicSalary\":\"" + basicSalary
                + "\",\"regularAllowance\":\"" + regularAllowance + "\",\"leaveDeductionDay\":\"" + leaveDeductionDay + "\",\"leaveDeductionAmount\":\"" + leaveDeductionAmount
                + "\",\"nonRegularAllowance\":\"" + nonRegularAllowance + "\",\"employersPf\":\"" + employersPf + "\",\"employeesPf\":\"" + employeesPf
                + "\",\"employersCit\":\"" + employersCit + "\",\"employeesCit\":\"" + employeesCit + "\",\"insurance\":\"" + insurance + "\",\"taxableSalary\":\"" + taxableSalary
                + "\",\"totalDayOfMonth\":\"" + totalDayOfMonth + "\",\"netPayable\":\"" + netPayable + "\",\"enterBy\":\"" + enterBy + "\",\"enterDate\":\"" + enterDate
                + "\",\"approveBy\":\"" + approveBy + "\",\"approveDate\":\"" + approveDate + "\",\"empId\":\"" + empId + "\",\"fy\":\"" + fy + "}";
    }

    public EmpMonthlySalaryPK getPk() {
        return pk;
    }

    public void setPk(EmpMonthlySalaryPK pk) {
        this.pk = pk;
    }

    public long getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(long fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public int getFiscalYearMonth() {
        return fiscalYearMonth;
    }

    public void setFiscalYearMonth(int fiscalYearMonth) {
        this.fiscalYearMonth = fiscalYearMonth;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getLevelUpgrade() {
        return levelUpgrade;
    }

    public void setLevelUpgrade(double levelUpgrade) {
        this.levelUpgrade = levelUpgrade;
    }

    public double getGradeUpgrade() {
        return gradeUpgrade;
    }

    public void setGradeUpgrade(double gradeUpgrade) {
        this.gradeUpgrade = gradeUpgrade;
    }

    public double getRegularAllowance() {
        return regularAllowance;
    }

    public void setRegularAllowance(double regularAllowance) {
        this.regularAllowance = regularAllowance;
    }

    public double getNonRegularAllowance() {
        return nonRegularAllowance;
    }

    public void setNonRegularAllowance(double nonRegularAllowance) {
        this.nonRegularAllowance = nonRegularAllowance;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public double getEmployersFund() {
        return employersFund;
    }

    public void setEmployersFund(double employersFund) {
        this.employersFund = employersFund;
    }

    public double getEmployersPf() {
        return employersPf;
    }

    public void setEmployersPf(double employersPf) {
        this.employersPf = employersPf;
    }

    public double getEmployersCit() {
        return employersCit;
    }

    public void setEmployersCit(double employersCit) {
        this.employersCit = employersCit;
    }


    public double getProjectingSalary() {
        return projectingSalary;
    }

    public void setProjectingSalary(double projectingSalary) {
        this.projectingSalary = projectingSalary;
    }


    public double getProjectedPFCIT() {
        return projectedPFCIT;
    }

    public void setProjectedPFCIT(double projectedPFCIT) {
        this.projectedPFCIT = projectedPFCIT;
    }

    public double getEmployeesPf() {
        return employeesPf;
    }

    public void setEmployeesPf(double employeesPf) {
        this.employeesPf = employeesPf;
    }

    public double getEmployeesCit() {
        return employeesCit;
    }

    public void setEmployeesCit(double employeesCit) {
        this.employeesCit = employeesCit;
    }

    public double getEmployeesFund() {
        return employeesFund;
    }

    public void setEmployeesFund(double employeesFund) {
        this.employeesFund = employeesFund;
    }

    public double getInsurance() {
        return insurance;
    }

    public void setInsurance(double insurance) {
        this.insurance = insurance;
    }

    public double getLeaveDeductionDay() {
        return leaveDeductionDay;
    }

    public void setLeaveDeductionDay(double leaveDeductionDay) {
        this.leaveDeductionDay = leaveDeductionDay;
    }

    public double getLeaveDeductionAmount() {
        return leaveDeductionAmount;
    }

    public void setLeaveDeductionAmount(double leaveDeductionAmount) {
        this.leaveDeductionAmount = leaveDeductionAmount;
    }

    public double getOvertimeAllowance() {
        return overtimeAllowance;
    }

    public void setOvertimeAllowance(double overtimeAllowance) {
        this.overtimeAllowance = overtimeAllowance;
    }

    public double getTaxableSalary() {
        return taxableSalary;
    }

    public void setTaxableSalary(double taxableSalary) {
        this.taxableSalary = taxableSalary;
    }

    public double getSstPayable() {
        return sstPayable;
    }

    public void setSstPayable(double sstPayable) {
        this.sstPayable = sstPayable;
    }

    public double getIncomeTaxPayable() {
        return incomeTaxPayable;
    }

    public void setIncomeTaxPayable(double incomeTaxPayable) {
        this.incomeTaxPayable = incomeTaxPayable;
    }

    public double getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(double netPayable) {
        this.netPayable = netPayable;
    }

    public int getTotalDayOfMonth() {
        return totalDayOfMonth;
    }

    public void setTotalDayOfMonth(int totalDayOfMonth) {
        this.totalDayOfMonth = totalDayOfMonth;
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


    public double getBankDepositAmount() {
        return bankDepositAmount;
    }

    public void setBankDepositAmount(double bankDepositAmount) {
        this.bankDepositAmount = bankDepositAmount;
    }

    public Date getBankDepositDate() {
        return bankDepositDate;
    }

    public void setBankDepositDate(Date bankDepositDate) {
        this.bankDepositDate = bankDepositDate;
    }


    public double getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(double totalEarning) {
        this.totalEarning = totalEarning;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public EmployeeInfo getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeInfo employee) {
        this.employee = employee;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
}
