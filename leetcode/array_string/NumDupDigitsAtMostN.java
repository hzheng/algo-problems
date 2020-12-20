import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1012: https://leetcode.com/problems/numbers-with-repeated-digits/
//
// Given a positive integer N, return the number of positive integers less than or equal to N that
// have at least 1 repeated digit.
//
// Note:
// 1 <= N <= 10^9
public class NumDupDigitsAtMostN {
    // Math
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100.00%), 35.8 MB(75.51%) for 129 tests
    public int numDupDigitsAtMostN(int N) {
        int pow10 = 1;
        int digits = 0;
        for (int a = N; a >= 10; a /= 10, digits++, pow10 *= 10) {}
        int noDupLessDigits = 0; // all non-repeated digits less than N's digits
        for (int i = 1; i <= digits; i++) {
            noDupLessDigits += permute(9, i - 1) * 9;
        }
        int noDupSameDigits = 0; // all non-repeated digits same as N's digits
        boolean[] used = new boolean[10];
        for (int i = digits, a = N, choice = 9; i >= 0; i--, choice--, a %= pow10, pow10 /= 10) {
            int curDigit = a / pow10;
            int nPerms = (i == digits) ? -1 : 0;  // first digit cannot be zero
            for (int d = curDigit - (i == 0 ? 0 : 1); d >= 0; d--) {
                nPerms += used[d] ? 0 : 1;
            }
            noDupSameDigits += permute(choice, i) * nPerms;
            if (used[curDigit]) { break; }

            used[curDigit] = true;
        }
        return N - noDupLessDigits - noDupSameDigits;
    }

    private int permute(int n, int m) {
        int res = 1;
        for (int i = n - m + 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    // Math
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 1 ms(94.90%), 36 MB(41.84%) for 129 tests
    public int numDupDigitsAtMostN2(int N) {
        List<Integer> digitList = new ArrayList<>();
        for (int a = N + 1; a > 0; a /= 10) {
            digitList.add(0, a % 10);
        }
        int res = 0;
        int n = digitList.size();
        for (int i = 1; i < n; i++) {
            res += permute2(9, i - 1) * 9;
        }
        Set<Integer> visited = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int nPerms = 0;
            for (int j = i > 0 ? 0 : 1; j < digitList.get(i); j++) {
                if (!visited.contains(j)) {
                    nPerms++;
                }
            }
            res += permute2(9 - i, n - i - 1) * nPerms;
            if (!visited.add(digitList.get(i))) { break; }
        }
        return N - res;
    }

    private int permute2(int n, int m) {
        return m == 0 ? 1 : permute2(n, m - 1) * (n - m + 1);
    }

    // Dynamic Programming
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 1 ms(94.90%), 36 MB(41.84%) for 129 tests
    public int numDupDigitsAtMostN3(int N) {
        int digits = 0;
        for (int a = N; a > 0; a /= 10, digits++) {}
        int[] digitArray = new int[digits];
        for (int i = 0, a = N; i < digits; i++, a /= 10) {
            digitArray[digits - 1 - i] = a % 10;
        }
        int noDupLessDigits = 0;
        int[] dp = new int[digits - 1];
        for (int i = 0; i < digits - 1; i++) {
            dp[i] = (i == 0) ? 9 : dp[i - 1] * (10 - i);
            noDupLessDigits += dp[i];
        }

        int[] count = new int[10];
        dp = new int[digits];
        boolean duplicate = false;
        for (int i = 0; i < digits; i++) {
            dp[i] = (i == 0) ? 9 : dp[i - 1] * (10 - i);
            if (duplicate) { continue; }

            for (int j = digitArray[i] + 1; j < 10; j++) {
                dp[i] -= (count[j] == 0) ? 1 : 0;
            }
            duplicate = (count[digitArray[i]]++ > 0);
        }
        return N - (noDupLessDigits + dp[digits - 1]);
    }

    // DFS + Recursion + Bit Manipulation
    // time complexity: O(N), space complexity: O(log(N))
    // 1820 ms(5.10%), 35.8 MB(75.51%) for 129 tests
    public int numDupDigitsAtMostN4(int N) {
        int[] uniqueDigits = new int[1];
        dfs(uniqueDigits, 0, 0, N);
        return N - uniqueDigits[0] + 1;
    }

    private void dfs(int[] uniqueDigits, long cur, int bitmask, int N) {
        if (cur > N) { return; }

        uniqueDigits[0]++;
        for (int digit = 0; digit < 10; digit++) {
            if (bitmask == 0 && digit == 0 || (bitmask & (1 << digit)) > 0) { continue; }

            dfs(uniqueDigits, cur * 10 + digit, bitmask | (1 << digit), N);
        }
    }

    private void test(int N, int expected) {
        assertEquals(expected, numDupDigitsAtMostN(N));
        assertEquals(expected, numDupDigitsAtMostN2(N));
        assertEquals(expected, numDupDigitsAtMostN3(N));
        assertEquals(expected, numDupDigitsAtMostN4(N));
    }

    @Test public void test() {
        test(1, 0);
        test(9, 0);
        test(10, 0);
        test(110, 12);
        test(352, 83);
        test(10000, 4726);
        test(3520, 1535);
        test(5320, 2383);
        test(20, 1);
        test(532, 127);
        test(235, 53);
        test(100, 10);
        test(1000, 262);
        test(1352, 479);
        test(13520, 7441);
        test(9999, 4725);
        test(10000, 4726);
        test(31520, 19729);
        test(98943520, 96597670);
        test(221518701, 218729331);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
