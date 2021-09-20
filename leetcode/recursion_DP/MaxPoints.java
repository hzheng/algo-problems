import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC1937: https://leetcode.com/problems/maximum-number-of-points-with-cost/
//
// You are given an m x n integer matrix points (0-indexed). Starting with 0 points, you want to
// maximize the number of points you can get from the matrix.
// To gain points, you must pick one cell in each row. Picking the cell at coordinates (r, c) will
// add points[r][c] to your score.
// However, you will lose points if you pick a cell too far from the cell that you picked in the
// previous row. For every two adjacent rows r and r + 1 (where 0 <= r < m - 1), picking cells at
// coordinates (r, c1) and (r + 1, c2) will subtract abs(c1 - c2) from your score.
// Return the maximum number of points you can achieve.
//
// Constraints:
// m == points.length
// n == points[r].length
// 1 <= m, n <= 10^5
// 1 <= m * n <= 10^5
// 0 <= points[r][c] <= 10^5
public class MaxPoints {
    // Dynamic Programming
    // time complexity: O(M*N), space complexity: O(N)
    // 11 ms(85.71%), 81.5 MB(14.29%) for 157 tests
    public long maxPoints(int[][] points) {
        int n = points[0].length;
        long[] dp = new long[n];
        for (int[] point : points) {
            for (int i = 0; i < n; i++) {
                dp[i] += point[i];
            }
            for (int i = 1; i < n; i++) {
                dp[i] = Math.max(dp[i], dp[i - 1] - 1);
            }
            for (int i = n - 2; i >= 0; i--) {
                dp[i] = Math.max(dp[i], dp[i + 1] - 1);
            }
        }
        return Arrays.stream(dp).max().getAsLong();
    }

    void test(int[][] points, int expected) {
        assertEquals(expected, maxPoints(points));
    }

    @Test public void test() {
        test(new int[][] {{1, 2, 3}, {1, 5, 1}, {3, 1, 1}}, 9);
        test(new int[][] {{1, 5}, {2, 3}, {4, 2}}, 11);
        test(new int[][] {{1, 5}, {3, 2}, {4, 2}}, 11);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
