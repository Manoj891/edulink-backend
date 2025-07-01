package com.ms.ware.online.solution.school.config;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BikramSambatConverter {
    private final BikramSambatCalendar calendar = new BikramSambatCalendar();
    private static long startMillis;
    private static Date startDate;

    public static void set() {
        startDate = DateConverted.toDate("1943-04-13");
        startMillis = Objects.requireNonNull(startDate).getTime();
    }

    public String convertAdToBs(Date adDate) {
        long totalDays = TimeUnit.DAYS.convert((adDate.getTime() - startMillis), TimeUnit.MILLISECONDS);
        if (totalDays < 0) throw new IllegalArgumentException("Date before supported range.");
        int bsYear = 2000;
        int bsMonth = 1;
        int bsDay = 1;

        // Add days to BS date
        while (totalDays > 0) {
            int daysInMonth = calendar.getDaysInMonth(bsYear, bsMonth);
            if (bsDay < daysInMonth) {
                bsDay++;
            } else {
                bsDay = 1;
                if (bsMonth < 12) {
                    bsMonth++;
                } else {
                    bsMonth = 1;
                    bsYear++;
                    if (!calendar.isSupportedYear(bsYear)) {
                        throw new IllegalArgumentException("Date exceeds supported BS calendar range.");
                    }
                }
            }
            totalDays--;
        }
        return String.format("%04d-%02d-%02d", bsYear, bsMonth, bsDay);

    }


    public Date convertBsToAd(int bsYear, int bsMonth, int bsDay) {
        if (!calendar.isSupportedYear(bsYear)) {
            throw new IllegalArgumentException("Unsupported BS year: " + bsYear);
        }
        int totalDays = 1;

        // Step 1: Add days from complete years
        int referenceBsYear = 2000;
        for (int y = referenceBsYear; y < bsYear; y++) {
            for (int m = 1; m <= 12; m++) {
                totalDays += calendar.getDaysInMonth(y, m);
            }
        }

        // Step 2: Add days from complete months in the target year
        for (int m = 1; m < bsMonth; m++) {
            totalDays += calendar.getDaysInMonth(bsYear, m);
        }

        // Step 3: Add days in the target month
        int referenceBsDay = 1;
        totalDays += (bsDay - referenceBsDay);
        return addDate(startDate, totalDays);

    }

    private final Calendar c = Calendar.getInstance();

    public Date addDate(Date date, int day) {
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }
}
