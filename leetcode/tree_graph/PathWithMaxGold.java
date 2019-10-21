import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1219: https://leetcode.com/problems/path-with-maximum-gold/
//
// In a gold mine grid, each cell in this mine has an integer representing the amount of gold in
// that cell, 0 if it is empty. Return the maximum amount of gold you can collect under the conditions:
// Every time you are located in a cell you will collect all the gold in that cell.
// From your position you can walk one step to the left, right, up or down.
// You can't visit the same cell more than once.
// Never visit a cell with 0 gold.
// You can start and stop collecting gold from any position in the grid that has some gold.
// Constraints:
// 1 <= grid.length, grid[i].length <= 15
// 0 <= grid[i][j] <= 100
// There are at most 25 cells containing gold.
public class PathWithMaxGold {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // Recursion + DFS + Backtracking
    // time complexity: O(4 ^ K + M * N), space complexity: O(M * N)
    // 31 ms(17.86%), 48.3 MB(100%) for 42 tests
    public int getMaximumGold(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int res = 0;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != 0) {
                    res = Math.max(res, dfs(grid, i, j, visited));
                }
            }
        }
        return res;
    }

    private int dfs(int[][] grid, int row, int col, boolean[][] visited) {
        int m = grid.length;
        int n = grid[0].length;
        visited[row][col] = true;
        int max = 0;
        for (int[] move : MOVES) {
            int x = row + move[0];
            int y = col + move[1];
            if (x >= 0 && x < m && y >= 0 && y < n && !visited[x][y] && grid[x][y] != 0) {
                max = Math.max(max, dfs(grid, x, y, visited));
            }
        }
        visited[row][col] = false;
        return grid[row][col] + max;
    }

    // BFS + Queue
    // time complexity: O(4 ^ K + M * N), space complexity: O(M * N)
    // 31 ms(17.86%), 48.3 MB(100%) for 42 tests
    public int getMaximumGold2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] visited = new int[m][n];
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0, cell = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0) {
                    visited[i][j] = 1 << cell++;
                    queue.offer(new int[]{i, j, grid[i][j], visited[i][j]});
                }
            }
        }
        int res = 0;
        while (!queue.isEmpty()) {
            int row = queue.peek()[0];
            int col = queue.peek()[1];
            int sum = queue.peek()[2];
            int mask = queue.poll()[3];
            res = Math.max(sum, res);
            for (int[] move : MOVES) {
                int x = row + move[0];
                int y = col + move[1];
                if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] > 0
                    && (mask & visited[x][y]) == 0) {
                    queue.offer(new int[]{x, y, sum + grid[x][y], mask | visited[x][y]});
                }
            }
        }
        return res;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, getMaximumGold(grid));
        assertEquals(expected, getMaximumGold2(grid));
    }

    @Test
    public void test() {
        test(new int[][]{{0, 6, 0}, {5, 8, 7}, {0, 9, 0}}, 24);
        test(new int[][]{{1, 0, 7}, {2, 0, 6}, {3, 4, 5}, {0, 3, 0}, {9, 0, 20}}, 28);
        test(new int[][]{{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15},
                         {16, 17, 18, 19, 20}, {21, 22, 23, 24, 25}}, 325);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
