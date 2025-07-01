package com.ms.ware.online.solution.school.entity.employee;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "online_vacancy")
public class OnlineVacancy implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "EMP_TYPE", columnDefinition = "VARCHAR(15)")
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
    @Column(name = "CODE", updatable = false)
    private String code;
    @Column(name = "house_no")
    private String houseNo;
    @Column(name = "temporary_address")
    private String temporaryAddress;
    @Column(name = "citizenship_no")
    private String citizenshipNo;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Email
    @NotNull
    @Column(name = "email", unique = true, nullable = false)
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
    @Column(name = "pan_no")
    private String panNo;
    @Column(name = "emp_level")
    private Long empLevel;
    @Column(name = "department")
    private Long department;
    @Column(name = "specialization", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String specialization;
    @Column(name = "CV", updatable = false)
    private String cv;
    @Column(name = "PHOTO", updatable = false)
    private String photo;
    @Column(name = "HIRE_FIRE_DATE")
    @Temporal(TemporalType.DATE)
    private Date hireFireDate;
    @Column(name = "HIRE_FIRE_STATUS")
    private String hireFireStatus;

    public OnlineVacancy() {
    }

    public OnlineVacancy(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMunicipal() {
        return municipal;
    }

    public void setMunicipal(String municipal) {
        this.municipal = municipal;
    }

    public String getWardNo() {
        return wardNo;
    }

    public void setWardNo(String wardNo) {
        this.wardNo = wardNo;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getTemporaryAddress() {
        return temporaryAddress;
    }

    public void setTemporaryAddress(String temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    public String getCitizenshipNo() {
        return citizenshipNo;
    }

    public void setCitizenshipNo(String citizenshipNo) {
        this.citizenshipNo = citizenshipNo;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(String dob) {
        if (dob.length() == 10) {
            int year = Integer.parseInt(dob.substring(0, 4));
            if (year > 2020) {
                this.dob = DateConverted.bsToAdDate(dob);
            } else {
                this.dob = DateConverted.toDate(dob);
            }
        } else {
            this.dob = null;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlternativeEmail() {
        return alternativeEmail;
    }

    public void setAlternativeEmail(String alternativeEmail) {
        this.alternativeEmail = alternativeEmail;
    }

    public String getEmergencyContactEmail() {
        return emergencyContactEmail;
    }

    public void setEmergencyContactEmail(String emergencyContactEmail) {
        this.emergencyContactEmail = emergencyContactEmail;
    }

    public String getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public void setEmergencyContactNo(String emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public String getEmergencyContactPerson() {
        return emergencyContactPerson;
    }

    public void setEmergencyContactPerson(String emergencyContactPerson) {
        this.emergencyContactPerson = emergencyContactPerson;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternativeMobile() {
        return alternativeMobile;
    }

    public void setAlternativeMobile(String alternativeMobile) {
        this.alternativeMobile = alternativeMobile;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public Long getEmpLevel() {
        return empLevel;
    }

    public void setEmpLevel(Long empLevel) {
        this.empLevel = empLevel;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getEmpType() {
        return empType;
    }

    public void setEmpType(String empType) {
        this.empType = empType;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

//    @Override
//    public String toString() {
//        return "\n{\"id\": \"" + id + "\",\"code\": \"" + code + "\",\"firstName\": \"" + firstName + "\",\"middleName\": \"" + middleName + "\",\"lastName\": \"" + lastName + "\",\"maritalStatus\": \"" + maritalStatus + "\",\"province\": \"" + province + "\",\"district\": \"" + district + "\",\"municipal\": \"" + municipal + "\",\"wardNo\": \"" + wardNo + "\",\"houseNo\": \"" + houseNo + "\",\"temporaryAddress\": \"" + temporaryAddress + "\",\"citizenshipNo\": \"" + citizenshipNo + "\",\"dob\": \"" + dob + "\",\"email\": \"" + email + "\",\"alternativeEmail\": \"" + alternativeEmail + "\",\"emergencyContactEmail\": \"" + emergencyContactEmail + "\",\"emergencyContactNo\": \"" + emergencyContactNo + "\",\"emergencyContactPerson\": \"" + emergencyContactPerson + "\",\"gender\": \"" + gender + "\",\"mobile\": \"" + mobile + "\",\"alternativeMobile\": \"" + alternativeMobile + "\",\"workingStatus\": \"" + workingStatus + "\",\"joinDate\": \"" + joinDate + "\",\"holyday1\": \"" + holyday1 + "\",\"holyday2\": \"" + holyday2 + "\",\"lateStatus\": \"" + lateStatus + "\",\"otStatus\": \"" + otStatus + "\",\"panNo\": \"" + panNo + "\",\"empLevel\": \"" + empLevel + "\",\"department\": \"" + department + "\"}";
//    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getHireFireDate() {
        return hireFireDate;
    }

    public void setHireFireDate(Date hireFireDate) {
        this.hireFireDate = hireFireDate;
    }

    public String getHireFireStatus() {
        return hireFireStatus;
    }

    public void setHireFireStatus(String hireFireStatus) {
        this.hireFireStatus = hireFireStatus;
    }
    
}
