package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "menu_master", uniqueConstraints = @UniqueConstraint(name = "unique_menu", columnNames = {"menu", "menu_item"}))
public class MenuMaster implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "menu")
    private String menu;
    @Column(name = "menu_item")
    private String menuItem;
    @Column(name = "URI", unique = true)
    private String uri;

    public MenuMaster() {
    }

    public MenuMaster(Long id) {
        this.id = id;
    }

    public MenuMaster(String id) {
        this.id = Long.parseLong(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
