/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.entity.account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "voucher_delete")
public class VoucherDelete implements java.io.Serializable {

    @Id
    @Column(name = "VOUCHER_NO")
    private String voucherNo;
    @Column(name = "DELETE_BY")
    private String deleteBy;
    @Column(name = "DELETE_DATE")
    private String deleteDate;
    @Column(name = "DELETE_DATA",columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String deleteData;

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(String deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    public String getDeleteData() {
        return deleteData;
    }

    public void setDeleteData(String deleteData) {
        this.deleteData = deleteData;
    }

}
