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
@Table(name = "employee_attendance")
public class EmployeeAttendance implements java.io.Serializable {

    @EmbeddedId
    private EmployeeAttendancePK pk;
    @Column(name = "emp_id", insertable = false, updatable = false)
    private Long empId;
    @Column(name = "punch_date", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date punchDate;
    @Column(name = "status", columnDefinition = "VARCHAR(1)")
    private String status;
    @Column(name = "in_time", columnDefinition = "TIME", updatable = false)
    private String inTime;
    @Column(name = "out_time", columnDefinition = "TIME", insertable = false)
    private String outTime;
    @Column(name = "enter_by", columnDefinition = "VARCHAR(10)", updatable = false)
    private String enterBy;
    @Column(name = "enter_date", columnDefinition = "DATETIME", updatable = false)
    private String enterDate;

    @Column(name = "update_by", columnDefinition = "VARCHAR(10)", insertable = false)
    private String updateBy;
    @Column(name = "update_date", columnDefinition = "DATETIME", insertable = false)
    private String updateDate;
    @Column(name = "remark", length = 50)
    private String remark;


    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "emp_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EmployeeInfo employeeInfo;

    @Override
    public String toString() {
        return "\n{\"punchDate\": \"" + punchDate + "\",\"status\": \"" + status + "\",\"inTime\": \"" + inTime + "\",\"outTime\": \"" + outTime + "\"}";
    }
}
