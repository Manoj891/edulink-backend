/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.setup;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.setup.BillMasterDao;
import com.ms.ware.online.solution.school.entity.setup.BillMaster;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillMasterServiceImp implements BillMasterService {

    @Autowired
    private BillMasterDao da;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Object getAll() {
        return da.getAll("from BillMaster where id>0 order by name");
    }

    @Override
    public Object getAllRecord() {
        return da.getAll("from BillMaster order by name");
    }

    @Override
    public Object save(BillMaster obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String isInventory = "";
        try {
            isInventory = obj.getIsInventory();
            if (isInventory.length() == 0) {
                isInventory = "N";
            }
        } catch (Exception e) {
            isInventory = "N";
        }

        try {

            if (isInventory.equalsIgnoreCase("Y")) {
                sql = "SELECT IFNULL(STUDENT_INVENTORY_ACCOUNT,'') AS mgrCode FROM organization_master WHERE ID=1";
                message.list = da.getRecord(sql);
                if (message.list.isEmpty()) {
                    return message.respondWithError("Please define STUDENT INVENTORY ACCOUNT in organization master");
                }
                message.map = (Map) message.list.get(0);
                String mgrCode = message.map.get("mgrCode").toString();
                if (mgrCode.length() == 0) {
                    return message.respondWithError("Please define STUDENT INVENTORY ACCOUNT in organization master");
                }
                if (getAcCode(mgrCode, obj.getName() + " Inventory")) {
                    obj.setAcCode(acCode);
                } else {
                    return message.respondWithError(errorMessage);
                }

            } else if (obj.getAcCode().length() == 0) {
                sql = "SELECT IFNULL(STUDENT_FEE_INCOME_ACCOUNT,'') AS mgrCode FROM organization_master WHERE ID=1";
                message.list = da.getRecord(sql);
                if (message.list.isEmpty()) {
                    return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
                }
                message.map = (Map) message.list.get(0);
                String mgrCode = message.map.get("mgrCode").toString();
                if (mgrCode.length() == 0) {
                    return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
                }
                if (getAcCode(mgrCode, obj.getName() + " Income")) {
                    obj.setAcCode(acCode);
                } else {
                    return message.respondWithError(errorMessage);
                }
            }
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM bill_master";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            sql = "DELETE FROM chart_of_account WHERE `AC_CODE`='" + acCode + "'";
            da.delete(sql);
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(BillMaster obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String isInventory = "";
        try {
            isInventory = obj.getIsInventory();
            if (isInventory.length() == 0) {
                isInventory = "N";
            }
        } catch (Exception e) {
            isInventory = "N";
        }
        if (obj.getAcCode().length() == 0) {
            if (isInventory.equalsIgnoreCase("Y")) {
                sql = "SELECT IFNULL(STUDENT_INVENTORY_ACCOUNT,'') AS mgrCode FROM organization_master WHERE ID=1";
                message.list = da.getRecord(sql);
                if (message.list.isEmpty()) {
                    return message.respondWithError("Please define STUDENT INVENTORY ACCOUNT in organization master");
                }
                message.map = (Map) message.list.get(0);
                String mgrCode = message.map.get("mgrCode").toString();
                if (mgrCode.length() == 0) {
                    return message.respondWithError("Please define STUDENT INVENTORY ACCOUNT in organization master");
                }
                if (getAcCode(mgrCode, obj.getName() + " Inventory")) {
                    obj.setAcCode(acCode);
                } else {
                    return message.respondWithError(errorMessage);
                }

            } else if (obj.getAcCode().length() == 0) {
                sql = "SELECT IFNULL(STUDENT_FEE_INCOME_ACCOUNT,'') AS mgrCode FROM organization_master WHERE ID=1";
                message.list = da.getRecord(sql);
                if (message.list.isEmpty()) {
                    return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
                }
                message.map = (Map) message.list.get(0);
                String mgrCode = message.map.get("mgrCode").toString();
                if (mgrCode.length() == 0) {
                    return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
                }
                if (getAcCode(mgrCode, obj.getName() + " Income")) {
                    obj.setAcCode(acCode);
                } else {
                    return message.respondWithError(errorMessage);
                }
            }
        }
        obj.setId(id);
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
        sql = "SELECT AC_CODE acCode FROM bill_master WHERE ID=" + id + "";
        message.map = (Map) da.getRecord(sql).get(0);
        acCode = message.map.get("acCode").toString();
        sql = "DELETE FROM bill_master WHERE ID=" + id + "";
        row = da.delete(sql);
        sql = "DELETE FROM chart_of_account WHERE `AC_CODE`='" + acCode + "'";
        da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }


    @Override
    public Object account() {
        sql = "SELECT IFNULL(STUDENT_FEE_INCOME_ACCOUNT,'') AS acCode FROM organization_master";
        message.list = da.getRecord(sql);
        if (message.list.isEmpty()) {
            return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
        }
        message.map = (Map) message.list.get(0);
        String acCode = message.map.get("acCode").toString();
        if (acCode.length() == 0) {
            return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
        }
        sql = "SELECT IFNULL(STUDENT_INVENTORY_ACCOUNT,'') AS acCode FROM organization_master";
        message.list = da.getRecord(sql);
        if (message.list.isEmpty()) {
            return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
        }
        message.map = (Map) message.list.get(0);
        String acCode1 = message.map.get("acCode").toString();
        sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE (MGR_CODE='" + acCode + "' OR MGR_CODE='" + acCode1 + "' ) AND TRANSACT='Y'";
        return da.getRecord(sql);
    }

    String acCode, errorMessage;

    public boolean getAcCode(String mgrCode, String acName) {
        try {
            acCode = "";
            sql = "SELECT LEVEL level FROM chart_of_account WHERE AC_CODE='" + mgrCode + "'";
            message.map = (Map) da.getRecord(sql).get(0);
            int level = Integer.parseInt(message.map.get("level").toString());
            sql = "SELECT ifnull(MAX(AC_SN),0)+1 AS id FROM chart_of_account WHERE MGR_CODE='" + mgrCode + "'";
            message.map = (Map) da.getRecord(sql).get(0);
            int sn = Integer.parseInt(message.map.get("id").toString());

            if (sn < 10) {
                acCode = mgrCode + "0" + sn;
            } else {
                acCode = mgrCode + sn;
            }
            sql = "INSERT INTO chart_of_account (AC_CODE, AC_NAME, AC_SN, MGR_CODE, TRANSACT, LEVEL) VALUES ('" + acCode + "', '" + acName + "', " + sn + ", '" + mgrCode + "', 'Y', " + level + ")";
            row = da.delete(sql);
            if (row == 1) {
                return true;
            } else {
                errorMessage = da.getMsg();
            }

        } catch (Exception e) {
            errorMessage = e.getMessage();
            return false;
        }

        return false;
    }
}
