package com.ms.ware.online.solution.school.entity.exam;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "exam_result_publish_subject")
public class ExamResultPublishSubject implements java.io.Serializable {

    @EmbeddedId
    protected ExamResultPublishSubjectPK pk;

    @Column(name = "subject_Code")
    private String subjectCode;
    @Column(name = "subject_Name")
    private String subjectName;
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Column(name = "TH_FM")
    private Float thFm;
    @Column(name = "TH_PM")
    private Float thPm;
    @Column(name = "PR_FM")
    private Float prFm;
    @Column(name = "PR_PM")
    private Float prPm;
    @Column(name = "EXAM", insertable = false, updatable = false)
    private Long exam;
    @Column(name = "PROGRAM", insertable = false, updatable = false)
    private Long program;
    @Column(name = "CLASS_ID", insertable = false, updatable = false)
    private Long classId;
    @Column(name = "SUBJECT_GROUP", insertable = false, updatable = false)
    private Long subjectGroup;
    @Column(name = "SUBJECT", insertable = false, updatable = false)
    private Long subject;
    @Column(name = "in_order")
    private Integer inOrder;
    @Column(name = "CREDIT_HOUR")
    private Float creditHour;
    @Column(name = "CREDIT_PH", columnDefinition = "FLOAT(5) default 0")
    private Float creditPh;
    @Column(name = "SUBJECT_CODE_PH",  columnDefinition = "VARCHAR(15) default ''")
    private String subjectCodePh;
    public ExamResultPublishSubjectPK getPk() {
        return pk;
    }

    public void setPk(ExamResultPublishSubjectPK pk) {
        this.pk = pk;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Float getThFm() {
        return thFm;
    }

    public void setThFm(Float thFm) {
        this.thFm = thFm;
    }

    public Float getThPm() {
        return thPm;
    }

    public void setThPm(Float thPm) {
        this.thPm = thPm;
    }

    public Float getPrFm() {
        return prFm;
    }

    public void setPrFm(Float prFm) {
        this.prFm = prFm;
    }

    public Float getPrPm() {
        return prPm;
    }

    public void setPrPm(Float prPm) {
        this.prPm = prPm;
    }

    public Long getExam() {
        return exam;
    }

    public void setExam(Long exam) {
        this.exam = exam;
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

    @Override
    public String toString() {
        return "\n{\"subject\": \"" + subject + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"subjectGroup\": \"" + subjectGroup + "\",\"exam\": \"" + exam + "\",\"subjectCode\": \"" + subjectCode + "\",\"subjectName\": \"" + subjectName + "\",\"groupName\": \"" + groupName + "\",\"thFm\": \"" + thFm + "\",\"thPm\": \"" + thPm + "\",\"prFm\": \"" + prFm + "\",\"prPm\": \"" + prPm + "\"}";
    }
}
