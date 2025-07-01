package com.ms.ware.online.solution.school.entity.setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "subject_group_detail", uniqueConstraints = @UniqueConstraint(columnNames = {"SUBJECT", "SUBJECT_GROUP", "CLASS_ID", "PROGRAM"}, name = "SUBJECT"))
public class SubjectGroupDetail implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "SUBJECT_GROUP", nullable = false)
    private Long subjectGroup;
    @Column(name = "CLASS_ID", nullable = false)
    private Long classId;
    @Column(name = "PROGRAM", nullable = false)
    private Long program;
    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectMaster subject;
    @Column(name = "CREDIT", nullable = false)
    private Float credit;
    @Column(name = "SUBJECT_CODE", nullable = false, columnDefinition = "VARCHAR(15)")
    private String subjectCode;
    @Column(name = "TH_FM", nullable = false, columnDefinition = "FLOAT(5)")
    private float thFm;
    @Column(name = "TH_PM", nullable = false, columnDefinition = "FLOAT(5)")
    private float thPm;
    @Column(name = "PR_FM", nullable = false, columnDefinition = "FLOAT(5)")
    private float prFm;
    @Column(name = "PR_PM", nullable = false, columnDefinition = "FLOAT(5)")
    private float prPm;
    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubjectGroup SubjectGroupMaster;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ClassMaster classMaster;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramMaster programMaster;
    @Column(name = "in_order")
    private Integer inOrder;
    @Column(name = "CREDIT_PH", columnDefinition = "FLOAT(5) default 0")
    private Float creditPh;
    @Column(name = "SUBJECT_CODE_PH", nullable = false, columnDefinition = "VARCHAR(15) default ''")
    private String subjectCodePh;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public SubjectMaster getSubject() {
        return subject;
    }

    public void setSubject(SubjectMaster subject) {
        this.subject = subject;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
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

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = program;
    }

    public Float getCredit() {
        return credit;
    }

    public void setCredit(Float credit) {
        this.credit = credit;
    }

    public Integer getInOrder() {
        return inOrder;
    }

    public void setInOrder(Integer inOrder) {
        this.inOrder = inOrder;
    }

    public Float getCreditPh() {
        return creditPh;
    }

    public void setCreditPh(Float creditPh) {
        this.creditPh = creditPh;
    }

    public String getSubjectCodePh() {
        return subjectCodePh;
    }

    public void setSubjectCodePh(String subjectCodePh) {
        this.subjectCodePh = subjectCodePh;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"subjectGroup\": \"" + subjectGroup + "\",\"subject\": \"" + subject + "\",\"subjectCode\": \"" + subjectCode + "\",\"thFm\": \"" + thFm + "\",\"thPm\": \"" + thPm + "\",\"prFm\": \"" + prFm + "\",\"prPm\": \"" + prPm + "\"}";
    }
}
