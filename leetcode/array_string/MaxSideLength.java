import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1292: https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/
//
// Given a m x n matrix mat and an integer threshold. Return the maximum side-length of a square
// with a sum less than or equal to threshold or return 0 if there is no such square.
//
// Constraints:
// 1 <= m, n <= 300
// m == mat.length
// n == mat[i].length
// 0 <= mat[i][j] <= 10000
// 0 <= threshold <= 10^5
public class MaxSideLength {
    // 2D-Dynamic Programming
    // time complexity: O(M*N*MAX(M,N)), space complexity: O(M*N)
    // 51 ms(32.49%), 46.5 MB(80.87%) for 23 tests
    public int maxSideLength(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] sum = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + mat[i][j];
            }
        }
        for (int d = Math.min(m, n); ; d--) {
            for (int i = 0; i <= m - d; i++) {
                for (int j = 0; j <= n - d; j++) {
                    if (threshold
                        >= sum[i + d][j + d] - sum[i + d][j] - sum[i][j + d] + sum[i][j]) {
                        return d;
                    }
                }
            }
        }
    }

    // 2D-Dynamic Programming + Binary Search
    // time complexity: O(M*N*log(MAX(M,N))), space complexity: O(M*N)
    // 6 ms(83.75%), 46.5 MB(80.87%) for 23 tests
    public int maxSideLength2(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] sum = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + mat[i][j];
            }
        }
        int low = 0;
        outer:
        for (int high = Math.min(m, n); low < high; ) {
            int mid = (low + high + 1) >>> 1;
            for (int i = 0; i <= m - mid; i++) {
                for (int j = 0; j <= n - mid; j++) {
                    if (threshold
                        >= sum[i + mid][j + mid] - sum[i + mid][j] - sum[i][j + mid] + sum[i][j]) {
                        low = mid;
                        continue outer;
                    }
                }
            }
            high = mid - 1;
        }
        return low;
    }

    // 2D-Dynamic Programming + Sliding Window
    // time complexity: O(M*N), space complexity: O(M*N)
    // 6 ms(83.75%), 46.5 MB(80.87%) for 23 tests
    public int maxSideLength3(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] sum = new int[m + 1][n + 1];
        int res = 0;
        int side = 1;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                sum[i][j] = sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1] + mat[i - 1][j - 1];
                if (i >= side && j >= side
                    && sum[i][j] - sum[i - side][j] - sum[i][j - side] + sum[i - side][j - side]
                       <= threshold) {
                    res = side++;
                }
            }
        }
        return res;
    }

    private void test(int[][] mat, int threshold, int expected) {
        assertEquals(expected, maxSideLength(mat, threshold));
        assertEquals(expected, maxSideLength2(mat, threshold));
        assertEquals(expected, maxSideLength3(mat, threshold));
    }

    @Test public void test() {
        test(new int[][] {{1, 1, 3, 2, 4, 3, 2}, {1, 1, 3, 2, 4, 3, 2}, {1, 1, 3, 2, 4, 3, 2}}, 4,
             2);
        test(new int[][] {{2, 2, 2, 2, 2}, {2, 2, 2, 2, 2}, {2, 2, 2, 2, 2}, {2, 2, 2, 2, 2},
                          {2, 2, 2, 2, 2}}, 1, 0);
        test(new int[][] {{1, 1, 1, 1}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}}, 6, 3);
        test(new int[][] {{18, 70}, {61, 1}, {25, 85}, {14, 40}, {11, 96}, {97, 96}, {63, 45}},
             40184, 2);
        test(new int[][] {{1, 1, 3, 2, 4, 3, 2}, {1, 1, 3, 2, 4, 3, 2}, {1, 1, 3, 2, 4, 3, 2}}, 1,
             1);
        test(new int[][] {{2, 5, 3, 2, 4, 3, 2}, {2, 7, 3, 2, 4, 3, 2}, {7, 3, 3, 2, 4, 3, 2}}, 1,
             0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
