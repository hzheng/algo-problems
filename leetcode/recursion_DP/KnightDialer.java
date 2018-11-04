import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/knight-dialer/
//
// A chess knight can move as indicated in the chess diagram below:
// This time, we place our chess knight on any numbered key of a phone pad
// (indicated above), and the knight makes N-1 hops.  Each hop must be from one 
// key to another numbered key. Each time it lands on a key (including the 
// initial placement of the knight), it presses the number of that key, pressing
// N digits total. How many distinct numbers can you dial in this manner?
// Since the answer may be large, output the answer modulo 10^9 + 7.
// Note:
// 1 <= N <= 5000
public class KnightDialer {
    private static int MOD = 1000_000_000 + 7;
    private static final int[][] NEXT = {{4, 6}, {6, 8}, {7, 9}, {4, 8}, {3, 9, 0}, {}, {1, 7, 0},
                                         {2, 6}, {1, 3}, {2, 4}};

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats %(70 ms for 120 tests)
    public int knightDialer(int N) {
        long[] dp = new long[10];
        Arrays.fill(dp, 1);
        for (int hops = 0; hops < N - 1; hops++) {
            long[] nextDp = new long[10];
            for (int i = 0; i < 10; i++) {
                for (int next : NEXT[i]) {
                    nextDp[i] = (nextDp[i] + dp[next]) % MOD;
                }
            }
            dp = nextDp;
        }
        long res = 0;
        for (long x : dp) {
            res += x;
        }
        return (int)(res % MOD);
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats %(72 ms for 120 tests)
    public int knightDialer2(int N) {
        int[][] dp = new int[2][10];
        Arrays.fill(dp[0], 1);
        for (int hops = 0; hops < N - 1; hops++) {
            Arrays.fill(dp[~hops & 1], 0);
            for (int i = 0; i < 10; i++) {
                for (int next : NEXT[i]) {
                    dp[~hops & 1][i] += dp[hops & 1][next];
                    dp[~hops & 1][i] %= MOD;
                    // or:
                    // dp[~hops & 1][next] += dp[hops & 1][i];
                    // dp[~hops & 1][next] %= MOD;
                }
            }
        }
        long res = 0;
        for (int x : dp[~N & 1]) {
            res += x;
        }
        return (int)(res % MOD);
    }

    private static final long[][] MATRIX = {{0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
                                            {0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
                                            {0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                                            {0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
                                            {1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
                                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                                            {1, 1, 0, 0, 0, 0, 0, 1, 0, 0},
                                            {0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                                            {0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                                            {0, 0, 1, 0, 1, 0, 0, 0, 0, 0}};

    // Math
    // time complexity: O(N), space complexity: O(1)
    // Time Limit Exceeded
    public int knightDialer3(int N) {
        if (N == 1) return 10;

        long[][] product = MATRIX;
        for (int i = N - 2; i > 0; i--) {
            product = multiply(product, MATRIX);
        }
        long res = 0;
        for (long[] row : product) {
            for (long x : row) {
                res = (res + x) % MOD;
            }
        }
        return (int)res;
    }

    private long[][] multiply(long[][] m1, long[][] m2) {
        int n = m1.length;
        long[][] res = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    res[i][j] = (res[i][j] + m1[i][k] * m2[k][j]) % MOD;
                }
            }
        }
        return res;
    }

    // Math
    // time complexity: O(log(N)), space complexity: O(log(N))
    // beats %(31 ms for 120 tests)
    public int knightDialer4(int N) {
        if (N == 1) return 10;

        long[][] product = pow(MATRIX, N - 1);
        long res = 0;
        for (long[] row : product) {
            for (long x : row) {
                res = (res + x) % MOD;
            }
        }
        return (int)res;
    }

    private long[][] pow(long[][] m, int times) {
        if (times == 1) return m;

        long[][] half = pow(m, times / 2);
        long[][] res = multiply(half, half);
        return (times % 2 == 0) ? res : multiply(res, m);
    }

    void test(int N, int expected) {
        assertEquals(expected, knightDialer(N));
        assertEquals(expected, knightDialer2(N));
        assertEquals(expected, knightDialer3(N));
        assertEquals(expected, knightDialer4(N));
    }

    @Test
    public void test() {
        test(1, 10);
        test(2, 20);
        test(3, 46);
        test(4, 104);
        test(5, 240);
        test(10, 14912);
        test(100, 540641702);
        test(3353, 283772296);
        test(5000, 406880451);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
