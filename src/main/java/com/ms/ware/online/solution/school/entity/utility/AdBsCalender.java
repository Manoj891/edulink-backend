package com.ms.ware.online.solution.school.entity.utility;

import lombok.*;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "ad_bs_calender")
public class AdBsCalender implements java.io.Serializable {

    @Id
    @Column(name = "ad_date")
    @Temporal(TemporalType.DATE)
    private Date adDate;
    @Column(name = "bs_date", updatable = false, unique = true)
    private String bsDate;
    @Column(name = "day", columnDefinition = "VARCHAR(3)", updatable = false)
    private String day;
    @Column(name = "school_holyday", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String schoolHolyday;
    @Column(name = "student_holyday", columnDefinition = "VARCHAR(1) DEFAULT 'N'")
    private String studentHolyday;
    @Column(name = "event", columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String event;


    @Override
    public String toString() {
        return "\n{\"adDate\": \"" + adDate + "\",\"bsDate\": \"" + bsDate + "\",\"day\": \"" + day + "\",\"schoolHolyday\": \"" + schoolHolyday + "\",\"studentHolyday\": \"" + studentHolyday + "\",\"event\": \"" + event + "\"}";
    }
}
