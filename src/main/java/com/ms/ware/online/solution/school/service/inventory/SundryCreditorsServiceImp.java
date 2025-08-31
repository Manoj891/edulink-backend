/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.inventory;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.inventory.SundryCreditorsDao;
import com.ms.ware.online.solution.school.entity.inventory.SundryCreditors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SundryCreditorsServiceImp implements SundryCreditorsService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private SundryCreditorsDao da;
    @Autowired
    private Message message;
    String msg = "", sql;
    int row;

    @Override
    public Object getAll() {
        return da.getAll("from SundryCreditors where id>0");
    }

    @Override
    public Object save(SundryCreditors obj) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            sql = "SELECT IFNULL(SUNDRY_CREDITORS,'') AS mgrCode FROM organization_master WHERE ID=1";
            message.list = da.getRecord(sql);
            if (message.list.isEmpty()) {
                return message.respondWithError("Please define SUNDRY CREDITORS ACCOUNT in organization master");
            }
            message.map = (Map) message.list.get(0);
            String mgrCode = message.map.get("mgrCode").toString();
            String acCode = getAcCode(mgrCode, obj.getName() + " Creditors");
            obj.setAcCode(acCode);
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM sundry_creditors";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));
            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            System.out.println(e);
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(SundryCreditors obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();
        ;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        String acCode = obj.getAcCode();
        if (acCode.length() == 0) {
            sql = "SELECT IFNULL(SUNDRY_CREDITORS,'') AS account FROM organization_master WHERE ID=1";
            message.list = da.getRecord(sql);
            if (message.list.isEmpty()) {
                return message.respondWithError("Please define SUNDRY CREDITORS ACCOUNT in organization master");
            }
            message.map = (Map) message.list.get(0);
            String mgrCode = message.map.get("mgrCode").toString();
            acCode = getAcCode(mgrCode, obj.getName() + " Creditors");
            obj.setAcCode(acCode);
        }
        obj.setId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            sql = "UPDATE chart_of_account SET AC_NAME='" + obj.getName() + " Creditors" + "' WHERE AC_CODE='" + acCode + "';";
            row = da.delete(sql);
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

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM sundry_creditors WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    public String getAcCode(String mgrCode, String acName) {
        try {
            sql = "SELECT LEVEL level FROM chart_of_account WHERE AC_CODE='" + mgrCode + "'";
            message.map = (Map) da.getRecord(sql).get(0);
            int level = Integer.parseInt(message.map.get("level").toString());
            sql = "SELECT ifnull(MAX(AC_SN),0)+1 AS id FROM chart_of_account WHERE MGR_CODE='" + mgrCode + "'";
            message.map = (Map) da.getRecord(sql).get(0);
            int sn = Integer.parseInt(message.map.get("id").toString());
            String acCode = "";
            if (sn < 10) {
                acCode = mgrCode + "0" + sn;
            } else {
                acCode = mgrCode + sn;
            }
            sql = "INSERT INTO chart_of_account (AC_CODE, AC_NAME, AC_SN, MGR_CODE, TRANSACT, LEVEL) VALUES ('" + acCode + "', '" + acName + "', " + sn + ", '" + mgrCode + "', 'Y', " + level + ")";
            row = da.delete(sql);
            if (row == 0) {
                if (msg.contains("Duplicate entry")) {
                    sql = "SELECT AC_CODE AS acCode FROM chart_of_account WHERE MGR_CODE='" + mgrCode + "' AND AC_NAME='" + acName + "'";
                    message.list = da.getRecord(sql);
                    if (message.list.isEmpty()) {
                        acCode = "Invalid Ac Code of " + acName + " Please select ac Code";
                    } else {
                        message.map = (Map) message.list.get(0);
                        acCode = message.map.get("acCode").toString();
                    }
                }
            }
            return acCode;
        } catch (Exception e) {
            System.out.println(e);
            return e.getMessage();
        }
    }
}
