import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1000: https://leetcode.com/problems/minimum-cost-to-merge-stones/
//
// There are N piles of stones arranged in a row. The i-th pile has stones[i] stones.
// A move consists of merging exactly K consecutive piles into one pile, and the cost of this move
// is equal to the total number of stones in these K piles.
// Find the minimum cost to merge all piles of stones into one pile. If it is impossible, return -1.
//
// Note:
// 1 <= stones.length <= 30
// 2 <= K <= 30
// 1 <= stones[i] <= 100
public class MergeStones {
    // 3-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3), space complexity: O(N^2*K)
    // 10 ms(5.68%), 39.4 MB(5.97%) for 83 tests
    public int mergeStones(int[] stones, int K) {
        int n = stones.length;
        if ((n - 1) % (K - 1) != 0) { return -1; }

        final int max = Integer.MAX_VALUE / 3;
        int[] sum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sum[i] = sum[i - 1] + stones[i - 1];
        }
        int[][][] dp = new int[n][n][K + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dp[i][j], max);
            }
        }
        for (int i = 0; i < n; i++) {
            dp[i][i][1] = 0;
        }
        for (int len = 1; len < n; len++) {
            for (int i = 0, j = i + len; j < n; i++, j++) {
                for (int p = K; p > 1; p--) { // piles
                    for (int k = j; k > i; k -= (K - 1)) { // or: for (int k = i + 1; k <= j; k++)
                        dp[i][j][p] = Math.min(dp[i][j][p], dp[i][k - 1][p - 1] + dp[k][j][1]);
                    }
                }
                dp[i][j][1] = dp[i][j][K] + sum[j + 1] - sum[i];
            }
        }
        return dp[0][n - 1][1];
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^3/K), space complexity: O(N^2)
    // 4 ms(40.34%), 38.4 MB(27.84%) for 83 tests
    public int mergeStones2(int[] stones, int K) {
        int n = stones.length;
        if ((n - 1) % (K - 1) != 0) { return -1; }

        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }
        int[][] dp = new int[n][n]; // min cost to merge from i to j as small as possible
        for (int len = K - 1; len < n; len++) {
            for (int i = 0, j = i + len; j < n; i++, j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k += K - 1) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j]);
                }
                if ((j - i) % (K - 1) == 0) {
                    dp[i][j] += sum[j + 1] - sum[i];
                }
            }
        }
        return dp[0][n - 1];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^3/K), space complexity: O(N^2)
    // 3 ms(48.01%), 38.2 MB(46.02%) for 83 tests
    public int mergeStones3(int[] stones, int K) {
        int n = stones.length;
        if ((n - 1) % (K - 1) != 0) { return -1; }

        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }
        return dfs(0, n - 1, 1, K, sum, new int[n][n][K + 1]);
    }

    private int dfs(int start, int end, int piles, int K, int[] sum, int[][][] dp) {
        if (start == end) { return 0; }

        if (dp[start][end][piles] != 0) { return dp[start][end][piles]; }

        if (piles == 1) {
            return dp[start][end][piles] =
                    dfs(start, end, K, K, sum, dp) + sum[end + 1] - sum[start];
        }

        int res = Integer.MAX_VALUE;
        for (int mid = start; mid < end; mid += (K - 1)) {
            int left = dfs(start, mid, 1, K, sum, dp);
            if (left >= res) { continue; }

            int right = dfs(mid + 1, end, piles - 1, K, sum, dp);
            if (right < res) {
                res = Math.min(res, left + right);
            }
        }
        return dp[start][end][piles] = res;
    }

    private void test(int[] stones, int K, int expected) {
        assertEquals(expected, mergeStones(stones, K));
        assertEquals(expected, mergeStones2(stones, K));
        assertEquals(expected, mergeStones3(stones, K));
    }

    @Test public void test() {
        test(new int[] {3, 2, 4, 1}, 2, 20);
        test(new int[] {3, 2, 4, 1}, 3, -1);
        test(new int[] {3, 5, 1, 2, 6}, 3, 25);
        test(new int[] {3, 5, 1, 2, 6, 8, 7, 13, 9, 4}, 4, 101);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
