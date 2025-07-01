package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sender_email")
public class SenderEmail implements java.io.Serializable {

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "host")
    private String host;
    @Column(name = "password")
    private String password;
    @Column(name = "port")
    private String port;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"email\": \"" + email + "\",\"host\": \"" + host + "\",\"password\": \"" + password + "\",\"port\": \"" + port + "\"}";
    }
}
