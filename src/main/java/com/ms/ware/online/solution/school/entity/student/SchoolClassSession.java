package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.config.DateConverted;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "school_class_session", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"program", "class_id", "academic_year"}, name = "unique_class_of_year")})
public class SchoolClassSession implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "program", columnDefinition = "BIGINT NOT NULL")
    private long program;
    @Column(name = "class_id", columnDefinition = "BIGINT NOT NULL")
    private long classId;
    @Column(name = "academic_year", columnDefinition = "BIGINT NOT NULL")
    private long academicYear;
    @Column(name = "start_date", columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.DATE)
    private java.util.Date startDate;
    @Column(name = "end_date", columnDefinition = "DATE NOT NULL")
    @Temporal(TemporalType.DATE)
    private java.util.Date endDate;
    @Column(name = "total_month")
    private Integer totalMonth;

    public SchoolClassSession() {
    }

    public SchoolClassSession(Long id) {
        this.id = id;
    }

    public SchoolClassSession(String id) {
        this.id = Long.parseLong(id);
    }


    public String getStartDateBs() {
        return DateConverted.adToBs(startDate);
    }

    public void setStartDate(String startDate) {
        this.startDate = DateConverted.bsToAdDate(startDate);
    }

    public String getEndDateBs() {
        return DateConverted.adToBs(endDate);
    }

    public void setEndDate(String endDate) {
        this.endDate = DateConverted.bsToAdDate(endDate);
    }


    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"academicYear\": \"" + academicYear + "\",\"startDate\": \"" + startDate + "\",\"endDate\": \"" + endDate + "\",\"startDateBs\": \"" + getStartDateBs() + "\",\"endDateBS\": \"" + getEndDateBs() + "\",\"totalMonth\": \"" + totalMonth + "\"}";
    }
}
