/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.entity.teacherpanel.TeachersHomework;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "student_homework")
public class StudentHomework implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected StudentHomeworkPK pk;
    @Column(name = "STU_ID", insertable = false, updatable = false)
    private Long stuId;
    @Column(name = "HOMEWORK", insertable = false, updatable = false)
    private Long homework;
    @Column(name = "ANSWER", columnDefinition = "TEXT")
    private String answer;
    @JoinColumn(name = "STU_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentInfo student;
    @JoinColumn(name = "HOMEWORK", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TeachersHomework teachersHomework;
    @Column(name = "CHECK_DATE", columnDefinition = "DATE")
    private String checkDate;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "STU_FILE")
    private String stuFile;
    @Column(name = "STU_FILE1")
    private String stuFile1;
    @Column(name = "STU_FILE2")
    private String stuFile2;
    @Column(name = "STU_FILE3")
    private String stuFile3;
    @Column(name = "STU_FILE4")
    private String stuFile4;
    @Column(name = "STU_FILE5")
    private String stuFile5;

    public void setPk(StudentHomeworkPK pk) {
        this.pk = pk;
    }

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public Long getHomework() {
        return homework;
    }

    public void setHomework(Long homework) {
        this.homework = homework;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public TeachersHomework getTeachersHomework() {
        return teachersHomework;
    }

    public void setTeachersHomework(TeachersHomework teachersHomework) {
        this.teachersHomework = teachersHomework;
    }

    public String getCheckDate() {
        return DateConverted.adToBs(checkDate);
    }

    public String getCheckDateAd() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = DateConverted.bsToAd(checkDate);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStuFile() {
        return stuFile;
    }

    public void setStuFile(String stuFile) {
        this.stuFile = stuFile;
    }

    public String getStuFile1() {
        return stuFile1;
    }

    public void setStuFile1(String stuFile1) {
        this.stuFile1 = stuFile1;
    }

    public String getStuFile2() {
        return stuFile2;
    }

    public void setStuFile2(String stuFile2) {
        this.stuFile2 = stuFile2;
    }

    public String getStuFile3() {
        return stuFile3;
    }

    public void setStuFile3(String stuFile3) {
        this.stuFile3 = stuFile3;
    }

    public String getStuFile4() {
        return stuFile4;
    }

    public void setStuFile4(String stuFile4) {
        this.stuFile4 = stuFile4;
    }

    public String getStuFile5() {
        return stuFile5;
    }

    public void setStuFile5(String stuFile5) {
        this.stuFile5 = stuFile5;
    }

    @Override
    public String toString() {
        return "\n{\"stuId\":\"" + pk.getStuId()+"\",\"homework\":\"" + pk.getHomework()+"\",\"answer\":\"" + answer+"\",\"student\":\"" + student+"\",\"teachersHomework\":\"" + teachersHomework+"\",\"checkDate\":\"" + checkDate+"\",\"remark\":\"" + remark+"\",\"stuFile\":\"" + stuFile+"\",\"stuFile1\":\"" + stuFile1+"\",\"stuFile2\":\"" + stuFile2+"\",\"stuFile3\":\"" + stuFile3+"\",\"stuFile4\":\"" + stuFile4+"\",\"stuFile5\":\"" + stuFile5 + "\"}";
    }

   
}
