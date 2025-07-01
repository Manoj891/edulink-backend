
package com.ms.ware.online.solution.school.entity.employee;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
@EqualsAndHashCode
public class EmpMonthlySalaryPK implements Serializable {
@Column(name = "EMP_ID")
private long empId;
@Column(name = "YEAR")
private int year;
@Column(name = "MONTH")
private int month;

    public EmpMonthlySalaryPK() {
    }

    public EmpMonthlySalaryPK(long empId, int year, int month) {
        this.empId = empId;
        this.year = year;
        this.month = month;
    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "{\"empId\":\"" + empId + "\",\"year\":\"" + year + "\",\"month\":\"" + month + "\"}";
    }

}
