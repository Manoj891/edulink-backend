package com.ms.ware.online.solution.school.entity.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "exam_terminal")
public class ExamTerminal implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "final_terminal", length = 1, columnDefinition = "varchar(1) default 'N'")
    private String finalTerminal;
    @Column(name = "final_percent", columnDefinition = "double default '0'")
    private Float finalPercent;

    public ExamTerminal() {
    }

    public ExamTerminal(Long id) {
        this.id = id;
    }

    public ExamTerminal(String id) {
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

    public String getFinalTerminal() {
        return finalTerminal;
    }

    public void setFinalTerminal(String finalTerminal) {
        this.finalTerminal = finalTerminal;
    }

    public Float getFinalPercent() {
        return finalPercent;
    }

    public void setFinalPercent(Float finalPercent) {
        this.finalPercent = finalPercent;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"name\": \"" + name + "\"}";
    }
}
