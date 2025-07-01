/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cash_bill_detail")
public class CashBillDetail implements java.io.Serializable {

    @Id
    @Column(name = "ID", length = 35)
    private String id;
    @Column(name = "BILL_NO", length = 30)
    private String billNo;
    @Column(name = "BILL_SN")
    private Integer billSn;
    @Column(name = "AC_CODE", length = 30, nullable = false)
    private String acCode;
    @Column(name = "PARTICULAR", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String particular;
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;
    @JoinColumn(name = "AC_CODE", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ChartOfAccount chartOfAccount;

    @JoinColumn(name = "BILL_NO", referencedColumnName = "BILL_NO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CashBill cashBill;

    public CashBillDetail() {
    }

    public CashBillDetail(String billNo, Integer billSn, String acCode, String particular, Double amount) {
        this.billNo = billNo;
        this.billSn = billSn;
        this.acCode = acCode;
        this.particular = particular;
        this.amount = amount;     
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ChartOfAccount getChartOfAccount() {
        return chartOfAccount;
    }

    public void setChartOfAccount(ChartOfAccount chartOfAccount) {
        this.chartOfAccount = chartOfAccount;
    }
    
    @Override
    public String toString() {
        return "\n{" + "id=" + id + ", billNo=" + billNo + ", billSn=" + billSn + ", acCode=" + acCode + ", particular=" + particular + ", amount=" + amount + "}";
    }
    
    

}
