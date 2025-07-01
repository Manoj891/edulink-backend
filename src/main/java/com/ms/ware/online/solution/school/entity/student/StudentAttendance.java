package com.ms.ware.online.solution.school.entity.student;

import lombok.*;
import com.ms.ware.online.solution.school.config.DateConverted;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "student_attendance")
public class StudentAttendance implements java.io.Serializable {
    @EmbeddedId
    private StudentAttendancePK pk;
    @Column(name = "stu_id", insertable = false, updatable = false)
    private Long stuId;
    @Column(name = "att_date", insertable = false, updatable = false)
    private String attDate;
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
    @Column(name = "remark")
    private String remark;
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    @JoinColumn(name = "stu_id", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentInfo studentInfo;


    public StudentAttendance(String attDate, Long stuId, String status, String inTime, String outTime, String enterBy, String enterDate) {
        pk = StudentAttendancePK.builder().attDate(DateConverted.toDate(attDate)).stuId(stuId).build();
        this.status = status;
        this.inTime = inTime;
        this.outTime = outTime;
        this.enterBy = enterBy;
        this.enterDate = enterDate;
    }

    @Override
    public String toString() {
        return "\n{stuId=" + pk.getStuId() + ", attDate=" + DateConverted.toString(pk.getAttDate()) + ", status=" + status + ", inTime=" + inTime + ", outTime=" + outTime + ", enterBy=" + enterBy + ", enterDate=" + enterDate + "\"}";
    }
}
