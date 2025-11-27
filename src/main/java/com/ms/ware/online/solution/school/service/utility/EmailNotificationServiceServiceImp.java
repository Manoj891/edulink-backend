
package com.ms.ware.online.solution.school.service.utility;

import com.ms.ware.online.solution.school.config.EmailService;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.dao.utility.EmailNotificationServiceDao;
import com.ms.ware.online.solution.school.entity.utility.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailNotificationServiceServiceImp implements EmailNotificationServiceService {
    @Autowired
    private EmailNotificationServiceDao da;
    @Autowired
    private EmailService service;
    @Autowired
    private Message message;

    @Override
    public ResponseEntity getAll() {
        return ResponseEntity.status(200).body(da.getAll("from EmailNotificationService"));
    }

    @Override
    public ResponseEntity save(EmailNotificationService obj) {


        String msg = "";
        try {
            obj.setEntityId(UUID.randomUUID().toString());
            int row = da.save(obj);
            msg = da.getMsg();
            if (row > 0) {
                return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
            } else if (msg.contains("Duplicate entry")) {
                msg = "This record already exist";
            }
            return ResponseEntity.status(200).body(message.respondWithError(msg));

        } catch (Exception e) {
            return ResponseEntity.status(200).body(message.respondWithError(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity update(EmailNotificationService obj, String id) {


        int row;
        String msg = "";
        obj.setEntityId(id);
        row = da.update(obj);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("Duplicate entry")) {
            msg = "This record already exist";
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    @Override
    public ResponseEntity delete(String id) {


        int row;
        String msg = "", sql;
        sql = "DELETE FROM email_notification_service WHERE ENTITY_ID='" + id + "'";
        row = da.delete(sql);
        msg = da.getMsg();
        if (row > 0) {
            return ResponseEntity.status(200).body(message.respondWithMessage("Success"));
        } else if (msg.contains("foreign key")) {
            msg = "this record already used in reference tables, Cannot delete of this record";
        }
        return ResponseEntity.status(200).body(message.respondWithError(msg));
    }

    @Override
    public void sendEmail() {
        send();
    }


    public void send() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Date d = new Date();
        String dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(d);
        String dateTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
        String sql = "select name from organization_master";
        String organizationName = da.getRecord(sql).get(0).get("name").toString();
        String html = "<div><div><h1>" + organizationName + "</h1></div>\n"
                + " <style>table, th, td {\n"
                + "border: 1px solid black;\n"
                + " border-collapse: collapse;\n"
                + "}"
                + "</style>\n"
                + "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n"
                + " <table class='table table-bordered table-hover table-striped' style=\"width: 800px;\">\n"
                + "<thead><tr><th>Ac Code</th><th>Ac Name</th><th>Dr Amount</th><th>Cr Amount</th></tr></thead><tbody>\n";
        String tr = "";

        double totalDr = 0, totalCr = 0;
        sql = "SELECT SUM(D.DR_AMT) dr,SUM(D.CR_AMT) cr,C.AC_NAME acName,D.AC_CODE acCode FROM voucher_detail D,chart_of_account C WHERE C.AC_CODE=D.AC_CODE AND CREATED_DATETIME BETWEEN '" + dateFrom + "' AND '" + dateTo + "' GROUP BY C.AC_CODE";
        List<Map<String, Object>> list = da.getRecord(sql);
        for (Map<String, Object> map : list) {
            totalDr += Double.parseDouble(map.get("dr").toString());
            totalCr += Double.parseDouble(map.get("cr").toString());
            tr = tr + " <tr><td>" + map.get("acCode") + "</td><td>" + map.get("acName") + "</td><td>" + decimalFormat.format(map.get("dr")) + "</td><td>" + decimalFormat.format(map.get("cr")) + "</td></tr>\n";
        }
        tr = tr + " <tr><td></td><td>Total</td><td>" + decimalFormat.format(totalDr) + "</td><td>" + decimalFormat.format(totalCr) + "</td></tr>\n";

        String body = html + tr + "</tbody></table> </div>";

        try {
            sql = "SELECT B.BILL_NO billNo,S.STU_NAME AS studentName,C.NAME 'className',(SELECT SUM(D.DR) FROM stu_billing_detail D WHERE D.BILL_NO=B.BILL_NO) AS billAmount FROM stu_billing_master B,student_info S,class_master C WHERE B.REG_NO=S.ID AND B.CLASS_ID=C.ID AND  B.`BILL_TYPE`='DR' AND B.`CREATE_AT` BETWEEN '" + dateFrom + "' AND '" + dateTo + "'\n"
                    + " UNION "
                    + "SELECT B.BILL_NO billNo,B.STUDENT_NAME AS studentName,C.NAME 'className',(SELECT SUM(D.DR) FROM stu_billing_detail D WHERE D.BILL_NO=B.BILL_NO) AS billAmount FROM stu_billing_master B,class_master C WHERE  B.CLASS_ID=C.ID AND B.REG_NO is null AND B.`BILL_TYPE`='DR'  AND B.`CREATE_AT` BETWEEN  '" + dateFrom + "' AND '" + dateTo + "'";
            List<Map<String, Object>> list1 = da.getRecord(sql);
            if (list1.isEmpty() && list.isEmpty()) {
                return;
            }

            tr = "<table class='table table-bordered table-hover table-striped' style=\"width: 800px;\">"
                    + "<tr><th>Bill No</th><th>Student Name</th><th>Class</th><th>Amount</th></tr>";
            double billAmount, totalBillAmount = 0;
            for (Map<String, Object> map : list1) {
                billAmount = Double.parseDouble(map.get("billAmount").toString());
                totalBillAmount += billAmount;
                tr = tr + " <tr><td>" + map.get("billNo") + "</td><td>" + map.get("studentName") + "</td><td>" + map.get("className") + "</td><td>" + decimalFormat.format(billAmount) + "</td></tr>\n";
            }
            tr = tr + " <tr><td></td><td></td><td>Total</td><td>" + decimalFormat.format(totalBillAmount) + "</td></tr>\n";
            tr = tr + "</table>";
            body = body + "<br><br>" + tr;
            sql = "SELECT EMAIL email FROM email_notification_service WHERE STATUS='Y' AND TYPE='DT'";
            list = da.getRecord(sql);

            StringBuilder email = new StringBuilder();
            for (Map<String, Object> map : list) {
                email.append(map.get("email")).append(",");
            }

            if (email.length() > 10) {
                service.sendmail(email.toString(), "Daily Transaction Of " + dateTo, body);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(cron = "00 30 20 * * ?")
    public void runEveryDay() {
        send();
    }
}
