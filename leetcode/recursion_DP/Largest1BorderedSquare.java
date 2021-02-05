import org.junit.Test;

import static org.junit.Assert.*;

// LC1139: https://leetcode.com/problems/largest-1-bordered-square/
//
// Given a 2D grid of 0s and 1s, return the number of elements in the largest square subgrid that
// has all 1s on its border, or 0 if such a subgrid doesn't exist in the grid.
//
// Constraints:
// 1 <= grid.length <= 100
// 1 <= grid[0].length <= 100
// grid[i][j] is 0 or 1
public class Largest1BorderedSquare {
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N*MIN(M,N)), space complexity: O(M*N)
    // 3 ms(95.70%), 39.5 MB(75.99%) for 61 tests
    public int largest1BorderedSquare(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] rows = new int[m][n + 1];
        int[][] cols = new int[m + 1][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0) {
                    rows[i][j + 1] = rows[i][j] + 1;
                    cols[i + 1][j] = cols[i][j] + 1;
                }
            }
        }
        for (int k = Math.min(m, n); k > 0; k--) {
            for (int i = 0; i <= m - k; i++) {
                for (int j = 0; j <= n - k; j++) {
                    if (rows[i][j + k] >= k && rows[i + k - 1][j + k] >= k && cols[i + k][j] >= k
                        && cols[i + k][j + k - 1] >= k) {
                        return k * k;
                    }
                }
            }
        }
        return 0;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N*MIN(M,N)), space complexity: O(M*N)
    // 2 ms(100.00%), 39.4 MB(84.59%) for 61 tests
    public int largest1BorderedSquare2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] rows = new int[m][n + 1];
        int[][] cols = new int[m + 1][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0) {
                    rows[i][j + 1] = rows[i][j] + 1;
                    cols[i + 1][j] = cols[i][j] + 1;
                }
            }
        }
        int max = 0;
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                for (int size = Math.min(rows[i][j + 1], cols[i + 1][j]); size > max; size--) {
                    if (cols[i + 1][j - size + 1] >= size && rows[i - size + 1][j + 1] >= size) {
                        max = size;
                        break;
                    }
                }
            }
        }
        return max * max;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N*MIN(M,N)), space complexity: O(M*N)
    // 4 ms(80.29%), 39.4 MB(84.59%) for 61 tests
    public int largest1BorderedSquare3(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] rows = new int[m + 1][n + 1];
        int[][] cols = new int[m + 1][n + 1];
        int max = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (grid[i - 1][j - 1] == 0) { continue; }

                rows[i][j] = rows[i - 1][j] + 1;
                cols[i][j] = cols[i][j - 1] + 1;
                for (int size = Math.min(rows[i][j], cols[i][j]); size > 0; size--) {
                    if (rows[i][j - size + 1] >= size && cols[i - size + 1][j] >= size) {
                        max = Math.max(max, size);
                        break;
                    }
                }
            }
        }
        return max * max;
    }

    private void test(int[][] grid, int expected) {
        assertEquals(expected, largest1BorderedSquare(grid));
        assertEquals(expected, largest1BorderedSquare2(grid));
        assertEquals(expected, largest1BorderedSquare3(grid));
    }

    @Test public void test() {
        test(new int[][] {{1, 1, 1, 1, 1, 1, 0, 1}, {1, 0, 1, 1, 1, 1, 1, 1},
                          {0, 1, 1, 0, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1},
                          {0, 1, 0, 1, 0, 1, 1, 0}}, 16);
        test(new int[][] {{0}}, 0);
        test(new int[][] {{1, 1, 0}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, 9);
        test(new int[][] {{1, 0}, {1, 1}}, 1);
        test(new int[][] {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}}, 9);
        test(new int[][] {{1, 1, 0, 0}}, 1);
        test(new int[][] {{0, 0}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
