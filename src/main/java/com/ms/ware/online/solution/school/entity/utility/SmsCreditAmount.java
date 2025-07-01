
package com.ms.ware.online.solution.school.entity.utility;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sms_credit_amount")
public class SmsCreditAmount {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "AMOUNT")
    private Double amount;
    @Column(name = "CREDIT_DATE")
    @Temporal(TemporalType.DATE)
    private Date creditDate;
    @Column(name = "CREDIT_BY")
    private String creditBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(Date creditDate) {
        this.creditDate = creditDate;
    }

    public String getCreditBy() {
        return creditBy;
    }

    public void setCreditBy(String creditBy) {
        this.creditBy = creditBy;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"amount\": \"" + amount + "\",\"creditDate\": \"" + creditDate + "\",\"creditBy\": \"" + creditBy + "\"}";
    }
}
