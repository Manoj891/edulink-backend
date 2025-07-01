/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author MS
 */
@Embeddable
public class SchoolClassSessionBillDatePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "MASTER_ID")
    private long masterId;
    @Basic(optional = false)
    @Column(name = "PAYMENT_TIME")
    private int paymentTime;

    public SchoolClassSessionBillDatePK() {
    }

    public SchoolClassSessionBillDatePK(long masterId, int paymentTime) {
        this.masterId = masterId;
        this.paymentTime = paymentTime;
    }

    public long getMasterId() {
        return masterId;
    }

    public void setMasterId(long masterId) {
        this.masterId = masterId;
    }

    public int getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(int paymentTime) {
        this.paymentTime = paymentTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) masterId;
        hash += (int) paymentTime;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SchoolClassSessionBillDatePK)) {
            return false;
        }
        SchoolClassSessionBillDatePK other = (SchoolClassSessionBillDatePK) object;
        if (this.masterId != other.masterId) {
            return false;
        }
        if (this.paymentTime != other.paymentTime) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{masterId=" + masterId + ", paymentTime=" + paymentTime + "}";
    }

  
    
}
