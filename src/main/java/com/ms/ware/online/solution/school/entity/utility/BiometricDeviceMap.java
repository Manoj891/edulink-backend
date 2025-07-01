package com.ms.ware.online.solution.school.entity.utility;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "biometric_device_map")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiometricDeviceMap {
    @Id
    @Column(name = "id", length = 50)
    private String id;
    @Column(name = "user_type", length = 10)
    private String userType;
    @Column(name = "student_id")
    private Long studentId;
    @Column(name = "emp_id")
    private Long empId;
    @Column(name = "device_id")
    private Long deviceId;
    @Column(name = "device_branch")
    private Integer deviceBranch;
}
