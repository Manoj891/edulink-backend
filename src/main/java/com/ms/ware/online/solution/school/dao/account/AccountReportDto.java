/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.dao.account;

public class AccountReportDto {

    private Object acCode;
    private Object acName;
    private Object transact;
    private double opening;
    private double cr;
    private double dr;

    public AccountReportDto(Object acCode, Object acName, Object transact, double opening, double cr, double dr) {
        this.acCode = acCode;
        this.acName = acName;
        this.transact = transact;
        this.opening = opening;
        this.cr = cr;
        this.dr = dr;
    }

    public Object getAcCode() {
        return acCode;
    }

    public void setAcCode(Object acCode) {
        this.acCode = acCode;
    }

    public Object getAcName() {
        return acName;
    }

    public void setAcName(Object acName) {
        this.acName = acName;
    }

    public Object getTransact() {
        return transact;
    }

    public void setTransact(Object transact) {
        this.transact = transact;
    }

    public double getOpening() {
        return opening;
    }

    public void setOpening(double opening) {
        this.opening = opening;
    }

    public double getCr() {
        return cr;
    }

    public void setCr(double cr) {
        this.cr = cr;
    }

    public double getDr() {
        return dr;
    }

    public void setDr(double dr) {
        this.dr = dr;
    }

   
}
