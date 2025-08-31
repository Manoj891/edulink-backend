/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.config.EmailService;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.utility.OrganizationUserInfoDao;
import com.ms.ware.online.solution.school.entity.utility.OrganizationUserInfo;
import com.ms.ware.online.solution.school.model.DatabaseName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrganizationUserInfoServiceImp implements OrganizationUserInfoService {

    @Autowired
    private OrganizationUserInfoDao da;
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    private EmailService e;
    private final Message message = new Message();

    String msg = "", sql;
    int row;

    @Override
    public Object getAll() {
        return da.getAll("from OrganizationUserInfo where id!=1");
    }

    @Override
    public Object save(OrganizationUserInfo obj, HttpServletRequest request) {
        AuthenticatedUser td = facade.getAuthentication();;

        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equalsIgnoreCase("ADM")) {
            return message.respondWithError("You have hot access this feature!!");
        }
        try {

            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM organization_user_info";
            message.map = (Map) da.getRecord(sql).get(0);
            obj.setId(Long.parseLong(message.map.get("id").toString()));

            if (obj.getCashAccount().length() == 0) {
                sql = "SELECT IFNULL(CASH_ACCOUNT,'') AS mgrCode FROM organization_master";
                message.list = da.getRecord(sql);
                if (message.list.isEmpty()) {
                    return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
                }
                message.map = (Map) message.list.get(0);
                String mgrCode = message.map.get("mgrCode").toString();
                if (mgrCode.length() == 0) {
                    return message.respondWithError("Please define STUDENT FEE INCOME ACCOUNT in organization master");
                }
                obj.setCashAccount(getAcCode(mgrCode, obj.getEmpName() + " Cash in Hand"));

            }
            String password = String.valueOf(Math.random()).substring(3, 9);
            row = da.save(obj);

            msg = da.getMsg();
            if (row > 0) {
                sql = "UPDATE organization_user_info SET LOGIN_PASS=CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + password + "'))))) WHERE ID='" + obj.getId() + "'";
                da.delete(sql);

                Map map = new HashMap();
                String url = request.getRequestURL().toString();
                url = url.substring(0, url.indexOf("api/Utility/OrganizationUserInfo"));
                String body = "Dear " + obj.getEmpName() + ",<br/> Your login id " + obj.getLoginId() + " and login password " + password + "<br/>" + "Please login and change password!!<br> link " + url;
                if (e.sendmail(obj.getEmail(), "Password generated from " + DatabaseName.getDocumentUrl().replace("/", "") + "!.", body)) {
                    map.put("message", "Success");
                } else {
                    map.put("message", "Success");
                    map.put("body", body);
                }
                return map;
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(OrganizationUserInfo obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();;

        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equalsIgnoreCase("ADM")) {
            return message.respondWithError("You have hot access this feature!!");
        }
        if (obj.getVoucherUnApprove().equalsIgnoreCase("Y")) {
            if (!td.getUserName().equalsIgnoreCase("ADMIN")) {
                return message.respondWithError("You have hot access this feature!!");
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
        AuthenticatedUser td = facade.getAuthentication();;

        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equalsIgnoreCase("ADM")) {
            return message.respondWithError("You have hot access this feature!!");
        }

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM organization_user_info WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }

    String getAcCode(String mgrCode, String acName) {
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
    }

    @Override
    public Object resetPassword(long id, HttpServletRequest request) {
        AuthenticatedUser td = facade.getAuthentication();;

        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        } else if (!td.getUserType().equalsIgnoreCase("ADM")) {
            return message.respondWithError("You have hot access this feature!!");
        }
        List<OrganizationUserInfo> list = da.getAll("from OrganizationUserInfo where id=" + id);
        if (list.isEmpty()) {
            return message.respondWithError("Invalid Id");
        }
        OrganizationUserInfo obj = list.get(0);
        String url = request.getRequestURL().toString();
        String password = String.valueOf(Math.random()).substring(3, 9);
        url = url.substring(0, url.indexOf("api/Utility/OrganizationUserInfo"));
        String body = "Dear " + obj.getEmpName() + ",<br/> Your login id: <u><b>" + obj.getLoginId() + "</b></u> and login password:  <u><b>" + password + "</b></u><br/>" + "Please login and change password!!<br> link " + url;
        if (e.sendmail(obj.getEmail(), "Password generated from " + DatabaseName.getDocumentUrl().replace("/", "") + "!.", body)) {
            sql = "UPDATE organization_user_info SET LOGIN_PASS=CONCAT('*', UPPER(SHA1(UNHEX(SHA1('" + password + "'))))) WHERE ID='" + obj.getId() + "'";
            da.delete(sql);
            return message.respondWithMessage("Success");
        } else {
            return message.respondWithError(e.getMsg());
        }
    }
}
