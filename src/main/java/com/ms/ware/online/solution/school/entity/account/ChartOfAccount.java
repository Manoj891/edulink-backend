package com.ms.ware.online.solution.school.entity.account;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.Index;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "chart_of_account", uniqueConstraints = @UniqueConstraint(columnNames = {"AC_NAME", "MGR_CODE"}, name = "AC_NAME_UNIQUE"))
public class ChartOfAccount implements java.io.Serializable {

    @Id
    @Column(name = "ac_code", columnDefinition = "VARCHAR(30)")
    private String acCode;
    @Index(columnNames = "index_chart_of_account_detail_ac_name", name = "ac_name")
    @Column(name = "ac_name", columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String acName;
    @Column(name = "ac_sn", columnDefinition = "INT(6)", updatable = false)
    private Integer acSn;
    @Index(columnNames = "index_chart_of_account_detail_mgr_code", name = "MGR_CODE")
    @Column(name = "mgr_code", columnDefinition = "VARCHAR(30)", updatable = false)
    private String mgrCode;
    @Index(columnNames = "index_chart_of_account_detail_transact", name = "transact")
    @Column(name = "transact", columnDefinition = "VARCHAR(1)")
    private String transact;
    @Index(columnNames = "index_chart_of_account_detail_level", name = "level")
    @Column(name = "level", columnDefinition = "INT(2)", updatable = false)
    private Integer level;

    @Column(name = "unit", columnDefinition = "VARCHAR(3)")
    private String unit;
    @Column(name = "rate")
    private Double rate;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;


    @Override
    public String toString() {
        return "\n{\"acCode\": \"" + acCode + "\",\"acName\": \"" + acName + "\",\"acSn\": \"" + acSn + "\",\"mgrCode\": \"" + mgrCode + "\",\"transact\": \"" + transact + "\",\"level\": \"" + level + "\"}";
    }
}
