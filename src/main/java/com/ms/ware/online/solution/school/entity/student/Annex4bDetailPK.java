/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

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
public class Annex4bDetailPK implements Serializable {

    @Column(name = "MASTER_ID")
    private String masterId;
    @Column(name = "SUBJECT")
    private Long subject;

    public Annex4bDetailPK() {
    }

    public Annex4bDetailPK(String masterId, Long subject) {
        this.masterId = masterId;
        this.subject = subject;
    }


}
