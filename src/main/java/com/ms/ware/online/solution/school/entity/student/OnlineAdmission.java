package com.ms.ware.online.solution.school.entity.student;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.CastEthnicityMaster;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.ReligionMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "online_admission", uniqueConstraints = @UniqueConstraint(columnNames = {"ACADEMIC_YEAR", "ACADEMIC_YEAR", "PROGRAM", "CLASS_ID", "STU_NAME", "MOBILE_NO"}, name = "STUDENT_NAME"))
public class OnlineAdmission implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "SN", updatable = false)
    private int sn;
    @Column(name = "ACADEMIC_YEAR")
    private Long academicYear;
    @Column(name = "PROGRAM")
    private Long program;
    @Column(name = "CLASS_ID")
    private Long classId;
    @Column(name = "SUBJECT_GROUP")
    private Long subjectGroup;
    @Column(name = "STU_NAME")
    private String stuName;
    @Column(name = "DATE_OF_BIRTH", columnDefinition = "VARCHAR(15)")
    private String dateOfBirth;

    @Column(name = "MOBILE_NO")
    private String mobileNo;
    @Column(name = "ALTERNATIVE_MOBILE")
    private String alternativeMobile;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "GENDER", columnDefinition = "VARCHAR(1)")
    private String gender;
    @Column(name = "PROVINCE")
    private Integer province;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "MUNICIPAL")
    private String municipal;
    @Column(name = "WARD_NO")
    private String wardNo;
    @Column(name = "TOL")
    private String tol;
    @Column(name = "PROVINCET")
    private Integer provincet;
    @Column(name = "DISTRICTT")
    private String districtt;
    @Column(name = "MUNICIPALT")
    private String municipalt;
    @Column(name = "WARD_NOT")
    private String wardNot;
    @Column(name = "TOLT")
    private String tolt;
    @Column(name = "STATUS", updatable = false)
    private String status;
    @Column(name = "STUDENT_PHOTO", updatable = false)
    private String studentPhoto;
    @Column(name = "DROP_OUT", updatable = false)
    private String dropOut;
    @Column(name = "RELIGION")
    private Integer religion;
    @Column(name = "CAST_ETHNICITY")
    private Integer castEthnicity;
    @Column(name = "PRE_SCHOOL")
    private String preSchool;

    @JoinColumn(name = "RELIGION", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ReligionMaster religionMaster;

    @JoinColumn(name = "CAST_ETHNICITY", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CastEthnicityMaster castEthnicityMaster;

    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProgramMaster programMaster;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ClassMaster classMaster;
    @JoinColumn(name = "ACADEMIC_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AcademicYear academicYearMaster;
    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubjectGroup subjectGroupMaster;
    @Column(name = "DISABILITY")
    private String disability;
    @Column(name = "CITIZENSHIP")
    private String citizenship;
    @Column(name = "MARITAL_STATUS", columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String maritalStatus;
    @Column(name = "FATHERS_NAME")
    private String fathersName;
    @Column(name = "FATHERS_MOBILE")
    private String fathersMobile;
    @Column(name = "FATHERS_OCCUPATION")
    private String fathersOccupation;
    @Column(name = "FATHERS_DESIGNATION")
    private String fathersDesignation;
    @Column(name = "FATHERS_QUALIFICATION")
    private String fathersQualification;
    @Column(name = "MOTHERS_NAME")
    private String mothersName;
    @Column(name = "MOTHERS_MOBILE")
    private String mothersMobile;
    @Column(name = "MOTHERS_OCCUPATION")
    private String mothersOccupation;
    @Column(name = "MOTHERS_DESIGNATION")
    private String mothersDesignation;
    @Column(name = "MOTHERS_QUALIFICATION")
    private String mothersQualification;
    @Column(name = "GUARDIANS_NAME")
    private String guardiansName;
    @Column(name = "GUARDIANS_RELATION")
    private String guardiansRelation;
    @Column(name = "GUARDIANS_MOBILE")
    private String guardiansMobile;
    @Column(name = "GUARDIANS_ADDRRESS")
    private String guardiansAddrress;
    @Column(name = "ENTER_DATE", columnDefinition = "DATETIME")
    @Temporal(TemporalType.DATE)
    private Date enterDate;

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getMaritalStatus() {
        try {
            if (maritalStatus == null) {
                return "Unmarried";
            }
        } catch (Exception e) {
            return "Unmarried";
        }
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getFathersOccupation() {
        try {
            if (fathersOccupation == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return fathersOccupation;
    }

    public void setFathersOccupation(String fathersOccupation) {
        this.fathersOccupation = fathersOccupation;
    }

    public String getFathersDesignation() {
        try {
            if (fathersDesignation == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return fathersDesignation;
    }

    public void setFathersDesignation(String fathersDesignation) {
        this.fathersDesignation = fathersDesignation;
    }

    public String getFathersQualification() {
        try {
            if (fathersQualification == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return fathersQualification;
    }

    public void setFathersQualification(String fathersQualification) {
        this.fathersQualification = fathersQualification;
    }

    public String getMothersName() {
        return mothersName;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public String getMothersOccupation() {
        try {
            if (mothersOccupation == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return mothersOccupation;
    }

    public void setMothersOccupation(String mothersOccupation) {
        this.mothersOccupation = mothersOccupation;
    }

    public String getMothersDesignation() {
        try {
            if (mothersDesignation == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return mothersDesignation;
    }

    public void setMothersDesignation(String mothersDesignation) {
        this.mothersDesignation = mothersDesignation;
    }

    public String getMothersQualification() {
        try {
            if (mothersQualification == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return mothersQualification;
    }

    public void setMothersQualification(String mothersQualification) {
        this.mothersQualification = mothersQualification;
    }

    public String getGuardiansName() {
        try {
            if (mothersQualification == null) {
                return fathersName;
            }
        } catch (Exception e) {
            return fathersName;
        }
        return guardiansName;
    }

    public void setGuardiansName(String guardiansName) {
        this.guardiansName = guardiansName;
    }

    public String getGuardiansRelation() {
        try {
            if (guardiansRelation == null) {
                return "Father";
            }
        } catch (Exception e) {
            return "Father";
        }
        return guardiansRelation;
    }

    public void setGuardiansRelation(String guardiansRelation) {
        this.guardiansRelation = guardiansRelation;
    }

    public String getGuardiansMobile() {
        try {
            if (guardiansMobile == null) {
                return fathersMobile;
            }
        } catch (Exception e) {
            return fathersMobile;
        }
        return guardiansMobile;
    }

    public void setGuardiansMobile(String guardiansMobile) {
        this.guardiansMobile = guardiansMobile;
    }

    public String getGuardiansAddrress() {
        return guardiansAddrress;
    }

    public void setGuardiansAddrress(String guardiansAddrress) {
        this.guardiansAddrress = guardiansAddrress;
    }

    public String getDisability() {
        try {
            if (disability == null || disability.length() == 0) {
                return "Normal";
            }
        } catch (Exception e) {
            return "Normal";
        }
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }

    public String getCitizenship() {
        try {
            if (citizenship == null) {
                return "Nepalese";
            }
        } catch (Exception e) {
            return "Nepalese";
        }
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public OnlineAdmission() {
    }

    public OnlineAdmission(Long id) {
        this.id = id;
    }

    public OnlineAdmission(String id) {
        this.id = Long.parseLong(id);
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Long academicYear) {
        this.academicYear = academicYear;
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

    public String getDropOut() {
        try {
            if (dropOut == null) {
                return "N";
            }
        } catch (Exception e) {
            return "N";
        }
        return dropOut;
    }

    public void setDropOut(String dropOut) {
        this.dropOut = dropOut;
    }

    public Long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getDateOfBirth() {
        try {
            if (dateOfBirth.length() == 10) {
                return dateOfBirth;
            }
        } catch (Exception e) {
        }
        return "";
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

    public String getAlternativeMobile() {
        try {
            if (alternativeMobile == null) {
                return "";
            }
            return alternativeMobile;
        } catch (Exception e) {
        }
        return "";
    }

    public void setAlternativeMobile(String alternativeMobile) {
        this.alternativeMobile = alternativeMobile;
    }

    public String getEmail() {
        try {
            if (email == null) {
                return "";
            }
            return email;
        } catch (Exception e) {
        }
        return "";
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getTol() {
        return tol;
    }

    public void setTol(String tol) {
        this.tol = tol;
    }

    public Integer getProvincet() {
        return provincet;
    }

    public void setProvincet(Integer provincet) {
        this.provincet = provincet;
    }

    public String getDistrictt() {
        return districtt;
    }

    public void setDistrictt(String districtt) {
        this.districtt = districtt;
    }

    public String getMunicipalt() {
        return municipalt;
    }

    public void setMunicipalt(String municipalt) {
        this.municipalt = municipalt;
    }

    public String getWardNot() {
        return wardNot;
    }

    public void setWardNot(String wardNot) {
        this.wardNot = wardNot;
    }

    public String getTolt() {
        return tolt;
    }

    public void setTolt(String tolt) {
        this.tolt = tolt;
    }

//
//    public String getEnterDate() {
//        return DateConveter.adToBs(enterDate);
//    }
//
//    public void setEnterDate(String enterDate) {
//        System.out.println("-----------"+enterDate+"---------");
//        this.enterDate =DateConveter.toString(DateConveter.bsToAdDate(enterDate));
//    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentPhoto() {

        try {
            if (studentPhoto.length() <= 5) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return studentPhoto;

    }

    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    public Integer getReligion() {
        return religion;
    }

    public void setReligion(Integer religion) {
        this.religion = religion;
    }

    public Integer getCastEthnicity() {
        return castEthnicity;
    }

    public void setCastEthnicity(Integer castEthnicity) {
        this.castEthnicity = castEthnicity;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPreSchool() {
        try {
            if (preSchool.length() > 0) {
                return preSchool;
            }
        } catch (Exception e) {
        }
        return "";
    }

    public void setPreSchool(String preSchool) {
        this.preSchool = preSchool;
    }

    public String getFathersMobile() {
        try {
            if (fathersMobile == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return fathersMobile;
    }

    public void setFathersMobile(String fathersMobile) {
        this.fathersMobile = fathersMobile;
    }

    public String getMothersMobile() {
        try {
            if (mothersMobile == null) {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
        return mothersMobile;
    }

    public void setMothersMobile(String mothersMobile) {
        this.mothersMobile = mothersMobile;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"gender\": \"" + gender + "\",\"academicYear\": \"" + academicYear + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"subjectGroup\": \"" + subjectGroup + "\",\"stuName\": \"" + stuName + "\",\"dateOfBirth\": \"" + dateOfBirth + "\",\"fathersName\": \"" + fathersName + "\",\"mothersName\": \"" + mothersName + "\",\"mobileNo\": \"" + mobileNo + "\",\"alternativeMobile\": \"" + alternativeMobile + "\",\"email\": \"" + email + "\",\"province\": \"" + province + "\",\"district\": \"" + district + "\",\"municipal\": \"" + municipal + "\",\"wardNo\": \"" + wardNo + "\",\"tol\": \"" + tol + "\",\"provincet\": \"" + provincet + "\",\"districtt\": \"" + districtt + "\",\"municipalt\": \"" + municipalt + "\",\"wardNot\": \"" + wardNot + "\",\"tolt\": \"" + tolt + "\",\"enterDate\": \"\",\"status\": \"" + status + "\",\"photo\": \"" + studentPhoto + "\"}";
    }
}
