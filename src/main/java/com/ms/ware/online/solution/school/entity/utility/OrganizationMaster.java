package com.ms.ware.online.solution.school.entity.utility;

import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "organization_master")
@Getter
@Setter
public class OrganizationMaster implements java.io.Serializable {
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;
    @Column(name = "ADDRESS", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String address;

    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "DISTRICT", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String district;
    @Column(name = "MUNICIPAL", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String municipal;
    @Column(name = "WARD_NO", columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String wardNo;
    @Column(name = "ESTABLISH_YEAR")
    private String establishYear;
    @Column(name = "TEL")
    private String tel;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "URL")
    private String url;
    @Column(name = "pan_number")
    private String panNumber;
    @Column(name = "BIOMETRIC_DATA_ID", insertable = false, updatable = false)
    private String biometricDataId;
    @Column(name = "BIOMETRIC_DATA_URL", insertable = false, updatable = false)
    private String biometricDataUrl;

    @Column(name = "SMS_SEND_API", updatable = false, insertable = false)
    private String smsSendApi;
    @Column(name = "SCHOOL_CODE", updatable = false, insertable = false)
    private String schoolCode;
    @Column(name = "STUDENT_FEE_INCOME_ACCOUNT", columnDefinition = "VARCHAR(30)")
    private String studentFeeIncomeAccount;
    @Column(name = "CASH_ACCOUNT", columnDefinition = "VARCHAR(30)")
    private String cashAccount;
    @Column(name = "INVENTORY_ACCOUNT", columnDefinition = "VARCHAR(30)")
    private String inventoryAccount;
    @Column(name = "STUDENT_INVENTORY_ACCOUNT", columnDefinition = "VARCHAR(30)")
    private String studentInventoryAccount;
    @Column(name = "SUNDRY_CREDITORS", columnDefinition = "VARCHAR(30)")
    private String sundryCreditors;
    @Column(name = "SUNDRY_DEBTORS", columnDefinition = "VARCHAR(30)")
    private String sundryDebtors;

    @Column(name = "RESERVES_AND_SURPLUS", columnDefinition = "VARCHAR(30)")
    private String reservesAndSurplus;

    @Column(name = "employee_fund_payable", columnDefinition = "VARCHAR(30)")
    private String employeeFundPayable;

    @Column(name = "cit_payable", columnDefinition = "VARCHAR(30)")
    private String citPayable;

    @Column(name = "pf_payable", columnDefinition = "VARCHAR(30)")
    private String pfPayable;
    @Column(name = "sst_payable", columnDefinition = "VARCHAR(30)")
    private String sstPayable;
    @Column(name = "income_tax_payable", columnDefinition = "VARCHAR(30)")
    private String incomeTaxPayable;

    @Column(name = "salary_expenses", columnDefinition = "VARCHAR(30)")
    private String salaryExpenses;

    @Column(name = "khalti_payment_active", columnDefinition = "VARCHAR(1)", updatable = false, insertable = false)
    private String khaltiPaymentActive;
    @Column(name = "installation_date", columnDefinition = "DATE")
    private String installationDate;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "STUDENT_FEE_INCOME_ACCOUNT", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ChartOfAccount chartofaccountstudentFeeIncomeAccount;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "INVENTORY_ACCOUNT", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ChartOfAccount chartofaccountinventoryAccount;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "STUDENT_FEE_INCOME_ACCOUNT", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ChartOfAccount chartofaccountstudentInventoryAccount;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "SUNDRY_CREDITORS", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ChartOfAccount chartofaccountsundryCreditors;

    @Column(name = "ABOUT_SCHOOL", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String aboutSchool;

    @Column(name = "device_serial_number", length = 15)
    private String deviceSerialNumber;

    @Column(name = "slogan", length = 60)
    private String slogan;
    @Column(name = "bill_bal_total", length = 1)
    private String billBalTotal;

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"municipal\": \"" + municipal + "\",\"district\": \"" + district + "\",\"wardNo\": \"" + wardNo + "\",\"name\": \"" + name + "\",\"address\": \"" + address + "\",\"province\": \"" + province + "\",\"establishYear\": \"" + establishYear + "\",\"tel\": \"" + tel + "\",\"email\": \"" + email + "\",\"url\": \"" + url + "\"}";
    }
}
