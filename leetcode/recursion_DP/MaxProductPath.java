import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1594: https://leetcode.com/problems/maximum-non-negative-product-in-a-matrix/
//
// You are given a rows x cols matrix grid. Initially, you are located at the top-left corner
// (0, 0), and in each step, you can only move right or down in the matrix.
// Among all possible paths starting from the top-left corner (0, 0) and ending in the bottom-right
// corner (rows - 1, cols - 1), find the path with the maximum non-negative product. The product of
// a path is the product of all integers in the grid cells visited along the path.
// Return the maximum non-negative product modulo 10^9+7. Return -1 if negative.
// Notice that the modulo is performed after getting the maximum product.
public class MaxProductPath {
    private static final int MOD = 1_000_000_007;

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(M*N)
    // 1 ms(100.00%), 38.2 MB(99.72%) for 159 tests
    public int maxProductPath(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        long[][] dpMax = new long[m][n];
        long[][] dpMin = new long[m][n];
        dpMax[0][0] = dpMin[0][0] = grid[0][0];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i + j == 0) { continue; }

                int v = grid[i][j];
                if (i == 0) {
                    dpMax[i][j] = v * dpMax[i][j - 1];
                    dpMin[i][j] = v * dpMin[i][j - 1];
                } else if (j == 0) {
                    dpMax[i][j] = v * dpMax[i - 1][j];
                    dpMin[i][j] = v * dpMin[i - 1][j];
                } else {
                    long a = v * Math.max(dpMax[i][j - 1], dpMax[i - 1][j]);
                    long b = v * Math.min(dpMin[i][j - 1], dpMin[i - 1][j]);
                    dpMax[i][j] = Math.max(a, b);
                    dpMin[i][j] = Math.min(a, b);
                }
            }
        }
        return (dpMax[m - 1][n - 1] < 0) ? -1 : (int)((dpMax[m - 1][n - 1]) % MOD);
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(M*N)
    // 1 ms(100.00%), 38.0 MB(99.81%) for 159 tests
    public int maxProductPath2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        long[][] dpMax = new long[m][n];
        long[][] dpMin = new long[m][n];
        dpMax[0][0] = dpMin[0][0] = grid[0][0];
        for (int i = 1; i < m; i++) {
            dpMax[i][0] = dpMin[i][0] = dpMax[i - 1][0] * grid[i][0];
        }
        for (int j = 1; j < n; j++) {
            dpMax[0][j] = dpMin[0][j] = dpMax[0][j - 1] * grid[0][j];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                int v = grid[i][j];
                long a = v * Math.max(dpMax[i][j - 1], dpMax[i - 1][j]);
                long b = v * Math.min(dpMin[i][j - 1], dpMin[i - 1][j]);
                dpMax[i][j] = Math.max(a, b);
                dpMin[i][j] = Math.min(a, b);
            }
        }
        return (dpMax[m - 1][n - 1] < 0) ? -1 : (int)((dpMax[m - 1][n - 1]) % MOD);
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(N)
    // 1 ms(100.00%), 38.3 MB(99.58%) for 159 tests
    public int maxProductPath3(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        long[] dpMax = new long[n];
        long[] dpMin = new long[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int v = grid[i][j];
                if (i == 0 && j == 0) {
                    dpMin[j] = dpMax[j] = v;
                    continue;
                }

                long min = (i == 0) ? Long.MAX_VALUE : Math.min(v * dpMin[j], v * dpMax[j]);
                long max = (i == 0) ? Long.MIN_VALUE : Math.max(v * dpMin[j], v * dpMax[j]);
                if (j > 0) {
                    min = Math.min(Math.min(v * dpMin[j - 1], v * dpMax[j - 1]), min);
                    max = Math.max(Math.max(v * dpMin[j - 1], v * dpMax[j - 1]), max);
                }
                dpMin[j] = min;
                dpMax[j] = max;
            }
        }
        return dpMax[n - 1] >= 0 ? (int)(dpMax[n - 1] % MOD) : -1;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, maxProductPath(grid));
        assertEquals(expected, maxProductPath2(grid));
        assertEquals(expected, maxProductPath3(grid));
    }

    @Test public void test() {
        test(new int[][] {{-1, -2, -3}, {-2, -3, -3}, {-3, -3, -2}}, -1);
        test(new int[][] {{1, -2, 1}, {1, -2, 1}, {3, -4, 1}}, 8);
        test(new int[][] {{1, 3}, {0, -4}}, 0);
        test(new int[][] {{1, 4, 4, 0}, {-2, 0, 0, 1}, {1, -1, 1, 1}}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
