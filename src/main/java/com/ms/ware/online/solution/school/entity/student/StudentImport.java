package com.ms.ware.online.solution.school.entity.student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student_import")
public class StudentImport {

    @Id
    @Column(name = "id")
    private long id;
    @Column(name = "students_name",length = 60)
    private String studentsName;
    @Column(name = "program")
    private Long program;
    @Column(name = "program_name",length = 30)
    private String programName;
    @Column(name = "class_id")
    private Long classId;
    @Column(name = "class_name",length = 30)
    private String className;
    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "group_name",length = 30)
    private String groupName;
    @Column(name = "section",length = 30)
    private String section;
    @Column(name = "gender",length = 6)
    private String gender;
    @Column(name = "roll_no")
    private Integer rollNo;
    @Column(name = "date_of_birth",length = 15)
    private String dateOfBirth;
    @Column(name = "mobile_no",length = 15)
    private String mobileNo;
    @Column(name = "fathers_name",length = 60)
    private String fathersName;
    @Column(name = "fathers_contact_no",length = 60)
    private String fathersContactNo;
    @Column(name = "fathers_occupation",length = 60)
    private String fathersOccupation;
    @Column(name = "mothers_name",length = 60)
    private String mothersName;
    @Column(name = "mothers_occupation",length = 60)
    private String mothersOccupation;
    @Column(name = "mothers_contact_no",length = 60)
    private String mothersContactNo;
    @Column(name = "permanent_address",length = 60)
    private String permanentAddress;
    @Column(name = "corresponding_address",length = 60)
    private String correspondingAddress;
    @Column(name = "guardians_name",length = 60)
    private String guardiansName;
    @Column(name = "registration_date",length = 10)
    private String registrationDate;
    @Column(name = "approved")
    private boolean approved;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentsName() {
        return studentsName;
    }

    public void setStudentsName(String studentsName) {
        this.studentsName = studentsName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getRollNo() {
        return rollNo;
    }

    public void setRollNo(Integer rollNo) {
        this.rollNo = rollNo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getFathersContactNo() {
        return fathersContactNo;
    }

    public void setFathersContactNo(String fathersContactNo) {
        this.fathersContactNo = fathersContactNo;
    }

    public String getFathersOccupation() {
        return fathersOccupation;
    }

    public void setFathersOccupation(String fathersOccupation) {
        this.fathersOccupation = fathersOccupation;
    }

    public String getMothersName() {
        return mothersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public String getMothersOccupation() {
        return mothersOccupation;
    }

    public void setMothersOccupation(String mothersOccupation) {
        this.mothersOccupation = mothersOccupation;
    }

    public String getMothersContactNo() {
        return mothersContactNo;
    }

    public void setMothersContactNo(String mothersContactNo) {
        this.mothersContactNo = mothersContactNo;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getCorrespondingAddress() {
        return correspondingAddress;
    }

    public void setCorrespondingAddress(String correspondingAddress) {
        this.correspondingAddress = correspondingAddress;
    }

    public String getGuardiansName() {
        return guardiansName;
    }

    public void setGuardiansName(String guardiansName) {
        this.guardiansName = guardiansName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Long getProgram() {
        return program;
    }

    public void setProgram(Long program) {
        this.program = program;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "\"id\":\"" + id + "\",\"studentsName\":\"'" + studentsName + "\",\"program\":\"" + program + "\",\"programName\":\"'" + programName + "\",\"classId\":\"" + classId + "\",\"className\":\"'" + className + "\",\"groupId\":\"" + groupId + "\",\"groupName\":\"'" + groupName + "\",\"section\":\"'" + section + "\",\"rollNo\":\"" + rollNo + "\",\"dateOfBirth\":\"'" + dateOfBirth + "\",\"mobileNo\":\"'" + mobileNo + "\",\"fathersName\":\"'" + fathersName + "\",\"fathersContactNo\":\"'" + fathersContactNo + "\",\"fathersOccupation\":\"'" + fathersOccupation + "\",\"mothersName\":\"'" + mothersName + "\",\"mothersOccupation\":\"'" + mothersOccupation + "\",\"mothersContactNo\":\"'" + mothersContactNo + "\",\"permanentAddress\":\"'" + permanentAddress + "\",\"correspondingAddress\":\"'" + correspondingAddress + "\",\"guardiansName\":\"'" + guardiansName + "\",\"registrationDate\":\"'" + registrationDate + "\",\"approved\":\"'" + approved + "\"}";
    }
}
