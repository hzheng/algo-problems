import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1223: https://leetcode.com/problems/dice-roll-simulation/
//
// A die simulator generates a random number from 1 to 6 for each roll. You introduced a constraint
// to the generator such that it cannot roll the number i more than rollMax[i] (1-indexed)
// consecutive times. Given an array of integers rollMax and an integer n, return the number of
// distinct sequences that can be obtained with exact n rolls.
// Two sequences are considered different if at least one element differs from each other. Since the
// answer may be too large, return it modulo 10^9 + 7.
//
// Constraints:
// 1 <= n <= 5000
// rollMax.length == 6
// 1 <= rollMax[i] <= 15
public class DieSimulator {
    private final static int MOD = 1_000_000_007;

    // 3-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*M^2*MAX), space complexity: O(N*M*MAX)
    // 291 ms(5.01%), 52.7 MB(10.62%) for 32 tests
    public int dieSimulator(int n, int[] rollMax) {
        int m = rollMax.length;
        int max = Arrays.stream(rollMax).max().getAsInt();
        long[][][] dp = new long[n + 1][m + 1][max + 1];
        dp[0][0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int streak = 0; streak <= max; streak++) {
                for (int prev = (i == 0) ? 0 : 1; prev <= m; prev++) {
                    for (int cur = 1; cur <= m; cur++) {
                        if (cur != prev) {
                            dp[i + 1][cur][1] += dp[i][prev][streak];
                            dp[i + 1][cur][1] %= MOD;
                        } else if (streak + 1 <= rollMax[prev - 1]) {
                            dp[i + 1][cur][streak + 1] += dp[i][prev][streak];
                            dp[i + 1][cur][streak + 1] %= MOD;
                        }
                    }
                }
            }
        }
        long res = 0;
        for (long[] a : dp[n]) {
            res += Arrays.stream(a).sum();
            res %= MOD;
        }
        return (int)res;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*M^2*MAX), space complexity: O(M*MAX)
    // 218 ms(5.01%), 50.9 MB(13.43%) for 32 tests
    public int dieSimulator2(int n, int[] rollMax) {
        int m = rollMax.length;
        int max = Arrays.stream(rollMax).max().getAsInt();
        long[][] dp = new long[m + 1][max + 1];
        for (int i = 0; i < m; i++) {
            dp[i][1] = 1;
        }
        for (int i = 1; i < n; i++) {
            long[][] dp2 = new long[m + 1][max + 1];
            for (int prev = 0; prev < m; prev++) {
                for (int streak = 1; streak <= max; streak++) {
                    for (int cur = 0; cur < m; cur++) {
                        if (cur != prev) {
                            dp2[cur][1] += dp[prev][streak];
                            dp2[cur][1] %= MOD;
                        } else if (streak + 1 <= rollMax[prev]) {
                            dp2[cur][streak + 1] += dp[prev][streak];
                            dp2[cur][streak + 1] %= MOD;
                        }
                    }
                }
            }
            dp = dp2;
        }
        long res = 0;
        for (long[] a : dp) {
            res += Arrays.stream(a).sum();
            res %= MOD;
        }
        return (int)res;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*M*MAX), space complexity: O(N*M)
    // 16 ms(70.34%), 38.1 MB(96.19%) for 32 tests
    public int dieSimulator3(int n, int[] rollMax) {
        int m = rollMax.length;
        int[][] dp = new int[n + 1][m + 1]; // i rolls ends with j, dp[i][m] records total
        dp[0][m] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = Math.min(rollMax[j], i); k > 0; k--) {
                    dp[i][j] = ((dp[i][j] + dp[i - k][m] - dp[i - k][j]) % MOD + MOD) % MOD;
                }
                dp[i][m] = (dp[i][m] + dp[i][j]) % MOD; // sum up
            }
        }
        return (dp[n][m] + MOD) % MOD;
    }

    // Solution of Choice
    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*M), space complexity: O(N*M)
    // 6 ms(5.01%), 38.5 MB(64.93%) for 32 tests
    public int dieSimulator4(int n, int[] rollMax) {
        int m = rollMax.length;
        int[][] dp = new int[n + 1][m + 1]; // i rolls ends with j, dp[i][m] records total
        dp[0][m] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < m; j++) {
                dp[i][j] = dp[i - 1][m];
                int k = i - rollMax[j] - 1;
                if (k >= 0) {
                    dp[i][j] = ((dp[i][j] - dp[k][m] + dp[k][j]) % MOD + MOD) % MOD;
                }
                dp[i][m] = (dp[i][m] + dp[i][j] % MOD) % MOD; // sum up
            }
        }
        return dp[n][m];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N*M*MAX), space complexity: O(N*M*MAX)
    // 102 ms(11.62%), 78.2 MB(5.01%) for 32 tests
    public int dieSimulator5(int n, int[] rollMax) {
        int m = rollMax.length;
        int max = Arrays.stream(rollMax).max().getAsInt();
        return dfs(n, 0, 0, rollMax, new int[n + 1][m + 1][max + 1]);
    }

    private int dfs(int left, int prev, int streak, int[] rollMax, int[][][] dp) {
        if (left == 0) { return 1; }

        if (dp[left][prev][streak] > 0) { return dp[left][prev][streak]; }

        int res = 0;
        for (int i = rollMax.length; i > 0; i--) {
            if (i != prev || streak < rollMax[i - 1]) {
                res = (res + dfs(left - 1, i, (i == prev ? streak : 0) + 1, rollMax, dp)) % MOD;
            }
        }
        return dp[left][prev][streak] = res;
    }

    private void test(int n, int[] rollMax, int expected) {
        assertEquals(expected, dieSimulator(n, rollMax));
        assertEquals(expected, dieSimulator2(n, rollMax));
        assertEquals(expected, dieSimulator3(n, rollMax));
        assertEquals(expected, dieSimulator4(n, rollMax));
        assertEquals(expected, dieSimulator5(n, rollMax));
    }

    @Test public void test() {
        test(2, new int[] {1, 1, 2, 2, 2, 3}, 34);
        test(2, new int[] {1, 1, 1, 1, 1, 1}, 30);
        test(3, new int[] {1, 1, 1, 2, 2, 3}, 181);
        test(20, new int[] {8, 5, 10, 8, 7, 2}, 822005673);
        test(200, new int[] {1, 1, 2, 2, 2, 3}, 373052394);
        test(2000, new int[] {1, 2, 2, 2, 2, 3}, 728567004);
        test(5000, new int[] {3, 2, 4, 9, 2, 10}, 743647911);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
