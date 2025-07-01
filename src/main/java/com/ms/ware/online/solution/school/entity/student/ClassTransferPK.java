/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
public class ClassTransferPK implements Serializable {

    @Column(name = "ACADEMIC_YEAR")
    private long academicYear;
    @Column(name = "STUDENT_ID")
    private long studentId;
    @Column(name = "CLASS_ID")
    private long classId;

    public ClassTransferPK() {
    }

    public ClassTransferPK(long studentId, long academicYear, long classId) {
        this.studentId = studentId;
        this.academicYear = academicYear;
        this.classId = classId;
    }


}
