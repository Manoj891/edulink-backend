/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "previous_education")
public class PreviousEducation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PreviousEducationPK pk;
    @Basic(optional = false)
    @Column(name = "EXAM_NAME")
    private String examName;
    @Basic(optional = false)
    @Column(name = "BOARD")
    private String board;
    @Column(name = "PASSED_YEAR")
    private Integer passedYear;
    @Column(name = "RESULT")
    private String result;
    @Column(name = "PERCENTAGE")
    private String percentage;
    @Column(name = "BORAD_REG_NO")
    private String boradRegNo;
    @Column(name = "SYMBOL_NO")
    private String symbolNo;
    @Column(name = "INSTITUTE_NAME")
    private String instituteName;
    @Column(name = "MAJOR_SUBJECT")
    private String majorSubject;
    @Column(name = "REG_NO", insertable = false, updatable = false)
    private Long regNo;
    @Column(name = "EDUCATION", insertable = false, updatable = false)
    private String education;

    @JoinColumn(name = "REG_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentInfo studentInfo;

    public PreviousEducation() {
    }

    public void setPk(PreviousEducationPK pk) {
        this.pk = pk;
    }

    public String getId() {
        return pk.getRegNo() + "-" + pk.getEducation();
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Integer getPassedYear() {
        return passedYear;
    }

    public void setPassedYear(Integer passedYear) {
        this.passedYear = passedYear;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getRegNo() {
        return regNo;
    }

    public void setRegNo(Long regNo) {
        this.regNo = regNo;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getBoradRegNo() {
        return boradRegNo;
    }

    public void setBoradRegNo(String boradRegNo) {
        this.boradRegNo = boradRegNo;
    }

    public String getSymbolNo() {
        return symbolNo;
    }

    public void setSymbolNo(String symbolNo) {
        this.symbolNo = symbolNo;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getMajorSubject() {
        return majorSubject;
    }

    public void setMajorSubject(String majorSubject) {
        this.majorSubject = majorSubject;
    }

    @Override
    public String toString() {
        return "{\"education\":\"" + pk.getEducation() + "\",\"examName\":\"" + examName + "\",\"board\":\"" + board + "\",\"passedYear\":\"" + passedYear + "\",\"result\":\"" + result + "\"}";
    }

}
