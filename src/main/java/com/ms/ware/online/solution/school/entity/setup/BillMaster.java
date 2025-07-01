package com.ms.ware.online.solution.school.entity.setup;

import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bill_master")
public class BillMaster implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME",  nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "AC_CODE", columnDefinition = "VARCHAR(30)")
    private String acCode;
    @Column(name = "IS_INVENTORY", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String isInventory;
    @JoinColumn(name = "AC_CODE", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ChartOfAccount chartofaccount;

    public BillMaster() {
    }

    public BillMaster(Long id) {
        this.id = id;
    }

    public BillMaster(String id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public String getIsInventory() {
        return isInventory;
    }

    public void setIsInventory(String isInventory) {
        this.isInventory = isInventory;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"name\": \"" + name + "\",\"acCode\": \"" + acCode + "\"}";
    }
}
