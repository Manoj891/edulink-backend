package com.ms.ware.online.solution.school.entity.utility;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "biometric_log")
public class BiometricLog {
    @Id
    @Column(name = "id", length = 50)
    private String id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "branch")
    private int branch;
    @Column(name = "punch_date", columnDefinition = "date")
    private String punchDate;
    @Column(name = "punch_time", columnDefinition = "time")
    private String punchTime;
    @Column(name = "status", columnDefinition = "boolean default false")
    private boolean status;
}
