
package com.ms.ware.online.solution.school.entity.utility;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sent_sms")
public class SentSms {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "sms", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String sms;
    @Column(name = "send_by", length = 60)
    private String sendBy;
    @Column(name = "send_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @Column(name = "sms_count", columnDefinition = "INT DEFAULT 1")
    private int smsCount;

    public String getMobile() {
        return mobile == null || mobile.isEmpty() ? "N/A" : mobile;
    }

    public String getSms() {
        return sms == null || sms.isEmpty() ? "N/A" : sms;
    }

    public String getSendBy() {
        return sendBy == null || sendBy.isEmpty() ? "N/A" : sendBy; 
    }

    public Date getSendDate() {
        return sendDate;
    }

    public int getSmsCount() {
        return smsCount;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"mobile\": \"" + mobile + "\",\"sms\": \"" + sms + "\",\"sendBy\": \"" + sendBy + "\",\"sendDate\": \"" + sendDate + "\"}";
    }
}
