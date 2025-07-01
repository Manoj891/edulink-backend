package com.ms.ware.online.solution.school.entity.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "certificate_issue")
public class CertificateIssue {
    @Id
    @Column(name = "reg_no")
    private Long regNo;
    @Column(name = "division")
    private String division;
    @Column(name = "issue_by")
    private String issueBy;
    @Column(name = "issue_date")
    private String issueDate;
    @Column(name = "receive_by")
    private String receiveBy;
    @Column(name = "neb_tu_reg_no")
    private String nebTuRegNo;
    @Column(name = "trans_issued_date")
    private String transIssuedDate;
    @Column(name = "transcript_no")
    private String transcriptNo;
    
    @Column(name = "fiscal_year")
    private Long fiscalYear;
    @Column(name = "sn")
    private Integer sn;
    
    public Long getRegNo() {
        return regNo;
    }

    public void setRegNo(Long regNo) {
        this.regNo = regNo;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getIssueBy() {
        return issueBy;
    }

    public void setIssueBy(String issueBy) {
        this.issueBy = issueBy;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getReceiveBy() {
        return receiveBy;
    }

    public void setReceiveBy(String receiveBy) {
        this.receiveBy = receiveBy;
    }
}
