/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.exam;


import com.ms.ware.online.solution.school.entity.setup.SubjectMaster;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;
import lombok.*;

import java.util.Date;
import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "exam_mark_entry", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"STUDENT_REG_NO", "EXAM", "SUBJECT"})
})
public class ExamMarkEntry {


    @EmbeddedId
    protected ExamMarkEntryPK pk;
    @Column(name = "EXAM_ROLL_NO", nullable = false)
    private String examRollNo;
    @Column(name = "STUDENT_REG_NO", nullable = false)
    private long studentRegNo;
    @Column(name = "EXAM", nullable = false)
    private long exam;
    @Column(name = "TH_OM", columnDefinition = "float(5,3) default 0")
    private Float thOm;
    @Column(name = "PR_OM", columnDefinition = "float(5,3)  default 0")
    private Float prOm;
    @Column(name = "extra_activity", length = 1)
    private String extraActivity;

    @Column(name = "t1t", columnDefinition = "float default 0")
    private Float t1t;
    @Column(name = "t2t", columnDefinition = "float default 0")
    private Float t2t;
    @Column(name = "t3t", columnDefinition = "float default 0")
    private Float t3t;
    @Column(name = "t1p", columnDefinition = "float default 0")
    private Float t1p;
    @Column(name = "t2p", columnDefinition = "float default 0")
    private Float t2p;
    @Column(name = "t3p", columnDefinition = "float default 0")
    private Float t3p;

    @Column(name = "ENTER_BY", nullable = false)
    private String enterBy;
    @Column(name = "APPROVE_BY")
    private String approveBy;
    @Column(name = "ENTER_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.DATE)
    private Date approveDate;

    @JoinColumn(name = "student_reg_no", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentInfo studentInfo;
    @JoinColumn(name = "exam_reg_id", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamStudentRegistration examStudentRegistration;
    @JoinColumn(name = "subject", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SubjectMaster subjectMaster;
    @JoinColumn(name = "exam", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamMaster examMaster;


    public ExamMarkEntry(long examRegId, long subject, long exam, String examRollNo, long studentRegNo, Float thOm, Float prOm, String enterBy, Date enterDate, String extraActivity) {
        pk = new ExamMarkEntryPK(examRegId, subject);
        this.examRollNo = examRollNo;
        this.studentRegNo = studentRegNo;
        this.thOm = thOm;
        this.prOm = prOm;
        this.enterBy = enterBy;
        this.enterDate = enterDate;
        this.exam = exam;
        this.extraActivity = extraActivity;
    }


}
