import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC741: https://leetcode.com/problems/cherry-pickup/
//
// In a N x N grid field of cherries, each cell is 1 of 3 possible integers.
// 0 means the cell is empty, so you can pass through;
// 1 means the cell contains a cherry, that you can pick up and pass through;
// -1 means the cell contains a thorn that blocks your way.
// Your task is to collect maximum cherries possible by the rules below:
// Starting at the position (0, 0) and reaching (N-1, N-1) by moving right or
// down through valid path cells (cells with value 0 or 1);
// After reaching (N-1, N-1), returning to (0, 0) by moving left or up through
// valid path cells;
// When passing through a path cell containing a cherry, you pick it up and the
// cell becomes an empty cell (0); If there is no valid path between (0, 0) and
// (N-1, N-1), then no cherries can be collected.
public class CherryPickup {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 3)
    // beats 40.12%(33 ms for 56 tests)
    public int cherryPickup(int[][] grid) {
        int n = grid.length;
        int[][][] dp = new int[n][n][n];
        // dp[r1][c1][c2] be the best by 2 people starting at (r1, c1) and
        // (r2, c2) and walking towards (N-1, N-1), where r2 = r1+c1-c2. 
        for (int[][] layer : dp) {
            for (int[] row : layer) {
                Arrays.fill(row, Integer.MIN_VALUE);
            }
        }
        return Math.max(0, pickup(grid, 0, 0, 0, dp));
    }

    private int pickup(int[][] grid, int r1, int c1, int c2, int[][][] dp) {
        int n = grid.length;
        int r2 = r1 + c1 - c2;
        if (r1  == n || r2 == n || c1 == n || c2 == n || grid[r1][c1] < 0
            || grid[r2][c2] < 0) return Integer.MIN_VALUE;        
        
        if (r1 == n - 1 && c1 == n - 1) return grid[r1][c1];

        if (dp[r1][c1][c2] != Integer.MIN_VALUE) return dp[r1][c1][c2];

        int res = grid[r1][c1] + ((c1 != c2) ? grid[r2][c2] : 0);
        res += Math.max(Math.max(pickup(grid, r1, c1 + 1, c2 + 1, dp),
                                 pickup(grid, r1 + 1, c1, c2 + 1, dp)),
                        Math.max(pickup(grid, r1, c1 + 1, c2, dp),
                                 pickup(grid, r1 + 1, c1, c2, dp)));
        return dp[r1][c1][c2] = res;
    }

    // Dynamic Programming(Bottom-up)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 23.84%(39 ms for 56 tests)
    public int cherryPickup2(int[][] grid) {
        int n = grid.length;
        int[][] dp = new int[n][n]; // dp[c1][c2]
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MIN_VALUE);
        }
        dp[0][0] = grid[0][0];
        for (int k = 1; k <= 2 * n - 2; k++) {
            int[][] dp2 = new int[n][n];
            for (int[] row : dp2) {
                Arrays.fill(row, Integer.MIN_VALUE);
            }
            for (int i = Math.max(0, k - n + 1); i <= Math.min(n - 1, k); i++) {
                for (int j = Math.max(0, k - n + 1); j <= Math.min(n - 1, k); j++) {
                    if (grid[i][k - i] < 0 || grid[j][k - j] < 0) continue;

                    int val = grid[i][k - i] + ((i != j) ? grid[j][k - j] : 0);
                    for (int ii = i - 1; ii <= i; ii++) {
                        for (int jj = j - 1; jj <= j; jj++) {
                            if (ii < 0 || jj < 0) continue;

                            dp2[i][j] = Math.max(dp2[i][j], dp[ii][jj] + val);
                        }
                    }
                }
            }
            dp = dp2;
        }
        return Math.max(0, dp[n - 1][n - 1]);
    }

    // Dynamic Programming(Bottom-up)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 23.84%(39 ms for 56 tests)
    public int cherryPickup3(int[][] grid) {
        int n = grid.length;
        int[][] dp = new int[n][n];
        dp[0][0] = grid[0][0];
        for (int k = 1; k < 2 * n - 1; k++) {
            for (int r1 = n - 1; r1 >= 0; r1--) {
                for (int r2 = n - 1; r2 >= 0; r2--) {
                    int c1 = k - r1;
                    int c2 = k - r2;
                    if (c1 < 0 || c1 >= n || c2 < 0 || c2 >= n 
                        || grid[r1][c1] < 0 || grid[r2][c2] < 0) {
                        dp[r1][r2] = -1;
                        continue;
                    }
                    if (r1 > 0) {
                        dp[r1][r2] = Math.max(dp[r1][r2], dp[r1 - 1][r2]);
                    }
                    if (r2 > 0) {
                        dp[r1][r2] = Math.max(dp[r1][r2], dp[r1][r2 - 1]);
                    }
                    if (r1 > 0 && r2 > 0) {
                        dp[r1][r2] = Math.max(dp[r1][r2], dp[r1 - 1][r2 - 1]);
                    }
                    if (dp[r1][r2] >= 0) {
                        dp[r1][r2] += grid[r1][c1] + (r1 != r2 ? grid[r2][c2] : 0);
                    }
                }
            }
        }
        return Math.max(dp[n - 1][n - 1], 0);
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, cherryPickup(grid));
        assertEquals(expected, cherryPickup2(grid));
        assertEquals(expected, cherryPickup3(grid));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 1, 1, 0, 0 }, { 0, 0, 1, 0, 1 }, { 1, 0, 1, 0, 0 },
                { 0, 0, 1, 0, 0 }, { 0, 0, 1, 1, 1 } }, 11);
        test(new int[][] { { 1, 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 1 },
                { 1, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 1, 1, 1 } },
                15);
        test(new int[][] { { 1, 1, -1 }, { 1, -1, 1 }, { -1, 1, 1 } }, 0);
        test(new int[][] { { 0, 1, -1 }, { 1, 0, -1 }, { 1, 1, 1 } }, 5);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
