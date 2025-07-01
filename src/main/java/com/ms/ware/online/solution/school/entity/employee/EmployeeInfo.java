package com.ms.ware.online.solution.school.entity.employee;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employee_info")
public class EmployeeInfo implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "code", columnDefinition = "VARCHAR(6)", unique = true)
    private String code;
    @Column(name = "emp_type", columnDefinition = "VARCHAR(15)")
    private String empType;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "marital_status")
    private String maritalStatus;
    @Column(name = "province")
    private Integer province;
    @Column(name = "district")
    private String district;
    @Column(name = "municipal")
    private String municipal;
    @Column(name = "ward_no")
    private String wardNo;
    @Column(name = "house_no")
    private String houseNo;
    @Column(name = "SECTOR")
    private String sector;

    @Column(name = "temporary_address")
    private String temporaryAddress;
    @Column(name = "citizenship_no")
    private String citizenshipNo;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "email")
    private String email;
    @Column(name = "alternative_email")
    private String alternativeEmail;
    @Column(name = "emergency_contact_email")
    private String emergencyContactEmail;
    @Column(name = "emergency_contact_no")
    private String emergencyContactNo;
    @Column(name = "emergency_contact_person")
    private String emergencyContactPerson;
    @Column(name = "gender", columnDefinition = "VARCHAR(6)")
    private String gender;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "alternative_mobile")
    private String alternativeMobile;
    @Column(name = "working_status", columnDefinition = "VARCHAR(1)")
    private String workingStatus;
    @Column(name = "join_date")
    @Temporal(TemporalType.DATE)
    private Date joinDate;
    @Column(name = "holyday1", columnDefinition = "VARCHAR(3)")
    private String holyday1;
    @Column(name = "holyday2", columnDefinition = "VARCHAR(3)")
    private String holyday2;
    @Column(name = "late_status", columnDefinition = "VARCHAR(1)")
    private String lateStatus;
    @Column(name = "ot_status", columnDefinition = "VARCHAR(1)")
    private String otStatus;
    @Column(name = "pan_no", unique = true)
    private String panNo;
    @Column(name = "emp_level")
    private Long empLevel;
    @Column(name = "department")
    private Long department;
    @Column(name = "specialization", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String specialization;
    @Column(name = "BIOMETRIC_COMPANY_ID", insertable = false, updatable = false)
    private Long biometricCompanyId;
    @Column(name = "BIOMETRIC_EMP_ID", insertable = false, updatable = false)
    private Long biometricEmpId;
    @Column(name = "PHOTO", nullable = true, updatable = false)
    private String photo;
    @Column(name = "ac_code")
    private String acCode;
    @Column(name = "QUALIFICATION")
    private String qualification;
    @Column(name = "RELIGION")
    private Long religion;
    @Column(name = "CAST_ETHNICITY")
    private Long castEthnicity;
    @Column(name = "LOGIN_PASSWORD", insertable = false, updatable = false)
    private String password;


    public EmployeeInfo(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", empType='" + empType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", province=" + province +
                ", district='" + district + '\'' +
                ", municipal='" + municipal + '\'' +
                ", wardNo='" + wardNo + '\'' +
                ", houseNo='" + houseNo + '\'' +
                ", sector='" + sector + '\'' +
                ", temporaryAddress='" + temporaryAddress + '\'' +
                ", citizenshipNo='" + citizenshipNo + '\'' +
                ", dob=" + dob +
                ", email='" + email + '\'' +
                ", alternativeEmail='" + alternativeEmail + '\'' +
                ", emergencyContactEmail='" + emergencyContactEmail + '\'' +
                ", emergencyContactNo='" + emergencyContactNo + '\'' +
                ", emergencyContactPerson='" + emergencyContactPerson + '\'' +
                ", gender='" + gender + '\'' +
                ", mobile='" + mobile + '\'' +
                ", alternativeMobile='" + alternativeMobile + '\'' +
                ", workingStatus='" + workingStatus + '\'' +
                ", joinDate=" + joinDate +
                ", holyday1='" + holyday1 + '\'' +
                ", holyday2='" + holyday2 + '\'' +
                ", lateStatus='" + lateStatus + '\'' +
                ", otStatus='" + otStatus + '\'' +
                ", panNo='" + panNo + '\'' +
                ", empLevel=" + empLevel +
                ", department=" + department +
                ", specialization='" + specialization + '\'' +
                ", biometricCompanyId=" + biometricCompanyId +
                ", biometricEmpId=" + biometricEmpId +
                ", photo='" + photo + '\'' +
                ", acCode='" + acCode + '\'' +
                ", qualification='" + qualification + '\'' +
                ", religion=" + religion +
                ", castEthnicity=" + castEthnicity +
                ", password='" + password + '\'' +
                '}';
    }
}
