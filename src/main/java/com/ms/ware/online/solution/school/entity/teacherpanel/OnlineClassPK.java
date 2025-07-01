/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.teacherpanel;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@EqualsAndHashCode
@Embeddable
public class OnlineClassPK implements Serializable {

    @Column(name = "ACADEMIC_YEAR", nullable = false)
    private Long academicYear;
    @Column(name = "PROGRAM", nullable = false)
    private Long program;
    @Column(name = "CLASS_ID", nullable = false)
    private Long classId;
    @Column(name = "SUBJECT_GROUP", nullable = false)
    private Long subjectGroup;
    @Column(name = "SUBJECT", nullable = false)
    private Long subject;

    public OnlineClassPK() {
    }

    public OnlineClassPK(Long academicYear, Long program, Long classId, Long subjectGroup, Long subject) {
        this.academicYear = academicYear;
        this.program = program;
        this.classId = classId;
        this.subjectGroup = subjectGroup;
        this.subject = subject;
    }

    public Long getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Long academicYear) {
        this.academicYear = academicYear;
    }

    public Long getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = program;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

}
