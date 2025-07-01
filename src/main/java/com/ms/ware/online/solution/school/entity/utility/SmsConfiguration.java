package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sms_configuration")
public class SmsConfiguration {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "request_ip")
    private String requestIp;
    @Column(name = "context")
    private String context;
    @Column(name = "token")
    private String token;
    @Column(name = "rate")
    private float rate;
    @Column(name = "created_by", length = 30)
    private String createdBy;
    @Column(name = "created_date", length = 30)
    private String createdDate;
    @Column(name = "send_from")
    private String sendFrom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }
}
