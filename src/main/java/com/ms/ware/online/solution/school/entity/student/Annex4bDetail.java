package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "annex4b_detail")
public class Annex4bDetail implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected Annex4bDetailPK pk;

    @Column(name = "MO1")
    private String mo1;
    @Column(name = "REM1")
    private String rem1;
    @Column(name = "MO2")
    private String mo2;
    @Column(name = "REM2")
    private String rem2;
    @Column(name = "MO3")
    private String mo3;
    @Column(name = "REM3")
    private String rem3;
    @Column(name = "MO4")
    private String mo4;
    @Column(name = "REM4")
    private String rem4;
    @Column(name = "MO5")
    private String mo5;
    @Column(name = "REM5")
    private String rem5;
    @Column(name = "MO6")
    private String mo6;
    @Column(name = "REM6")
    private String rem6;
    @Column(name = "MO7")
    private String mo7;
    @Column(name = "REM7")
    private String rem7;
    @Column(name = "REMARK")
    private String remark;
    @JoinColumn(name = "MASTER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Annex4bMaster annex4bMaster;
    @JoinColumn(name = "SUBJECT", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private SubjectMaster subjectMaster;

    public Annex4bDetail() {
    }

    public Annex4bDetail(String masterId, Long subject, String mo1, String mo2, String mo3, String mo4, String mo5, String mo6, String mo7, String rem1, String rem2, String rem3, String rem4, String rem5, String rem6, String rem7, String remark) {
        pk = new Annex4bDetailPK(masterId, subject);
        this.mo1 = mo1;
        this.rem1 = rem1;
        this.mo2 = mo2;
        this.rem2 = rem2;
        this.mo3 = mo3;
        this.rem3 = rem3;
        this.mo4 = mo4;
        this.rem4 = rem4;
        this.mo5 = mo5;
        this.rem5 = rem5;
        this.mo6 = mo6;
        this.rem6 = rem6;
        this.mo7 = mo7;
        this.rem7 = rem7;
        this.remark = remark;
    }

    public void setPk(Annex4bDetailPK pk) {
        this.pk = pk;
    }

    public String getMo1() {
        return mo1;
    }

    public void setMo1(String mo1) {
        this.mo1 = mo1;
    }

    public String getRem1() {
        return rem1;
    }

    public void setRem1(String rem1) {
        this.rem1 = rem1;
    }

    public String getMo2() {
        return mo2;
    }

    public void setMo2(String mo2) {
        this.mo2 = mo2;
    }

    public String getRem2() {
        return rem2;
    }

    public void setRem2(String rem2) {
        this.rem2 = rem2;
    }

    public String getMo3() {
        return mo3;
    }

    public void setMo3(String mo3) {
        this.mo3 = mo3;
    }

    public String getRem3() {
        return rem3;
    }

    public void setRem3(String rem3) {
        this.rem3 = rem3;
    }

    public String getMo4() {
        return mo4;
    }

    public void setMo4(String mo4) {
        this.mo4 = mo4;
    }

    public String getRem4() {
        return rem4;
    }

    public void setRem4(String rem4) {
        this.rem4 = rem4;
    }

    public String getMo5() {
        return mo5;
    }

    public void setMo5(String mo5) {
        this.mo5 = mo5;
    }

    public String getRem5() {
        return rem5;
    }

    public void setRem5(String rem5) {
        this.rem5 = rem5;
    }

    public String getMo6() {
        return mo6;
    }

    public void setMo6(String mo6) {
        this.mo6 = mo6;
    }

    public String getRem6() {
        return rem6;
    }

    public void setRem6(String rem6) {
        this.rem6 = rem6;
    }

    public String getMo7() {
        return mo7;
    }

    public void setMo7(String mo7) {
        this.mo7 = mo7;
    }

    public String getRem7() {
        return rem7;
    }

    public void setRem7(String rem7) {
        this.rem7 = rem7;
    }

    public SubjectMaster getSubjectMaster() {
        return subjectMaster;
    }

    public void setSubjectMaster(SubjectMaster subjectMaster) {
        this.subjectMaster = subjectMaster;
    }

    @Override
    public String toString() {
        return "\n{\"subject\":\"" + pk.getSubject() + "\",\"remark\":\"" + remark + "\",\"mo1\":\"" + mo1 + "\",\"rem1\":\"" + rem1 + "\",\"mo2\":\"" + mo2 + "\",\"rem2\":\"" + rem2 + "\",\"mo3\":\"" + mo3 + "\",\"rem3\":\"" + rem3 + "\",\"mo4\":\"" + mo4 + "\",\"rem4\":\"" + rem4 + "\",\"mo5\":\"" + mo5 + "\",\"rem5\":\"" + rem5 + "\",\"mo6\":\"" + mo6 + "\",\"rem6\":\"" + rem6 + "\",\"mo7\":\"" + mo7 + "\",\"rem7\":\"" + rem7 + "\"}";
    }

}
