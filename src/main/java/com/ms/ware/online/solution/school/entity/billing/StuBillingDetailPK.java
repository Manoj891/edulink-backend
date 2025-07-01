/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.billing;

import lombok.*;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Embeddable
public class StuBillingDetailPK implements Serializable {

    @Column(name = "bill_no")
    private String billNo;
    @Column(name = "bill_sn")
    private int billSn;
}
