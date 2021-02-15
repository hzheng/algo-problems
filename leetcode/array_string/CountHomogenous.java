import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1759: https://leetcode.com/problems/count-number-of-homogenous-substrings/
//
// Given a string s, return the number of homogenous substrings of s. Since the answer may be too
// large, return it modulo 10^9 + 7. A string is homogenous if all the characters of the string are
// the same. A substring is a contiguous sequence of characters within a string.
//
// Constraints:
// 1 <= s.length <= 10^5
// s consists of lowercase letters.
public class CountHomogenous {
    private static final int MOD = 1_000_000_007;

    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 25 ms(25.00%), 39.9 MB(75.00%) for 84 tests
    public int countHomogenous(String s) {
        int n = s.length();
        Map<String, Integer> count = new HashMap<>();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            char c = s.charAt(i);
            for (sb.append(c); i + 1 < n && s.charAt(i + 1) == c; i++) {
                sb.append(c);
            }
            String homoStr = sb.toString();
            count.put(homoStr, count.getOrDefault(homoStr, 0) + 1);
        }
        long res = 0;
        for (String key : count.keySet()) {
            int len = key.length();
            res += (len + 1L) * key.length() / 2 * count.get(key);
            res %= MOD;
        }
        return (int)res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 25 ms(25.00%), 39.5 MB(100.00%) for 84 tests
    public int countHomogenous2(String s) {
        int n = s.length();
        Map<String, Integer> count = new HashMap<>();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int cnt = 1;
            for (; i + 1 < n && s.charAt(i + 1) == c; i++, cnt++) {}
            String homoStr = ("" + c).repeat(cnt);
            count.put(homoStr, count.getOrDefault(homoStr, 0) + 1);
        }
        long res = 0;
        for (String key : count.keySet()) {
            int len = key.length();
            res += (len + 1L) * key.length() / 2 * count.get(key);
            res %= MOD;
        }
        return (int)res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 10 ms(100.00%), 40 MB(75.00%) for 84 tests
    public int countHomogenous3(String s) {
        int res = 0;
        char prev = 0;
        int repeated = 0;
        for (char c : s.toCharArray()) {
            if (c == prev) {
                repeated++;
            } else {
                repeated = 1;
            }
            prev = c;
            res = (res + repeated) % MOD;
        }
        return res;
    }

    private void test(String s, int expected) {
        assertEquals(expected, countHomogenous(s));
        assertEquals(expected, countHomogenous2(s));
        assertEquals(expected, countHomogenous3(s));
    }

    @Test public void test() {
        test("xy", 2);
        test("zzzzz", 15);
        test("abbcccaa", 13);
        test("w".repeat(100000), 49965);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
