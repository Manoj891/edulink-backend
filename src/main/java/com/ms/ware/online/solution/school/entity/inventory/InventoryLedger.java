package com.ms.ware.online.solution.school.entity.inventory;

import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;
import com.ms.ware.online.solution.school.entity.employee.DepartmentMaster;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.ms.ware.online.solution.school.config.DateConverted;

@Entity
@Table(name = "inventory_ledger")
public class InventoryLedger implements java.io.Serializable {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "TRANSACTION_NO")
    private String transactionNo;
    @Column(name = "ORDER_NO", nullable = false)
    private Long orderNo;
    @Column(name = "AC_CODE", nullable = false)
    private String acCode;
    @Column(name = "IN_QTY", nullable = false)
    private Float inQty;
    @Column(name = "OUT_QTY", nullable = false)
    private Float outQty;
    @Column(name = "UNIT", nullable = true)
    private String unit;
    @Column(name = "RATE", nullable = false)
    private Float rate;
    @Column(name = "VAT", nullable = false)
    private Float vat;
    @Column(name = "TOTAL_AMOUNT", nullable = false)
    private Float totalAmount;
    @Column(name = "STU_REG_NO", nullable = true)
    private Long stuRegNo;
    @Column(name = "STAFF_ID", nullable = true)
    private Long staffId;
    @Column(name = "BILL_NO", nullable = true)
    private String billNo;
    @Column(name = "ENTER_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date enterDate;
    @Column(name = "ENTER_BY", nullable = false)
    private String enterBy;
    @Column(name = "APPROVE_DATE", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date approveDate;
    @Column(name = "APPROVE_BY", nullable = true)
    private String approveBy;
    @Column(name = "SUPPLIER", nullable = false)
    private Long supplier;
    @Column(name = "SPECIFICATION", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String specification;
    @JoinColumn(name = "SUPPLIER", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SundryCreditors sundryCreditors;
    @JoinColumn(name = "AC_CODE", referencedColumnName = "AC_CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ChartOfAccount chartOfAccount;

    @JoinColumn(name = "ORDER_NO", referencedColumnName = "ORDER_NO", nullable = true, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PurchaseOrder purchaseOrder;

    @JoinColumn(name = "DEPARTMENT", referencedColumnName = "ID", nullable = true, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DepartmentMaster departmentMaster;

//    @JoinColumn(name = "LEVEL", referencedColumnName = "ID", nullable = true, insertable = false, updatable = false)
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    private EmpLevelMaster empLevelMaster;
    @Column(name = "DEPARTMENT", columnDefinition = "BIGINT DEFAULT NULL")
    private Long department;
//    @Column(name = "LEVEL", columnDefinition = "BIGINT DEFAULT NULL")
//    private Long level;
    @Column(name = "EMP_NAME", columnDefinition = "TEXT CHARACTER SET utf8 COLLATE utf8_general_ci")
    private String empName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getAcCode() {
        return acCode;
    }

    public void setAcCode(String acCode) {
        this.acCode = acCode;
    }

    public Float getInQty() {
        return inQty;
    }

    public void setInQty(Float inQty) {
        this.inQty = inQty;
    }

    public Float getOutQty() {
        return outQty;
    }

    public void setOutQty(Float outQty) {
        this.outQty = outQty;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getVat() {
        return vat;
    }

    public void setVat(Float vat) {
        this.vat = vat;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getStuRegNo() {
        return stuRegNo;
    }

    public void setStuRegNo(Long stuRegNo) {
        this.stuRegNo = stuRegNo;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Date getEnterDateAd() {
        return enterDate;
    }

    public String getEnterDate() {
        return DateConverted.adToBs(enterDate);
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = DateConverted.bsToAdDate(enterDate);
    }

    public void setEnterDateAd(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public Long getSupplier() {
        return supplier;
    }

    public void setSupplier(Long supplier) {
        this.supplier = supplier;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "\n{\"id\": \"" + id + "\",\"orderNo\": \"" + orderNo + "\",\"acCode\": \"" + acCode + "\",\"specification\": \"" + specification + "\",\"inQty\": \"" + inQty + "\",\"outQty\": \"" + outQty + "\",\"rate\": \"" + rate + "\",\"vat\": \"" + vat + "\",\"totalAmount\": \"" + totalAmount + "\",\"stuRegNo\": \"" + stuRegNo + "\",\"staffId\": \"" + staffId + "\",\"billNo\": \"" + billNo + "\",\"enterDate\": \"" + enterDate + "\",\"enterBy\": \"" + enterBy + "\",\"approveDate\": \"" + approveDate + "\",\"approveBy\": \"" + approveBy + "\"}";
    }
}
