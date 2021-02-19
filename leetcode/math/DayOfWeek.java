import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

// LC1185: https://leetcode.com/problems/day-of-the-week/
//
// Given a date, return the corresponding day of the week for that date.
// The input is given as three integers representing the day, month and year respectively.
// Return the answer as one of the following values {"Sunday", "Monday", "Tuesday", "Wednesday",
// "Thursday", "Friday", "Saturday"}.
//
// Constraints:
// The given dates are valid dates between the years 1971 and 2100.
public class DayOfWeek {
    private static final String[] WEEKS =
            {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    // time complexity: O(1), space complexity: O(1)
    // 11 ms(23.36%), 37.1 MB(36.31%) for 42 tests
    public String dayOfTheWeek(int day, int month, int year) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month - 1, day);
        return WEEKS[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 36.3 MB(65.15%) for 42 tests
    public String dayOfTheWeek2(int day, int month, int year) {
        int days = daysSince(day, month, year);
        int today = daysSince(18, 2, 2021); // known day of week
        return WEEKS[((days - today) % 7 + 7 + 4) % 7]; // Thursday
    }

    private boolean isLeap(int year) {
        return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
    }

    private int daysSince(int day, int month, int year) {
        int[] days = new int[] {31, isLeap(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int res = day - 1;
        for (int y = 1970; y < year; y++) {
            res += 365 + (isLeap(y) ? 1 : 0);
        }
        for (int i = 0, m = month - 1; i < m; i++) {
            res += days[i];
        }
        return res;
    }

    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.9 MB(95.26%) for 42 tests
    public String dayOfTheWeek3(int day, int month, int year) {
        int[] days = new int[] {31, isLeap(year) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int res = day - 1;
        for (int y = 1970; y < year; y++) {
            res += 365 + (isLeap(y) ? 1 : 0);
        }
        for (int i = 0, m = month - 1; i < m; i++) {
            res += days[i];
        }
        return WEEKS[(res + 4) % 7]; // 1970-1-1 is Thursday
    }

    // Zeller's congruence
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 36.3 MB(65.15%) for 42 tests
    public String dayOfTheWeek4(int day, int month, int year) {
        if (month < 3) {
            month += 12;
            year--;
        }
        int c = year / 100;
        year %= 100;
        int w = (c / 4 - 2 * c + year + year / 4 + 13 * (month + 1) / 5 + day - 1) % 7;
        return WEEKS[(w + 7) % 7];
    }

    // Solution of Choice
    // Tomohiko Sakamoto
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.9 MB(91.42%) for 42 tests
    public String dayOfTheWeek5(int day, int month, int year) {
        int[] daysMonthly = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4}; // Jan: 0, Feb: 31%7, Mar: 58%7...
        if (month < 3) {
            year--;
        }
        // Jan 1st 1 AD is Monday in Gregorian calendar
        return WEEKS[(year + (year / 4 - year / 100 + year / 400) + daysMonthly[month - 1] + day)
                     % 7];
    }

    private void test(int day, int month, int year, String expected) {
        assertEquals(expected, dayOfTheWeek(day, month, year));
        assertEquals(expected, dayOfTheWeek2(day, month, year));
        assertEquals(expected, dayOfTheWeek3(day, month, year));
        assertEquals(expected, dayOfTheWeek4(day, month, year));
        assertEquals(expected, dayOfTheWeek5(day, month, year));
    }

    @Test public void test() {
        test(18, 7, 1999, "Sunday");
        test(30, 12, 2019, "Monday");
        test(31, 12, 2019, "Tuesday");
        test(6, 1, 2021, "Wednesday");
        test(18, 2, 2021, "Thursday");
        test(1, 1, 1971, "Friday");
        test(31, 8, 2019, "Saturday");
        test(1, 1, 1970, "Thursday");
        test(15, 8, 1993, "Sunday");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
