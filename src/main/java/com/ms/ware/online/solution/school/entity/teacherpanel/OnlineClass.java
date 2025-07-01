/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.teacherpanel;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "online_class")
public class OnlineClass implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OnlineClassPK pk;
    @Column(name = "LINK", nullable = false)
    private String link;
    @Column(name = "START_TIME", nullable = false)
    private String startTime;
    @Column(name = "END_TIME", nullable = false)
    private String endTime;
     @Column(name = "TEACHER", nullable = false)
    private Long teacher;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramMaster program;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ClassMaster classId;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AcademicYear academicYear;

    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectGroup subjectGroup;

    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectMaster subject;

    public OnlineClassPK getPk() {
        return pk;
    }

    public void setPk(OnlineClassPK pk) {
        this.pk = pk;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.pk);
        hash = 41 * hash + Objects.hashCode(this.link);
        hash = 41 * hash + Objects.hashCode(this.startTime);
        hash = 41 * hash + Objects.hashCode(this.endTime);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OnlineClass other = (OnlineClass) obj;
        if (!Objects.equals(this.link, other.link)) {
            return false;
        }
        if (!Objects.equals(this.startTime, other.startTime)) {
            return false;
        }
        if (!Objects.equals(this.endTime, other.endTime)) {
            return false;
        }
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
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

    public SubjectGroup getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(SubjectGroup subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public SubjectMaster getSubject() {
        return subject;
    }

    public void setSubject(SubjectMaster subject) {
        this.subject = subject;
    }

    public Long getTeacher() {
        return teacher;
    }

    public void setTeacher(Long teacher) {
        this.teacher = teacher;
    }

}
