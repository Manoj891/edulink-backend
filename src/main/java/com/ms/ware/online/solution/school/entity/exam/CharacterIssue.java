package com.ms.ware.online.solution.school.entity.exam;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import javax.persistence.Table;


@Entity
@Table(name = "character_issue")
public class CharacterIssue implements java.io.Serializable {

    @EmbeddedId
    protected CharacterIssuePK pk;
    @Column(name = "REG_NO", insertable = false, updatable = false)
    private Long regNo;
    @Column(name = "EXAM_ID", insertable = false, updatable = false)
    private Long examId;
    @Column(name = "SN", unique = true)
    private Long sn;

    public CharacterIssuePK getPk() {
        return pk;
    }

    public void setPk(CharacterIssuePK pk) {
        this.pk = pk;
    }

    public Long getRegNo() {
        return regNo;
    }

    public void setRegNo(Long regNo) {
        this.regNo = regNo;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Long getSn() {
        return sn;
    }

    public void setSn(Long sn) {
        this.sn = sn;
    }


}
