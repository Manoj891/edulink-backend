/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.teacherpanel;

import com.ms.ware.online.solution.school.entity.employee.EmployeeInfo;
import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "upload_teachers_video", uniqueConstraints = @UniqueConstraint(columnNames = {"TEACHER", "CLASS_ID", "ACADEMIC_YEAR", "PROGRAM", "SUBJECT_GROUP", "SUBJECT", "VIDEO_TITLE", "COURSE_CHAPTER"}))
public class UploadTeachersVideo {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "VIDEO_TITLE", nullable = false)
    private String videoTitle;
    @Column(name = "COURSE_CHAPTER", nullable = false)
    private String courseChapter;
    @Column(name = "YOUTUBE_LINK", nullable = false)
    private String youtubeLink;

    @JoinColumn(name = "TEACHER", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EmployeeInfo teacher;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AcademicYear academicYear;
    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectGroup subjectGroup;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ProgramMaster program;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ClassMaster classId;
    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectMaster subject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getCourseChapter() {
        return courseChapter;
    }

    public void setCourseChapter(String courseChapter) {
        this.courseChapter = courseChapter;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public EmployeeInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(Long teacher) {
        this.teacher = new EmployeeInfo(teacher);
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Long academicYear) {
        this.academicYear = new AcademicYear(academicYear);
    }

    public SubjectGroup getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = new SubjectGroup(subjectGroup);
    }

    public ProgramMaster getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = new ProgramMaster(program);
    }

    public ClassMaster getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = new ClassMaster(classId);
    }

    public SubjectMaster getSubject() {
        return subject;
    }

    public void setSubject(Long subject) {
        this.subject = new SubjectMaster(subject);
    }

    @Override
    public String toString() {
        return "\n{\"id\":\"" + id + "\",\"teacher\":\"" + teacher + "\",\"academicYear\":\"" + academicYear + "\",\"subjectGroup\":\"" + subjectGroup + "\",\"program\":\"" + program + "\",\"classId\":\"" + classId + "\",\"subject\":\"" + subject + "\",\"videoTitle\":\"" + videoTitle + "\",\"courseChapter\":\"" + courseChapter + "\",\"youtubeLink\":\"" + youtubeLink + "\"}";
    }

}
