package com.ms.ware.online.solution.school.entity.inventory;

import com.ms.ware.online.solution.school.entity.account.FiscalYear;
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
import com.ms.ware.online.solution.school.config.DateConverted;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements java.io.Serializable {

    @Id
    @Column(name = "ORDER_NO")
    private Long orderNo;
    @Column(name = "APPROVE_BY")
    private String approveBy;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.DATE)
    private Date approveDate;
    @Column(name = "ENTER_BY", nullable = false)
    private String enterBy;
    @Column(name = "ENTER_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Column(name = "NARRATION")
    private String narration;
    @Column(name = "ORDER_SN")
    private Integer orderSn;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "WITHIN_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date withinDate;
    @Column(name = "FISCAL_YEAR", nullable = false)
    private Long fiscalYear;
    @Column(name = "SUPPLIER", nullable = false)
    private Long supplier;
    @Column(name = "OPENING", columnDefinition = "VARCHAR(1) DEFAULT 'N'", updatable = false)
    private String opening;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SundryCreditors sundryCreditors;
    @JoinColumn(name = "FISCAL_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FiscalYear fiscalYearMaster;
    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseOrder", fetch = FetchType.EAGER)
    private List<PurchaseOrderDetail> detail;

    public PurchaseOrder() {
    }

    public PurchaseOrder(Long id) {
        this.orderNo = id;
    }

    public PurchaseOrder(String id) {
        this.orderNo = Long.parseLong(id);
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setId(Long orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getApproveBy() {
        try {
            if (approveBy != null) {
                return approveBy;
            }
        } catch (Exception e) {
        }
        return "";
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getApproveDate() {
        try {
            if (approveDate != null) {
                return DateConverted.adToBs(enterDate);
            }
        } catch (Exception e) {
        }
        return "";
    }

    public Date getApproveDateAd() {
        return enterDate;

    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
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

    public void setEnterDateAd(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Integer getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(Integer orderSn) {
        this.orderSn = orderSn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWithinDate() {
        return DateConverted.adToBs(withinDate);
    }

    public Date getWithinDateAd() {
        return withinDate;
    }

    public void setWithinDate(String withinDate) {
        this.withinDate = DateConverted.bsToAdDate(withinDate);
    }

    public void setWithinDateAd(Date withinDate) {
        this.withinDate = withinDate;
    }

    public Long getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Long fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Long getSupplier() {
        return supplier;
    }

    public void setSupplier(Long supplier) {
        this.supplier = supplier;
    }

    public List<PurchaseOrderDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<PurchaseOrderDetail> detail) {
        this.detail = detail;
    }

    public void setObj(List<PurchaseOrderDetail> detail) {
        this.detail = detail;
    }

    public SundryCreditors getSundryCreditors() {
        return sundryCreditors;
    }

    public void setSundryCreditors(SundryCreditors sundryCreditors) {
        this.sundryCreditors = sundryCreditors;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    @Override
    public String toString() {
        return "\n{\"orderNo\": \"" + orderNo + "\",\"approveBy\": \"" + approveBy + "\",\"approveDate\": \"" + approveDate + "\",\"enterBy\": \"" + enterBy + "\",\"enterDate\": \"" + enterDate + "\",\"narration\": \"" + narration + "\",\"orderSn\": \"" + orderSn + "\",\"status\": \"" + status + "\",\"withinDate\": \"" + withinDate + "\",\"fiscalYear\": \"" + fiscalYear + "\",\"supplier\": \"" + supplier + "\",\"detail\": \"" + detail + "\"}";
    }
}
