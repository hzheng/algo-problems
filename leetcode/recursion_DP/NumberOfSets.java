import java.math.BigInteger;
import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1621: https://leetcode.com/problems/number-of-sets-of-k-non-overlapping-line-segments/
//
// Given n points on a 1-D plane, where the ith point (from 0 to n-1) is at x = i, find the number
// of ways we can draw exactly k non-overlapping line segments such that each segment covers two or
// more points. The endpoints of each segment must have integral coordinates. The k line segments do
// not have to cover all n points, and they are allowed to share endpoints.
// Return the number of ways we can draw k non-overlapping line segments. Since this number can be
// huge, return it modulo 10^9 + 7.
//
// Constraints:
// 2 <= n <= 1000
// 1 <= k <= n-1
public class NumberOfSets {
    private static final int MOD = 1_000_000_007;

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2*K), space complexity: O(N*K)
    // Time Limit Exceeded
    public int numberOfSets(int n, int k) {
        int[][] dp = new int[k + 1][n + 1];
        Arrays.fill(dp[0], 1, n + 1, 1);
        for (int i = 1; i <= k; i++) {
            for (int j = i + 1; j <= n; j++) {
                dp[i][j] = dp[i][j - 1];
                for (int p = i - 1; p < j; p++) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][p]) % MOD;
                }
            }
        }
        return dp[k][n];
    }

    // use cumulative sum to optimize time complexity
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N*K), space complexity: O(N*K)
    // 25 ms(87.81%), 46.7 MB(74.80%) for 68 tests
    public int numberOfSets2(int n, int k) {
        int[][] dp = new int[k + 1][n + 1];
        int[] prefixSum = new int[n + 1];
        prefixSum[0] = 1;
        for (int i = 0; i <= k; i++) {
            int[] prefixSum2 = new int[n + 1];
            for (int j = i + 1; j <= n; j++) {
                dp[i][j] = (dp[i][j - 1] + prefixSum[j - 1]) % MOD;
                prefixSum2[j] = (prefixSum2[j - 1] + dp[i][j]) % MOD;
            }
            prefixSum = prefixSum2;
        }
        return dp[k][n];
    }

    // above 2D-array `dp` is unnecessary
    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N*K), space complexity: O(N)
    // 20 ms(87.81%), 38.6 MB(95.12%) for 68 tests
    public int numberOfSets3(int n, int k) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 0; i <= k; i++) {
            int[] dp2 = new int[n + 1];
            for (int j = i + 1, sum = 0; j <= n; j++) {
                sum = (sum + dp[j - 1]) % MOD;
                dp2[j] = (dp2[j - 1] + sum) % MOD;
            }
            dp = dp2;
        }
        return (dp[n] - dp[n - 1] + MOD) % MOD; // == sum
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N*K), space complexity: O(N*K)
    // 49 ms(62.60%), 45.2 MB(83.74%) for 68 tests
    public int numberOfSets4(int n, int k) {
        int[][] dp = new int[n + 1][k + 1];
        for (int i = 2; i <= n; i++) {
            dp[i][1] = i * (i - 1) / 2;
        }
        for (int i = 3; i <= n; i++) {
            for (int j = 2; j <= k; j++) {
                long count = dp[i - 1][j]; // count of last one not ending at i
                count += dp[i - 1][j - 1]; // count of last one is [i-1, i]
                count += dp[i - 1][j] - dp[i - 2][j]; // count of last one ending at i - 1
                dp[i][j] = (int)((count + MOD) % MOD);
            }
        }
        return dp[n][k];
    }

    // Combinatorics
    // time complexity: O(K), space complexity: O(1)
    // 8 ms(98.37%), 39.1 MB(91.87%) for 68 tests
    public int numberOfSets5(int n, int k) {
        // convert to problem: take k no-endpoints sharing segments from n + k - 1 points
        BigInteger res = BigInteger.valueOf(1);
        for (int i = 1; i <= k * 2; i++) {
            res = res.multiply(BigInteger.valueOf(n + k - i));
            res = res.divide(BigInteger.valueOf(i));
        }
        return res.mod(BigInteger.valueOf(MOD)).intValue();
    }

    // Combinatorics
    // time complexity: O(K*log(MOD)), space complexity: O(1)
    // 5 ms(99.19%), 35.9 MB(97.56%) for 68 tests
    public int numberOfSets6(int n, int k) {
        return (int)combinate(n + k - 1, k * 2);
    }

    private long combinate(int n, int r) {
        long res = 1;
        for (int i = n; i >= n - r + 1; i--) {
            res = (res * i) % MOD;
        }
        for (int i = 1; i <= r; i++) {
            res = (res * inverse(i, MOD)) % MOD;
        }
        return res;
    }

    private long inverse(long x, int mod) {
        long res = 1; // a^(-1) = a^(m-2) % (mod m)
        for (int n = mod - 2; n > 0; n >>= 1) {
            if ((n & 1) != 0) {
                res = (res * x) % mod;
            }
            x = (x * x) % mod;
        }
        return res;
    }

    // 2D-Dynamic Programming(Top-Down) + Recursive
    // time complexity: O(N*K), space complexity: O(N*K)
    // 302 ms(29.27%), 95.2 MB(9.76%) for 68 tests
    public int numberOfSets7(int n, int k) {
        return numberOfSets(n, k, 1, new Integer[n + 1][k + 1][2]);
    }

    private int numberOfSets(int i, int k, int isStart, Integer[][][] dp) {
        if (dp[i][k][isStart] != null) { return dp[i][k][isStart]; }
        if (k == 0) { return 1; }
        if (i == 0) { return 0; }

        int res = numberOfSets(i - 1, k, isStart, dp);
        if (isStart == 1) {
            res += numberOfSets(i - 1, k, 0, dp);
        } else {
            res += numberOfSets(i, k - 1, 1, dp);
        }
        return dp[i][k][isStart] = res % MOD;
    }

    private void test(int n, int k, int expected) {
        assertEquals(expected, numberOfSets(n, k));
        assertEquals(expected, numberOfSets2(n, k));
        assertEquals(expected, numberOfSets3(n, k));
        assertEquals(expected, numberOfSets4(n, k));
        assertEquals(expected, numberOfSets5(n, k));
        assertEquals(expected, numberOfSets6(n, k));
        assertEquals(expected, numberOfSets7(n, k));
    }

    @Test public void test() {
        test(3, 1, 3);
        test(4, 1, 6);
        test(5, 2, 15);
        test(6, 3, 28);
        test(7, 4, 45);
        test(8, 5, 66);
        test(15, 10, 10626);
        test(4, 2, 5);
        test(30, 7, 796297179);
        test(12, 8, 969);
        test(5, 3, 7);
        test(3, 2, 1);
        test(10, 8, 17);
        test(48, 12, 337883431);
        test(1000, 998, 1997);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
