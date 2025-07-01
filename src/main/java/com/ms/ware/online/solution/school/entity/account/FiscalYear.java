package com.ms.ware.online.solution.school.entity.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "fiscal_year")
public class FiscalYear implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "YEAR", unique = true, columnDefinition = "VARCHAR(10) NOT NULL")
    private String year;
    @Column(name = "START_DATE", columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.DATE)
    private java.util.Date startDate;
    @Column(name = "END_DATE", columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.DATE)
    private java.util.Date endDate;
    @Column(name = "STATUS", columnDefinition = "VARCHAR(1) NOT NULL")
    private String status;

    public FiscalYear() {
    }

    public FiscalYear(Long id) {
        this.id = id;
    }

    public FiscalYear(String id) {
        this.id = Long.parseLong(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStartDate() {
        return DateConverted.adToBs(startDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = DateConverted.bsToAdDate(startDate);
    }

    public String getEndDate() {
        return DateConverted.adToBs(endDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = DateConverted.bsToAdDate(endDate);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"year\": \"" + year + "\",\"startDate\": \"" + startDate + "\",\"endDate\": \"" + endDate + "\",\"status\": \"" + status + "\"}";
    }
}
