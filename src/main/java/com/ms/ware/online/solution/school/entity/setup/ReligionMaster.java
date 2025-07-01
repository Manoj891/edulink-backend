package com.ms.ware.online.solution.school.entity.setup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "religion_master")
public class ReligionMaster implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;

    public ReligionMaster() {
    }

    public ReligionMaster(Integer id) {
        this.id = id;
    }

    public ReligionMaster(String id) {
        this.id = Integer.parseInt(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
