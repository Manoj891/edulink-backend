package com.ms.ware.online.solution.school.entity.utility;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_user_access")
public class MenuUserAccess implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MenuUserAccessPK pk;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private OrganizationUserInfo userId;
    @JoinColumn(name = "MENU", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private MenuMaster menu;
    @Column(name = "STATUS")
    private String status;

    public void setPk(MenuUserAccessPK pk) {
        this.pk = pk;
    }

    public OrganizationUserInfo getUserId() {
        return userId;
    }

    public void setUserId(OrganizationUserInfo userId) {
        this.userId = userId;
    }

    public MenuMaster getMenu() {
        return menu;
    }

    public void setMenu(MenuMaster menu) {
        this.menu = menu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\n{\"userId\":\"" + pk.getUserId() + "\",\"menu\":\"" + pk.getMenu() + "\",\"status\":\"" + status + "\"}";
    }

}
