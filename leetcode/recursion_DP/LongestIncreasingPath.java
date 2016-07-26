import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
//
// Given an integer matrix, find the length of the longest increasing path.
// From each cell, you can either move to four directions: left, right, up or
// down. You may NOT move diagonally or move outside of the boundary .
public class LongestIncreasingPath {
    // DFS + Memoization
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 66.10%(16 ms)
    private static final int[][] shifts = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public int longestIncreasingPath(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int max = 0;
        int[][] memo = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                max = Math.max(max, maxPath(matrix, m, n, i, j, memo));
            }
        }
        return max;
    }

    private int maxPath(int[][] matrix, int m, int n, int i, int j, int[][] memo) {
        if (memo[i][j] > 0) return memo[i][j];

        int val = matrix[i][j];
        int max = 0;
        for (int[] shift : shifts) {
            int x = i + shift[0];
            int y = j + shift[1];
            if (x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] > val) {
                max = Math.max(max, maxPath(matrix, m, n, x, y, memo));
            }
        }
        return memo[i][j] = max + 1;
    }

    // Topological Sort
    // beats 2.95%(164ms) or 5.21%(95 ms)
    public int longestIncreasingPath2(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int len = 0;
        Queue<Long> removeList = new LinkedList<>();
        for (int count = m * n; count > 0; len++) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int val = matrix[i][j];
                    if (val == Integer.MIN_VALUE) continue;

                    boolean canDelete = true;
                    for (int[] shift : shifts) {
                        int x = i + shift[0];
                        if (x < 0 || x >= m) continue;

                        int y = j + shift[1];
                        if (y < 0 || y >= n) continue;

                        if (matrix[x][y] > val) {
                            canDelete = false;
                            break;
                        }
                    }
                    if (canDelete) {
                        removeList.add((((long)i) << 32) | j);
                    }
                }
            }

            while (!removeList.isEmpty()) {
                long pos = removeList.poll();
                matrix[(int)(pos >> 32)][(int)pos] = Integer.MIN_VALUE;
                count--;
            }
        }
        return len;
    }

    private void test(int[][] matrix, int expected) {
        assertEquals(expected, longestIncreasingPath(matrix));
        assertEquals(expected, longestIncreasingPath2(matrix));
    }

    @Test
    public void test1() {
        test(new int[][] {{1, 2}}, 2);
        test(new int[][] {{9, 9, 4},
                          {6, 6, 8},
                          {2, 2, 1}}, 4);
        test(new int[][] {{3, 4, 5},
                          {3, 2, 6},
                          {2, 2, 1}}, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestIncreasingPath");
    }
}
