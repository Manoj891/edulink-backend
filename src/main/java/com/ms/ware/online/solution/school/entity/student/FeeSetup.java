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
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author MS
 */
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

    public void setPk(FeeSetupPK pk) {
        this.pk = pk;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPayTime() {
        return payTime;
    }

    public void setPayTime(int payTime) {
        this.payTime = payTime;
    }

    public ProgramMaster getProgram() {
        return program;
    }

    public void setProgram(ProgramMaster program) {
        this.program = program;
    }

    public ClassMaster getClassId() {
        return classId;
    }

    public void setClassId(ClassMaster classId) {
        this.classId = classId;
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
    }

    public BillMaster getFeeId() {
        return feeId;
    }

    public void setFeeId(BillMaster feeId) {
        this.feeId = feeId;
    }

    public SubjectGroup getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(SubjectGroup subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    @Override
    public String toString() {
        return "\n{\"program\": \"" + pk.getProgram() + "\",\"classId\": \"" + pk.getClassId() + "\",\"academicYear\": \"" + pk.getAcademicYear() + "\",\"feeId\": \"" + pk.getFeeId() + "\",\"amount\": \"" + amount + "\",\"payTime\": \"" + payTime + "\",\"totalAmount\": \"" + totalAmount + "\"}";
    }

}
