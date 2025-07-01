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
@Table(name = "program_master")
public class ProgramMaster implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name", unique = true, nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;
    @Column(name = "program_name")
    private String programName;

    public ProgramMaster() {
    }

    public ProgramMaster(Long id) {
        this.id = id;
    }

    public ProgramMaster(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProgramMaster(String id) {
        this.id = Long.parseLong(id);
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"name\": \"" + name + "\"}";
    }
}
