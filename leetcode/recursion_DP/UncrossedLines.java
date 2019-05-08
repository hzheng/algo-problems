import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1035: https://leetcode.com/problems/uncrossed-lines/
//
// We write the integers of A and B (in the order they are given) on two separate horizontal lines.
// Now, we may draw a straight line connecting two numbers A[i] and B[j] as long as A[i] == B[j],
// and the line we draw does not intersect any other connecting (non-horizontal) line.
// Return the maximum number of connecting lines we can draw in this way.
public class UncrossedLines {
    // Dynamic Programming
    // LCS problem
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // 4 ms(96.81%), 35.4 MB(100%) for 74 tests
    public int maxUncrossedLines(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (A[i - 1] == B[j - 1]) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        return dp[m][n];
    }

    void test(int[] A, int[] B, int expected) {
        assertEquals(expected, maxUncrossedLines(A, B));
    }

    @Test
    public void test() {
        test(new int[]{1, 4, 2}, new int[]{1, 2, 4}, 2);
        test(new int[]{2, 5, 1, 2, 5}, new int[]{10, 5, 2, 1, 5, 2}, 3);
        test(new int[]{1, 3, 7, 1, 7, 5}, new int[]{1, 9, 2, 5, 1}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
