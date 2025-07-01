/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.utility;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RoutingPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ACADEMIC_YEAR")
    private long academicYear;
    @Basic(optional = false)
    @Column(name = "SUBJECT_GROUP")
    private long subjectGroup;
    @Basic(optional = false)
    @Column(name = "PROGRAM")
    private long program;
    @Basic(optional = false)
    @Column(name = "CLASS_ID")
    private long classId;
    @Basic(optional = false)
    @Column(name = "SECTION")
    private String section;
    @Basic(optional = false)
    @Column(name = "SUBJECT")
    private long subject;
    @Basic(optional = false)
    @Column(name = "TEACHER")
    private long teacher;

    public RoutingPK() {
    }

    public RoutingPK(long academicYear, long subjectGroup, long program, long classId, String section, long subject, long teacher) {
        this.academicYear = academicYear;
        this.subjectGroup = subjectGroup;
        this.program = program;
        this.classId = classId;
        this.section = section;
        this.subject = subject;
        this.teacher = teacher;
    }

    public long getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(long academicYear) {
        this.academicYear = academicYear;
    }

    public long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(long subjectGroup) {
        this.subjectGroup = subjectGroup;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public long getSubject() {
        return subject;
    }

    public void setSubject(long subject) {
        this.subject = subject;
    }

    public long getTeacher() {
        return teacher;
    }

    public void setTeacher(long teacher) {
        this.teacher = teacher;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) academicYear;
        hash += (int) subjectGroup;
        hash += (int) program;
        hash += (int) classId;
        hash += (section != null ? section.hashCode() : 0);
        hash += (int) subject;
        hash += (int) teacher;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoutingPK)) {
            return false;
        }
        RoutingPK other = (RoutingPK) object;
        if (this.academicYear != other.academicYear) {
            return false;
        }
        if (this.subjectGroup != other.subjectGroup) {
            return false;
        }
        if (this.program != other.program) {
            return false;
        }
        if (this.classId != other.classId) {
            return false;
        }
        if ((this.section == null && other.section != null) || (this.section != null && !this.section.equals(other.section))) {
            return false;
        }
        if (this.subject != other.subject) {
            return false;
        }
        if (this.teacher != other.teacher) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject3.RoutingPK[ academicYear=" + academicYear + ", subjectGroup=" + subjectGroup + ", program=" + program + ", classId=" + classId + ", section=" + section + ", subject=" + subject + ", teacher=" + teacher + " ]";
    }
    
}
