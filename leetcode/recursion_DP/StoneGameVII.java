import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1690: https://leetcode.com/problems/stone-game-vii/
//
// Alice and Bob take turns playing a game, with Alice starting first. There are n stones arranged
// in a row. On each player's turn, they can remove either the leftmost stone or the rightmost stone
// from the row and receive points equal to the sum of the remaining stones' values in the row. The
// winner is the one with the higher score when there are no stones left to remove. Bob found that
// he will always lose this game, he always loses), so he tries to minimize the score's difference.
// Alice's goal is to maximize the difference in the score. Given an array of integers stones where
// stones[i] represents the value of the ith stone from the left, return the difference in Alice and
// Bob's score if they both play optimally.
//
// Constraints:
// n == stones.length
// 2 <= n <= 1000
// 1 <= stones[i] <= 1000
public class StoneGameVII {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 135 ms(17.14%), 71 MB(54.29%) for 68 tests
    public int stoneGameVII(int[] stones) {
        int n = stones.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }
        return dfs(sum, 0, n - 1, 0, new Integer[n][n]);
    }

    private int dfs(int[] sum, int start, int end, int turn, Integer[][] dp) {
        if (start == end) { return 0; }

        if (dp[start][end] != null) { return dp[start][end]; }

        int v1 = (sum[end + 1] - sum[start + 1]) * (turn == 0 ? 1 : -1) + dfs(sum, start + 1, end,
                                                                              1 - turn, dp);
        int v2 = (sum[end] - sum[start]) * (turn == 0 ? 1 : -1) + dfs(sum, start, end - 1, 1 - turn,
                                                                      dp);
        int res = (turn == 0) ? Math.max(v1, v2) : Math.min(v1, v2);
        return dp[start][end] = res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 170 ms(11.43%), 121.5 MB(5.71%) for 68 tests
    public int stoneGameVII2(int[] stones) {
        int n = stones.length;
        int sum = Arrays.stream(stones).sum();
        return dfs2(stones, 0, n - 1, sum, new Integer[n][n]);
    }

    private int dfs2(int[] stones, int start, int end, int sum, Integer[][] dp) {
        if (start == end) { return 0; }

        if (dp[start][end] != null) { return dp[start][end]; }

        int v1 = stones[start] + dfs2(stones, start + 1, end, sum - stones[start], dp);
        int v2 = stones[end] + dfs2(stones, start, end - 1, sum - stones[end], dp);
        return dp[start][end] = sum - Math.min(v1, v2);
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 37 ms(88.57%), 47.4 MB(62.86%) for 68 tests
    public int stoneGameVII3(int[] stones) {
        int n = stones.length;
        int[][] dp = new int[n][n + 1];
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }
        for (int dist = 1; dist < n; dist++) {
            for (int i = 0, j = dist; i < n - dist; i++, j++) {
                dp[i][dist + 1] = sum[j + 1] - sum[i] - Math
                        .min(stones[j] + dp[i][dist], stones[i] + dp[i + 1][dist]);
            }
        }
        return dp[0][n];
    }

    private void test(int[] stones, int expected) {
        assertEquals(expected, stoneGameVII(stones));
        assertEquals(expected, stoneGameVII2(stones));
        assertEquals(expected, stoneGameVII3(stones));
    }

    @Test public void test() {
        test(new int[] {5, 3, 1, 4, 2}, 6);
        test(new int[] {7, 90, 5, 1, 100, 10, 10, 2}, 122);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
