import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1269: https://leetcode.com/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/
//
// You have a pointer at index 0 in an array of size arrLen. At each step, you can move 1 position
// to the left, 1 position to the right in the array or stay in the same place  (The pointer should
// not be placed outside the array at any time). Given two integers steps and arrLen, return the
// number of ways such that your pointer still at index 0 after exactly steps steps.
// Since the answer may be too large, return it modulo 10^9 + 7.
// Constraints:
//
// 1 <= steps <= 500
// 1 <= arrLen <= 10^6
public class NumWays {
    static final int MOD = 1_000_000_000 + 7;

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(S ^ 2), space complexity: O(S ^ 2)
    // 11 ms(100.00%), 35.8 MB(100%) for 31 tests
    public int numWays(int steps, int arrLen) {
        int n = Math.min(steps, arrLen);
        int[][] dp = new int[steps + 1][n + 1];
        dp[0][0] = 1;
        for (int s = 1; s <= steps; s++) {
            for (int i = 0; i < n; i++) {
                dp[s][i] = dp[s - 1][i];
                if (i > 0) {
                    dp[s][i] += dp[s - 1][i - 1];
                    dp[s][i] %= MOD;
                }
                if (i < n - 1) {
                    dp[s][i] += dp[s - 1][i + 1];
                    dp[s][i] %= MOD;
                }
            }
        }
        return dp[steps][0];
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(S ^ 2), space complexity: O(S)
    // 10 ms(100.00%), 35.8 MB(100%) for 31 tests
    public int numWays2(int steps, int arrLen) {
        int n = Math.min(steps, arrLen);
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int s = 0; s < steps; s++) {
            int[] ndp = new int[n];
            for (int i = 0; i < n; i++) {
                for (int j = i - 1; j <= i + 1; j++) {
                    if (j >= 0 && j < n) {
                        ndp[j] = (ndp[j] + dp[i]) % MOD;
                    }
                }
            }
            dp = ndp;
        }
        return dp[0];
    }

    // Solution of Choice
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(S ^ 2), space complexity: O(S)
    // 7 ms(100.00%), 33.3 MB(100%) for 31 tests
    public int numWays3(int steps, int arrLen) {
        int n = Math.min(steps / 2 + 1, arrLen);
        int[][] dp = new int[n + 2][2];
        dp[1][0] = 1;
        for (int s = 1; s <= steps; s++) {
            for (int i = 1; i <= n; i++) {
                dp[i][s & 1] = (dp[i][(s - 1) & 1] + dp[i - 1][(s - 1) & 1]) % MOD;
                dp[i][s & 1] = (dp[i][s & 1] + dp[i + 1][(s - 1) & 1]) % MOD;
            }
        }
        return dp[1][steps & 1];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(S ^ 2), space complexity: O(S ^ 2)
    // 5 ms(100.00%), 35.5 MB(100%) for 31 tests
    public int numWays4(int steps, int arrLen) {
        int n = Math.min(steps / 2 + 1, arrLen);
        int[][] dp = new int[n][steps + 1];
        for (int[] a : dp) {
            Arrays.fill(a, -1);
        }
        return countWays(0, steps, dp);
    }

    private int countWays(int pos, int steps, int[][] dp) {
        if (pos < 0 || pos >= dp.length) { return 0; }
        if (steps == 0) { return pos == 0 ? 1 : 0; }
        if (dp[pos][steps] >= 0) { return dp[pos][steps]; }

        return dp[pos][steps] =
                ((countWays(pos + 1, steps - 1, dp)
                  + countWays(pos - 1, steps - 1, dp)) % MOD
                 + countWays(pos, steps - 1, dp)) % MOD;
    }

    void test(int steps, int arrLen, int expected) {
        assertEquals(expected, numWays(steps, arrLen));
        assertEquals(expected, numWays2(steps, arrLen));
        assertEquals(expected, numWays3(steps, arrLen));
        assertEquals(expected, numWays4(steps, arrLen));
    }

    @Test
    public void test() {
        //        test(2, 2, 2);
        //        test(3, 2, 4);
        //        test(2, 4, 2);
        //        test(5, 8, 21);
        //        test(12, 10, 15511);
        //        test(16, 13, 853467);
        test(17, 5, 2226219);
        test(27, 7, 127784505);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
