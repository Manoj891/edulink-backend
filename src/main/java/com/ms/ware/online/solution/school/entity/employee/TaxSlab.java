package com.ms.ware.online.solution.school.entity.employee;

import com.ms.ware.online.solution.school.entity.account.FiscalYear;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tax_slab", uniqueConstraints = @UniqueConstraint(columnNames = {"FISCAL_YEAR", "PERCENTAGE"}))
public class TaxSlab implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "FISCAL_YEAR")
    private Long fiscalYear;
    @Column(name = "PERCENTAGE")
    private Float percentage;
    @Column(name = "COUPLE_INCOME")
    private Double coupleIncome;
    @Column(name = "INDIVIDUL_INCOME")
    private Double individulIncome;
    @JoinColumn(name = "FISCAL_YEAR", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private FiscalYear fy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(Long fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    public Double getCoupleIncome() {
        return coupleIncome;
    }

    public void setCoupleIncome(Double coupleIncome) {
        this.coupleIncome = coupleIncome;
    }

    public Double getIndividulIncome() {
        return individulIncome;
    }

    public void setIndividulIncome(Double individulIncome) {
        this.individulIncome = individulIncome;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"fiscalYear\": \"" + fiscalYear + "\",\"percentage\": \"" + percentage + "\",\"coupleIncome\": \"" + coupleIncome + "\",\"individulIncome\": \"" + individulIncome + "\"}";
    }
}
