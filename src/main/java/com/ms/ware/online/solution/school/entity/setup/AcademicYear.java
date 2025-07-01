package com.ms.ware.online.solution.school.entity.setup;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = " academic_year")
public class AcademicYear implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "YEAR", unique = true, nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String year;
    @Column(name = "STATUS")
    private String status;

    public AcademicYear() {
    }

    public AcademicYear(Long id) {
        this.id = id;
    }

    public AcademicYear(String id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"year\": \"" + year + "\",\"status\": \"" + status + "\"}";
    }
}
