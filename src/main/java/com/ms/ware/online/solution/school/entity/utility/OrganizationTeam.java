package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organization_team")
public class OrganizationTeam implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "TEAM_TYPE")
    private String teamType;
    @Column(name = "MEMBER_NAME")
    private String memberName;
    @Column(name = "QUALIFICATION")
    private String qualification;
    @Column(name = "EMAIL", columnDefinition = "VARCHAR(255)")
    private String email;
    @Column(name = "MOBILE_NO", columnDefinition = "VARCHAR(30)")
    private String mobileNo;
    @Column(name = "ABOUT_MEMBER", columnDefinition = "TEXT")
    private String aboutMember;
    @Column(name = "STATUS", columnDefinition = "VARCHAR(1)")
    private String status;
    @Column(name = "PHOTO", insertable = false, updatable = false)
    private String photo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAboutMember() {
        return aboutMember;
    }

    public void setAboutMember(String aboutMember) {
        this.aboutMember = aboutMember;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"teamType\": \"" + teamType + "\",\"memberName\": \"" + memberName + "\",\"qualification\": \"" + qualification + "\",\"email\": \"" + email + "\",\"mobileNo\": \"" + mobileNo + "\",\"aboutMember\": \"" + aboutMember + "\",\"status\": \"" + status + "\",\"photo\": \"" + photo + "\"}";
    }

}
