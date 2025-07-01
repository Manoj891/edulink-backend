package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "about_app")
public class AboutApp implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "DESCRIPTION", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
