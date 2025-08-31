package com.ms.ware.online.solution.school.config;

import com.ms.ware.online.solution.school.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Message {
    @Autowired
    private DB db;
    public Map map;
    public List<Map<String, Object>> list;

    public String getFilepath(String contextPath) {
        return "/home/tomcat/webapps" + contextPath + "Document";
//        return "D:/home/tomcat/webapps" + contextPath + "Document";
    }


    public static String exceptionMsg(Exception e) {
        try {
            return e.getCause().getMessage();
        } catch (Exception ignored) {
        }
        return e.getMessage();
    }

    public String respondWithError(String message) {
        return "{\"error\":{\"message\":\"" + message + "\"}}";
    }

    public void errorMessage(String message) {
        throw new CustomException(message);
    }

    public String respondWithError(String message, String data) {
        return "{\"error\":{\"message\":\"" + message + "\",\"data\":\"" + data + "\"}}";
    }

    public String respondWithMessage(String message) {
        return "{\"message\":\"" + message + "\"}";
    }

    public Object respondWithMessage(String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("data", data);
        return map;
    }

    public String[] jsonDataToStringArray(String jsonData) {
        String[] val = new String[2];
        int arrayStart = jsonData.indexOf("[");
        int arrayEnd = jsonData.lastIndexOf("]") + 1;
        int len = jsonData.length();
        String array = jsonData.substring(arrayStart, arrayEnd);
        String jsconStart = jsonData.substring(0, arrayStart);
        String jsconEnd = jsonData.substring(arrayEnd, len);
        val[1] = (array);
        val[0] = (jsconStart + "0" + jsconEnd);
        return val;
    }

    public String getFeeDueAmount(String regNo, String startDateAd, String endDateAd) {

        String sql;
        
        double totalAmount = 0;
        int chargeDay;
        String chargeStartDate;
        float pardayCharge;
        List ll;
        try {
            sql = "SELECT ROUND(SUM(D.CR)-SUM(D.DR),2) AS amount FROM stu_billing_master M,stu_billing_detail D,bill_master B,program_master P,class_master C WHERE M.BILL_NO=D.BILL_NO AND D.BILL_ID=B.ID AND D.PROGRAM=P.ID AND D.CLASS_ID=C.ID AND M.REG_NO='" + regNo + "' AND D.PAYMENT_DATE<='" + endDateAd + "' ";
            list = db.getRecord(sql);
            if (!list.isEmpty()) {
                map = list.get(0);
                totalAmount = Double.parseDouble(map.get("amount").toString());
            }
        } catch (Exception e) {
        }
        try {
            sql = "SELECT START_DATE startDate,MONTHLY_CHARGE monthlyCharge,S.ACADEMIC_YEAR academicYear,S.PROGRAM program,S.CLASS_ID AS classId FROM student_transportation T,student_info S WHERE T.REG_NO=S.ID AND T.REG_NO='" + regNo + "' AND '" + startDateAd + "'>=START_DATE AND (END_DATE IS NULL OR  '" + startDateAd + "'<=END_DATE)";
            ll = db.getRecord(sql);
            if (!ll.isEmpty()) {
                map = (Map) ll.get(0);
                chargeStartDate = map.get("startDate").toString();
                pardayCharge = Float.parseFloat(map.get("monthlyCharge").toString()) / 30;
                sql = "SELECT DATEDIFF('" + endDateAd + "',IFNULL(((SELECT MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-1))),'" + chargeStartDate + "')) AS chargeDay,IFNULL((SELECT  MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-1)),'" + chargeStartDate + "') AS startDate FROM DUAL";
                map =  db.getRecord(sql).get(0);
                chargeDay = Integer.parseInt(map.get("chargeDay").toString());
                chargeStartDate = map.get("startDate").toString();
                String chargeStartDateBs = DateConverted.adToBs(chargeStartDate);
                if (chargeDay > 0) {
                    String dd = chargeStartDateBs.substring(8, 10);
                    if (dd.equalsIgnoreCase("01")) {
                        chargeDay = Math.round(chargeDay / 30);
                        totalAmount = totalAmount + (chargeDay * (pardayCharge * 30));
                    } else {
                        totalAmount = totalAmount + (chargeDay * pardayCharge);
                    }

                }
            }
        } catch (Exception ignored) {
        }
        try {
            sql = "SELECT START_DATE startDate,MONTHLY_CHARGE monthlyCharge,S.ACADEMIC_YEAR academicYear,S.PROGRAM program,S.CLASS_ID AS classId FROM school_hostal T,student_info S WHERE T.REG_NO=S.ID AND T.REG_NO='" + regNo + "' AND '" + startDateAd + "'>=START_DATE AND (END_DATE IS NULL OR  '" + startDateAd + "'<=END_DATE)";
            ll = db.getRecord(sql);
            if (!ll.isEmpty()) {
                map = (Map) ll.get(0);
                chargeStartDate = map.get("startDate").toString();
                pardayCharge = Float.parseFloat(map.get("monthlyCharge").toString()) / 30;
                sql = "SELECT DATEDIFF('" + endDateAd + "',IFNULL(((SELECT MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-2))),'" + chargeStartDate + "')) AS chargeDay,IFNULL((SELECT  MAX(PAYMENT_DATE) FROM stu_billing_detail WHERE DR>0 AND REG_NO='" + regNo + "' AND BILL_ID=(-2)),'" + chargeStartDate + "') AS startDate FROM DUAL";
                map = db.getRecord(sql).get(0);
                chargeDay = Integer.parseInt(map.get("chargeDay").toString());
                chargeStartDate = map.get("startDate").toString();
                String chargeStartDateBs = DateConverted.adToBs(chargeStartDate);
                if (chargeDay > 0) {
                    String dd = chargeStartDateBs.substring(8, 10);
                    if (dd.equalsIgnoreCase("01")) {
                        chargeDay = Math.round(chargeDay / 30);
                        totalAmount = totalAmount + (chargeDay * (pardayCharge * 30));
                    } else {
                        totalAmount = totalAmount + (chargeDay * pardayCharge);
                    }

                }
            }
        } catch (Exception ignored) {
        }

        return new DecimalFormat("#.##").format(totalAmount);
    }

    public String getMonthName(String month) {

        if (month.equalsIgnoreCase("01")) {
            return "Baisakh";
        } else if (month.equalsIgnoreCase("02")) {
            return "Jeshtha";
        } else if (month.equalsIgnoreCase("03")) {
            return "Aashad";
        } else if (month.equalsIgnoreCase("04")) {
            return "Shrawan";
        } else if (month.equalsIgnoreCase("05")) {
            return "Bhadra";
        } else if (month.equalsIgnoreCase("06")) {
            return "Aashwin";
        } else if (month.equalsIgnoreCase("07")) {
            return "Kartik";
        } else if (month.equalsIgnoreCase("08")) {
            return "Mangsir";
        } else if (month.equalsIgnoreCase("09")) {
            return "Push";
        } else if (month.equalsIgnoreCase("10")) {
            return "Magh";
        } else if (month.equalsIgnoreCase("11")) {
            return "Falgun";
        } else if (month.equalsIgnoreCase("12")) {
            return "Chaitra";
        } else {
            return "";
        }
    }



    public boolean isNull(Object obj) {
        return obj == null;
    }

}
