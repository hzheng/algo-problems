import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1360: https://leetcode.com/problems/number-of-days-between-two-dates/
//
// Write a program to count the number of days between two dates.
// The two dates are given as strings, their format is YYYY-MM-DD as shown in the examples.
// Constraints:
// The given dates are valid dates between the years 1971 and 2100.
public class DaysBetweenDates {
    // time complexity: O(N), space complexity: O(N)
    // 22 ms(21.41%), 40.6 MB(100%) for 105 tests
    public int daysBetweenDates(String date1, String date2) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setTimeZone(TimeZone.getTimeZone("UTC")); // DON'T OMIT! (may have daylight saving time)
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            long diffInMillis = Math.abs(d2.getTime() - d1.getTime());
            return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return -1;
        }
    }

    // time complexity: O(N), space complexity: O(N)
    // 8 ms(32.93%), 38.5 MB(100%) for 105 tests
    public int daysBetweenDates2(String date1, String date2) {
        return Math.abs((int) ChronoUnit.DAYS.between(LocalDate.parse(date1), LocalDate.parse(date2)));
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(96.53%), 37.6 MB(100%) for 105 tests
    public int daysBetweenDates3(String date1, String date2) {
        return Math.abs(days(date1) - days(date2));
    }

    private static final int[] DAYS = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365};

    // days from 1900
    private int days(String date) {
        String[] dateArray = date.split("-");
        int year = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int day = Integer.parseInt(dateArray[2]);
        int yearDiff = year - 1 - 1900;
        int dayDiff = yearDiff * 365 + yearDiff / 4;
        int monthDiff = DAYS[month - 1];
        if (isLeap(year) && month > 2) {
            day++;
        }
        return dayDiff + monthDiff + day;
    }

    private boolean isLeap(int year) {
        return (year % 100 != 0 && year % 4 == 0) || (year % 400 == 0);
    }

    private static final int[] MONTH_DAYS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(96.53%), 37.4 MB(100%) for 105 tests
    public int daysBetweenDates4(String date1, String date2) {
        return Math.abs(daysFrom1900(date1) - daysFrom1900(date2));
    }

    private int daysFrom1900(String date) {
        String[] dateArray = date.split("-");
        int year = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int day = Integer.parseInt(dateArray[2]);
        for (int y = 1901; y < year; y++) {
            day += isLeap(y) ? 366 : 365;
        }
        for (int m = 0; m < month - 1; m++) {
            day += MONTH_DAYS[m];
        }
        if (month > 2 && isLeap(year)) {
            day++;
        }
        return day;
    }

    void test(String date1, String date2, int expected) {
        assertEquals(expected, daysBetweenDates(date1, date2));
        assertEquals(expected, daysBetweenDates2(date1, date2));
        assertEquals(expected, daysBetweenDates3(date1, date2));
        assertEquals(expected, daysBetweenDates4(date1, date2));
    }

    @Test
    public void test() {
        test("2019-06-29", "2019-06-30", 1);
        test("2020-01-15", "2019-12-31", 15);
        test("2000-02-29", "2019-02-13", 6924);
        test("2074-09-12", "1983-01-08", 33485);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
