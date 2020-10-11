import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1568: https://leetcode.com/problems/minimum-number-of-days-to-disconnect-island/
//
// Given a 2D grid consisting of 1s (land) and 0s (water).  An island is a maximal 4-directionally
// (horizontal or vertical) connected group of 1s.
// The grid is said to be connected if we have exactly one island, otherwise is said disconnected.
// In one day, we are allowed to change any single land cell (1) into a water cell (0).
// Return the minimum number of days to disconnect the grid.
// Constraints:
// 1 <= grid.length, grid[i].length <= 30
// grid[i][j] is 0 or 1.
public class MinDaysToDisconnect {
    // Union Find
    // time complexity: O(M^2*N^2), space complexity: O(M*N)
    // 36 ms(58.37%), 38.7 MB(5.10%) for 95 tests
    static int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    public int minDays(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        List<int[]> points = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    points.add(new int[] {i, j, 1});
                }
            }
        }
        if (disconnected(m, n, points)) { return 0; }

        for (int[] pt : points) {
            pt[2] = 0;
            if (disconnected(m, n, points)) {
                return 1;
            }
            pt[2] = 1;
        }
        return 2;
    }

    private boolean disconnected(int m, int n, List<int[]> points) {
        int[] id = new int[m * n];
        Arrays.fill(id, -1);
        int islands = 0;
        for (int[] point : points) {
            int x = point[0];
            int y = point[1];
            if (point[2] == 0) {
                continue;
            }
            int index = x * n + y;
            id[index] = index;
            islands++;
            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                int ni = n * nx + ny;
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || id[ni] < 0) { continue; }

                for (; ni != id[ni]; ni = id[ni] = id[id[ni]]) {}
                if (index != ni) {
                    index = id[index] = ni;
                    islands--;
                }
            }
        }
        return islands != 1;
    }

    // DFS + Recursion
    // time complexity: O(M^2*N^2), space complexity: O(M*N)
    // 24 ms(88.89%), 39 MB(5.10%) for 95 tests
    public int minDays2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        if (disconnected(grid, m, n)) { return 0; }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    grid[i][j] = 0;
                    if (disconnected(grid, m, n)) { return 1; }
                    grid[i][j] = 1;
                }
            }
        }
        return 2;
    }

    private boolean disconnected(int[][] grid, int m, int n) {
        int islands = 0;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (!visited[i][j] && grid[i][j] == 1) {
                    islands++;
                    dfs(visited, grid, i, j, m, n);
                }
            }
        }
        return islands != 1;
    }

    private void dfs(boolean[][] visited, int[][] grid, int i, int j, int m, int n) {
        if (i >= 0 && j >= 0 && i < m && j < n && !visited[i][j] && grid[i][j] == 1) {
            visited[i][j] = true;
            dfs(visited, grid, i - 1, j, m, n);
            dfs(visited, grid, i + 1, j, m, n);
            dfs(visited, grid, i, j - 1, m, n);
            dfs(visited, grid, i, j + 1, m, n);
        }
    }

    private void test(int[][] grid, int expected) {
        assertEquals(expected, minDays(grid));
        //        assertEquals(expected, minDays2(grid));
    }

    @Test public void test() {
        test(new int[][] {{1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}}, 2);
        test(new int[][] {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, 2);
        test(new int[][] {{1, 1}}, 2);
        test(new int[][] {{1, 0, 1, 0}}, 0);
        test(new int[][] {{1, 1, 0, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 0, 1, 1}, {1, 1, 0, 1, 1}}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
