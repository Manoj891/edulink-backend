package com.ms.ware.online.solution.school.entity.student;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transportation_hostel_bill_generated")
public class TransportationHostelBillGenerated {

    @Id
    @Column(name = "id", length = 50)
    private String id;
    @Column(name = "reg_no", nullable = false)
    private Long regNo;
    @Column(name = "generated_month")
    private Integer generatedMonth;

    @Column(name = "hostel_amount")
    private double hostelAmount;

    @Column(name = "transportation_amount")
    private double transportationAmount;

    @JoinColumn(name = "reg_no", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private StudentInfo studentInfo;

}
