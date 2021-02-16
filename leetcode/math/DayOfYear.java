import org.junit.Test;

import static org.junit.Assert.*;

// LC1154: https://leetcode.com/problems/day-of-the-year/
//
// Given a string date representing a Gregorian calendar date formatted as YYYY-MM-DD, return the
// day number of the year.
//
// Constraints:
// date.length == 10
// date[4] == date[7] == '-', and all other date[i]'s are digits
// date represents a calendar date between Jan 1st, 1900 and Dec 31, 2019.
public class DayOfYear {
    // time complexity: O(1), space complexity: O(1)
    // 2 ms(58.11%), 37 MB(86.73%) for 246 tests
    public int dayOfYear(String date) {
        String[] dates = date.split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]) - 1;
        int day = Integer.parseInt(dates[2]);
        int res = 0;
        int[] days = new int[] {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int i = 0; i < month; i++) {
            res += days[i];
        }
        boolean isLeap = (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
        return res + day + ((month > 1 && isLeap) ? 1 : 0);
    }

    // time complexity: O(1), space complexity: O(1)
    // 2 ms(58.11%), 37 MB(86.73%) for 246 tests
    public int dayOfYear2(String date) {
        String[] dates = date.split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]) - 1;
        int day = Integer.parseInt(dates[2]);
        boolean isLeap = (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
        int[] days = new int[] {31, isLeap ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int res = day;
        for (int i = 0; i < month; i++) {
            res += days[i];
        }
        return res;
    }

    private void test(String date, int expected) {
        assertEquals(expected, dayOfYear(date));
        assertEquals(expected, dayOfYear2(date));
    }

    @Test public void test() {
        test("2019-01-09", 9);
        test("2019-02-10", 41);
        test("2003-03-01", 60);
        test("2004-03-01", 61);
        test("2016-02-29", 60);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
