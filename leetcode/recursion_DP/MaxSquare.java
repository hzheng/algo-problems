import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC221: https://leetcode.com/problems/maximal-square/
//
// Given a 2D binary matrix filled with 0's and 1's, find the largest square
// containing all 1's and return its area.
public class MaxSquare {
    // time complexity: O(M * N ^ 2), space complexity: O(N)
    // beats 83.29%(10 ms)
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int[] oneCounts = new int[n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m && matrix[i][j] == '1'; i++) {
                oneCounts[j]++;
            }
        }
        int max = maxSide(oneCounts);
        for (int i = 1; i < m; i++) {
            boolean hasZero = false; // skip all 1's when possible
            for (int j = 0; j < n; j++) {
                if (oneCounts[j] > 0) {
                    oneCounts[j]--;
                } else {
                    hasZero = true;
                    for (int k = i; k < m && matrix[k][j] == '1'; k++) {
                        oneCounts[j]++;
                    }
                }
            }
            if (hasZero) {
                max = Math.max(max, maxSide(oneCounts));
            }
        }
        return max * max;
    }

    private int maxSide(int[] heights) {
        int max = 0;
        int n = heights.length;
        int front = 0;
        int[] heights2 = heights.clone();
        for (int i = 1; i < n; i++) {
            if (i - front >= heights2[front]) {
                max = Math.max(max, heights2[front]);
                if (++front == n) return max;

                i--;
                continue;
            }

            int cur = heights[i];
            if (heights2[i - 1] > cur) {
                max = Math.max(max, i - front);
                for (int j = i - 1; j >= front; j--) {
                    if (heights2[j] > cur) {
                        heights2[j] = cur;
                    } else break;
                }
            }
        }
        return Math.max(max, Math.min(n - front, heights2[front]));
    }

    public void testMaxSide(int expected, int ... nums) {
        assertEquals(expected, maxSide(nums));
    }

    // 2D-Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 45.34%(12 ms for 68 tests)
    public int maximalSquare2(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        // largest square side that ends at (i, j)
        int[][] dp = new int[m + 1][n + 1];
        int maxSide = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (matrix[i - 1][j - 1] != '1') continue;

                dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1],
                                                               dp[i - 1][j - 1]));
                maxSide = Math.max(maxSide, dp[i][j]);
            }
        }
        return maxSide * maxSide;
    }

    // Solution of Choice
    // 1D-Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 45.34%(12 ms for 68 tests)
    public int maximalSquare3(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int[] dp = new int[n + 1];
        int maxSide = 0;
        for (int i = 1, prev = 0; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int tmp = dp[j];
                if (matrix[i - 1][j - 1] != '1') {
                    dp[j] = 0;
                } else {
                    dp[j] = 1 + Math.min(dp[j], Math.min(dp[j - 1], prev));
                    maxSide = Math.max(maxSide, dp[j]);
                }
                prev = tmp;
            }
        }
        return maxSide * maxSide;
    }

    // time complexity: O(M * N), space complexity: O(N)
    // beats 67.69%(13 ms)
    public int maximalSquare4(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int[][] sides = new int[2][n];
        int maxSide = 0;
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == '1') {
                sides[0][j] = 1;
                maxSide = 1;
            }
        }
        for (int i = 1; i < m; i++) {
            int k = i % 2;
            sides[k][0] = matrix[i][0] - '0';
            maxSide = Math.max(sides[k][0], maxSide);
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == '0') {
                    sides[k][j] = 0;
                } else {
                    sides[k][j] = Math.min(sides[1 - k][j],
                                           Math.min(sides[k][j - 1],
                                                    sides[1 - k][j - 1])) + 1;
                    maxSide = Math.max(sides[k][j], maxSide);
                }
            }
        }
        return maxSide * maxSide;
    }

    @Test
    public void testMaxSide() {
        testMaxSide(2, 4, 0, 3, 4, 0);
        testMaxSide(1, 4, 0, 3, 0, 0);
        testMaxSide(4, 0, 2, 3, 6, 4, 5, 4);
        testMaxSide(3, 0, 2, 3, 6, 4, 5);
        testMaxSide(2, 2, 3, 6, 1, 5);
        testMaxSide(2, 2, 3, 1, 1, 5);
        testMaxSide(3, 8, 5, 6, 1, 5, 2);
    }

    void test(int expected, String ... matrixStr) {
        char[][] matrix =  new char[matrixStr.length][];
        for (int i = 0; i < matrixStr.length; i++) {
            matrix[i] = matrixStr[i].toCharArray();
        }
        assertEquals(expected, maximalSquare(matrix));
        assertEquals(expected, maximalSquare2(matrix));
        assertEquals(expected, maximalSquare3(matrix));
        assertEquals(expected, maximalSquare4(matrix));
    }

    @Test
    public void test1() {
        test(1, "1");
        test(4, "10100", "10111", "11111", "10010");
        test(4, "10110", "10111", "11111", "10010");
        test(9, "10111", "10111", "11111", "10010");
        test(4, "101101","111111","011011","111010","011111","110111");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSquare");
    }
}
