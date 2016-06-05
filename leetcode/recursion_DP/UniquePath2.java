import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Follow up for "Unique Paths":
// Now consider if some obstacles are added to the grids. How many unique paths
// would there be?
// An obstacle and empty space is marked as 1 and 0 respectively in the grid.
public class UniquePath2 {
    // Time Limit Exceeded
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int[] count = new int[1];
        visit(obstacleGrid, 0, 0, count);
        return count[0];
    }

    private void visit(int[][] grid, int x, int y, int[] count) {
        int m = grid.length;
        int n = grid[0].length;
        if (x == m - 1 && y == n - 1) {
            count[0]++;
            return;
        }

        if (x < m - 1) {
            if (grid[x + 1][y] == 0) {
                visit(grid, x + 1, y, count);
            }
        }

        if (y < n - 1) {
            if (grid[x][y + 1] == 0) {
                visit(grid, x, y + 1, count);
            }
        }
    }

    // the recursion method used in UniquePath's uniquePaths2 and its DP
    // version uniquePaths3 should still work, but too tedious to show here.

    // beats 17.74%
    public int uniquePathsWithObstacles2(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        int[][] count = new int[m][n];
        count[0][0] = 1; // 1 - obstacleGrid[0][0];
        for (int i = 1; i < m; i++) {
            if (obstacleGrid[i][0] == 0) {
                count[i][0] = count[i - 1][0];
            }
        }
        for (int j = 1; j < n; j++) {
            if (obstacleGrid[0][j] == 0) {
                count[0][j] = count[0][j - 1];
            }
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) {
                    count[i][j] = count[i - 1][j] + count[i][j - 1];
                }
            }
        }
        return count[m - 1][n - 1];
    }

    // beats 17.74%
    public int uniquePathsWithObstacles3(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[] count = new int[n];

        count[0] = 1; // 1 - obstacleGrid[0][0];
        for (int i = 1; i < n; i++) {
            count[i] = (1 - obstacleGrid[0][i]) * count[i - 1];
        }

        for (int i = 1; i < m; i++) {
            count[0] *= (1 - obstacleGrid[i][0]);
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) {
                    count[j] += count[j - 1];
                } else {
                    count[j] = 0;
                }
            }
        }
        return count[n - 1];
    }

    void test(Function<int[][], Integer> path, String name,
              int expected, int[][] grid) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)path.apply(grid));
        System.out.format("%s %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int expected, int[][] grids) {
        UniquePath2 p = new UniquePath2();
        test(p::uniquePathsWithObstacles, "uniquePathsWithObstacles", expected, grids);
        test(p::uniquePathsWithObstacles2, "uniquePathsWithObstacles2", expected, grids);
        test(p::uniquePathsWithObstacles3, "uniquePathsWithObstacles3", expected, grids);
    }

    @Test
    public void test1() {
        test(2, new int[][] { {0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        test(0, new int[][] { {0, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        test(0, new int[][] { {0}, {1}});
        // unclear when the (0, 0) is obstacle
        test(2, new int[][] { {1, 0, 0}, {0, 1, 0}, {0, 0, 0}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UniquePath2");
    }
}
