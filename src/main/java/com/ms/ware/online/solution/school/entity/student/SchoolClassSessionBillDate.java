/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "school_class_session_bill_date")
public class SchoolClassSessionBillDate implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SchoolClassSessionBillDatePK pk;
    @Basic(optional = false)
    @Column(name = "PAYMENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date paymentDate;
    @Column(name = "PAYMENT_DATE_BS", columnDefinition = "VARCHAR(10) NOT NULL")
    private String paymentDateBs;
    @JoinColumn(name = "MASTER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SchoolClassSession masterId;

    public long getMasterId() {
        return pk.getMasterId();
    }

    public int getPaymentTime() {
        return pk.getPaymentTime();
    }

    public void setPk(SchoolClassSessionBillDatePK pk) {
        this.pk = pk;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentDateBs() {
        return paymentDateBs;
    }

    public void setPaymentDateBs(String paymentDateBs) {
        this.paymentDateBs = paymentDateBs;
    }

   

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "{" + "pk=" + pk + ", paymentDate=" + paymentDate + ", paymentDateBs=" + paymentDate + ", masterId=" + pk.getMasterId() + '}';
    }

}
