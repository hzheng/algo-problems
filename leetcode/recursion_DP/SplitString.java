import org.junit.Test;

import static org.junit.Assert.*;

// LC1849: https://leetcode.com/problems/splitting-a-string-into-descending-consecutive-values/
//
// You are given a string s that consists of only digits.
// Check if we can split s into two or more non-empty substrings such that the numerical values of
// the substrings are in descending order and the difference between numerical values of every two
// adjacent substrings is equal to 1.
// For example, the string s = "0090089" can be split into ["0090", "089"] with numerical values
// [90,89]. The values are in descending order and adjacent values differ by 1, so this way is
// valid. Another example, the string s = "001" can be split into ["0", "01"], ["00", "1"], or
// ["0", "0", "1"]. However all the ways are invalid because they have numerical values [0,1],
// [0,1], and [0,0,1] respectively, all of which are not in descending order.
// Return true if it is possible to split s as described above, or false otherwise.
//
// Constraints:
// 1 <= s.length <= 20
// s only consists of digits.
public class SplitString {
    // Recursion + DFS
    // time complexity: O(N^2), space complexity: O(N)
    // 2 ms(50.00%), 38.8 MB(50.00%) for 173 tests
    public boolean splitString(String s) {
        return dfs(s, -1, 0);
    }

    private boolean dfs(String s, long prev, int count) {
        int n = s.length();
        if (n == 0) { return prev >= 0 && count > 1; }

        for (int i = 1; i <= n; i++) {
            String first = s.substring(0, i);
            try {
                long cur = Long.parseLong(first);
                if (prev >= 0 && cur + 1 != prev) { continue; }

                String second = s.substring(i);
                if (dfs(second, cur, count + 1)) {
                    return true;
                }
            } catch (Exception e) { return false;}
        }
        return false;
    }

    // Recursion + DFS
    // time complexity: O(N^2), space complexity: O(N)
    // 0 ms(100.00%), 37.4 MB(62.50%) for 173 tests
    public boolean splitString2(String s) {
        return splitString(s, -1);
    }

    private static final long MAX = 9999999999L;

    private boolean splitString(String s, long prev) {
        long cur = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            cur = cur * 10 + s.charAt(i) - '0';
            if (cur > MAX) { return false; } // avoid overflow

            if (prev >= 0 && cur + 1 != prev) { continue; }

            if (prev >= 0 && i == n - 1 || splitString(s.substring(i + 1), cur)) {
                return true;
            }
        }
        return false;
    }

    // Solution of Choice
    // time complexity: O(N^2), space complexity: O(N)
    // 0 ms(100.00%), 36.6 MB(100.00%) for 173 tests
    public boolean splitString3(String s) {
        long start = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            start = start * 10 + s.charAt(i) - '0';
            if (start > MAX) { return false; }

            long cur = 0;
            for (int j = i + 1, diff = 1; j < n; j++) {
                cur = cur * 10 + s.charAt(j) - '0';
                if (cur > MAX || cur > start - diff) { break; }
                if (cur < start - diff) { continue; }
                if (j == n - 1) { return true; }

                diff += (cur == 0) ? 0 : 1; // if cur is 0, all remaining should be 0's
                cur = 0;
            }
        }
        return false;
    }

    private void test(String s, boolean expected) {
        assertEquals(expected, splitString(s));
        assertEquals(expected, splitString2(s));
        assertEquals(expected, splitString3(s));
    }

    @Test public void test() {
        test("200100", true);
        test("0896942443130", false);
        test("99999999999999999999", false);
        test("10", true);
        test("1234", false);
        test("050043", true);
        test("9080701", false);
        test("10009998", true);
        test("4771447713", true);
        test("0166537080", false);
        test("1051546050", false);
        test("1000999", true);
        test("99999999999999999998", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
