import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1866: https://leetcode.com/problems/number-of-ways-to-rearrange-sticks-with-k-sticks-visible/
//
// There are n uniquely-sized sticks whose lengths are integers from 1 to n. You want to arrange the
// sticks such that exactly k sticks are visible from the left. A stick is visible from the left if
// there are no longer sticks to the left of it.
//
// For example, if the sticks are arranged [1,3,2,5,4], then the sticks with lengths 1, 3, and 5
// are visible from the left.
// Given n and k, return the number of such arrangements. Since the answer may be large, return it
// modulo 10^9 + 7.
//
// Constraints:
// 1 <= n <= 1000
// 1 <= k <= n
public class RearrangeSticks {
    private static final int MOD = 1_000_000_007;

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N*(N-K)), space complexity: O(N*(N-K))
    // 107 ms(75.93%), 44.2 MB(93.56%) for 50 tests
    public int rearrangeSticks(int n, int k) {
        int m = n - k;
        return f(n, m, new int[n + 1][m + 1]);
    }

    private int f(int n, int m, int[][] dp) {
        if (n <= m) { return 0; }
        if (m == 0) { return 1; }

        int v = dp[n][m];
        if (v > 0) { return v; }

        long res = (f(n - 1, m, dp) + (n - 1L) * f(n - 1, m - 1, dp) % MOD) % MOD;
        return dp[n][m] = (int)res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N*K), space complexity: O(N*K)
    // 104 ms(66.29%), 47.9 MB(86.78%) for 50 tests
    public int rearrangeSticks2(int n, int k) {
        return f2(n, k, new int[n + 1][k + 1]);
    }

    private int f2(int n, int k, int[][] dp) {
        if (n == k) { return 1; }
        if (k == 0) { return 0; }

        int v = dp[n][k];
        if (v > 0) { return v; }

        long res = (f2(n - 1, k - 1, dp) + (n - 1L) * f2(n - 1, k, dp) % MOD) % MOD;
        return dp[n][k] = (int)res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N*K), space complexity: O(N*K)
    // 145 ms(61.69%), 48.1 MB(86.10%) for 50 tests
    public int rearrangeSticks3(int n, int k) {
        int[][] dp = new int[n + 1][k + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= Math.min(i, k); j++) {
                dp[i][j] = (int)((dp[i - 1][j] * (i - 1L) % MOD + dp[i - 1][j - 1]) % MOD);
            }
        }
        return dp[n][k];
    }

    private void test(int n, int k, int expected) {
        assertEquals(expected, rearrangeSticks(n, k));
        assertEquals(expected, rearrangeSticks2(n, k));
        assertEquals(expected, rearrangeSticks3(n, k));
    }

    @Test public void test1() {
        test(3, 2, 3);
        test(5, 5, 1);
        test(20, 11, 647427950);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
