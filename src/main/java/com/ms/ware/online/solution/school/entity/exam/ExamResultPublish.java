package com.ms.ware.online.solution.school.entity.exam;

import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "exam_result_publish")
public class ExamResultPublish implements java.io.Serializable {

    @EmbeddedId
    protected ExamResultPublishPK pk;
    @Column(name = "PUBLISH_DATE")
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    @Column(name = "PUBLISH_BY")
    private String publishBy;
    @Column(name = "WORKING_DAYS")
    private Integer workingDays;

    @JoinColumn(name = "EXAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ExamMaster exam;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ProgramMaster program;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ClassMaster classId;
    public String getId() {
        return pk.getExam() + "-" + pk.getProgram() + "-" + pk.getClassId();
    }

    public ExamResultPublish() {
    }

    public ExamResultPublish(Long exam, Long program, Long classId, Date publishDate, String publishBy) {
        pk = new ExamResultPublishPK(exam, program, classId);
        this.publishDate = publishDate;
        this.publishBy = publishBy;
    }

    public ExamResultPublish(Long exam, Long program, Long classId, String publishDate, String publishBy) {
        pk = new ExamResultPublishPK(exam, program, classId);
        this.publishDate = DateConverted.bsToAdDate(publishDate);
        this.publishBy = publishBy;
    }

    public ExamResultPublishPK getPk() {
        return pk;
    }

    public void setPk(ExamResultPublishPK pk) {
        this.pk = pk;
    }

    public String getPublishDate() {
        return DateConverted.adToBs(publishDate);
    }

    public Date getPublishDateAd() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = DateConverted.bsToAdDate(publishDate);
    }

    public void setPublishDateAd(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy;
    }

    public ExamMaster getExam() {
        return exam;
    }

    public void setExam(ExamMaster exam) {
        this.exam = exam;
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

    public Integer getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    @Override
    public String toString() {
        return "\n{\"exam\": \"" + exam + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"publishDate\": \"" + publishDate + "\",\"publishBy\": \"" + publishBy + "\"}";
    }
}
