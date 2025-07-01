/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.exam;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter
@Getter
@Embeddable
@EqualsAndHashCode
public class ExamMarkEntryPK implements Serializable {

    @Column(name = "EXAM_REG_ID")
    private long examRegId;
    @Column(name = "SUBJECT")
    private long subject;

    public ExamMarkEntryPK() {
    }

    public ExamMarkEntryPK(long examRegId, long subject) {
        this.examRegId = examRegId;
        this.subject = subject;
    }

}
