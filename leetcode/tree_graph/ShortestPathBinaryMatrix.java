import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1091: https://leetcode.com/problems/shortest-path-in-binary-matrix/
//
// In an N by N square grid, each cell is either empty (0) or blocked (1).
// A clear path from top-left to bottom-right has length k if and only if it is composed of cells
// C_1, C_2, ..., C_k such that:
// Adjacent cells C_i and C_{i+1} are connected 8-directionally (ie., they are different and share
// an edge or corner)
// C_1 is at location (0, 0) (ie. has value grid[0][0])
// C_k is at location (N-1, N-1) (ie. has value grid[N-1][N-1])
// If C_i is located at (r, c), then grid[r][c] is empty (ie. grid[r][c] == 0).
// Return the length of the shortest such clear path from top-left to bottom-right. If such a path
// does not exist, return -1.
//
// Note:
// 1 <= grid.length == grid[0].length <= 100
// grid[r][c] is 0 or 1
public class ShortestPathBinaryMatrix {
    private static final int[][] MOVES =
            {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    // BFS + Queue
    // time complexity: O(N^2), space complexity: O(N^2)
    // 15 ms(73.12%), 40.1 MB(75.63%) for 84 tests
    public int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0] != 0) { return -1; }

        int n = grid.length;
        boolean[][] visited = new boolean[n][n];
        visited[0][0] = true;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[2]);
        for (int res = 1; !queue.isEmpty(); res++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] cur = queue.poll();
                int x = cur[0];
                int y = cur[1];
                if (x == n - 1 && y == n - 1) { return res; }

                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < n && ny >= 0 && ny < n && !visited[nx][ny]
                        && grid[nx][ny] == 0) {
                        visited[nx][ny] = true;
                        queue.offer(new int[] {nx, ny});
                    }
                }
            }
        }
        return -1;
    }

    // TODO: A* Search

    private void test(int[][] grid, int expected) {
        assertEquals(expected, shortestPathBinaryMatrix(grid));
    }

    @Test public void test() {
        test(new int[][] {{0, 1}, {1, 0}}, 2);
        test(new int[][] {{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}, 4);
        test(new int[][] {{0, 0, 0}, {0, 1, 1}, {0, 1, 0}}, -1);
        test(new int[][] {{0, 1, 0, 0, 0}, {0, 1, 0, 1, 0}, {0, 1, 0, 1, 0}, {0, 1, 0, 1, 0},
                          {0, 0, 0, 1, 0}}, 13);
        test(new int[][] {{0, 1, 0, 1, 0}, {1, 0, 0, 0, 1}, {0, 0, 1, 1, 1}, {0, 0, 0, 0, 0},
                          {1, 0, 1, 0, 0}}, 6);
        test(new int[][] {{0, 0, 0, 1, 0, 0, 1, 0}, {0, 0, 0, 0, 0, 0, 0, 0},
                          {1, 0, 0, 1, 1, 0, 1, 0}, {0, 1, 1, 1, 0, 0, 0, 0},
                          {0, 0, 0, 0, 0, 1, 1, 1}, {1, 0, 1, 0, 0, 0, 0, 0},
                          {1, 1, 0, 0, 0, 1, 0, 0}, {0, 0, 0, 0, 0, 1, 0, 0}}, 11);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
