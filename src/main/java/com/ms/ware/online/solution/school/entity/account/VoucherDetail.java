package com.ms.ware.online.solution.school.entity.account;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "voucher_detail", uniqueConstraints = @UniqueConstraint(name = "VOUCHER_NO", columnNames = {"VOUCHER_NO", "VOUCHER_SN"}))
public class VoucherDetail implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "VOUCHER_NO")
    private String voucherNo;
    @Column(name = "VOUCHER_SN", updatable = false)
    private Integer voucherSn;
    @Column(name = "AC_CODE", nullable = false)
    private String acCode;
    @Column(name = "PARTICULAR", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String particular;
    @Column(name = "DR_AMT", nullable = false)
    private Double drAmt;
    @Column(name = "CR_AMT", nullable = false)
    private Double crAmt;
    @Column(name = "BILL_NO")
    private String billNo;
    @Column(name = "CHEQUE_NO")
    private String chequeNo;
    @Column(name = "CREATED_DATETIME", columnDefinition = "DATETIME DEFAULT now()")
    private Date created = new Date();
    @JoinColumn(name = "AC_CODE", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ChartOfAccount chartOfAccount;
    @JoinColumn(name = "VOUCHER_NO", referencedColumnName = "VOUCHER_NO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Voucher voucher;

    public VoucherDetail(String id, String voucherNo, Integer voucherSn, String acCode, String particular, double drAmt, double crAmt) {
        this.id = id;
        this.voucherNo = voucherNo;
        this.voucherSn = voucherSn;
        this.acCode = acCode;
        this.particular = particular;
        this.drAmt = drAmt;
        this.crAmt = crAmt;

    }

    public VoucherDetail(String voucherNo, Integer voucherSn, String acCode, String particular, Double drAmt, Double crAmt, String billNo, String chequeNo) {
        this.id = voucherNo + "-" + voucherSn;
        this.voucherNo = voucherNo;
        this.voucherSn = voucherSn;
        this.acCode = acCode;
        this.particular = particular;
        this.drAmt = drAmt;
        this.crAmt = crAmt;
        this.billNo = billNo;
        this.chequeNo = chequeNo;
    }

    public VoucherDetail(String acCode, String particular, Double drAmt, Double crAmt) {
        this.acCode = acCode;
        this.particular = particular;
        this.drAmt = drAmt;
        this.crAmt = crAmt;
    }


    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"voucherNo\": \"" + voucherNo + "\",\"voucherSn\": \"" + voucherSn + "\",\"acCode\": \"" + acCode + "\",\"particular\": \"" + particular + "\",\"drAmt\": \"" + drAmt + "\",\"crAmt\": \"" + crAmt + "\"}";
    }
}
