/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.exam;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
@EqualsAndHashCode
@Embeddable
public class CharacterIssuePK implements Serializable {

    @Column(name = "REG_NO")
    private Long regNo;
    @Column(name = "EXAM_ID")
    private Long examId;

    public CharacterIssuePK() {
    }

    public CharacterIssuePK(Long regNo, Long examId) {
        this.regNo = regNo;
        this.examId = examId;
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

}
