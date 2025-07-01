/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.inventory;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
@Setter
@Getter
public class PurchaseOrderDetailPK implements Serializable {

    @Column(name = "ORDER_NO")
    private Long orderNo;
    @Column(name = "AC_CODE")
    private String acCode;

    public PurchaseOrderDetailPK() {
    }

    public PurchaseOrderDetailPK(Long orderNo, String acCode) {
        this.orderNo = orderNo;
        this.acCode = acCode;
    }


}
