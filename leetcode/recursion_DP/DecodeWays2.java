import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC639: https://leetcode.com/problems/decode-ways-ii/
//
// A message containing letters from A-Z is being encoded to numbers using the
// following mapping way:
// 'A' -> 1, 'B' -> 2, ..., 'Z' -> 26
// Beyond that, now the encoded string can also contain the character '*', which
// can be treated as one of the numbers from 1 to 9.
// Given the encoded message containing digits and the character '*', return the
// total number of ways to decode it. Return the output mod 10 ^ 9 + 7.
public class DecodeWays2 {
    private static final int MOD = 1000_000_000 + 7;

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats 90.39%(51 ms for 195 tests)
    public int numDecodings(String s) {
        int len = s.length();
        if (len == 0 || s.charAt(0) == '0') return 0;

        long prev = (s.charAt(0) == '*') ? 9 : 1;
        long prev2 = 1;
        for (int i = 1; i < len; i++) {
            char nextToLast = s.charAt(i - 1);
            char last = s.charAt(i);
            long cur = 0;
            if (last == '*') {
                cur = prev * 9;
                if (nextToLast == '*') {
                    cur += prev2 * 15;
                } else if (nextToLast == '1') {
                    cur += prev2 * 9;
                } else if (nextToLast == '2') {
                    cur += prev2 * 6;
                }
            } else {
                cur = (last > '0') ? prev : 0;
                if (nextToLast == '*' || nextToLast == '1') {
                    cur += prev2;
                }
                if ((nextToLast == '*' || nextToLast == '2') && last < '7') {
                    cur += prev2;
                }
            }
            prev2 = prev;
            prev = cur % MOD;
        }
        return (int)prev;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats 97.48%(44 ms for 195 tests)
    public int numDecodings2(String s) {
        int len = s.length();
        long first = 1;
        long second = (s.charAt(0) == '*') ? 9 : (s.charAt(0) == '0' ? 0 : 1);
        for (int i = 1; i < len; i++) {
            long tmp = second;
            char nextToLast = s.charAt(i - 1);
            char last = s.charAt(i);
            if (last == '*') {
                second *= 9;
                if (nextToLast == '1') {
                    second = second + 9 * first;
                } else if (nextToLast == '2') {
                    second = second + 6 * first;
                } else if (nextToLast == '*') {
                    second = second + 15 * first;
                }
                second %= MOD;
            } else {
                second = (last != '0') ? second : 0;
                if (nextToLast == '1' || (nextToLast == '2' && last < '7')) {
                    second = (second + first) % MOD;
                } else if (nextToLast == '*') {
                    second = (second + (last < '7' ? 2 : 1) * first) % MOD;
                }
            }
            first = tmp;
        }
        return (int)second;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 69.63%(61 ms for 195 tests)
    public int numDecodings3(String s) {
        int len = s.length();
        long[] dp = new long[len + 1];
        dp[0] = 1;
        dp[1] = (s.charAt(0) == '*') ? 9 : (s.charAt(0) == '0' ? 0 : 1);
        for (int i = 1; i < len; i++) {
            char nextToLast = s.charAt(i - 1);
            char last = s.charAt(i);
            if (last == '*') {
                dp[i + 1] = dp[i] * 9;
                if (nextToLast == '1') {
                    dp[i + 1] = (dp[i + 1] + dp[i - 1] * 9) % MOD;
                } else if (nextToLast == '2') {
                    dp[i + 1] = (dp[i + 1] + dp[i - 1] * 6) % MOD;
                } else if (nextToLast == '*') {
                    dp[i + 1] = (dp[i + 1] + dp[i - 1] * 15) % MOD;
                }
            } else {
                dp[i + 1] = (last != '0') ? dp[i] : 0;
                if (nextToLast == '1' || nextToLast == '2' && last < '7') {
                    dp[i + 1] = (dp[i + 1] + dp[i - 1]) % MOD;
                } else if (nextToLast == '*') {
                    dp[i + 1] =
                        (dp[i + 1] + dp[i - 1] * (last < '7' ? 2 : 1)) % MOD;
                }
            }
        }
        return (int)dp[len];
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats 55.53%(67 ms for 195 tests)
    public int numDecodings4(String s) {
        long first = ways(s.charAt(0));
        int len = s.length();
        if (len < 2) return (int)first;

        long second = first * ways(s.charAt(1)) +
                      ways(s.charAt(0), s.charAt(1));
        for (int i = 2; i < len; i++) {
            long tmp = second;
            second = (second * ways(s.charAt(i))
                      + first * ways(s.charAt(i - 1), s.charAt(i))) % MOD;
            first = tmp;
        }
        return (int)second;
    }

    private int ways(int c) {
        return (c == '*') ? 9 : (c == '0' ? 0 : 1);
    }

    private int ways(char c1, char c2) {
        if (c1 == '*' && c2 == '*') return 15;

        if (c1 == '*') return (c2 >= '0' && c2 < '7') ? 2 : 1;

        if (c2 == '*') return (c1 == '1') ? 9 : (c1 == '2') ? 6 : 0;

        int val = Integer.valueOf("" + c1 + c2);
        return (val >= 10 && val <= 26) ? 1 : 0;
    }

    void test(String s, int expected) {
        assertEquals(expected, numDecodings(s));
        assertEquals(expected, numDecodings2(s));
        assertEquals(expected, numDecodings3(s));
        assertEquals(expected, numDecodings4(s));
    }

    @Test
    public void test() {
        test("0", 0);
        test("*", 9);
        test("**", 96);
        test("1*", 18);
        test("*1*", 180);
        test("**2*", 1602);
        test("**********1111111111", 133236775);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
