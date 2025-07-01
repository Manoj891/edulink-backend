/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author MS
 */
@Embeddable
public class PreviousEducationPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "REG_NO")
    private long regNo;
    @Basic(optional = false)
    @Column(name = "EDUCATION")
    private String education;

    public PreviousEducationPK() {
    }

    public PreviousEducationPK(long regNo, String education) {
        this.regNo = regNo;
        this.education = education;
    }

    public long getRegNo() {
        return regNo;
    }

    public void setRegNo(long regNo) {
        this.regNo = regNo;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) regNo;
        hash += (education != null ? education.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PreviousEducationPK)) {
            return false;
        }
        PreviousEducationPK other = (PreviousEducationPK) object;
        if (this.regNo != other.regNo) {
            return false;
        }
        if ((this.education == null && other.education != null) || (this.education != null && !this.education.equals(other.education))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject3.PreviousEducationPK[ regNo=" + regNo + ", education=" + education + " ]";
    }
    
}
