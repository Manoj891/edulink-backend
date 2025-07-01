package com.ms.ware.online.solution.school.controller.utility;

import com.ms.ware.online.solution.school.entity.utility.AdBsCalender;
import com.ms.ware.online.solution.school.service.utility.AdBsCalenderService;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import com.ms.ware.online.solution.school.config.DB;
import com.ms.ware.online.solution.school.config.Message;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/Utility/AdBsCalender")
public class AdBsCalenderRestController {

    @Autowired
    AdBsCalenderService service;

    @GetMapping
    public Object index(@RequestParam String yearMonth) {

        return service.getAll(yearMonth);
    }

    @PostMapping
    public Object doSave(@RequestParam String baisakh, @RequestParam String jesth, @RequestParam String ashar, @RequestParam String shrawan, @RequestParam String bhadra, @RequestParam String ashoj, @RequestParam String kartik, @RequestParam String marsir, @RequestParam String push, @RequestParam String magha, @RequestParam String falgun, @RequestParam String chaitra, @RequestParam String ad, @RequestParam String bs) throws IOException, ParseException {

        String bsDate = "", sql = "", month[] = new String[13];
        String MM[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String DD[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32"};
        month[0] = "";
        month[1] = baisakh;
        month[2] = jesth;
        month[3] = ashar;
        month[4] = shrawan;
        month[5] = bhadra;
        month[6] = ashoj;
        month[7] = kartik;
        month[8] = marsir;
        month[9] = push;
        month[10] = magha;
        month[11] = falgun;
        month[12] = chaitra;

        String yyyy = bs.substring(0, 4);
        java.text.SimpleDateFormat d, d1;
        d = new java.text.SimpleDateFormat("yyyy-MM-dd");
        d1 = new java.text.SimpleDateFormat("E");
        DateFormat df = d;
        Date date, today = df.parse(ad);
        date = df.parse(ad);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        DB db = new DB();
        int dayInMonth, totalDay = 0;
        try {

            for (int i = 1; i < MM.length; i++) {

                dayInMonth = Integer.parseInt(month[i]);
                for (int j = 1; j <= dayInMonth; j++) {
                    try {

                        bsDate = yyyy + "-" + MM[i] + "-" + DD[j];
                        sql = "INSERT INTO ad_bs_calender(AD_DATE,BS_DATE,DAY) VALUES('" + d.format(date) + "','" + bsDate + "','" + d1.format(date) + "')";
//                        db.save(sql);
                        System.out.println(sql);
                        c.add(Calendar.DATE, 1);
                        date = c.getTime();

                    } catch (Exception e) {
//                                    out.print(e.getMessage() + "<br>" + sql + "<br>");
                    }
//                                totalDay++;
                }
            }
        } catch (Exception e) {
        }

        return "";
    }

    @PutMapping
    public Object doUpdate(@RequestBody AdBsCalender obj) throws IOException {
        return service.update(obj);
    }

    @PutMapping("/{year}/{month}/{isHoliday}/{day}")
    public Object doUpdate(@PathVariable String year, @PathVariable String month, @PathVariable String isHoliday, @PathVariable String day) throws IOException {
        DB db = new DB();
        String sql;
        if (isHoliday.equalsIgnoreCase("Y")) {
            sql = "UPDATE ad_bs_calender SET SCHOOL_HOLYDAY='" + isHoliday + "',STUDENT_HOLYDAY='" + isHoliday + "',EVENT='Default holiday' WHERE DAY='" + day + "' AND BS_DATE LIKE '" + year + month + "%'";
        } else {
            sql = "UPDATE ad_bs_calender SET SCHOOL_HOLYDAY='" + isHoliday + "',STUDENT_HOLYDAY='" + isHoliday + "',EVENT='' WHERE DAY='" + day + "' AND BS_DATE LIKE '" + year + month + "%'";

        }
        return new Message().respondWithMessage(db.save(sql) + " Record Updated");
    }

//    @DeleteMapping("/{id}")
//    public Object doDelete(@PathVariable String id) {
//        return service.delete(id);
//    }
}
