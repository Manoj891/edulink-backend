package com.ms.ware.online.solution.school.entity.employee;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "teachers_class_subject")
public class TeachersClassSubject implements java.io.Serializable {

    @EmbeddedId
    protected TeachersClassSubjectPK pk;
    @Column(name = "entity_id", unique = true, updatable = false)
    private String entityId;
    @Column(name = "TEACHER", insertable = false, updatable = false)
    private Long teacher;
    @Column(name = "ACADEMIC_YEAR", insertable = false, updatable = false)
    private Long academicYear;
    @Column(name = "PROGRAM", insertable = false, updatable = false)
    private Long program;
    @Column(name = "CLASS_ID", insertable = false, updatable = false)
    private Long classId;
    @Column(name = "SUBJECT", insertable = false, updatable = false)
    private Long subject;
    @Column(name = "SUBJECT_GROUP", nullable = false, insertable = false, updatable = false)
    private Long subjectGroup;
    @Column(name = "section", columnDefinition = "VARCHAR(40) DEFAULT ''", insertable = false, updatable = false)
    private String section;
    @Column(name = "IS_CLASS_TEACHER", columnDefinition = "VARCHAR(1)")
    private String isClassTeacher;
    @JoinColumn(name = "TEACHER", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private EmployeeInfo teacherMaster;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AcademicYear academicYearMaster;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private ProgramMaster programMaster;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private ClassMaster classMaster;
    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectGroup subjectGroupMaster;
    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private SubjectMaster subjectMaster;
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

    @Column(name = "CLASS_START_DATE", columnDefinition = "VARCHAR(15)")
    private String classStartDate;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setPk(TeachersClassSubjectPK pk) {
        this.pk = pk;
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

    public Long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public void setSubject(Long subject) {
        this.subject = subject;
    }

    public EmployeeInfo getTeacherMaster() {
        return teacherMaster;
    }

    public ProgramMaster getProgramMaster() {
        return programMaster;
    }

    public ClassMaster getClassMaster() {
        return classMaster;
    }

    public SubjectMaster getSubjectMaster() {
        return subjectMaster;
    }

    public SubjectGroup getSubjectGroupMaster() {
        return subjectGroupMaster;
    }

    public String getIsClassTeacher() {
        return isClassTeacher;
    }

    public void setIsClassTeacher(String isClassTeacher) {
        this.isClassTeacher = isClassTeacher;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getWed() {
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
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

    public String getClassStartDate() {
        return classStartDate;
    }

    public void setClassStartDate(String classStartDate) {
        this.classStartDate = classStartDate;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "\n{\"teacher\": \"" + teacher + "\",\"academicYear\": \"" + academicYear + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"subject\": \"" + subject + "\",\"isClassTeacher\": \"" + isClassTeacher + "\"}";
    }
}
