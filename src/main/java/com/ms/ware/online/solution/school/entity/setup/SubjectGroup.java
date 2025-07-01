package com.ms.ware.online.solution.school.entity.setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subject_group")
public class SubjectGroup implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    public SubjectGroup() {
    }

    public SubjectGroup(Long id) {
        this.id = id;
    }

    public SubjectGroup(String id) {
        this.id = Long.parseLong(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return name;
    }

    public void setGroupName(String groupName) {
        this.name = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"groupName\": \"" + name + "\"}";
    }
}
