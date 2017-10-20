import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC681: https://leetcode.com/problems/next-closest-time/
//
// Given a time represented in the format "HH:MM", form the next closest time by
// reusing the current digits. There is no limit on how many times a digit can
// be reused.
// You may assume the given input string is always valid.
public class NextClosestTime {
    // beats 32.35%(15 ms for 62 tests)
    public String nextClosestTime(String time) {
        char[] t = {time.charAt(0), time.charAt(1),
                    time.charAt(3), time.charAt(4)};
        int val = convert(t);
        char[] cand = new char[4];
        int min = Integer.MAX_VALUE;
        int closest = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    for (int m = 0; m < 4; m++) {
                        cand[0] = t[i];
                        cand[1] = t[j];
                        cand[2] = t[k];
                        cand[3] = t[m];
                        int v = convert(cand);
                        if (v < 0 || v == val) continue;

                        if (v > val) {
                            closest = Math.min(closest, v);
                        } else {
                            min = Math.min(min, v);
                        }
                    }
                }
            }
        }
        if (closest == Integer.MAX_VALUE) {
            closest = min;
        }
        if (closest == Integer.MAX_VALUE) return time;

        return String.format("%02d:%02d", closest / 60, closest % 60);
    }

    private int convert(char[] buf) {
        int hour = (buf[0] - '0') * 10 + buf[1] - '0';
        if (hour >= 24) return -1;

        int minute = (buf[2] - '0') * 10 + buf[3] - '0';
        return (minute < 60) ? hour * 60 + minute : -1;
    }

    // Set
    // beats 29.05%(16 ms for 62 tests)
    public String nextClosestTime2(String time) {
        Set<Integer> digits = new HashSet<>();
        for (char c : time.toCharArray()) {
            if (c != ':') {
                digits.add(c - '0');
            }
        }
        final int start = 60 * Integer.parseInt(time.substring(0, 2))
                          + Integer.parseInt(time.substring(3));
        int res = start;
        int min = 24 * 60;
        for (int h1 : digits) {
            for (int h2 : digits) {
                if (h1 * 10 + h2 >= 24) continue;

                for (int m1 : digits) {
                    for (int m2 : digits) {
                        if (m1 * 10 + m2 >= 60) continue;

                        int cur = 60 * (h1 * 10 + h2) + (m1 * 10 + m2);
                        int elapsed = Math.floorMod(cur - start, 24 * 60);
                        if (elapsed > 0 && elapsed < min) {
                            res = cur;
                            min = elapsed;
                        }
                    }
                }
            }
        }
        return String.format("%02d:%02d", res / 60, res % 60);
    }

    // Simulation
    // beats 21.06%(18 ms for 62 tests)
    public String nextClosestTime3(String time) {
        Set<Integer> digits = new HashSet<>();
        for (char c : time.toCharArray()) {
            if (c != ':') {
                digits.add(c - '0');
            }
        }
        int cur = 60 * Integer.parseInt(time.substring(0, 2))
                  + Integer.parseInt(time.substring(3));
        outer : while (true) {
            cur = (cur + 1) % (24 * 60);
            for (int d : new int[] {cur / 60 / 10, cur / 60 % 10,
                                    cur % 60 / 10, cur % 60 % 10}) {
                if (!digits.contains(d)) continue outer;
            }
            return String.format("%02d:%02d", cur / 60, cur % 60);
        }
    }

    // beats 32.35%(15 ms for 62 tests)
    public String nextClosestTime4(String time) {
        final int[] minutes = {600, 60, 10, 1};
        int cur = 60 * Integer.parseInt(time.substring(0, 2))
                  + Integer.parseInt(time.substring(3)) + 1;
        char[] next = new char[4];
        for (int i = 0; i < 4; cur++) {
            int minute = cur % (24 * 60);
            for (i = 0; i < 4; minute %= minutes[i++]) {
                next[i] = (char)('0' + minute / minutes[i]);
                if (time.indexOf(next[i]) < 0) break;
            }
        }
        return String.format("%c%c:%c%c", next[0], next[1], next[2], next[3]);
    }

    private static class Result {
        int min = Integer.MAX_VALUE;
        String str = "";
    }

    // DFS + Recursion
    // beats 12.27%(24 ms for 62 tests)
    public String nextClosestTime5(String time) {
        Set<Integer> digits = new HashSet<>();
        for (char c : time.toCharArray()) {
            if (c != ':') {
                digits.add(c - '0');
            }
        }
        if (digits.size() == 1) return time;

        Result res = new Result();
        int start = Integer.parseInt(time.substring(0, 2)) * 60
                    + Integer.parseInt(time.substring(3, 5));
        dfs(start, "", 0, digits, res);
        char[] buf = res.str.toCharArray();
        return String.format("%c%c:%c%c", buf[0], buf[1], buf[2], buf[3]);
    }

    private void dfs(int start, String cur, int pos, Set<Integer> digits, 
                     Result res) {
        if (pos == 4) {
            int m = Integer.parseInt(cur.substring(0, 2)) * 60
                    + Integer.parseInt(cur.substring(2, 4));
            int elapsed = Math.floorMod(m - start, 24 * 60);
            if (elapsed > 0 && elapsed < res.min) {
                res.min = elapsed;
                res.str = cur;
            }
            return;
        }
        for (int digit : digits) {
            if (pos == 0 && digit > 2) continue;
            if (pos == 1 && Integer.parseInt(cur) * 10 + digit > 23) continue;
            if (pos == 2 && digit > 5) continue;
            if (pos == 3 &&
                Integer.parseInt(cur.substring(2)) * 10 + digit > 59) continue;

            dfs(start, cur + digit, pos + 1, digits, res);
        }
    }

    void test(String time, String expected) {
        assertEquals(expected, nextClosestTime(time));
        assertEquals(expected, nextClosestTime2(time));
        assertEquals(expected, nextClosestTime3(time));
        assertEquals(expected, nextClosestTime4(time));
        assertEquals(expected, nextClosestTime5(time));
    }

    @Test
    public void test() {
        test("19:34", "19:39");
        test("23:59", "22:22");
        test("00:00", "00:00");
        test("11:11", "11:11");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
