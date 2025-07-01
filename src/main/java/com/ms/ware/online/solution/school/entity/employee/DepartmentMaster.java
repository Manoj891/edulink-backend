package com.ms.ware.online.solution.school.entity.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "department_master")
public class DepartmentMaster implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;

    public DepartmentMaster() {
    }

    public DepartmentMaster(Long id) {
        this.id = id;
    }

    public DepartmentMaster(String id) {
        this.id = Long.parseLong(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"name\": \"" + name + "\"}";
    }
}
