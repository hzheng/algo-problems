import org.junit.Test;

import static org.junit.Assert.*;

// LC1736: https://leetcode.com/problems/latest-time-by-replacing-hidden-digits/
//
// You are given a string time in the form of hh:mm, where some of the digits in the string are
// hidden (represented by ?). The valid times are those inclusively between 00:00 and 23:59.
// Return the latest valid time you can get from time by replacing the hidden digits.
//
// Constraints:
// time is in the format hh:mm.
// It is guaranteed that you can produce a valid time from the given string.
public class MaximumTime {
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 37 MB(97.47%) for 159 tests
    public String maximumTime(String time) {
        char[] t = time.toCharArray();
        t[0] = (t[0] != '?') ? t[0] : ((t[1] == '?' || t[1] <= '3') ? '2' : '1');
        t[1] = (t[1] != '?') ? t[1] : ((t[0] == '2') ? '3' : '9');
        t[3] = (t[3] != '?') ? t[3] : '5';
        t[4] = (t[4] != '?') ? t[4] : '9';
        return String.valueOf(t);
    }

    // time complexity: O(1), space complexity: O(1)
    // 11 ms(11.61%), 37.7 MB(75.71%) for 159 tests
    public String maximumTime2(String time) {
        char[] t = time.toCharArray();
        for (int h = 23; ; h--) {
            if (t[0] != '?' && t[0] != h / 10 + '0') { continue; }
            if (t[1] != '?' && t[1] != h % 10 + '0') { continue; }
            for (int m = 59; m >= 0; m--) {
                if (t[3] != '?' && t[3] != m / 10 + '0') { continue; }
                if (t[4] != '?' && t[4] != m % 10 + '0') { continue; }

                return String.format("%02d:%02d", h, m);
            }
        }
    }

    private void test(String time, String expected) {
        assertEquals(expected, maximumTime(time));
        assertEquals(expected, maximumTime2(time));
    }

    @Test public void test() {
        test("2?:?0", "23:50");
        test("0?:3?", "09:39");
        test("1?:22", "19:22");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
