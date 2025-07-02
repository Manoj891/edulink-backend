
package com.ms.ware.online.solution.school.config;


import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class DateConverted {

    private static final BikramSambatConverter converter = new BikramSambatConverter();
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm");

    public static String today() {
        return df.format(new Date());
    }

    public static String now() {
        return dateTime.format(new Date());
    }

    public static Date addDate(Date date, long day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, (int) day);
        return c.getTime();
    }

    public static Date addDate(String date, int day) {
        return addDate(toDate(date), day);
    }

    public static String toString(Date date) {
        return df.format(date);
    }

    public static Date toDate(String date) {
        try {
            return df.parse(date);
        } catch (Exception ignored) {
        }
        return null;
    }
    public static Date toTime(String date) {
        try {
            return time.parse(date);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String adToBs(Date date) {
        try {
            return converter.convertAdToBs(Objects.requireNonNull(toDate(toString(date))));
        } catch (Exception ex) {
            log.error(ex.getMessage() + " BS AD DATE CONVERT ERROR: " + date);
        }
        return null;
    }

    public static String adToBs(String date) {
        try {
            return converter.convertAdToBs(Objects.requireNonNull(toDate(date)));
        } catch (Exception ex) {
            log.error(ex.getMessage() + " BS AD DATE CONVERT ERROR: " + date);
        }
        return null;
    }

    public static String bsToAd(String date) {
        try {
            String[] parts = date.split("-");
            int bsYear = Integer.parseInt(parts[0]);
            Date adDate = converter.convertBsToAd(bsYear, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            String nextBs = adToBs(adDate);
            assert nextBs != null;
            if (!nextBs.equalsIgnoreCase(date)) {
                return null;
            }
            return toString(adDate);
        } catch (Exception ex) {
            log.error(ex.getMessage() + " BS AD DATE CONVERT ERROR: " + date);
        }
        return null;
    }

    public static Date bsToAdDate(String date) {

        try {
            String[] parts = date.split("-");
            int bsYear = Integer.parseInt(parts[0]);
            int adYear = Calendar.getInstance().getWeekYear() + 2;
            if (bsYear > adYear) {
                Date ld = converter.convertBsToAd(bsYear, Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                String nextBs = adToBs(ld);
                assert nextBs != null;
                if (!nextBs.equalsIgnoreCase(date)) {
                    return null;
                }
                return ld;
            } else {
                return df.parse(date);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
