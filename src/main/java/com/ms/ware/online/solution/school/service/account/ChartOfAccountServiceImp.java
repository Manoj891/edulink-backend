/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.account;


import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.account.ChartOfAccountDao;
import com.ms.ware.online.solution.school.entity.account.ChartOfAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ChartOfAccountServiceImp implements ChartOfAccountService {

    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private ChartOfAccountDao da;
    @Autowired
    private Message message;
    String msg = "", sql = "";
    int row;

    @Override
    public Object getAll() {
        sql = "SELECT AC_CODE acCode,AC_NAME acName,MGR_CODE mgrCode,AC_SN acSn,LEVEL level,TRANSACT transact,IFNULL((SELECT I.AC_NAME FROM chart_of_account I WHERE I.AC_CODE=O.MGR_CODE),'Main Group') AS mgrName FROM chart_of_account O WHERE TRANSACT='N' ORDER BY AC_CODE";
        message.list = da.getRecord(sql);
        if (message.list.isEmpty()) {
            setCHartOfAccount();
            message.list = da.getRecord(sql);
        }
        return message.list;
    }

    @Override
    public Object save(ChartOfAccount obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid authorization");
        }
        try {
            sql = "SELECT LEVEL level FROM chart_of_account WHERE AC_CODE='" + obj.getMgrCode() + "'";
            message.list = da.getRecord(sql);
            if (message.list.isEmpty()) {
                if (obj.getMgrCode().length() == 1) {
                    setCHartOfAccount();
                    sql = "SELECT LEVEL level FROM chart_of_account WHERE AC_CODE='" + obj.getMgrCode() + "'";
                    message.list = da.getRecord(sql);
                    if (message.list.isEmpty()) {
                        return message.respondWithError("Invalid Mgr COde ");
                    }
                } else {
                    return message.respondWithError("Invalid Mgr COde ");
                }

            }
            message.map = (Map) message.list.get(0);
            int level = Integer.parseInt(message.map.get("level").toString());
            try {
                sql = "SELECT ifnull(MAX(AC_SN),0)+1 AS id FROM chart_of_account WHERE MGR_CODE='" + obj.getMgrCode() + "'";
                message.list = da.getRecord(sql);
                message.map = (Map) message.list.get(0);
                int sn = Integer.parseInt(message.map.get("id").toString());
                String acCode = "";
                if (sn < 10) {
                    acCode = obj.getMgrCode() + "0" + sn;
                } else {
                    acCode = obj.getMgrCode() + sn;
                }
                obj.setAcCode(acCode);
                obj.setAcSn(sn);
                obj.setLevel(level + 1);
            } catch (Exception e) {
                return message.respondWithError("connection error or invalid table name");
            }
            obj.setUpdatedBy(td.getUserName());
            obj.setUpdatedDate(new Date());
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success", obj.getAcCode());
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(ChartOfAccount obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        obj.setUpdatedBy(td.getUserName());
        obj.setUpdatedDate(new Date());
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    @Override
    public Object delete(String id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        sql = "SELECT AC_CODE FROM chart_of_account WHERE MGR_CODE='" + id + "' LIMIT 10";
        message.list = da.getRecord(sql);
        if (!message.list.isEmpty() || message.list.size() > 0) {
            msg = "this record already used in reference tables, Cannot delete of this record";
            return message.respondWithError(msg);
        }
        sql = "DELETE FROM chart_of_account WHERE AC_CODE='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    void setCHartOfAccount() {
        sql = "INSERT INTO chart_of_account (AC_CODE, AC_NAME, AC_SN, MGR_CODE, TRANSACT, LEVEL) VALUES ('1', 'Assets', 1, NULL, 'N', 1);";
        da.delete(sql);
        sql = "INSERT INTO chart_of_account (AC_CODE, AC_NAME, AC_SN, MGR_CODE, TRANSACT, LEVEL) VALUES ('2', 'Liabilities', 2, NULL, 'N', 1);";
        da.delete(sql);
        sql = "INSERT INTO chart_of_account (AC_CODE, AC_NAME, AC_SN, MGR_CODE, TRANSACT, LEVEL) VALUES ('3', 'Income', 3, NULL, 'N', 1);";
        da.delete(sql);
        sql = "INSERT INTO chart_of_account (AC_CODE, AC_NAME, AC_SN, MGR_CODE, TRANSACT, LEVEL) VALUES ('4', 'Expense', 4, NULL, 'N', 1)";
        da.delete(sql);
    }

    @Override
    public Object getAll(String mgrCode) {

        sql = "SELECT AC_NAME mgrName FROM chart_of_account I WHERE AC_CODE='" + mgrCode + "'";
        message.list = da.getRecord(sql);
        message.map = (Map) message.list.get(0);
        String mgrName = message.map.get("mgrName").toString();
        sql = "SELECT AC_CODE acCode,AC_NAME acName,MGR_CODE mgrCode,AC_SN acSn,LEVEL level,IFNULL(UNIT,'') unit,IFNULL(RATE,'') rate,TRANSACT transact,'" + mgrName + "' AS mgrName FROM chart_of_account O WHERE MGR_CODE='" + mgrCode + "' AND TRANSACT='Y' ORDER BY AC_CODE";
        return da.getRecord(sql);
    }

    @Override
    public Object getByname(String name) {
        try {
            if (name.lastIndexOf(" ") >= 0) {
                name = name.substring(0, name.length());
            }
        } catch (Exception e) {
        }
        return da.getRecord("SELECT IFNULL(UNIT,'') unit,IFNULL(RATE,'') rate,AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE (AC_NAME LIKE '%" + name + "%'  OR AC_CODE LIKE '" + name + "%' ) AND TRANSACT='Y' ORDER BY acName");
    }

    @Override
    public Object getInventoryByname(String name) {

        try {
            if (name.lastIndexOf(" ") >= 0) {
                name = name.substring(0, name.length());
            }
        } catch (Exception e) {
        }
        return da.getRecord("SELECT IFNULL(UNIT,'') unit,IFNULL(RATE,'') rate,AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE AC_CODE LIKE CONCAT((SELECT INVENTORY_ACCOUNT FROM organization_master WHERE ID=1),'%') AND (AC_NAME LIKE '%" + name + "%'  OR AC_CODE LIKE '" + name + "%' ) AND TRANSACT='Y' ORDER BY acName");
    }

    @Override
    public Object getInventoryFixedAssetByname(String name) {

        try {
            if (name.lastIndexOf(" ") >= 0) {
                name = name.substring(0, name.length());
            }
        } catch (Exception e) {
        }
        return da.getRecord("SELECT IFNULL(UNIT,'') unit,IFNULL(RATE,'') rate,AC_CODE acCode,AC_NAME acName,'' AS storeQty FROM chart_of_account WHERE (AC_CODE LIKE CONCAT((SELECT INVENTORY_ACCOUNT FROM organization_master WHERE ID=1),'%') OR AC_CODE LIKE '101%') AND (AC_NAME LIKE '%" + name + "%'  OR AC_CODE LIKE '" + name + "%' ) AND TRANSACT='Y' ORDER BY acName");

    }

    @Override
    public Object getInventoryFixedAssetIssueByname(String name) {

        try {
            if (name.lastIndexOf(" ") >= 0) {
                name = name.substring(0, name.length());
            }
        } catch (Exception e) {
        }
        return da.getRecord("SELECT IFNULL(C.UNIT,'') unit,IFNULL(C.RATE,'') rate,C.AC_CODE acCode,C.AC_NAME acName,IFNULL(SUM(L.IN_QTY)-SUM(L.OUT_QTY),0) AS storeQty FROM chart_of_account C,inventory_ledger L WHERE L.AC_CODE=C.AC_CODE AND (C.AC_CODE LIKE CONCAT((SELECT INVENTORY_ACCOUNT FROM organization_master WHERE ID=1),'%') OR C.AC_CODE LIKE '101%') AND (C.AC_NAME LIKE '%" + name + "%'  OR C.AC_CODE LIKE '" + name + "%' ) GROUP BY L.AC_CODE ORDER BY acName");

    }

    @Override
    public List<Map<String, Object>> groupAccount() {
        return da.getRecord("select ac_code,ac_name from chart_of_account where TRANSACT='N' order by ac_code");
    }
}
