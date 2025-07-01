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


@Embeddable
@EqualsAndHashCode
@Setter
@Getter
public class ExamResultPublishPK implements Serializable {

    @Column(name = "EXAM")
    private Long exam;
    @Column(name = "PROGRAM")
    private Long program;
    @Column(name = "CLASS_ID")
    private Long classId;

    public ExamResultPublishPK() {
    }

    public ExamResultPublishPK(Long exam, Long program, Long classId) {
        this.exam = exam;
        this.program = program;
        this.classId = classId;
    }

}
