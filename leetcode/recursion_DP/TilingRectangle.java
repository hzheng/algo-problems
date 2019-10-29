import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1240: https://leetcode.com/problems/tiling-a-rectangle-with-the-fewest-squares/
//
// Given a rectangle of size n x m, find the minimum number of integer-sided squares that tile the
// rectangle.
public class TilingRectangle {
    // time complexity: O(M * N), space complexity: O(M * N)
    // 0 ms(0%), 32.8 MB(100%) for 38 tests
    // Recursion + Dynamic Programming(Top-Down)
    public int tilingRectangle(int n, int m) {
        return tiling(n, m, new int[Math.max(m, n) + 1][Math.max(m, n) + 1]);
    }

    private int tiling(int n, int m, int[][] dp) {
        if (n == 0 || m == 0) { return 0; }
        if (n == m) { return 1; }
        if (n < m) { return tiling(m, n, dp); }
        if (dp[n][m] != 0) { return dp[n][m]; }

        if (m == 1) { return n; }

        int res = tiling(n - m, m, dp) + 1;
        if (n >= m * 2) { return res; }

        // now 2m > n > m > 1
        for (int i = Math.max((n + 1) / 2, n - m); i <= m; i++) {
            for (int j = i; j <= n; j++) {
                res = Math.min(res,
                               tiling(m - i, j, dp)
                               + tiling(n - j, m + i - n, dp)
                               + tiling(j - i, i * 2 - n, dp)
                               + 2);
            }
        }
        return dp[n][m] = res;
    }

    // time complexity: O(M ^ 2 * N ^ 2), space complexity: O(M * N)
    // 3 ms(39.03%), 33.1 MB(100%) for 38 tests
    // Dynamic Programming(Bottom-Up)
    public int tilingRectangle2(int n, int m) {
        int[][] dp = new int[Math.max(m, n) + 1][Math.max(m, n) + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dp[i][j] = (i == j) ? 1 : i * j;
                for (int k = 1; k < i; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[k][j] + dp[i - k][j]);
                }
                for (int k = 1; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[i][j - k]);
                }
                for (int x = 1; x < i; x++) {
                    for (int y = 1; y < j; y++) {
                        dp[i][j] = Math.min(dp[i][j],
                                            dp[x][y + 1] + dp[i - x][y]
                                            + dp[x + 1][j - y - 1]
                                            + dp[i - x - 1][j - y]
                                            + 1); // 1 * 1 square
                    }
                }
            }
        }
        return dp[n][m];
    }

    private void test(int n, int m, int expected) {
        assertEquals(expected, tilingRectangle(n, m));
        assertEquals(expected, tilingRectangle2(n, m));
    }

    @Test public void test() {
        test(2, 3, 3);
        test(5, 8, 5);
        test(11, 13, 6);
        test(11, 12, 7);
        test(11, 6, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
