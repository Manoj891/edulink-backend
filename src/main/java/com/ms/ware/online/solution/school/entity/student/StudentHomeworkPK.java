/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.student;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Setter
@Getter
@Embeddable
@EqualsAndHashCode
public class StudentHomeworkPK implements Serializable {

    @Column(name = "stu_id")
    private Long stuId;
    @Column(name = "homework")
    private Long homework;

    public StudentHomeworkPK() {
    }

    public StudentHomeworkPK(Long stuId, Long homework) {
        this.stuId = stuId;
        this.homework = homework;
    }

}
