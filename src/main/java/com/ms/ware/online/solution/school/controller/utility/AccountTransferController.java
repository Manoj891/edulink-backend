package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.config.DB;
import lombok.extern.slf4j.Slf4j;
import com.ms.ware.online.solution.school.model.HibernateUtilImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/Utility/AccountTransfer")
public class AccountTransferController {
    @Autowired
    private DB db;
    private String message;

    @PostMapping("/{mgrCode}")
    public ResponseEntity<String> doSave(@PathVariable String mgrCode, @RequestBody List<String> acCodes) {

        acCodes.forEach(acCode -> {
            if (transfer(acCode, mgrCode)) {
                try {
                    log.info("Removed chart_of_account {} {}", acCode, db.delete("delete from chart_of_account where ac_code='" + acCode + "'"));
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(acCodes.size() == 1 ? "{\"message\":\"" + message + "\"}" : "{\"message\":\"Success\"}");

    }


    private boolean transfer(String acCode, String mgrCode) {
        List<Map<String, Object>> list;
        Map<String, Object> map;
        int sn, level;
        Session session = HibernateUtilImpl.getSession();
        list = db.getRecord("select ac_code,ac_name,transact from chart_of_account where ac_code='" + acCode + "'");
        if (list.isEmpty()) {
            session.close();
            return false;
        }
        map = list.get(0);
        String transact = map.get("transact").toString();
        if (!transact.equalsIgnoreCase("Y")) {
            message = "This is not transact account";
            session.close();
            return false;
        }
        String ac_name = map.get("ac_name").toString();
        list = db.getRecord("select ifnull(max(AC_SN),0)+1 as sn from chart_of_account where MGR_CODE='" + mgrCode + "'");
        if (list.isEmpty()) {
            message = "SN Fetching Error";
            session.close();
            return false;
        }
        map = list.get(0);
        sn = Integer.parseInt(map.get("sn").toString());
        list = db.getRecord("select LEVEL+1 as level from chart_of_account where AC_CODE='" + mgrCode + "'");
        if (list.isEmpty()) {
            session.close();
            message = "Level Fetching Error";
            return false;
        }
        map = list.get(0);
        level = Integer.parseInt(map.get("level").toString());
        String newAcCode = (sn < 10 ? mgrCode + "0" + sn : mgrCode + sn);
        Transaction tr = session.beginTransaction();
        try {
            log.info("{} record Saved {}", session.createSQLQuery("insert into chart_of_account(AC_CODE, AC_NAME, AC_SN, LEVEL, MGR_CODE, TRANSACT) value ('" + newAcCode + "','" + ac_name + "','" + sn + "','" + level + "','" + mgrCode + "','Y')").executeUpdate(), newAcCode);
            log.info("{} ledger update {} to {}", session.createSQLQuery("update ledger set AC_CODE='" + newAcCode + "' where AC_CODE='" + acCode + "'").executeUpdate(), newAcCode, acCode);
            log.info("{} voucher_detail update {} to {}", session.createSQLQuery("update voucher_detail set AC_CODE='" + newAcCode + "' where AC_CODE='" + acCode + "'").executeUpdate(), newAcCode, acCode);
            tr.commit();
            session.close();
            System.out.println(acCode + " " + mgrCode);
            message = "Success";
            return true;
        } catch (Exception e) {
            tr.rollback();
            session.close();
            message = e.getMessage();
            return false;
        }
    }
}
