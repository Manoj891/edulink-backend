package com.ms.ware.online.solution.school.entity.exam;

import com.ms.ware.online.solution.school.entity.setup.AcademicYear;
import com.ms.ware.online.solution.school.entity.setup.ClassMaster;
import com.ms.ware.online.solution.school.entity.setup.ProgramMaster;
import com.ms.ware.online.solution.school.entity.setup.SubjectGroup;
import com.ms.ware.online.solution.school.entity.student.StudentInfo;

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

import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "exam_student_registration", uniqueConstraints = @UniqueConstraint(columnNames = {"EXAM", "STUDENT_ID"}))
public class ExamStudentRegistration implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "EXAM", nullable = false)
    private Long exam;
    @Column(name = "STUDENT_ID", nullable = false)
    private Long studentId;
    @Column(name = "YEAR", nullable = false)
    private Long year;
    @Column(name = "PROGRAM", nullable = false)
    private Long program;
    @Column(name = "CLASS_ID", nullable = false)
    private Long classId;
    @Column(name = "SUBJECT_GROUP", nullable = false)
    private Long subjectGroup;
    @Column(name = "EXAM_ROLL_NO", insertable = false, updatable = false, unique = true, nullable = true)
    private String examRollNo;
    @Column(name = "EXAM_ROLL_SN", insertable = false, updatable = false, nullable = true)
    private Integer examRollSn;
    @Column(name = "PRESENT_DAYS", insertable = false, updatable = false, nullable = true)
    private Integer presentDays;
    @Column(name = "ABSENT_DAYS", insertable = false, updatable = false, nullable = true)
    private Integer absentDays;
    @Column(name = "REMARK", insertable = false, updatable = false, nullable = true)
    private String remark;
    @Column(name = "ENTER_BY", nullable = false)
    private String enterBy;
    @Column(name = "ENTER_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Column(name = "APPROVE_BY", nullable = true)
    private String approveBy;
    @Column(name = "APPROVE_DATE", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date approveDate;
    @Column(name = "board_symbol_no", length = 15)
    private String boardSymbolNo;

    @JoinColumn(name = "SUBJECT_GROUP", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectGroup subjectGroupMaster;
    @JoinColumn(name = "EXAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamMaster examMaster;
    @JoinColumn(name = "STUDENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private StudentInfo studentInfo;
    @JoinColumn(name = "YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AcademicYear academicYear;
    @JoinColumn(name = "PROGRAM", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProgramMaster programMaster;
    @JoinColumn(name = "CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ClassMaster classMaster;

    public ExamStudentRegistration() {
    }

    public ExamStudentRegistration(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExam() {
        return exam;
    }

    public void setExam(Long exam) {
        this.exam = exam;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
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

    public String getExamRollNo() {
        return examRollNo;
    }

    public void setExamRollNo(String examRollNo) {
        this.examRollNo = examRollNo;
    }

    public String getEnterBy() {
        return enterBy;
    }

    public Integer getExamRollSn() {
        return examRollSn;
    }

    public void setExamRollSn(Integer examRollSn) {
        this.examRollSn = examRollSn;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
    }

    public String getEnterDate() {
        return DateConverted.adToBs(enterDate);
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = DateConverted.bsToAdDate(enterDate);
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = (enterDate);
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public String getApproveDate() {
        return DateConverted.adToBs(approveDate);
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Long getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Long subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public Integer getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(Integer presentDays) {
        this.presentDays = presentDays;
    }

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"exam\": \"" + exam + "\",\"studentId\": \"" + studentId + "\",\"year\": \"" + year + "\",\"subjectGroup\": \"" + subjectGroup + "\",\"program\": \"" + program + "\",\"classId\": \"" + classId + "\",\"examRollNo\": \"" + examRollNo + "\",\"enterBy\": \"" + enterBy + "\",\"enterDate\": \"" + enterDate + "\",\"approveBy\": \"" + approveBy + "\",\"approveDate\": \"" + approveDate + "\"}";
    }
}
