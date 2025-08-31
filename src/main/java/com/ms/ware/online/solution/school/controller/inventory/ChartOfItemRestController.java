/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ms.ware.online.solution.school.controller.inventory;

import java.util.List;
import java.util.Map;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/Inventory/ChartOfItem")
public class ChartOfItemRestController {
    @Autowired
    private DB db;
    @Autowired
    private Message message;

    @GetMapping
    public Object index() {
        String sql;

        sql = "SELECT `INVENTORY_ACCOUNT` AS inventoryAccount FROM organization_master";
        List list = db.getRecord(sql);
        if (list.isEmpty()) {
            return message.respondWithError("Please define INVENTORY_ACCOUNT in organization_master");
        }
        Map map = (Map) list.get(0);
        String inventoryAccount = map.get("inventoryAccount").toString();
        sql = "SELECT AC_CODE acCode,AC_NAME acName,MGR_CODE mgrCode,AC_SN acSn,LEVEL level,TRANSACT transact,IFNULL((SELECT I.AC_NAME FROM chart_of_account I WHERE I.AC_CODE=O.MGR_CODE),'Main Group') AS mgrName FROM chart_of_account O WHERE TRANSACT='N' AND (AC_CODE LIKE '" + inventoryAccount + "%' OR AC_CODE LIKE '101%') ORDER BY AC_CODE";
        return db.getRecord(sql);
    }

}
