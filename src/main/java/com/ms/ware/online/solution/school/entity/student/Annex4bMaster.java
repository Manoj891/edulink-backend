package com.ms.ware.online.solution.school.entity.student;


import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "annex4b_master", uniqueConstraints = @UniqueConstraint(columnNames = {"REG_NO", "SEME_YEAR"}))
public class Annex4bMaster implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "REG_NO", nullable = false)
    private Long regNo;
    @Column(name = "SEME_YEAR", nullable = false)
    private Integer semYear;
    @Column(name = "SEME_YEAR_NAME", nullable = false)
    private String semYearName;
    @Column(name = "YEAR1")
    private String year1;
    @Column(name = "YEAR2")
    private String year2;
    @Column(name = "YEAR3")
    private String year3;
    @Column(name = "YEAR4")
    private String year4;
    @Column(name = "YEAR5")
    private String year5;
    @Column(name = "YEAR6")
    private String year6;
    @Column(name = "YEAR7")
    private String year7;
    @Column(name = "PASS_PERCENT")
    private String passPercent;
    
    @JoinColumn(name = "REG_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentInfo stuInfo;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "annex4bMaster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Annex4bDetail> detail;

    public Annex4bMaster() {
    }

    public Annex4bMaster(Long regNo, Integer semYear, String semYearName, String year1, String year2, String year3, String year4, String year5, String year6, String year7, String passPercent) {
        this.id = regNo + "-" + semYear;
        this.regNo = regNo;
        this.semYear = semYear;
        this.semYearName = semYearName;
        this.year1 = year1;
        this.year2 = year2;
        this.year3 = year3;
        this.year4 = year4;
        this.year5 = year5;
        this.year6 = year6;
        this.year7 = year7;
        this.passPercent = passPercent;
    }

    public Annex4bMaster(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getRegNo() {
        return regNo;
    }

    public void setRegNo(Long regNo) {
        this.regNo = regNo;
    }

    public Integer getSemYear() {
        return semYear;
    }

    public void setSemYear(Integer semYear) {
        this.semYear = semYear;
    }

    public String getSemYearName() {
        return semYearName;
    }

    public void setSemYearName(String semYearName) {
        this.semYearName = semYearName;
    }

    public String getYear1() {
        return year1;
    }

    public void setYear1(String year1) {
        this.year1 = year1;
    }

    public String getYear2() {
        return year2;
    }

    public void setYear2(String year2) {
        this.year2 = year2;
    }

    public String getYear3() {
        return year3;
    }

    public void setYear3(String year3) {
        this.year3 = year3;
    }

    public String getYear4() {
        return year4;
    }

    public void setYear4(String year4) {
        this.year4 = year4;
    }

    public String getYear5() {
        return year5;
    }

    public void setYear5(String year5) {
        this.year5 = year5;
    }

    public String getYear6() {
        return year6;
    }

    public void setYear6(String year6) {
        this.year6 = year6;
    }

    public String getYear7() {
        return year7;
    }

    public void setYear7(String year7) {
        this.year7 = year7;
    }

    public List<Annex4bDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<Annex4bDetail> detail) {
        this.detail = detail;
    }

    public String getPassPercent() {
        return passPercent;
    }

    public void setPassPercent(String passPercent) {
        this.passPercent = passPercent;
    }

    @Override
    public String toString() {
        return "{" + "id\":\"" + id + "\",\"passPercent\":\"" + passPercent + "\"\",\"regNo\":\"" + regNo + "\",\"semYear\":\"" + semYear + "\",\"semYearName\":\"" + semYearName + "\",\"year1\":\"" + year1 + "\",\"year2\":\"" + year2 + "\",\"year3\":\"" + year3 + "\",\"year4\":\"" + year4 + "\",\"year5\":\"" + year5 + "\",\"year6\":\"" + year6 + "\",\"year7\":\"" + year7 + "\",\"passPercent\":\"" + passPercent + "\",\"detail\":\"" + detail + "\"}";
    }

}
