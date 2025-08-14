/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import lombok.*;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author MS
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Embeddable
public class FeeSetupPK implements Serializable {

    @Column(name = "ACADEMIC_YEAR")
    private long academicYear;
    @Column(name = "PROGRAM")
    private long program;
    @Column(name = "CLASS_ID")
    private long classId;
    @Column(name = "SUBJECT_GROUP")
    private long subjectGroup;
    @Column(name = "FEE_ID")
    private long feeId;

}
