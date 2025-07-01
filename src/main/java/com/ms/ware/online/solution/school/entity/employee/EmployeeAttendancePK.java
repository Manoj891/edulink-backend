
package com.ms.ware.online.solution.school.entity.employee;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
@EqualsAndHashCode
public class EmployeeAttendancePK implements Serializable {

    @Column(name = "emp_id")
    private Long empId;
    @Column(name = "punch_date")
    @Temporal(TemporalType.DATE)
    private Date punchDate;


}
