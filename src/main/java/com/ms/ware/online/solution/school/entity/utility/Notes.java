package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.*;

import com.ms.ware.online.solution.school.entity.student.StudentInfo;


import java.util.Date;


@Entity
@Table(name = "notes")
public class Notes implements java.io.Serializable {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;

    @Column(name = "student_id")
    private Long studentId;

    @JoinColumn(name = "student_id", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentInfo studentInfo;

    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"title\": \"" + title + "\",\"body\": \"" + body + "\",\"created\": \"" + created + "\"}";
    }
}