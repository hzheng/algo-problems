import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC695: https://leetcode.com/problems/max-area-of-island/description/
//
// Given a non-empty 2D array grid of 0's and 1's, an island is a group of 1's
// (representing land) connected 4-directionally (horizontal or vertical.) You
// may assume all four edges of the grid are surrounded by water.
// Find the maximum area of an island in the given 2D array.
public class MaxAreaOfIsland {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // DFS + Recursion
    // time complexity: O(N * M), space complexity: O(N * M)
    // beats 73.78%(38 ms for 726 tests)
    public int maxAreaOfIsland(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        boolean[][] visited = new boolean[n][m];
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 0 || visited[i][j]) continue;

                max = Math.max(max, area(grid, n, m, i, j, visited));
            }
        }
        return max;
    }

    private int area(int[][] grid, int nRow, int nCol, int x, int y,
                     boolean[][] visited) {
        int res = 1;
        visited[x][y] = true;
        for (int[] dir : DIRS) {
            int i = x + dir[0];
            int j = y + dir[1];
            if (i >= 0 && j >= 0 && i < nRow && j < nCol
                && grid[i][j] == 1 && !visited[i][j]) {
                res += area(grid, nRow, nCol, i, j, visited);
            }
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N * M), space complexity: O(N * M)
    // beats 39.38%(43 ms for 726 tests)
    public int maxAreaOfIsland2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        boolean[][] visited = new boolean[n][m];
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                max = Math.max(max, area2(grid, n, m, i, j, visited));
            }
        }
        return max;
    }

    private int area2(int[][] grid, int nRow, int nCol, int x, int y,
                      boolean[][] visited) {
        if (x < 0 || x >= nRow || y < 0 || y >= nCol || visited[x][y]
            || grid[x][y] == 0) {
            return 0;
        }
        visited[x][y] = true;
        return 1 + area2(grid, nRow, nCol, x + 1, y, visited)
               + area2(grid, nRow, nCol, x - 1, y, visited)
               + area2(grid, nRow, nCol, x, y - 1, visited)
               + area2(grid, nRow, nCol, x, y + 1, visited);
    }

    // DFS + Stack
    // time complexity: O(N * M), space complexity: O(N * M)
    // beats 39.38%(43 ms for 726 tests)
    public int maxAreaOfIsland3(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int max = 0;
        boolean[][] visited = new boolean[n][m];
        ArrayDeque<int[]> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 0 || visited[i][j]) continue;

                visited[i][j] = true;
                stack.push(new int[] {i, j});
                int count = 0;
                for (; !stack.isEmpty(); count++) {
                    int[] pos = stack.pop();
                    for (int[] dir : DIRS) {
                        int x = pos[0] + dir[0];
                        int y = pos[1] + dir[1];
                        if (x >= 0 && y >= 0 && x < n && y < m
                            && grid[x][y] == 1 && !visited[x][y]) {
                            stack.push(new int[] {x, y});
                            visited[x][y] = true;
                        }
                    }
                }
                max = Math.max(max, count);
            }
        }
        return max;
    }

    // BFS + Queue
    // time complexity: O(N * M), space complexity: O(N * M)
    // beats 27.73%(45 ms for 726 tests)
    public int maxAreaOfIsland4(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int max = 0;
        boolean[][] visited = new boolean[n][m];
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 0 || visited[i][j]) continue;

                visited[i][j] = true;
                queue.offer(new int[] {i, j});
                int count = 0;
                for (; !queue.isEmpty(); count++) {
                    int[] pos = queue.poll();
                    for (int[] dir : DIRS) {
                        int x = pos[0] + dir[0];
                        int y = pos[1] + dir[1];
                        if (x >= 0 && y >= 0 && x < n && y < m
                            && grid[x][y] == 1 && !visited[x][y]) {
                            queue.offer(new int[] {x, y});
                            visited[x][y] = true;
                        }
                    }
                }
                max = Math.max(max, count);
            }
        }
        return max;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, maxAreaOfIsland(grid));
        assertEquals(expected, maxAreaOfIsland2(grid));
        assertEquals(expected, maxAreaOfIsland3(grid));
        assertEquals(expected, maxAreaOfIsland4(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                          {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                          {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
                          {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                          {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}}, 6);
        test(new int[][] {{1, 1, 0, 0, 0}, {1, 1, 0, 0, 0}, {0, 0, 0, 1, 1},
                          {0, 0, 0, 1, 1}}, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
