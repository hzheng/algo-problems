import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1289: https://leetcode.com/problems/minimum-falling-path-sum-ii/
//
// Given a square grid of integers arr, a falling path with non-zero shifts is a choice of exactly
// one element from each row of arr, such that no two elements chosen in adjacent rows are in the
// same column.
// Return the minimum sum of a falling path with non-zero shifts.
//
// Constraints:
// 1 <= arr.length == arr[i].length <= 200
// -99 <= arr[i][j] <= 99
public class MinFallingPathSumII {
    // 2D-Dynamic Programming
    // time complexity: O(R*C^2), space complexity: O(R*C)
    // 64 ms(28.25%), 45.8 MB(55.63%) for 13 tests
    public int minFallingPathSum(int[][] arr) {
        int row = arr.length;
        int col = arr[0].length;
        int[][] dp = new int[row + 1][col];
        for (int r = row - 1; r >= 0; r--) {
            Arrays.fill(dp[r], Integer.MAX_VALUE);
            for (int c = 0; c < col; c++) {
                for (int c2 = 0; c2 < col; c2++) {
                    if (c != c2) {
                        dp[r][c] = Math.min(dp[r][c], arr[r][c] + dp[r + 1][c2]);
                    }
                }
            }
        }
        return Arrays.stream(dp[0]).min().getAsInt();
    }

    // 1D-Dynamic Programming
    // time complexity: O(R*C), space complexity: O(C)
    // 2 ms(96.25%), 46.4 MB(28.03%) for 13 tests
    public int minFallingPathSum2(int[][] arr) {
        int row = arr.length;
        int col = arr[0].length;
        int[] dp = new int[col];
        for (int r = 0; r < row; r++) {
            int min1 = Integer.MAX_VALUE;
            int min2 = Integer.MAX_VALUE;
            for (int c = 0; c < col; c++) {
                if (dp[c] <= min1) {
                    min2 = min1;
                    min1 = dp[c];
                } else if (dp[c] < min2) {
                    min2 = dp[c];
                }
            }
            for (int c = 0; c < col; c++) {
                dp[c] = arr[r][c] + (dp[c] == min1 ? min2 : min1);
            }
        }
        return Arrays.stream(dp).min().getAsInt();
    }

    // Solution of Choice
    // 0D-Dynamic Programming
    // time complexity: O(R*C), space complexity: O(1)
    // 1 ms(100.00%), 45.9 MB(48.56%) for 13 tests
    public int minFallingPathSum3(int[][] arr) {
        int col = arr[0].length;
        int min1 = 0;
        int min2 = 0;
        int minPos = -1;
        for (int[] a : arr) {
            int nextMin1 = Integer.MAX_VALUE;
            int nextMin2 = Integer.MAX_VALUE;
            int nextMinPos = -1;
            for (int c = 0; c < col; c++) {
                int cur = a[c] + (c == minPos ? min2 : min1);
                if (cur < nextMin1) {
                    nextMin2 = nextMin1;
                    nextMin1 = cur;
                    nextMinPos = c;
                } else if (cur < nextMin2) {
                    nextMin2 = cur;
                }
            }
            min1 = nextMin1;
            min2 = nextMin2;
            minPos = nextMinPos;
        }
        return min1;
    }

    private void test(int[][] arr, int expected) {
        assertEquals(expected, minFallingPathSum(arr));
        assertEquals(expected, minFallingPathSum2(arr));
        assertEquals(expected, minFallingPathSum3(arr));
    }

    @Test public void test() {
        test(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 13);
        test(new int[][] {{1, 2, 3, 4}, {4, 5, 6, 3}, {7, 8, 9, 9}, {4, 6, 5, 7}}, 16);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
