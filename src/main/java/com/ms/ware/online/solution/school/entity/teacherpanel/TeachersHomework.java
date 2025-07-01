package com.ms.ware.online.solution.school.entity.teacherpanel;

import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "teachers_homework", uniqueConstraints = @UniqueConstraint(columnNames = {"HOMEWORK_DATE", "ACADEMIC_YEAR", "PROGRAM", "SUBJECT_GROUP", "SUBJECT", "TEACHER"}))
public class TeachersHomework implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "TEACHER", nullable = false)
    private Long teacher;
    @Column(name = "HOMEWORK_DATE", nullable = false)
    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    private Date homeworkDate;
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
    @Column(name = "HOMEWORK_TITLE", nullable = false)
    private String homeworkTitle;
    @Column(name = "HOME_WORK", columnDefinition = "TEXT", nullable = false)
    private String homeWork;
    @Column(name = "FILE_URL", updatable = false, nullable = true)
    private String fileUrl;
    @Column(name = "ENTER_DATE", updatable = false, nullable = false, columnDefinition = "DATETIME")
    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    private Date enterDate;
    @JoinColumn(name = "TEACHER", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EmployeeInfo teachersInformation;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramMaster programMaster;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ClassMaster classMaster;
    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubjectGroup subjectGroupMaster;
    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectMaster subjectMaster;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AcademicYear AcademicYearMaster;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacher() {
        return teacher;
    }

    public void setTeacher(Long teacher) {
        this.teacher = teacher;
    }

    public String getHomeworkDate() {
        return DateConverted.adToBs(homeworkDate);
    }

    public Date getHomeworkDateAd() {
        return homeworkDate;
    }

    public void setHomeworkDate(String homeworkDate) {
        this.homeworkDate = DateConverted.bsToAdDate(homeworkDate);
    }

    public void setHomeworkDateAd(Date homeworkDate) {
        this.homeworkDate = homeworkDate;
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

    public String getHomeworkTitle() {
        return homeworkTitle;
    }

    public void setHomeworkTitle(String homeworkTitle) {
        this.homeworkTitle = homeworkTitle;
    }

    public String getHomeWork() {
        return homeWork;
    }

    public Long getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Long academicYear) {
        this.academicYear = academicYear;
    }

    public void setHomeWork(String homeWork) {
        this.homeWork = homeWork;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getEnterDate() {
        return DateConverted.adToBs(enterDate);
    }

    public SubjectMaster getSubjectMaster() {
        return subjectMaster;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"teacher\": \"" + teacher + "\",\"homeworkDate\": \"" + homeworkDate + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"subjectGroup\": \"" + subjectGroup + "\",\"subject\": \"" + subject + "\",\"homeworkTitle\": \"" + homeworkTitle + "\",\"homeWork\": \"" + homeWork + "\"}";
    }

}
