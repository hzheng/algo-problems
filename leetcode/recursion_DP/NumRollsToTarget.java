import org.junit.Test;

import static org.junit.Assert.*;

// LC1155: https://leetcode.com/problems/number-of-dice-rolls-with-target-sum/
//
// You have d dice, and each die has f faces numbered 1, 2, ..., f. Return the number of possible
// ways (out of fd total ways) modulo 10^9 + 7 to roll the dice so the sum of the face up numbers
// equals target.
//
// Constraints:
// 1 <= d, f <= 30
// 1 <= target <= 1000
public class NumRollsToTarget {
    private static final int MOD = 1_000_000_007;

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(D*T*F), space complexity: O(D*T)
    // 37 ms(41.14%), 38.1 MB(57.29%) for 65 tests
    public int numRollsToTarget(int d, int f, int target) {
        int[][] dp = new int[d + 1][target + 1];
        dp[0][0] = 1;
        for (int t = 1; t <= target; t++) {
            for (int i = 1; i <= d; i++) {
                for (int j = Math.min(f, t); j > 0; j--) {
                    dp[i][t] += dp[i - 1][t - j];
                    dp[i][t] %= MOD;
                }
            }
        }
        return dp[d][target];
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(D*T*F), space complexity: O(T)
    // 30 ms(54.77%), 35.5 MB(99.60%) for 65 tests
    public int numRollsToTarget2(int d, int f, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int i = 1; i <= d; i++) {
            for (int t = target; t >= 0; t--) {
                dp[t] = 0;
                for (int j = Math.min(f, t); j > 0; j--) {
                    dp[t] = (dp[t] + dp[t - j]) % MOD;
                }
            }
        }
        return dp[target];
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(D*T), space complexity: O(T)
    // 6 ms(92.07%), 36 MB(94.85%) for 65 tests
    public int numRollsToTarget3(int d, int f, int target) {
        int[] dp1 = new int[target + 1];
        int[] dp2 = new int[target + 1];
        dp1[0] = 1;
        for (int i = 1; i <= d; i++) {
            int prev = dp1[0]; // prev=dp[i-1][j-1]+dp[i-1][j-2]+...+dp[i-1][j-f]
            for (int t = 1; t <= target; t++) {
                dp2[t] = prev;
                prev = (prev + dp1[t]) % MOD;
                if (t >= f) {
                    prev = (prev - dp1[t - f] + MOD) % MOD;
                }
            }
            int[] tmp = dp1;
            dp1 = dp2;
            dp2 = tmp;
            dp2[0] = 0;
        }
        return dp1[target];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(D*T*F), space complexity: O(D*T)
    // 12 ms(79.05%), 37.9 MB(74.15%) for 65 tests
    public int numRollsToTarget4(int d, int f, int target) {
        return numRolls(d, f, target, new Integer[d + 1][target + 1]);
    }

    private int numRolls(int d, int f, int target, Integer[][] dp) {
        if (d == 0) { return target > 0 ? 0 : 1; }

        Integer cached = dp[d][target];
        if (cached != null) { return cached; }

        int res = 0;
        for (int t = Math.max(0, target - f); t < target; t++) {
            res = (res + numRolls(d - 1, f, t, dp)) % MOD;
        }
        return dp[d][target] = res;
    }

    private void test(int d, int f, int target, int expected) {
        assertEquals(expected, numRollsToTarget(d, f, target));
        assertEquals(expected, numRollsToTarget2(d, f, target));
        assertEquals(expected, numRollsToTarget3(d, f, target));
        assertEquals(expected, numRollsToTarget4(d, f, target));
    }

    @Test public void test() {
        test(1, 6, 3, 1);
        test(2, 6, 7, 6);
        test(2, 5, 10, 1);
        test(1, 2, 3, 0);
        test(30, 30, 500, 222616187);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
