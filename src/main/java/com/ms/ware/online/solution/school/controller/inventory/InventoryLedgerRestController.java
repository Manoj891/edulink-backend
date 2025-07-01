/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.inventory;


import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.inventory.InventoryLedgerDao;
import com.ms.ware.online.solution.school.entity.account.Voucher;
import com.ms.ware.online.solution.school.entity.account.VoucherDetail;
import com.ms.ware.online.solution.school.entity.inventory.InventoryLedger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.DateConverted;
import org.springframework.beans.factory.annotation.Autowired;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Inventory/Ledger")
public class InventoryLedgerRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    InventoryLedgerDao da;

    @GetMapping
    public Object index(@RequestParam(required = false) Long supplier) {

        String sql;
        sql = "SELECT GET_BS_DATE(M.ENTER_DATE) AS date,S.ID supplier,S.NAME AS supplierName,IFNULL(C.UNIT,'') unit,C.AC_CODE acCode,C.AC_NAME acName,IFNULL(D.SPECIFICATION,'') specification,D.ORDER_QTY-IFNULL(D.GRN_QTY,0) AS quantity,D.RATE rate,IFNULL(D.VAT,0) charge,D.TOTAL totalAmount,D.ORDER_NO orderNo FROM purchase_order M,purchase_order_detail D,chart_of_account C,sundry_creditors S WHERE D.ORDER_NO=M.ORDER_NO AND C.AC_CODE=D.AC_CODE AND M.SUPPLIER=S.ID AND D.ORDER_QTY-IFNULL(D.GRN_QTY,0)>0 AND IFNULL(D.DELETED,'N')!='Y'  AND M.SUPPLIER=IFNULL(" + supplier + ",M.SUPPLIER) ORDER BY supplier,date,acName";
        return da.getRecord(sql);
    }

    @GetMapping("/GRNReport")
    public Object findGRNReport(@RequestParam(required = false) Long supplier, @RequestParam String dateFrom, @RequestParam String dateTo) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        dateFrom = DateConverted.bsToAd(dateFrom);
        dateTo = DateConverted.bsToAd(dateTo);
        String sql;
        sql = "SELECT C.NAME AS supplierName,C.ADDRESS supplierAddress,C.CONTACT_NO supplierContact,P.ORDER_NO orderNo,GET_BS_DATE(P.ENTER_DATE) orderDate,P.NARRATION narration,GET_BS_DATE(L.ENTER_DATE) enterDate,SUM(L.TOTAL_AMOUNT) totalAmount,L.ENTER_BY enterBy,L.TRANSACTION_NO transactionNo FROM inventory_ledger L,purchase_order P,sundry_creditors C WHERE L.ORDER_NO=P.ORDER_NO AND P.SUPPLIER=C.ID AND P.SUPPLIER=IFNULL(" + supplier + ",P.SUPPLIER) AND L.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' GROUP BY TRANSACTION_NO";
        return da.getRecord(sql);
    }

    @GetMapping("/{transactionNo}")
    public Object findByTransactionNo(@PathVariable String transactionNo) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String sql;
        sql = "SELECT C.NAME AS supplierName,C.ADDRESS supplierAddress,C.CONTACT_NO supplierContact,P.ORDER_NO orderNo,GET_BS_DATE(P.ENTER_DATE) orderDate,P.NARRATION narration,GET_BS_DATE(L.ENTER_DATE) enterDate,SUM(L.TOTAL_AMOUNT) totalAmount,L.ENTER_BY enterBy FROM inventory_ledger L,purchase_order P,sundry_creditors C WHERE L.ORDER_NO=P.ORDER_NO AND P.SUPPLIER=C.ID AND TRANSACTION_NO='" + transactionNo + "' GROUP BY TRANSACTION_NO";
        List list = da.getRecord(sql);
        if (list.isEmpty()) {
            return message.respondWithError("invalid Transaction  No!!");
        }
        Map map = (Map) list.get(0);
        sql = "SELECT IFNULL(C.UNIT,'') unit,C.AC_NAME acName,C.AC_CODE acCode,L.IN_QTY qty,L.RATE rate,L.VAT vat,L.TOTAL_AMOUNT totalAmount,L.SPECIFICATION particular FROM inventory_ledger L,chart_of_account C WHERE L.AC_CODE=C.AC_CODE AND L.TRANSACTION_NO='" + transactionNo + "'";
        map.put("detail", da.getRecord(sql));
        return map;
    }

    @GetMapping("/Stock")
    public Object index() {

        String sql;
        sql = "SELECT IFNULL(C.UNIT,'') unit,SUM(IN_QTY)-SUM(OUT_QTY) AS quantity,C.AC_CODE acCode,C.AC_NAME acName FROM inventory_ledger L,chart_of_account C WHERE L.AC_CODE=C.AC_CODE GROUP BY C.AC_CODE";
        return da.getRecord(sql);
    }

    @GetMapping("/StudentIssue")
    public Object studentIssue() {
        String sql;
        sql = "SELECT B.AC_CODE acCode,B.NAME acName,CONCAT(D.BILL_NO,'-',D.BILL_SN) AS id,S.ID regNo,S.STU_NAME stuName,M.BILL_NO billNo FROM stu_billing_detail D,stu_billing_master M,bill_master B,student_info S WHERE M.BILL_NO=D.BILL_NO AND B.ID=D.BILL_ID AND M.REG_NO=S.ID AND D.DR>0 AND B.IS_INVENTORY='Y' AND IFNULL(INVENTORY_ISSUE,'')!='Y'";
        return da.getRecord(sql);
    }

    @PostMapping("/StudentIssue")
    public Object studentIssue(@RequestBody String jsonData) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String jsonDataArray[] = message.jsonDataToStringArray(jsonData);
        try {
            message.map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[0], new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
            String date = DateConverted.bsToAd(message.map.get("date").toString());
            String sql, userName = td.getUserName();
            message.list = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonDataArray[1], new com.fasterxml.jackson.core.type.TypeReference<>() {
            });
            int row = 0, a;
            Map map;
            String billNo, amount, acCode;
            for (int i = 0; i < message.list.size(); i++) {
                sql = "SELECT BILL_NO billNo,DR amount,B.AC_CODE acCode FROM stu_billing_detail D,bill_master B WHERE D.BILL_ID=B.ID AND CONCAT(BILL_NO,'-',BILL_SN)='" + message.list.get(i) + "'";
                map = (Map) da.getRecord(sql).get(0);
                billNo = map.get("billNo").toString();
                amount = map.get("amount").toString();
                acCode = map.get("acCode").toString();
                sql = "SELECT SUM(IN_QTY)-SUM(OUT_QTY) AS restQty,AC_NAME acName FROM inventory_ledger L,chart_of_account C WHERE L.AC_CODE=C.AC_CODE AND L.AC_CODE='" + acCode + "'";
                map = (Map) da.getRecord(sql).get(0);
                double restQty = Double.parseDouble(map.get("restQty").toString());
                if (restQty < 1) {
                    return message.respondWithError("insufficient quantity of " + map.get("acName").toString());
                }
                sql = "INSERT INTO inventory_ledger (ID, AC_CODE, APPROVE_BY, APPROVE_DATE, BILL_NO, ENTER_BY, ENTER_DATE, IN_QTY, ORDER_NO, OUT_QTY, RATE, SPECIFICATION, SUPPLIER, TOTAL_AMOUNT, TRANSACTION_NO, VAT) VALUES ('BIL" + billNo + "-" + acCode + "', '" + acCode + "', '" + userName + "', '" + date + "', '" + billNo + "', '" + userName + "', '" + date + "', 0, -1, 1, " + amount + ", '', -1, " + amount + ", '" + billNo + "', 0.0)";
                a = da.delete(sql);
                if (a == 0) {
                    if (da.getMsg().contains("FOREIGN KEY")) {
                        sql = "INSERT INTO sundry_creditors (ID, AC_CODE, ADDRESS, CONTACT_NO, NAME, PAN_VAT_NO) VALUES (-1, '1', '', '', 'Student inventory Issue', '');";
                        da.delete(sql);
                        sql = "INSERT INTO sundry_creditors (ID, AC_CODE, ADDRESS, CONTACT_NO, NAME, PAN_VAT_NO) VALUES (0, '101', '', '', 'Direct Purchase', '');";
                        da.delete(sql);
                        sql = "INSERT INTO purchase_order (ORDER_NO, APPROVE_BY, APPROVE_DATE, ENTER_BY, ENTER_DATE, FISCAL_YEAR, NARRATION, ORDER_SN, STATUS, SUPPLIER, WITHIN_DATE) VALUES (-1, '', '2020-03-08', '', '2020-03-08', 7677, '', 1, 'A', -1, '2020-03-08');";
                        da.delete(sql);
                        sql = "INSERT INTO purchase_order (ORDER_NO, APPROVE_BY, APPROVE_DATE, ENTER_BY, ENTER_DATE, FISCAL_YEAR, NARRATION, ORDER_SN, STATUS, SUPPLIER, WITHIN_DATE) VALUES (0, '', '2020-03-08', '', '2020-03-08', 7677, '', 1, 'A', 0, '2020-03-08');";
                        da.delete(sql);
                        sql = "INSERT INTO inventory_ledger (ID, AC_CODE, APPROVE_BY, APPROVE_DATE, BILL_NO, ENTER_BY, ENTER_DATE, IN_QTY, ORDER_NO, OUT_QTY, RATE, SPECIFICATION, SUPPLIER, TOTAL_AMOUNT, TRANSACTION_NO, VAT) VALUES ('BIL" + billNo + "-" + acCode + "', '" + acCode + "', '" + userName + "', '" + date + "', '" + billNo + "', '" + userName + "', '" + date + "', 0, -1, 1, " + amount + ", '', -1, " + amount + ", '" + billNo + "', 0.0);";
                        a = da.delete(sql);
                    }
                }

                if (a == 1) {
                    sql = "UPDATE stu_billing_detail SET INVENTORY_ISSUE='Y',INVENTORY_ISSUE_BY='" + userName + "',INVENTORY_ISSUE_DATE='" + date + "' WHERE CONCAT(BILL_NO,'-',BILL_SN)='" + message.list.get(i) + "'";
                    row += da.delete(sql);
                }

            }
            return message.respondWithMessage(row + " Item Issued!!");
        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @PostMapping("/StudentIssue/Report")
    public Object studentIssueReport(@RequestParam String dateFrom, @RequestParam String dateTo) {
        dateFrom = DateConverted.bsToAd(dateFrom);
        dateTo = DateConverted.bsToAd(dateTo);
        String sql;
//        sql = " SELECT C.AC_CODE acCode,C.AC_NAME acName,L.RATE AS billAmount,L.BILL_NO billNo,B.REG_NO regNo,S.STU_NAME studentName,S.FATHERS_NAME fatherName,GET_BS_DATE(B.ENTER_DATE) billDate,GET_BS_DATE(L.ENTER_DATE) issueDate FROM inventory_ledger L,chart_of_account C, stu_billing_master B,student_info S WHERE L.AC_CODE=C.AC_CODE AND L.BILL_NO=B.BILL_NO AND B.REG_NO=S.ID AND OUT_QTY>0 AND L.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' ORDER BY issueDate,billNo";
        sql = " SELECT C.AC_CODE acCode,C.AC_NAME acName,L.OUT_QTY AS billAmount,L.BILL_NO billNo,S.STU_NAME studentName,GET_BS_DATE(L.ENTER_DATE) issueDate,L.ENTER_BY enterBy,'Student' department,(L.RATE*L.OUT_QTY) amount FROM inventory_ledger L,chart_of_account C, stu_billing_master B,student_info S WHERE L.AC_CODE=C.AC_CODE AND L.BILL_NO=B.BILL_NO AND B.REG_NO=S.ID AND OUT_QTY>0 AND L.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' "
                + " UNION SELECT C.AC_CODE acCode,C.AC_NAME acName,L.OUT_QTY AS billAmount,'' billNo,EMP_NAME AS studentName,GET_BS_DATE(L.ENTER_DATE) issueDate,L.ENTER_BY enterBy,D.NAME department,(L.RATE*L.OUT_QTY) amount FROM inventory_ledger L,chart_of_account C, department_master D  WHERE L.AC_CODE=C.AC_CODE AND D.ID=L.DEPARTMENT AND L.ENTER_DATE BETWEEN '" + dateFrom + "' AND '" + dateTo + "' ";

        return da.getRecord(sql);
    }

    @PostMapping
    public Object doSave(@RequestBody List<InventoryLedger> list) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (list.isEmpty()) {
            return message.respondWithError("Please select item");
        }
        int row = 0;
        InventoryLedger obj;
        String voucherNo = "", voucherType = "JVR";
        String sql, msg = "", userName = td.getUserName(), enterDate = DateConverted.today();
        Map map;
        sql = "SELECT ID id FROM fiscal_year WHERE '" + enterDate + "' BETWEEN START_DATE AND END_DATE;";
        List l = da.getRecord(sql);
        if (l.isEmpty()) {
            msg = "Please define fiscal year of " + enterDate;
            return message.respondWithError(msg);
        }
        map = (Map) l.get(0);
        long fiscalYear = Long.parseLong(map.get("id").toString());
        sql = "SELECT IFNULL(max(VOUCHER_SN),0)+1 AS voucherSn FROM voucher WHERE FISCAL_YEAR='" + fiscalYear + "' AND VOUCHER_TYPE='" + voucherType + "'";
        map = (Map) da.getRecord(sql).get(0);
        int voucherSn = Integer.parseInt(map.get("voucherSn").toString());

        if (voucherSn < 10) {
            voucherNo = fiscalYear + "0000" + voucherSn + voucherType;
        } else if (voucherSn < 100) {
            voucherNo = fiscalYear + "000" + voucherSn + voucherType;
        } else if (voucherSn < 1000) {
            voucherNo = fiscalYear + "00" + voucherSn + voucherType;
        } else if (voucherSn < 10000) {
            voucherNo = fiscalYear + "0" + voucherSn + voucherType;
        } else {
            voucherNo = fiscalYear + "" + voucherSn + voucherType;
        }
        List<VoucherDetail> detail = new ArrayList<>();
        double totalCredit = 0;
        for (int i = 0; i < list.size(); i++) {
            obj = new InventoryLedger();
            try {
                if (i < 10) {
                    obj.setId(voucherNo + "-00" + (i + 1));
                } else if (i < 100) {
                    obj.setId(voucherNo + "-0" + (i + 1));
                } else {
                    obj.setId(voucherNo + "-" + (i + 1));
                }
                obj.setAcCode(list.get(i).getAcCode());
                obj.setInQty(list.get(i).getInQty());
                obj.setOrderNo(list.get(i).getOrderNo());
                obj.setSupplier(list.get(i).getSupplier());
                obj.setRate(list.get(i).getRate());
                obj.setVat(list.get(i).getVat());
                obj.setTotalAmount((obj.getInQty() * obj.getRate()) + obj.getVat());
                obj.setSpecification(list.get(i).getSpecification());
                obj.setEnterBy(userName);
                obj.setEnterDateAd(list.get(i).getEnterDateAd());
                obj.setOutQty(0f);
                obj.setApproveDate(obj.getEnterDateAd());
                obj.setApproveBy(userName);
                obj.setTransactionNo(voucherNo);
                if (da.save(obj) == 1) {
                    totalCredit = totalCredit + obj.getTotalAmount().doubleValue();
                    detail.add(new VoucherDetail(voucherNo, (i + 1), obj.getAcCode(), "Purchase item (" + obj.getInQty() + "x" + obj.getRate() + ")+" + obj.getVat(), obj.getTotalAmount().doubleValue(), 0d, "", ""));
                    sql = "UPDATE purchase_order_detail SET GRN_QTY=(IFNULL(GRN_QTY,0)+" + obj.getInQty() + ") WHERE AC_CODE='" + obj.getAcCode() + "' AND ORDER_NO='" + obj.getOrderNo() + "'";
                    da.delete(sql);
                    row++;
                }
            } catch (Exception e) {
                msg = e.getMessage();
            }
        }

        if (row > 0) {
            detail.add(new VoucherDetail(voucherNo, list.size() + 1, td.getCashAccount(), "Being Inventory " + list.size() + " item purchased.", 0d, totalCredit, "", ""));
            Voucher v = new Voucher(voucherNo);
            v.setVoucherNo(voucherNo);
            v.setVoucherType(voucherType);
            v.setFiscalYear(fiscalYear);
            v.setVoucherSn(voucherSn);
            v.setTotalAmount(totalCredit);
            v.setEnterBy(td.getUserName());
            v.setEnterDateAd(enterDate);
            v.setDetail(detail);
            da.save(v);
            return message.respondWithMessage(row + " Record Saved!!", voucherNo);
        } else {
            return message.respondWithError(msg);
        }
    }

    @DeleteMapping
    public Object rejectInventory(@RequestBody List<RejectGoodsReceived> list) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String userName = td.getUserName(), date = DateConverted.now();
        String sql;
        DB db = new DB();
        for (RejectGoodsReceived d : list) {
            sql = "UPDATE purchase_order_detail SET DELETED='Y',DELETED_BY='" + userName + "',DELETED_DATE='" + date + "' WHERE AC_CODE='" + d.getAcCode() + "' AND ORDER_NO='" + d.getOrderNo() + "' ";
            db.delete(sql);
        }
        return message.respondWithMessage("SUccess");
    }

    @PostMapping("/Opening")
    public Object doOpening(@RequestBody List<InventoryLedger> list) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        int row = 0;
        InventoryLedger obj;
        String sql, msg = "", userName = td.getUserName();

        long count = 0;
        Long transactionNo = 0l;
        for (int i = 0; i < list.size(); i++) {
            obj = new InventoryLedger();
            try {
                transactionNo = list.get(i).getOrderNo();
                obj.setTransactionNo(String.valueOf(transactionNo));
                obj.setAcCode(list.get(i).getAcCode());
                obj.setInQty(list.get(i).getInQty());
                obj.setOrderNo(list.get(i).getOrderNo());
                obj.setSupplier(list.get(i).getSupplier());
                obj.setRate(list.get(i).getRate());
                obj.setVat(list.get(i).getVat());
                obj.setTotalAmount((obj.getInQty() * obj.getRate()) + obj.getVat());
                obj.setSpecification(list.get(i).getSpecification());
                obj.setId(obj.getOrderNo() + "-" + obj.getAcCode());
                obj.setEnterBy(userName);
                obj.setEnterDateAd(list.get(i).getEnterDateAd());
                obj.setOutQty(0f);
                obj.setApproveDate(obj.getEnterDateAd());
                obj.setApproveBy(userName);

                row = da.save(obj);
                if (row == 1) {
                    sql = "UPDATE purchase_order_detail SET GRN_QTY=" + obj.getInQty() + " WHERE AC_CODE='" + obj.getAcCode() + "' AND ORDER_NO='" + obj.getOrderNo() + "'";
                    da.delete(sql);
                    count = count + row;
                }
            } catch (Exception e) {
                msg = e.getMessage();
            }
        }
        if (row > 0) {
            return message.respondWithMessage(count + " Record Saved!!", transactionNo);
        } else {
            return message.respondWithError(msg);
        }
    }
}
