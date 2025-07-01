package com.ms.ware.online.solution.school.entity.account;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "cash_bill")
public class CashBill implements Serializable {

    @Id
    @Column(name = "BILL_NO")
    private String billNo;
    @Column(name = "BILL_SN")
    private Integer billSn;
    @Column(name = "FISCAL_YEAR")
    private Long fiscalYear;
    @Column(name = "CUSTOMER_TYPE")
    private String customerType;
    @Column(name = "CUSTOMER_NAME", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String customerName;
    @Column(name = "MOBILE_NO", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String mobileNo;
    @Column(name = "ADDRESS", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String address;
    @Column(name = "BILL_AMOUNT")
    private Double billAmount;
    @Column(name = "CASH_TRANSACTION_TYPE")
    private String cashTransactionType;
    @Column(name = "CASH_TRANSACTION_NO")
    private String cashTransactionNo;
    @Column(name = "ENTER_BY")
    private String enterBy;
    @Column(name = "APPROVE_BY")
    private String approveBy;

    @Column(name = "ENTER_DATE", columnDefinition = "DATE NOT NULL")
    private String enterDate;
    @Column(name = "APPROVE_DATE", columnDefinition = "DATE DEFAULT NULL")
    private String approveDate;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cashBill", fetch = FetchType.EAGER)
    private List<CashBillDetail> detail;

    public CashBill() {
    }

    public CashBill(String billNo) {
        this.billNo = billNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getBillSn() {
        return billSn;
    }

    public void setBillSn(Integer billSn) {
        this.billSn = billSn;
    }

    public Long getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Long fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getEnterDate() {
        return DateConverted.adToBs(enterDate);
    }

    public String getEnterDateAd() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = DateConverted.bsToAd(enterDate);
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public List<CashBillDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<CashBillDetail> detail) {
        this.detail = detail;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCashTransactionType() {
        return cashTransactionType;
    }

    public void setCashTransactionType(String cashTransactionType) {
        this.cashTransactionType = cashTransactionType;
    }

    public String getCashTransactionNo() {
        return cashTransactionNo;
    }

    public void setCashTransactionNo(String cashTransactionNo) {
        this.cashTransactionNo = cashTransactionNo;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    @Override
    public String toString() {
        return "{" + "billNo=" + billNo + ", billSn=" + billSn + ", fiscalYear=" + fiscalYear + ", customerName=" + customerName + ", mobileNo=" + mobileNo + ", address=" + address + ", enterBy=" + enterBy + ", approveBy=" + approveBy + ", enterDate=" + enterDate + ", approveDate=" + approveDate + ", detail=" + detail + '}';
    }

}
