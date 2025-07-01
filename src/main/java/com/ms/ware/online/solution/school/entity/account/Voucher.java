package com.ms.ware.online.solution.school.entity.account;

import com.ms.ware.online.solution.school.entity.billing.StuBillingMaster;

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

import com.ms.ware.online.solution.school.config.DateConverted;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "voucher", uniqueConstraints = @UniqueConstraint(name = "VOUCHER_SN", columnNames = {"VOUCHER_TYPE", "FISCAL_YEAR", "VOUCHER_SN"}))
public class Voucher implements java.io.Serializable {

    @Id
    @Column(name = "VOUCHER_NO")
    private String voucherNo;
    @Column(name = "VOUCHER_TYPE")
    private String voucherType;
    @Column(name = "FISCAL_YEAR")
    private Long fiscalYear;
    @Column(name = "VOUCHER_SN")
    private Integer voucherSn;
    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;
    @Column(name = "ENTER_BY")
    private String enterBy;
    @Column(name = "ENTER_DATE")
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Column(name = "APPROVE_BY", updatable = false)
    private String approveBy;
    @Column(name = "APPROVE_DATE", updatable = false)
    @Temporal(TemporalType.DATE)
    private Date approveDate;

    @Column(name = "REJECT_BY", updatable = false, insertable = false)
    private String rejectBy;
    @Column(name = "REJECT_DATE", updatable = false, insertable = false)
    @Temporal(TemporalType.DATE)
    private Date rejectDate;

    @Column(name = "un_approve_by_date", insertable = false, updatable = false)
    private String unApproveByDate;


    @Column(name = "NARRATION", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String narration;
    @Column(name = "CHEQUE_NO")
    private String chequeNo;
    @Column(name = "FEE_RECEIPT_NO", unique = true, nullable = true)
    private String feeReceiptNo;
    @JoinColumn(name = "FEE_RECEIPT_NO", referencedColumnName = "BILL_NO", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StuBillingMaster stuBillingMaster;
    @JoinColumn(name = "FISCAL_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private FiscalYear fiscalYearMaster;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voucher", fetch = FetchType.EAGER)
    private List<VoucherDetail> detail;



    public Voucher(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getEnterDate() {
        return DateConverted.adToBs(enterDate);
    }

    public Date getEnterDateAd() {
        return enterDate;
    }

    public void setEnterDateAd(String enterDate) {
        this.enterDate = DateConverted.toDate(enterDate);
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = DateConverted.bsToAdDate(enterDate);
    }

    public void setEnterDateAd(Date enterDate) {
        this.enterDate = enterDate;
    }



    public List<VoucherDetail> getObj() {
        return detail;
    }

    public void setObj(List<VoucherDetail> detail) {
        this.detail = detail;
    }



    @Override
    public String toString() {
        return "\n{\"voucherNo\": \"" + voucherNo + "\",\"voucherType\": \"" + voucherType + "\",\"fiscalYear\": \"" + fiscalYear + "\",\"voucherSn\": \"" + voucherSn + "\",\"totalAmount\": \"" + totalAmount + "\",\"enterBy\": \"" + enterBy + "\",\"enterDate\": \"" + enterDate + "\",\"approveBy\": \"" + approveBy + "\",\"approveDate\": \"" + approveDate + "\",\"narration\": \"" + narration + "\",\"chequeNo\": \"" + chequeNo + "\",\"feeReceiptNo\": \"" + feeReceiptNo + "\",\"detail\": \"" + detail + "\"}";
    }
}
