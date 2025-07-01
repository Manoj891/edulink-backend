/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.inventory;

import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "purchase_order_detail")
public class PurchaseOrderDetail implements java.io.Serializable {

    @EmbeddedId
    protected PurchaseOrderDetailPK pk;
    @Column(name = "GRN_QTY")
    private Float grnQty;
    @Column(name = "RATE")
    private Float rate;
    @Column(name = "ORDER_QTY")
    private Float orderQty;
    @Column(name = "TOTAL")
    private Float total;
    @Column(name = "VAT")
    private Float vat;
    @Column(name = "SPECIFICATION")
    private String specification;
    @Column(name = "DELETED", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String deleted;
    @Column(name = "DELETED_BY", columnDefinition = "VARCHAR(20) ")
    private String deletedBy;

    @Column(name = "DELETED_DATE", columnDefinition = "VARCHAR(20)")
    private String deletedDate;
    @JoinColumn(name = "AC_CODE", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ChartOfAccount acCode;
    @JoinColumn(name = "ORDER_NO", referencedColumnName = "ORDER_NO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PurchaseOrder purchaseOrder;

    public PurchaseOrderDetail() {
    }

    public PurchaseOrderDetail(Long orderNo, String acCode, Float grnQty, Float rate, Float orderQty, Float total, Float vat, String specification) {
        this.pk = new PurchaseOrderDetailPK(orderNo, acCode);
        this.grnQty = grnQty;
        this.rate = rate;
        this.orderQty = orderQty;
        this.total = total;
        this.vat = vat;
        this.specification = specification;
    }

    public void setPk(PurchaseOrderDetailPK pk) {
        this.pk = pk;
    }

    public Float getGrnQty() {
        return grnQty;
    }

    public void setGrnQty(Float grnQty) {
        this.grnQty = grnQty;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Float orderQty) {
        this.orderQty = orderQty;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getVat() {
        return vat;
    }

    public void setVat(Float vat) {
        this.vat = vat;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public ChartOfAccount getAcCode() {
        return acCode;
    }

    public void setAcCode(ChartOfAccount acCode) {
        this.acCode = acCode;
    }

    @Override
    public String toString() {
        return "\n{\"grnQty\":\"" + grnQty + "\",\"rate\":\"" + rate + "\",\"orderQty\":\"" + orderQty + "\",\"total\":\"" + total + "\",\"vat\":\"" + vat + "\",\"specification\":\"" + specification + "\",\"acCode\":\"" + acCode + "\"}";
    }

}
