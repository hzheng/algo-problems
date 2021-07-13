import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1931: https://leetcode.com/problems/painting-a-grid-with-three-different-colors/
//
// You are given two integers m and n. Consider an m x n grid where each cell is initially white.
// You can paint each cell red, green, or blue. All cells must be painted.
// Return the number of ways to color the grid with no two adjacent cells having the same color.
// Since the answer can be very large, return it modulo 10^9 + 7.
//
// Constraints:
// 1 <= m <= 5
// 1 <= n <= 1000
public class ColorTheGrid {
    private static final int MOD = 1_000_000_007;

    // 1D-Dynamic Programming(Bottom-Up) + Hash Table
    // time complexity: O(N*(3^2M)), space complexity: O(3^M)
    // 92 ms(79.26%), 38.4 MB(89.89%) for 84 tests
    public int colorTheGrid(int m, int n) {
        int max = (int)Math.pow(3, m);
        int[] dp = new int[max];
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < max; i++) {
            int[] a = convert(i, m);
            if (bad(a)) { continue; }

            dp[i] = 1;
            outer:
            for (int j = 0; j < max; j++) {
                int[] b = convert(j, m);
                if (bad(b)) { continue; }

                for (int k = 0; k < m; k++) {
                    if (a[k] == b[k]) { continue outer; }
                }
                map.computeIfAbsent(i, x -> new ArrayList<>()).add(j);
            }
        }
        for (int col = 1; col < n; col++) {
            int[] ndp = new int[max];
            for (int i = 0; i < max; i++) {
                for (int j : map.getOrDefault(i, Collections.emptyList())) {
                    ndp[j] = (ndp[j] + dp[i]) % MOD;
                }
            }
            dp = ndp;
        }
        int res = 0;
        for (int cnt : dp) {
            res = (res + cnt) % MOD;
        }
        return res;
    }

    private int[] convert(int a, int m) {
        int[] res = new int[m];
        for (int x = a, i = 0; x > 0; i++, x /= 3) {
            res[i] = x % 3;
        }
        return res;
    }

    private boolean bad(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            if (a[i] == a[i - 1]) { return true; }
        }
        return false;
    }

    // Recursion + DFS + Dynamic Programming(Top-Down) + Bit Manipulation
    // time complexity: O(N*(3^2M)), space complexity: O(N*(4^M))
    // 123 ms(76.06%), 41.7 MB(60.64%) for 84 tests
    public int colorTheGrid2(int m, int n) {
        return colorTheGrid(m, n, 0, 0, 0, 0, new int[n + 1][1 << (m * 2)]);
    }

    private int colorTheGrid(int m, int n, int row, int col, int cur, int prev, int[][] dp) {
        if (row == m) { // start a new column
            return colorTheGrid(m, n, 0, col + 1, 0, cur, dp);
        }
        if (col == n) { return 1; } // finish 1
        if (row == 0 && dp[col][prev] > 0) { return dp[col][prev]; } // read cache

        int res = 0;
        int up = (row == 0) ? 0 : (cur >> ((row - 1) * 2)) & 3;
        int left = prev >> (row * 2) & 3;
        for (int k = 1; k <= 3; k++) { // try all colors
            if (k != left && k != up) {
                res = (res + colorTheGrid(m, n, row + 1, col, cur + (k << (row * 2)), prev, dp))
                      % MOD;
            }
        }
        if (row == 0) {
            dp[col][prev] = res;
        }
        return res;
    }

    // Recursion + DFS + 2D-Dynamic Programming(Bottom-Up) + Bit Manipulation + Hash Table
    // time complexity: O(N*(3^2M)), space complexity: O(N*(4^M))
    // 302 ms(45.75%), 38.3 MB(94.15%) for 84 tests
    public int colorTheGrid3(int m, int n) {
        Map<Integer, Integer> count = new HashMap<>();
        dfs(count, 0, m, -1, 0);
        Set<Integer> set = count.keySet();
        for (int i = 1; i < n; i++) {
            Map<Integer, Integer> nextCount = new HashMap<>();
            for (int a : set) {
                for (int b : set) {
                    if ((a & b) == 0) {
                        nextCount.put(a, (nextCount.getOrDefault(a, 0) + count.get(b)) % MOD);
                    }
                }
            }
            count = nextCount;
        }
        int res = 0;
        for (int cnt : count.values()) {
            res = (res + cnt) % MOD;
        }
        return res;
    }

    private void dfs(Map<Integer, Integer> count, int row, int m, int prev, int cur) {
        if (row == m) {
            count.put(cur, 1);
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (i != prev) {
                dfs(count, row + 1, m, i, (cur << 3) | (1 << i));
            }
        }
    }

    private void test(int m, int n, int expected) {
        assertEquals(expected, colorTheGrid(m, n));
        assertEquals(expected, colorTheGrid2(m, n));
        assertEquals(expected, colorTheGrid3(m, n));
    }

    @Test public void test1() {
        test(1, 1, 3);
        test(1, 2, 6);
        test(5, 5, 580986);
        test(5, 500, 859596253);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
