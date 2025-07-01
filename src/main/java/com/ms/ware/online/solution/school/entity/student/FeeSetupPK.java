/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author MS
 */
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

    public FeeSetupPK() {
    }

    public FeeSetupPK(long program, long classId, long academicYear, long feeId) {
        this.program = program;
        this.classId = classId;
        this.academicYear = academicYear;
        this.feeId = feeId;
    }

    public long getProgram() {
        return program;
    }

    public void setProgram(long program) {
        this.program = program;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(long academicYear) {
        this.academicYear = academicYear;
    }

    public long getFeeId() {
        return feeId;
    }

    public void setFeeId(long feeId) {
        this.feeId = feeId;
    }

    public long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) program;
        hash += (int) classId;
        hash += (int) academicYear;
        hash += (int) feeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FeeSetupPK)) {
            return false;
        }
        FeeSetupPK other = (FeeSetupPK) object;
        if (this.program != other.program) {
            return false;
        }
        if (this.classId != other.classId) {
            return false;
        }
        if (this.academicYear != other.academicYear) {
            return false;
        }
        if (this.feeId != other.feeId) {
            return false;
        }
        return true;
    }

}
