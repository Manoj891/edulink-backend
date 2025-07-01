package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.entity.utility.OrganizationMaster;
import com.ms.ware.online.solution.school.model.DatabaseName;
import com.ms.ware.online.solution.school.service.utility.OrganizationMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/Utility/OrganizationMaster")
public class OrganizationMasterRestController {

    @Autowired
    private OrganizationMasterService service;

    @GetMapping("/name")
    public Object OrganizationName() {
        List l = new DB().getRecord("SELECT UPPER(`NAME`) name FROM organization_master");
        if (l.isEmpty()) {
            Map m = new HashMap();
            m.put("name", "NA");
            return m;
        }
        return l.get(0);

    }

    @GetMapping
    public Object index() {

        return service.getAll();
    }

    @GetMapping("/Salary")
    public Object salary() {
        DB db = new DB();
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_CODE LIKE '2%' AND TRANSACT='Y'";
        Map<String, Object> map = new HashMap<>();
        map.put("payable", db.getRecord(sql));
        sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_CODE LIKE '4%' AND TRANSACT='Y'";
        map.put("salary", db.getRecord(sql));
        return map;
    }

    @GetMapping("/CashAccount")
    public Object cashAccount() {
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_NAME LIKE '%Cash%' AND AC_CODE LIKE '1%' AND TRANSACT='N'";
        return new DB().getRecord(sql);
    }

    @GetMapping("/StudentFeeIncomeAccount")
    public Object studentFeeIncomeAccount() {
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_CODE LIKE '3%' AND TRANSACT='N'";
        return new DB().getRecord(sql);
    }

    @GetMapping("/InventoryAccount")
    public Object inventoryAccount() {
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_CODE LIKE '1%' AND AC_NAME LIKE '%Inventory%' AND TRANSACT='N'";
        return new DB().getRecord(sql);
    }

    @GetMapping("/SundryCreditors")
    public Object sundryCreditors() {
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_CODE LIKE '2%' AND AC_NAME LIKE '%Sundry Credi%' AND TRANSACT='N'";
        return new DB().getRecord(sql);
    }

    @GetMapping("/SundryDebtors")
    public Object sundryDebtors() {
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_CODE LIKE '1%' AND AC_NAME LIKE '%Sundry%' AND TRANSACT='N'";
        return new DB().getRecord(sql);
    }

    @GetMapping("/ReservesAndSurplus")
    public Object reservesAndSurplus() {
        String sql = "SELECT AC_CODE acCode,AC_NAME acName FROM chart_of_account WHERE LEVEL>1 AND AC_CODE LIKE '2%' AND (AC_NAME LIKE '%Reserv%' OR AC_NAME LIKE '%Surplu%' OR AC_NAME LIKE '%Reserves and Surplus%') AND TRANSACT='Y'";
        return new DB().getRecord(sql);
    }

    @PostMapping
    public Object doSave(@RequestBody OrganizationMaster obj) throws IOException {
        return service.save(obj);
    }



    @GetMapping("/CheckKhaltiPaymentActive")
    public Object checkKhaltiPaymentActive() {
        String sql = "SELECT IFNULL(`KHALTI_PAYMENT_ACTIVE`,'N') status FROM organization_master";
        return new DB().getRecord(sql).get(0);
    }

    @PostMapping("/CheckKhaltiPaymentActive")
    public Object checkKhaltiPaymentActive(HttpServletRequest request, @RequestParam MultipartFile schoolRegistrationCertificate, @RequestParam MultipartFile panRegistrationCertificate, @RequestParam MultipartFile taxClearanceCertificate) throws IOException {
        Message message = new Message();
        File f;
        int count = 0;
        String location = message.getFilepath(DatabaseName.getDocumentUrl());

        try {
            f = new File(location + "/Organization");
            try {
                if (!f.exists()) {
                    f.mkdirs();
                }
            } catch (Exception e) {
                return message.respondWithError(e.getMessage());
            }
        } catch (Exception e) {
        }
        try {
            if (schoolRegistrationCertificate.getSize() > 100) {
                f = new File(location + "/Organization/SchoolRegistrationCertificate.png");
                schoolRegistrationCertificate.transferTo(f);
                count++;
            }
        } catch (Exception e) {
        }
        try {
            f = new File(location + "/Organization/PanRegistrationCertificate.png");
            panRegistrationCertificate.transferTo(f);
            count++;
        } catch (Exception e) {
        }
        try {
            f = new File(location + "/Organization/TaxClearanceCertificate.png");
            taxClearanceCertificate.transferTo(f);
            count++;
        } catch (Exception e) {
        }
        return message.respondWithMessage(count + " Files Uploaded!!");

    }
}
