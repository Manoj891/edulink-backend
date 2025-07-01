/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.employee;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
public class TeachersClassSubjectPK implements Serializable {

    @Column(name = "TEACHER")
    private Long teacher;
    @Column(name = "ACADEMIC_YEAR")
    private Long academicYear;
    @Column(name = "PROGRAM")
    private Long program;
    @Column(name = "CLASS_ID")
    private Long classId;
    @Column(name = "SUBJECT")
    private Long subject;
    @Column(name = "SUBJECT_GROUP", nullable = false)
    private Long subjectGroup;
    @Column(name = "section", columnDefinition = "VARCHAR(40) DEFAULT ''")
    private String section;

    public TeachersClassSubjectPK() {
    }

    public TeachersClassSubjectPK(Long teacher, Long academicYear, Long program, Long classId, Long subjectGroup, Long subject, String section) {
        this.teacher = teacher;
        this.academicYear = academicYear;
        this.program = program;
        this.classId = classId;
        this.subjectGroup = subjectGroup;
        this.subject = subject;
        this.section = section;
    }

    public Long getTeacher() {
        return teacher;
    }

    public void setTeacher(Long teacher) {
        this.teacher = teacher;
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

    public Long getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public Long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
