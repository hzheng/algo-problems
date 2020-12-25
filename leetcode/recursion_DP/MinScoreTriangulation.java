import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1039: https://leetcode.com/problems/minimum-score-triangulation-of-polygon/
//
// Given N, consider a convex N-sided polygon with vertices labelled A[0], A[i], ..., A[N-1] in
// clockwise order. Suppose you triangulate the polygon into N-2 triangles. For each triangle, the
// value of that triangle is the product of the labels of the vertices, and the total score of the
// triangulation is the sum of these values over all N-2 triangles in the triangulation. Return the
// smallest possible total score that you can achieve with some triangulation of the polygon.
//
// Note:
// 3 <= A.length <= 50
// 1 <= A[i] <= 100
public class MinScoreTriangulation {
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 7 ms(6.37%), 36.3 MB(92.83%) for 93 tests
    public int minScoreTriangulation(int[] A) {
        int n = A.length;
        int[][] dp = new int[n][n];
        int res = Integer.MAX_VALUE;
        for (int len = 2; len < n; len++) {
            for (int i = 0, j = (i + len) % n; i < n; i++, j = (j + 1) % n) {
                int score = Integer.MAX_VALUE;
                for (int k = (i + 1) % n, count = 1; count < len; count++, k = (k + 1) % n) {
                    score = Math.min(score, dp[i][k] + dp[k][j] + A[i] * A[j] * A[k]);
                }
                dp[i][j] = score;
                if (len == n - 1) {
                    res = Math.min(res, score);
                }
            }
        }
        return res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 3 ms(58.17%), 36.2 MB(92.83%) for 93 tests
    public int minScoreTriangulation2(int[] A) {
        int n = A.length;
        int[][] dp = new int[n][n];
        for (int len = 2; len < n; len++) {
            for (int i = 0, j = i + len; j < n; i++, j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i + 1; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + A[i] * A[j] * A[k]);
                }
            }
        }
        return dp[0][n - 1];
    }

    // Solution of Choice
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 3 ms(58.17%), 36.2 MB(92.83%) for 93 tests
    public int minScoreTriangulation3(int[] A) {
        int n = A.length;
        int[][] dp = new int[n][n];
        for (int j = 2; j < n; j++) {
            for (int i = j - 2; i >= 0; i--) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i + 1; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j] + A[i] * A[j] * A[k]);
                }
            }
        }
        return dp[0][n - 1];
    }

    private void test(int[] A, int expected) {
        assertEquals(expected, minScoreTriangulation(A));
        assertEquals(expected, minScoreTriangulation2(A));
        assertEquals(expected, minScoreTriangulation3(A));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3}, 6);
        test(new int[] {3, 7, 4, 5}, 144);
        test(new int[] {1, 3, 1, 4, 1, 5}, 13);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
