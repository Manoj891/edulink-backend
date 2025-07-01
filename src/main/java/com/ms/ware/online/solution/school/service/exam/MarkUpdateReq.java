/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.service.exam;

public class MarkUpdateReq {

    private long examRegNo;
    private long subject;
    private double thOm;
    private double phOm;

    public long getExamRegNo() {
        return examRegNo;
    }

    public void setExamRegNo(long examRegNo) {
        this.examRegNo = examRegNo;
    }

    public long getSubject() {
        return subject;
    }

    public void setSubject(long subject) {
        this.subject = subject;
    }

    public double getThOm() {
        return thOm;
    }

    public void setThOm(double thOm) {
        this.thOm = thOm;
    }

    public double getPhOm() {
        return phOm;
    }

    public void setPhOm(double phOm) {
        this.phOm = phOm;
    }
    

}
