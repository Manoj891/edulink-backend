package com.ms.ware.online.solution.school.entity.setup;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "class_master")
public class ClassMaster implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;
    @Column(name = "level")
    private String level;
    @Column(name = "degree_name")
    private String degreeName;

    public ClassMaster() {
    }

    public ClassMaster(Long id) {
        this.id = id;
    }

    public ClassMaster(String id) {
        this.id = Long.parseLong(id);
    }

    public ClassMaster(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"name\": \"" + name + "\"}";
    }
}
