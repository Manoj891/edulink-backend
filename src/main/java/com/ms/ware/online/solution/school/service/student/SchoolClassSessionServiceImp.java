/*    map = new com.fasterxml.jackson.databind.ObjectMapper().readValue(jsonData, new com.fasterxml.jackson.core.type.TypeReference<>() {});
 */
package com.ms.ware.online.solution.school.service.student;


import com.ms.ware.online.solution.school.config.DateConverted;
import com.ms.ware.online.solution.school.config.Message;
import com.ms.ware.online.solution.school.config.security.AuthenticatedUser;
import com.ms.ware.online.solution.school.config.security.AuthenticationFacade;
import com.ms.ware.online.solution.school.dao.student.SchoolClassSessionDao;
import com.ms.ware.online.solution.school.entity.student.SchoolClassSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Map;

@Service
public class SchoolClassSessionServiceImp implements SchoolClassSessionService {
    @Autowired
    private AuthenticationFacade facade;
    @Autowired
    SchoolClassSessionDao da;
    Message message = new Message();
    String msg = "", sql;
    int row;

    @Override
    public Object getAll(Long academicYear) {
        sql = "SELECT S.ID  AS id, ACADEMIC_YEAR  AS academicYear, CLASS_ID  AS classId, GET_BS_DATE(END_DATE)  AS endDateBs, PROGRAM  AS program,GET_BS_DATE( START_DATE)  AS startDateBs, TOTAL_MONTH  AS totalMonth,C.NAME className,P.NAME programName FROM school_class_session S,program_master P,class_master C WHERE S.PROGRAM=P.ID AND S.CLASS_ID=C.ID AND ACADEMIC_YEAR=ifnull(" + academicYear + ", ACADEMIC_YEAR) order by PROGRAM, CLASS_ID";
        return da.getRecord(sql);
    }

    @Override
    public Object save(SchoolClassSession obj) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = df.format(obj.getStartDate());
            String endDate = df.format(obj.getEndDate());
            sql = "SELECT COUNT(*) totalDay FROM ad_bs_calender WHERE AD_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
            message.map = (Map) da.getRecord(sql).get(0);
            long totalDay = Long.parseLong(message.map.get("totalDay").toString());
            if (totalDay < 5) {
                return message.respondWithError("Calendar not configured");
            }
            sql = "SELECT ifnull(MAX(ID),0)+1 AS id ,ROUND(DATEDIFF('" + endDate + "','" + startDate + "')/30) AS totalMonth FROM school_class_session";
            message.map = (Map) da.getRecord(sql).get(0);
            long id = Long.parseLong(message.map.get("id").toString());
            int totalMonth = Integer.parseInt(message.map.get("totalMonth").toString());
            startDate = obj.getStartDateBs();
            String date[] = startDate.split("-");
            int year = Integer.parseInt(date[0]);
            int month = Integer.parseInt(date[1]);
            obj.setId(id);
            obj.setTotalMonth(totalMonth);

            row = da.save(obj);
            msg = da.getMsg();
            if (row == 0) {
                if (msg.contains("Duplicate entry")) {
                    msg = "This record already exist";
                }
                return message.respondWithError(msg);
            }

            String paymentDateAd;
            for (int i = 1; i <= totalMonth; i++) {

                if (month <= 9) {
                    endDate = year + "-0" + month + "-" + date[2];
                } else {
                    endDate = year + "-" + month + "-" + date[2];
                }
                paymentDateAd = DateConverted.bsToAd(endDate);
                sql = "INSERT INTO school_class_session_bill_date (MASTER_ID, PAYMENT_TIME, PAYMENT_DATE, PAYMENT_DATE_BS) VALUES (" + id + ", " + i + ", '" + paymentDateAd + "', '" + endDate + "')";
                da.delete(sql);
                month++;
                if (month == 13) {
                    month = 1;
                    year++;
                }
            }
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
    public Object update(SchoolClassSession obj, long id) {
        AuthenticatedUser td = facade.getAuthentication();;
        if (!td.isStatus()) {
            return message.respondWithError("invalid token");
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        obj.setId(id);
        String startDate = df.format(obj.getStartDate());
        String endDate = df.format(obj.getEndDate());
        sql = "SELECT COUNT(*) totalDay FROM ad_bs_calender WHERE AD_DATE BETWEEN '" + startDate + "' AND '" + endDate + "'";
        message.map = (Map) da.getRecord(sql).get(0);
        long totalDay = Long.parseLong(message.map.get("totalDay").toString());
        if (totalDay < 5) {
            return message.respondWithError("Calendar not configured");
        }
        sql = "SELECT ROUND(DATEDIFF('" + endDate + "','" + startDate + "')/30) AS totalMonth FROM dual";
        message.map = (Map) da.getRecord(sql).get(0);
        int totalMonth = Integer.parseInt(message.map.get("totalMonth").toString());
        startDate = obj.getStartDateBs();
        String date[] = startDate.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        obj.setId(id);
        obj.setTotalMonth(totalMonth);
        row = da.update(obj);
        String paymentDateAd;
        sql = "DELETE FROM school_class_session_bill_date WHERE MASTER_ID='" + id + "'";
        da.delete(sql);

        for (int i = 1; i <= totalMonth; i++) {
            if (month <= 9) {
                endDate = year + "-0" + month + "-" + date[2];
            } else {
                endDate = year + "-" + month + "-" + date[2];
            }
            paymentDateAd = DateConverted.bsToAd(endDate);
            sql = "INSERT INTO school_class_session_bill_date (MASTER_ID, PAYMENT_TIME, PAYMENT_DATE, PAYMENT_DATE_BS) VALUES (" + id + ", " + i + ", '" + paymentDateAd + "', '" + endDate + "')";
            da.delete(sql);
            month++;
            if (month == 13) {
                month = 1;
                year++;
            }
        }
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
        }

        sql = "DELETE FROM school_class_session_bill_date WHERE MASTER_ID='" + id + "'";
        da.delete(sql);
        sql = "DELETE FROM school_class_session WHERE ID=" + id + "";
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
