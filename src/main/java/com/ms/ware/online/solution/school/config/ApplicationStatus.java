package com.ms.ware.online.solution.school.config;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationStatus {

    public long  get(){
        try {
            SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd");
            Date d2 = t.parse("2026-04-14");
            Date d1 = new Date();

            long diff = d2.getTime() - d1.getTime();
            //            System.out.println(day);
            return diff / (1000 * 60 * 60 * 24);
        } catch (Exception ignored) {
        }
        return -1;
    }
}
