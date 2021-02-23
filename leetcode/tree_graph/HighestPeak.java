import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1765: https://leetcode.com/problems/map-of-highest-peak/
//
// You are given matrix isWater of size m x n that represents a map of land and water cells.
// If isWater[i][j] == 0, cell (i, j) is a land cell.
// If isWater[i][j] == 1, cell (i, j) is a water cell.
// You must assign each cell a height in a way that follows these rules:
// The height of each cell must be non-negative.
// If the cell is a water cell, its height must be 0.
// Any two adjacent cells must have an absolute height difference of at most 1. A cell is adjacent
// to another cell if the former is directly north, east, south, or west of the latter (i.e., their
// sides are touching).
// Find an assignment of heights such that the maximum height in the matrix is maximized.
// Return an integer matrix height of size m x n where height[i][j] is cell (i, j)'s height. If
// there are multiple solutions, return any of them.
//
// Constraints:
// m == isWater.length
// n == isWater[i].length
// 1 <= m, n <= 1000
// isWater[i][j] is 0 or 1.
// There is at least one water cell.
public class HighestPeak {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // BFS + Queue
    // time complexity: O(M*N), space complexity: O(M*N)
    // 139 ms(16.67%), 160.8 MB(83.33%) for 59 tests
    public int[][] highestPeak(int[][] isWater) {
        int m = isWater.length;
        int n = isWater[0].length;
        int[][] res = new int[m][n];
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = isWater[i][j] - 1;
                if (isWater[i][j] == 1) {
                    queue.offer(new int[] {i, j});
                }
            }
        }
        for (int level = 1; !queue.isEmpty(); level++) {
            for (int size = queue.size(); size > 0; size--) {
                int[] cur = queue.poll();
                for (int[] move : MOVES) {
                    int nx = cur[0] + move[0];
                    int ny = cur[1] + move[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && res[nx][ny] < 0) {
                        res[nx][ny] = level;
                        queue.offer(new int[] {nx, ny});
                    }
                }
            }
        }
        return res;
    }

    private void test(int[][] isWater, int[][] expected) {
        assertArrayEquals(expected, highestPeak(isWater));
    }

    @Test public void test() {
        test(new int[][] {{0, 1}, {0, 0}}, new int[][] {{1, 0}, {2, 1}});
        test(new int[][] {{0, 0, 1}, {1, 0, 0}, {0, 0, 0}},
             new int[][] {{1, 1, 0}, {0, 1, 1}, {1, 2, 2}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
