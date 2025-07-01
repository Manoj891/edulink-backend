/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.employee;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
public class EmpWorkingHourPK implements Serializable {

@Column(name = "EMP_ID")
private Long empId;
@Column(name = "WORKING_DAY", columnDefinition = "VARCHAR(3)")
private String workingDay;

    public EmpWorkingHourPK(Long empId, String workingDay) {
        this.empId = empId;
        this.workingDay = workingDay;
    }

    public EmpWorkingHourPK() {
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }


}
