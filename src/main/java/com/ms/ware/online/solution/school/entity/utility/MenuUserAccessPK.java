/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.utility;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
@EqualsAndHashCode
@Embeddable
public class MenuUserAccessPK implements Serializable {

    @Column(name = "MENU")
    private Long menu;
    @Column(name = "USER_ID")
    private Long userId;

    public MenuUserAccessPK() {
    }

    public MenuUserAccessPK(Long menu, Long userId) {
        this.menu = menu;
        this.userId = userId;
    }

    public Long getMenu() {
        return menu;
    }

    public void setMenu(Long menu) {
        this.menu = menu;
    }

  

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
}
