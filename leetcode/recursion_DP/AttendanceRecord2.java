import java.util.*;
import java.math.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC552: https://leetcode.com/problems/student-attendance-record-ii
//
// Given a positive integer n, return the number of all possible attendance records
// with length n. The answer may be very large, return it after mod 10 ^ 9 + 7.
// A student attendance record is a string that only contains the following three characters:
// 'A' : Absent. 'L' : Late. 'P' : Present.
// A record is regarded as rewardable if it doesn't contain more than one 'A' or more
// than two continuous 'L'.
public class AttendanceRecord2 {
    private static final int MOD = 1000000007;

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats N/A(423 ms for 58 tests)
    public int checkRecord(int n) {
        long[][][] dp = new long[n + 1][2][3]; // {total}{absent}{late}
        dp[1][0][0] = 1; // P
        dp[1][0][1] = 1; // L
        dp[1][1][0] = 1; // A
        for (int j = 1; j < n; j++) {
            dp[j + 1][0][0] = (dp[j][0][0] + dp[j][0][1] + dp[j][0][2]) % MOD;
            dp[j + 1][1][0] = (dp[j][1][0] + dp[j][1][1] + dp[j][1][2] + dp[j + 1][0][0]) % MOD;
            dp[j + 1][0][1] = dp[j][0][0];
            dp[j + 1][1][1] = dp[j][1][0];
            dp[j + 1][0][2] = dp[j][0][1];
            dp[j + 1][1][2] = dp[j][1][1];
        }
        long res = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                res += dp[n][i][j];
            }
        }
        return (int)(res % MOD);
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats N/A(191 ms for 58 tests)
    public int checkRecord2(int n) {
        int[][] prev = new int[2][3];
        int[][] next = new int[2][3];
        prev[0][0] = 1;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    next[i][0] += prev[i][j]; // P
                    next[i][0] %= MOD;
                    if (i == 0) {
                        next[1][0] += prev[0][j]; // A
                        next[1][0] %= MOD;
                    }
                    if (j < 2) {
                        next[i][j + 1] += prev[i][j]; // L
                        next[i][j + 1] %= MOD;
                    }
                }
            }
            System.arraycopy(next[0], 0, prev[0], 0, 3);
            System.arraycopy(next[1], 0, prev[1], 0, 3);
            Arrays.fill(next[0], 0);
            Arrays.fill(next[1], 0);
        }
        int res = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                res += prev[i][j];
                res %= MOD;
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats N/A(50 ms for 58 tests)
    public int checkRecord3(int n) {
        long[] dp = new long[n <= 5 ? 6 : n + 1];
        dp[0] = 1;
        dp[1] = 2;
        dp[2] = 4;
        dp[3] = 7;
        // recurring relation: f[n]=2f[n−1]−f[n−4]
        for (int i = 4; i <= n; i++) {
            dp[i] = ((2 * dp[i - 1]) % MOD + (MOD - dp[i - 4])) % MOD;
        }
        long res = dp[n]; // no absent
        for (int i = 1; i <= n; i++) { // one absent
            res += (dp[i - 1] * dp[n - i]) % MOD;
        }
        return (int)(res % MOD);
    }

    // State Machine + Dynamic Programming
    // https://leetcode.com/articles/student-attendance-record-ii/
    // time complexity: O(N), space complexity: O(1)
    // beats N/A(29 ms for 58 tests)
    public int checkRecord4(int n) {
        // axly: # of strings of length i containing x a's ​and ending with y l's
        long a0l0 = 1;
        long a0l1 = 0, a0l2 = 0, a1l0 = 0, a1l1 = 0, a1l2 = 0;
        for (int i = 0; i < n; i++) {
            long new_a0l0 = (a0l0 + a0l1 + a0l2) % MOD;
            long new_a0l1 = a0l0;
            long new_a0l2 = a0l1;
            long new_a1l0 = (a0l0 + a0l1 + a0l2 + a1l0 + a1l1 + a1l2) % MOD;
            long new_a1l1 = a1l0;
            long new_a1l2 = a1l1;
            a0l0 = new_a0l0;
            a0l1 = new_a0l1;
            a0l2 = new_a0l2;
            a1l0 = new_a1l0;
            a1l1 = new_a1l1;
            a1l2 = new_a1l2;
        }
        return (int)((a0l0 + a0l1 + a0l2 + a1l0 + a1l1 + a1l2) % MOD);
    }

    // State Machine + Dynamic Programming
    // https://leetcode.com/articles/student-attendance-record-ii/
    // Simple version of <tt>checkRecord</tt> and <tt>checkRecord4</tt>
    // time complexity: O(N), space complexity: O(1)
    // beats N/A(27 ms for 58 tests)
    public int checkRecord5(int n) {
        long a0l0 = 1, a0l1 = 0, a0l2 = 0, a1l0 = 0, a1l1 = 0, a1l2 = 0;
        for (int i = 0; i <= n; i++) {
            long a0l0_ = (a0l0 + a0l1 + a0l2) % MOD;
            a0l2 = a0l1;
            a0l1 = a0l0;
            a0l0 = a0l0_;
            long a1l0_ = (a0l0 + a1l0 + a1l1 + a1l2) % MOD;
            a1l2 = a1l1;
            a1l1 = a1l0;
            a1l0 = a1l0_;
        }
        return (int)a1l0;
    }

    void test(int n, int expected) {
        assertEquals(expected, checkRecord(n));
        assertEquals(expected, checkRecord2(n));
        assertEquals(expected, checkRecord3(n));
        assertEquals(expected, checkRecord4(n));
        assertEquals(expected, checkRecord5(n));
    }

    @Test
    public void test() {
        test(1, 3);
        test(2, 8);
        test(3, 19);
        test(4, 43);
        test(5, 94);
        test(6, 200);
        test(7, 418);
        test(8, 861);
        test(9, 1753);
        test(10, 3536);
        test(100, 985598218);
        test(1000, 250434094);
        test(10000, 67802379);
        test(100000, 749184020);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
