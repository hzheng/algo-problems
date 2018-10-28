import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC931: https://leetcode.com/problems/minimum-falling-path-sum/
//
// Given a square array of integers A, we want the minimum sum of a falling path
// through A. A falling path starts at any element in the first row, and chooses 
// one element from each row.  The next row's choice must be in a column that is
// different from the previous row's column by at most one.
public class MinFallingPathSum {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats %(7 ms for 46 tests)
    public int minFallingPathSum(int[][] A) {
        int n = A.length;
        int[][] dp = new int[n + 1][n];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j > 0) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
                }
                if (j < n - 1) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j + 1]);
                }
                dp[i][j] += A[i - 1][j];
            }
        }
        int res = Integer.MAX_VALUE;
        for (int x : dp[n]) {
            res = Math.min(res, x);
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(9 ms for 46 tests)
    public int minFallingPathSum2(int[][] A) {
        int n = A.length;
        int[] dp1 = new int[n];
        int[] dp2 = new int[n];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                dp2[j] = dp1[j];
                if (j > 0) {
                    dp2[j] = Math.min(dp2[j], dp1[j - 1]);
                }
                if (j < n - 1) {
                    dp2[j] = Math.min(dp2[j], dp1[j + 1]);
                }
                dp2[j] += A[i - 1][j];
            }
            int[] tmp = dp1;
            dp1 = dp2;
            dp2 = tmp;
        }
        int res = Integer.MAX_VALUE;
        for (int x : dp1) {
            res = Math.min(res, x);
        }
        return res;
    }

    void test(int[][] A, int expected) {
        assertEquals(expected, minFallingPathSum(A));
        assertEquals(expected, minFallingPathSum2(A));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 12);
        test(new int[][] {{1, -2, 3}, {-4, 5, 6}, {7, 8, -9}}, -6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
