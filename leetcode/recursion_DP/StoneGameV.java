import org.junit.Test;

import static org.junit.Assert.*;

// LC1563: https://leetcode.com/problems/stone-game-v/
//
// There are several stones arranged in a row, and each stone has an associated value which is an
// integer given in the array stoneValue. In each round of the game, Alice divides the row into two
// non-empty rows (i.e. left row and right row), then Bob calculates the value of each row which is
// the sum of the values of all the stones in this row. Bob throws away the row which has the
// maximum value, and Alice's score increases by the value of the remaining row. If the value of the
// two rows are equal, Bob lets Alice decide which row will be thrown away. The next round starts
// with the remaining row. The game ends when there is only one stone remaining. Alice's is initially zero.
// Return the maximum score that Alice can obtain.
// Constraints:
// 1 <= stoneValue.length <= 500
// 1 <= stoneValue[i] <= 10^6
public class StoneGameV {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 122 ms(93.56%), 40.3 MB(5.43%) for 131 tests
    public int stoneGameV(int[] stoneValue) {
        int n = stoneValue.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stoneValue[i];
        }
        return dfs(sum, new int[n + 1][n + 1], 1, n);
    }

    private int dfs(int[] sum, int[][] dp, int start, int end) {
        if (start >= end) { return 0; }

        if (dp[start][end] > 0) { return dp[start][end]; }

        int res = 0;
        for (int i = start; i < end; i++) {
            int left = sum[i] - sum[start - 1];
            int right = sum[end] - sum[i];
            if (left <= right) {
                res = Math.max(res, left + dfs(sum, dp, start, i));
            }
            if (left >= right) {
                res = Math.max(res, right + dfs(sum, dp, i + 1, end));
            }
        }
        return dp[start][end] = res;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 288 ms(34.81%), 40.4 MB(5.43%) for 131 tests
    public int stoneGameV2(int[] stoneValue) {
        int n = stoneValue.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stoneValue[i];
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int len = 2; len <= n; len++) {
            for (int start = 1, end = start + len - 1; end <= n; start++, end++) {
                int res = 0;
                for (int i = start; i < end; i++) {
                    int left = sum[i] - sum[start - 1];
                    int right = sum[end] - sum[i];
                    if (left <= right) {
                        res = Math.max(res, left + dp[start][i]);
                    }
                    if (left >= right) {
                        res = Math.max(res, right + dp[i + 1][end]);
                    }
                }
                dp[start][end] = res;
            }
        }
        return dp[1][n];
    }

    private void test(int[] stoneValue, int expected) {
        assertEquals(expected, stoneGameV(stoneValue));
        assertEquals(expected, stoneGameV2(stoneValue));
    }

    @Test public void test() {
        test(new int[] {6, 2, 3, 4, 5, 5}, 18);
        test(new int[] {7, 7, 7, 7, 7, 7, 7}, 28);
        test(new int[] {4}, 0);
        test(new int[] {44, 51, 96}, 139);
        test(new int[] {7, 7, 7}, 7);
        test(new int[] {6, 12, 2, 44, 51, 96}, 96);
        test(new int[] {98, 77, 24, 49, 6, 12, 2, 44, 51, 96}, 330);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
