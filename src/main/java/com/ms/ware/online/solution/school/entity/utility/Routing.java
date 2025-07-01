package com.ms.ware.online.solution.school.entity.utility;

import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "routing")
public class Routing implements Serializable {

    @EmbeddedId
    private RoutingPK pk;
    @Column(name = "SUN", columnDefinition = "VARCHAR(5)")
    private String sun;
    @Column(name = "MON", columnDefinition = "VARCHAR(5)")
    private String mon;
    @Column(name = "TUE", columnDefinition = "VARCHAR(5)")
    private String tue;
    @Column(name = "WED", columnDefinition = "VARCHAR(5)")
    private String wed;
    @Column(name = "THU", columnDefinition = "VARCHAR(5)")
    private String thu;
    @Column(name = "FRI", columnDefinition = "VARCHAR(5)")
    private String fri;
    @Column(name = "SAT", columnDefinition = "VARCHAR(5)")
    private String sat;
    @Column(name = "START_TIME", columnDefinition = "VARCHAR(15)")
    private String startTime;
    @Column(name = "END_TIME", columnDefinition = "VARCHAR(15)")
    private String endTime;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AcademicYear academicYear;
    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectGroup subjectGroup;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ProgramMaster program;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ClassMaster classId;
    @Column(name = "SECTION", insertable = false, updatable = false)
    private String section;
    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectMaster subject;
    @JoinColumn(name = "TEACHER", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EmployeeInfo teacher;

    public Routing() {
    }

    public String getId() {
        return pk.getAcademicYear() + "-" + pk.getSubjectGroup() + "-" + pk.getProgram() + "-" + pk.getClassId() + "-" + pk.getSection() + "-" + pk.getSubject() + "-" + pk.getTeacher();
    }

    public void setPk(RoutingPK pk) {
        this.pk = pk;
    }

    public String getSun() {
        try {
            if (sun == null) {
                return "";
            }
        } catch (Exception e) {
        }
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getMon() {
        try {
            if (mon == null) {
                return "";
            }
        } catch (Exception e) {
        }
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTue() {
        try {
            if (tue == null) {
                return "";
            }
        } catch (Exception e) {
        }
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getWed() {
        try {
            if (wed == null) {
                return "";
            }
        } catch (Exception e) {
        }
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getThu() {
        try {
            if (thu == null) {
                return "";
            }
        } catch (Exception e) {
        }
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getFri() {
        try {
            if (fri == null) {
                return "";
            }
        } catch (Exception e) {
        }
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getSat() {
        try {
            if (sat == null) {
                return "";
            }
        } catch (Exception e) {
        }
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public SubjectMaster getSubject() {
        return subject;
    }

    public void setSubject(SubjectMaster subject) {
        this.subject = subject;
    }

    public EmployeeInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(EmployeeInfo teacher) {
        this.teacher = teacher;
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
    public String toString() {
        return "\n{\"startTime\":\"" + startTime + "\",\"endTime\":\"" + endTime + "\",\"sun\":\"" + sun + "\",\"mon\":\"" + mon + "\",\"tue\":\"" + tue + "\",\"wed\":\"" + wed + "\",\"thu\":\"" + thu + "\",\"fri\":\"" + fri + "\",\"sat\":\"" + sat + "\",\"academicYear\":\"" + academicYear + "\",\"subjectGroup\":\"" + subjectGroup + "\",\"program\":\"" + program + "\",\"classId\":\"" + classId + "\",\"section\":\"" + section + "\",\"subject\":\"" + subject + "\",\"teacher\":\"" + teacher + "\"}";
    }

}
