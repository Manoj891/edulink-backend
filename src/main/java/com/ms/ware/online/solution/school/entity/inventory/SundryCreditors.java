package com.ms.ware.online.solution.school.entity.inventory;

import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sundry_creditors")
public class SundryCreditors implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "CONTACT_NO")
    private String contactNo;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PAN_VAT_NO")
    private String panVatNo;
    @Column(name = "AC_CODE", columnDefinition = "VARCHAR(30) DEFAULT NULL", unique = true)
    private String acCode;
    @JoinColumn(name = "AC_CODE", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ChartOfAccount chartofaccount;

    public SundryCreditors() {
    }

    public SundryCreditors(Long id) {
        this.id = id;
    }

    public SundryCreditors(String id) {
        this.id = Long.parseLong(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPanVatNo() {
        return panVatNo;
    }

    public void setPanVatNo(String panVatNo) {
        this.panVatNo = panVatNo;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"address\": \"" + address + "\",\"contactNo\": \"" + contactNo + "\",\"name\": \"" + name + "\",\"panVatNo\": \"" + panVatNo + "\",\"acCode\": \"" + acCode + "\"}";
    }
}
