import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1293: https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/
//
// Given a m * n grid, where each cell is either 0 (empty) or 1 (obstacle). In one step, you can
// move up, down, left or right from and to an empty cell.
// Return the minimum number of steps to walk from the upper left corner (0, 0) to the lower right
// corner (m-1, n-1) given that you can eliminate at most k obstacles. If it is not possible to find
// such walk return -1.
//
// Constraints:
// grid.length == m
// grid[0].length == n
// 1 <= m, n <= 40
// 1 <= k <= m*n
// grid[i][j] == 0 or 1
// grid[0][0] == grid[m-1][n-1] == 0
public class ShortestPath {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // BFS + Queue
    // time complexity: O(M*N*K), space complexity: O(M*N*K)
    // 46 ms(24.68%), 43.6 MB(26.56%) for 46 tests
    public int shortestPath(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {0, 0, k});
        boolean[][][] visited = new boolean[m][n][k + 1];
        for (int step = 0; !queue.isEmpty(); step++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] cur = queue.poll();
                int avail = cur[2];
                if (avail < 0) { continue; }

                int x = cur[0];
                int y = cur[1];
                if (x == m - 1 && y == n - 1) { return step; }

                if (visited[x][y][avail]) { continue; }

                visited[x][y][avail] = true;
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                        queue.offer(new int[] {nx, ny, avail - grid[nx][ny]});
                    }
                }
            }
        }
        return -1;
    }

    // BFS + Queue
    // time complexity: O(M*N*K), space complexity: O(M*N)
    // 17 ms(61.83%), 44.9 MB(23.91%) for 46 tests
    public int shortestPath2(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[3]);
        int[][] visited = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(visited[i], Integer.MAX_VALUE);
        }
        visited[0][0] = 0;
        for (int step = 0; !queue.isEmpty(); step++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] cur = queue.poll();
                int x = cur[0];
                int y = cur[1];
                if (x == m - 1 && y == n - 1) { return step; }

                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx < 0 || nx >= m || ny < 0 || ny >= n) { continue; }

                    int removedObstacles = cur[2] + grid[x][y];
                    if (removedObstacles < visited[nx][ny] && removedObstacles <= k) {
                        visited[nx][ny] = removedObstacles;
                        queue.offer(new int[] {nx, ny, removedObstacles});
                    }
                }
            }
        }
        return -1;
    }

    private void test(int[][] grid, int k, int expected) {
        assertEquals(expected, shortestPath(grid, k));
        assertEquals(expected, shortestPath2(grid, k));
    }

    @Test public void test() {
        test(new int[][] {{0, 0, 0}, {1, 1, 0}, {0, 0, 0}, {0, 1, 1}, {0, 0, 0}}, 1, 6);
        test(new int[][] {{0, 1, 1}, {1, 1, 1}, {1, 0, 0}}, 1, -1);
        test(new int[][] {{0, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {1, 0}, {0, 0}, {0, 1}, {0, 1},
                          {0, 1}, {0, 0}, {1, 0}, {1, 0}, {0, 0}}, 4, 14);
        test(new int[][] {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                          {0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
                          {0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                          {0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
                          {0, 1, 0, 1, 1, 1, 1, 0, 0, 0}, {0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
                          {0, 1, 1, 1, 1, 1, 1, 0, 1, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 1, 0}}, 1, 20);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
