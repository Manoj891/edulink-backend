package com.ms.ware.online.solution.school.entity.student;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "certificate_data", uniqueConstraints = {
        @UniqueConstraint(name = "certificate_data_unique", columnNames = {"reg_no", "board_name"}),
        @UniqueConstraint(name = "certificate_data_campus_reg_no", columnNames = {"campus_reg_no"})
})
public class CertificateData {
    @Id
    @Column(name = "id", length = 50)
    private String id;
    @Column(name = "reg_no", length = 15)
    private String regNo;
    @Column(name = "board_name", length = 15)
    private String boardName;
    @Column(name = "level", length = 30)
    private String level;
    @Column(name = "title", length = 15)
    private String title;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "subject_group", length = 100)
    private Long subjectGroup;
    @Column(name = "sn")
    private int sn;
    @Column(name = "serial_number",length = 20)
    private String serialNumber;

    @Column(name = "fathers_name", length = 100)
    private String fathersName;
    @Column(name = "district", length = 100)
    private String district;
    @Column(name = "municipal", length = 100)
    private String municipal;
    @Column(name = "ward_no", length = 100)
    private String wardNo;

    @Column(name = "degree", length = 100)
    private String degree;
    @Column(name = "program_name", length = 100)
    private String programName;

    @Column(name = "join_year", length = 4)
    private int joinYear;
    @Column(name = "passed_year", length = 4)
    private int passedYear;
    @Column(name = "date_of_birth", length = 10)
    private String dateOfBirth;
    @Column(name = "place", length = 20)
    private String place;
    @Column(name = "campus_reg_date", length = 10)
    private String campusRegDate;
    @Column(name = "campus_reg_no", length = 10)
    private String campusRegNo;
    @Column(name = "issued_date", length = 10)
    private String issuedDate;
    @Getter(AccessLevel.NONE)
    @Column(name = "create_by", length = 20, updatable = false)
    private String createBy;
    @Getter(AccessLevel.NONE)
    @Column(name = "create_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Getter(AccessLevel.NONE)
    @Column(name = "modify_by", length = 20, insertable = false)
    private String modifyBy;
    @Getter(AccessLevel.NONE)
    @Column(name = "modify_date", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;


}
