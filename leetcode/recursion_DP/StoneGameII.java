import org.junit.Test;

import static org.junit.Assert.*;

// LC1140: https://leetcode.com/problems/stone-game-ii/
//
// Alice and Bob continue their games with piles of stones. There are a number of piles arranged in
// a row, and each pile has a positive integer number of stones piles[i]. The objective of the game
// is to end with the most stones. Alice and Bob take turns, with Alice starting first. Initially,
// M = 1. On each player's turn, that player can take all the stones in the first X remaining piles,
// where 1 <= X <= 2M.  Then, we set M = max(M, X).
// The game continues until all the stones have been taken.
// Assuming Alice and Bob play optimally, return the maximum number of stones Alice can get.
//
// Constraints:
// 1 <= piles.length <= 100
// 1 <= piles[i] <= 10^4
public class StoneGameII {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 3 ms(78.60%), 38 MB(97.76%) for 92 tests
    public int stoneGameII(int[] piles) {
        int n = piles.length;
        int[] sum = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            sum[i] = sum[i + 1] + piles[i];
        }
        return dfs(sum, 0, 1, new int[n][n + 1]);
    }

    private int dfs(int[] sum, int cur, int max, int[][] dp) {
        int n = sum.length - 1;
        if (cur + 2 * max >= n) { return sum[cur]; }

        int cache = dp[cur][max];
        if (cache > 0) { return cache; }

        int res = 0;
        for (int i = cur, j = cur + max * 2, m = max; i < j; i++) {
            m = Math.max(m, i - cur + 1);
            int total = sum[cur] - dfs(sum, i + 1, m, dp);
            res = Math.max(res, total);
        }
        return dp[cur][max] = res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3), space complexity: O(N^2)
    // 20 ms(22.94%), 38.8 MB(35.24%) for 92 tests
    public int stoneGameII2(int[] piles) {
        int n = piles.length;
        int[] sum = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            sum[i] = sum[i + 1] + piles[i];
        }
        int[][] dp = new int[n + 1][n + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int max = n; max > 0; max--) {
                for (int j = 1, k = Math.min(2 * max, n - i); j <= k; j++) {
                    dp[i][max] = Math.max(dp[i][max], sum[i] - dp[i + j][Math.max(max, j)]);
                }
            }
        }
        return dp[0][1];
    }

    private void test(int[] piles, int expected) {
        assertEquals(expected, stoneGameII(piles));
        assertEquals(expected, stoneGameII2(piles));
    }

    @Test public void test() {
        test(new int[] {2, 7, 9, 4, 4}, 10);
        test(new int[] {1, 2, 3, 4, 5, 100}, 104);
        test(new int[] {3111, 4303, 2722, 2183, 6351, 5227, 8964, 7167, 9286, 6626, 2347, 1465,
                        5201, 7240, 5463, 8523, 8163, 9391, 8616, 5063, 7837, 7050, 1246, 9579,
                        7744, 6932, 7704, 9841, 6163, 4829, 7324, 6006, 4689, 8781, 621}, 112766);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
