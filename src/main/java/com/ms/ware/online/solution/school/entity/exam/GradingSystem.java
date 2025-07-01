package com.ms.ware.online.solution.school.entity.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "grading_system")
public class GradingSystem implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "RANG_FROM", nullable = false)
    private Float rangFrom;
    @Column(name = "GRADE", nullable = false, columnDefinition = "VARCHAR(3)")
    private String grade;
    @Column(name = "GPA", nullable = false)
    private Float gpa;
    @Column(name = "REMARK", nullable = false, columnDefinition = "VARCHAR(30)")
    private String remark;
    @Column(name = "EFFECTIVE_DATE_FROM", nullable = false)
    @Temporal(TemporalType.DATE)
    private java.util.Date effectiveDateFrom;
    @Column(name = "EFFECTIVE_DATE_TO", nullable = true)
    @Temporal(TemporalType.DATE)
    private java.util.Date effectiveDateTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRangFrom() {
        return rangFrom;
    }

    public void setRangFrom(Float rangFrom) {
        this.rangFrom = rangFrom;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Float getGpa() {
        return gpa;
    }

    public void setGpa(Float gpa) {
        this.gpa = gpa;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEffectiveDateFrom() {
        return DateConverted.adToBs(effectiveDateFrom);
    }

    public void setEffectiveDateFrom(String effectiveDateFrom) {
        this.effectiveDateFrom = DateConverted.bsToAdDate(effectiveDateFrom);
    }

    public String getEffectiveDateTo() {
        try {
            if (effectiveDateTo != null) {
                return DateConverted.adToBs(effectiveDateTo);
            }
        } catch (Exception e) {
        }
        return "";
    }

    public void setEffectiveDateTo(String effectiveDateTo) {
        try {
            if (effectiveDateTo.length() == 10) {
                this.effectiveDateTo = DateConverted.bsToAdDate(effectiveDateTo);
                return;
            }
        } catch (Exception e) {
        }
        this.effectiveDateTo = null;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"rangFrom\": \"" + rangFrom + "\",\"grad\": \"" + grade + "\",\"gpa\": \"" + gpa + "\",\"remark\": \"" + remark + "\",\"effectiveDateFrom\": \"" + effectiveDateFrom + "\",\"effectiveDateTo\": \"" + effectiveDateTo + "\"}";
    }
}
