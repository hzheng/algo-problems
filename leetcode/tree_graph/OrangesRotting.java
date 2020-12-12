import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.Utils;

// LC994: https://leetcode.com/problems/rotting-oranges/
//
// In a given grid, each cell can have one of three values:
// the value 0 representing an empty cell;
// the value 1 representing a fresh orange;
// the value 2 representing a rotten orange.
// Every minute, any fresh orange that is adjacent (4-directionally) to a rotten one becomes rotten.
// Return the minimum number of minutes that must elapse until no cell has a fresh orange.  If this
// is impossible, return -1 instead.
//
// Note:
// 1 <= grid.length <= 10
// 1 <= grid[0].length <= 10
// grid[i][j] is only 0, 1, or 2.
public class OrangesRotting {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // BFS + Queue
    // time complexity: O(N*M), space complexity: O(N)
    // 2 ms(96.26%), 38.8 MB(18.77%) for 55 tests
    public int orangesRotting(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        Queue<Integer> queue = new LinkedList<>();
        int totalFresh = 0;
        for (int i = 0; i < n; i++) {
            int[] row = grid[i];
            for (int j = 0; j < m; j++) {
                if (row[j] == 2) {
                    queue.offer(i * m + j);
                } else if (row[j] == 1) {
                    totalFresh++;
                }
            }
        }
        if (totalFresh == 0) { return 0; }

        for (int res = 1; !queue.isEmpty(); res++) {
            for (int i = queue.size(); i > 0; i--) {
                int cur = queue.poll();
                int x = cur / m;
                int y = cur % m;
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < n && ny >= 0 && ny < m && grid[nx][ny] == 1) {
                        queue.offer(nx * m + ny);
                        grid[nx][ny] = 2;
                        if (--totalFresh == 0) { return res; }
                    }
                }
            }
        }
        return -1;
    }

    // BFS
    // time complexity: O(N*M*(M+H)), space complexity: O(1)
    // 2 ms(96.26%), 38.2 MB(81.46%) for 55 tests
    public int orangesRotting2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int fresh = 0;
        for (int[] row : grid) {
            for (int j = 0; j < m; j++) {
                if (row[j] == 1) {
                    fresh++;
                }
            }
        }
        int minutes = 0;
        for (int preFresh = fresh; fresh > 0; minutes++, preFresh = fresh) {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < m; y++) {
                    if (grid[x][y] != minutes + 2) { continue; }

                    for (int[] move : MOVES) {
                        int nx = x + move[0];
                        int ny = y + move[1];
                        if (nx >= 0 && nx < n && ny >= 0 && ny < m && grid[nx][ny] == 1) {
                            grid[nx][ny] = minutes + 3;
                            if (--fresh == 0)
                                return minutes + 1;
                        }
                    }
                }
            }
            if (fresh == preFresh) { return -1; }
        }
        return minutes;
    }

    // DFS + Recursion
    // time complexity: O(N*M), space complexity: O(1)
    // 1 ms(100.00%), 38 MB(96.66%) for 55 tests
    public int orangesRotting3(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 2) {
                    rot(grid, i, j, 2);
                }
            }
        }
        int minutes = 2;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 1) { return -1; }

                minutes = Math.max(minutes, cell);
            }
        }
        return minutes - 2;
    }

    private void rot(int[][] grid, int x, int y, int minutes) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length || grid[x][y] == 0 || (
                grid[x][y] > 1 && grid[x][y] < minutes)) {
            return;
        }

        grid[x][y] = minutes;
        rot(grid, x - 1, y, minutes + 1);
        rot(grid, x + 1, y, minutes + 1);
        rot(grid, x, y - 1, minutes + 1);
        rot(grid, x, y + 1, minutes + 1);
    }

    private void test(int[][] grid, int expected) {
        assertEquals(expected, orangesRotting(Utils.clone(grid)));
        assertEquals(expected, orangesRotting2(Utils.clone(grid)));
        assertEquals(expected, orangesRotting3(Utils.clone(grid)));
    }

    @Test public void test() {
        test(new int[][] {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}, 4);
        test(new int[][] {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}, -1);
        test(new int[][] {{0, 2}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
