/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.inventory;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.inventory.InventoryLedgerDao;
import com.ms.ware.online.solution.school.entity.inventory.InventoryLedger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Inventory/IssueInDepartment")
public class IssueInDepartmentRestController {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    InventoryLedgerDao da;

    @GetMapping
    public Object index(@RequestParam(required = false) Long department) {

        String sql;
        sql = "SELECT D.NAME department,IFNULL(L.SPECIFICATION,'') specification,C.AC_NAME acName,IFNULL(L.UNIT,'') unit,L.RATE rate,L.IN_QTY inQty,L.OUT_QTY outQty,L.EMP_NAME empName,GET_BS_DATE(L.ENTER_DATE) enterDate,L.ENTER_BY enterBy FROM inventory_ledger L,department_master D,chart_of_account C WHERE L.DEPARTMENT=D.ID AND L.AC_CODE=C.AC_CODE AND `DEPARTMENT`=IFNULL(null,`DEPARTMENT`)";
        return da.getRecord(sql);
    }

    @PostMapping
    public Object doSave(@RequestBody List<InventoryLedger> list) {
        Message message = new Message();
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid Authorization");
        }
        String userId = td.getUserId();
        if (userId.length() == 1) userId = "00" + userId;
        else if (userId.length() == 2) userId = "0" + userId;
        DecimalFormat df = new DecimalFormat("#.##");
        String msg = "", userName = td.getUserName();
        InventoryLedger obj;
        String sql, id, transactionNo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + userId;
        int count = 0;
        Map map;

        for (int i = 0; i < list.size(); i++) {
            obj = new InventoryLedger();
            try {
                if (i < 10) id = transactionNo + "00" + (i + 1);
                else if (i < 100) id = transactionNo + "0" + (i + 1);
                else id = transactionNo + "" + (i + 1);
                obj.setTransactionNo(transactionNo);
                obj.setAcCode(list.get(i).getAcCode());
                sql = "select sum(ifnull(in_qty,0))-sum(ifnull(out_qty,0)) as restQty,ac_name acName FROM inventory_ledger L,chart_of_account C WHERE L.ac_code=C.ac_code AND L.ac_code='" + obj.getAcCode() + "'";
                map = (Map) da.getRecord(sql).get(0);
                System.out.println(map);
                double restQty =Double.parseDouble(map.get("restQty").toString());
                System.out.println("---------"+restQty);
                if (restQty < list.get(i).getOutQty()) {
                    return message.respondWithError("insufficient quantity of " + map.get("acName").toString());
                }
                sql = "select ifnull(rate,0) rate ,ifnull(unit,'') unit from chart_of_account where `ac_code`='" + obj.getAcCode() + "'";
                map = (Map) da.getRecord(sql).get(0);
                obj.setRate(Float.parseFloat(df.format(map.get("rate"))));
                obj.setUnit(map.get("unit").toString());
                obj.setVat(0f);
                obj.setOrderNo(-2l);
                obj.setSupplier(-2l);
                obj.setSpecification(list.get(i).getSpecification());
                obj.setId(id);
                obj.setEnterBy(userName);
                obj.setEnterDateAd(list.get(i).getEnterDateAd());
                obj.setOutQty(list.get(i).getOutQty());
                obj.setApproveDate(obj.getEnterDateAd());
                obj.setApproveBy(userName);
                obj.setDepartment(list.get(i).getDepartment());
                obj.setEmpName(list.get(i).getEmpName());
                obj.setTotalAmount(obj.getOutQty() * obj.getRate());
                obj.setInQty(0f);
                System.out.println(obj);
                if (da.save(obj) == 0) {
                    msg = da.getMsg();
                    if (msg.contains("purchase_order") || msg.contains("sundry_creditors")) {
                        if (saveSupplier()) {
                            count += da.save(obj);
                        }
                    }
                } else {
                    count++;
                }
            } catch (Exception e) {
                msg = e.getMessage().toLowerCase();

            }
        }
        if (count == 0) {
            return message.respondWithError(msg, transactionNo);
        }
        return message.respondWithMessage(count + " Record Saved!!");
    }

    boolean saveSupplier() {
        String sql = "INSERT INTO sundry_creditors (ID, AC_CODE, ADDRESS, CONTACT_NO, NAME, PAN_VAT_NO) VALUES (-2, null, '', '', 'Item Issue to Department', '');";
        int creditors = da.delete(sql);
        sql = "INSERT INTO purchase_order (ORDER_NO, APPROVE_BY, APPROVE_DATE, ENTER_BY, ENTER_DATE, FISCAL_YEAR, NARRATION, ORDER_SN, STATUS, SUPPLIER, WITHIN_DATE, OPENING) VALUES (-2, '', '2020-03-08', '', '2020-03-08', 7677, '', 1, 'A', -2, '2020-03-08', 'N');";
        int order = da.delete(sql);
        if (creditors == 1 || order == 1) {
            return true;
        } else {
            return false;
        }
    }
}
