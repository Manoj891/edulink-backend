/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.BillMaster;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import lombok.*;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author MS
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "fee_setup")
public class FeeSetup implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private FeeSetupPK pk;

    @Column(name = "AMOUNT")
    private float amount;
    @Column(name = "TOTAL_AMOUNT")
    private float totalAmount;

    @Column(name = "PAY_TIME")
    private int payTime;
    @Column(name = "fee_month", columnDefinition = "VARCHAR(2) default '01'")
    private String feeMonth;

    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ProgramMaster program;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ClassMaster classId;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AcademicYear academicYear;
    @JoinColumn(name = "FEE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private BillMaster feeId;
    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectGroup subjectGroup;

    @Override
    public String toString() {
        return "\n{\"program\": \"" + pk.getProgram() + "\",\"classId\": \"" + pk.getClassId() + "\",\"academicYear\": \"" + pk.getAcademicYear() + "\",\"feeId\": \"" + pk.getFeeId() + "\",\"amount\": \"" + amount + "\",\"payTime\": \"" + payTime + "\",\"totalAmount\": \"" + totalAmount + "\"}";
    }

}
