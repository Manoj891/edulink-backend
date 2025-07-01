/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.inventory;

import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.inventory.InventoryLedgerDao;
import com.ms.ware.online.solution.school.entity.inventory.InventoryLedger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InventoryLedgerServiceImp implements InventoryLedgerService {

    @Autowired
    InventoryLedgerDao da;
    Message message = new Message();
    String msg = "", sql;
    int row;
    @Autowired
    private AuthenticationFacade facade;

    @Override
    public Object getAll() {
        return da.getAll("from InventoryLedger");
    }

    @Override
    public Object save(InventoryLedger obj) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id FROM inventory_ledger";
            message.map = (Map) da.getRecord(sql).get(0);

            row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return message.respondWithMessage("Success");
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return message.respondWithError(msg);

        } catch (Exception e) {
            return message.respondWithError(e.getMessage());
        }
    }

    @Override
    public Object update(InventoryLedger obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
      
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
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }

        id = "'" + id.replace(",", "','") + "'";
        sql = "DELETE FROM inventory_ledger WHERE ID IN (" + id + ")";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return message.respondWithMessage("Success");
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return message.respondWithError(msg);
    }
}
