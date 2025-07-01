package com.ms.ware.online.solution.school.entity.account;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.Index;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ledger")
public class Ledger implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private String id;
    @Index(columnNames = "index_ledger_ac_code", name = "ac_code")
    @Column(name = "AC_CODE")
    private String acCode;
    @Column(name = "VOUCHER_NO")
    private String voucherNo;
    @Column(name = "DR_AMT", nullable = false)
    private Double drAmt;
    @Column(name = "CR_AMT", nullable = false)
    private Double crAmt;
    @Column(name = "PARTICULAR", columnDefinition = "TEXT")
    private String particular;
    @Column(name = "FEE_RECEIPT_NO", nullable = true, columnDefinition = "VARCHAR(30)")
    private String feeReceiptNo;
    @Index(columnNames = "index_ledger_enter_date", name = "ENTER_DATE")
    @NotNull
    @Column(name = "ENTER_DATE")
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Size(max = 30)
    @Column(name = "ENTER_BY")
    private String enterBy;
    @Index(columnNames = "index_ledger_post_date", name = "POST_DATE")
    @Column(name = "POST_DATE")
    @Temporal(TemporalType.DATE)
    private Date postDate;
    @Size(max = 30)
    @Column(name = "POST_BY")
    private String postBy;
    @Size(max = 30)
    @Column(name = "CHEQUE_NO")
    private String chequeNo;
    @Column(name = "NARRATION", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String narration;
    @JoinColumn(name = "AC_CODE", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private ChartOfAccount chartOfAccount;
    @JoinColumn(name = "VOUCHER_NO", referencedColumnName = "VOUCHER_NO", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Voucher voucher;
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private VoucherDetail voucherDetail;
    @Column(name = "BANK_RECONCILIATION", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String bankReconciliation;

    @Column(name = "BANK_RECONCILIATION_DATE", columnDefinition = "VARCHAR(30) DEFAULT NULL")
    private String bankReconciliationDate;


    public Ledger(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", acCode=" + acCode + ", voucherNo=" + voucherNo + ", drAmt=" + drAmt + ", crAmt=" + crAmt + ", particular=" + particular + ", feeReceiptNo=" + feeReceiptNo + ", enterDate=" + enterDate + ", enterBy=" + enterBy + ", postDate=" + postDate + ", postBy=" + postBy + ", chequeNo=" + chequeNo + ", narration=" + narration + ", chartOfAccount=" + chartOfAccount + ", voucher=" + voucher + ", voucherDetail=" + voucherDetail + '}';
    }

}
