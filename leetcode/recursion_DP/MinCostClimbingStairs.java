import org.junit.Test;

import static org.junit.Assert.*;

// LC746: https://leetcode.com/problems/min-cost-climbing-stairs/
//
// On a staircase, the i-th step has some non-negative cost cost[i] assigned (0 indexed).
// Once you pay the cost, you can either climb one or two steps. You need to find minimum cost to
// reach the top of the floor, and you can either start from the step with index 0, or the step
// with index 1.
// Note:
// cost will have a length in the range [2, 1000].
// Every cost[i] will be an integer in the range [0, 999].
public class MinCostClimbingStairs {
    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(75.95%), 40.5 MB(8.62%) for 276 tests
    public int minCostClimbingStairs(int[] cost) {
        int n = cost.length;
        int[] dp = new int[n + 2];
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = cost[i] + Math.min(dp[i + 1], dp[i + 2]);
        }
        return Math.min(dp[0], dp[1]);
    }

    // 0-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(75.95%), 40.1 MB(8.62%) for 276 tests
    public int minCostClimbingStairs2(int[] cost) {
        int a = 0;
        int b = 0;
        for (int i = cost.length - 1; i >= 0; i--) {
            int c = cost[i] + Math.min(a, b);
            a = b;
            b = c;
        }
        return Math.min(a, b);
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(75.95%), 40.1 MB(8.62%) for 276 tests
    public int minCostClimbingStairs3(int[] cost) {
        int n = cost.length;
        int[] dp = new int[n];
        return Math.min(dfs(cost, n - 1, dp), dfs(cost, n - 2, dp));
    }

    private int dfs(int[] cost, int cur, int[] dp) {
        if (cur < 0) { return 0; }
        if (cur == 0 || cur == 1) { return cost[cur]; }
        if (dp[cur] != 0) { return dp[cur];}

        return dp[cur] = cost[cur] + Math.min(dfs(cost, cur - 1, dp), dfs(cost, cur - 2, dp));
    }

    private void test(int[] cost, int expected) {
        assertEquals(expected, minCostClimbingStairs(cost));
        assertEquals(expected, minCostClimbingStairs2(cost));
        assertEquals(expected, minCostClimbingStairs3(cost));
    }

    @Test public void test() {
        test(new int[] {10, 15, 20}, 15);
        test(new int[] {1, 100, 1, 1, 1, 100, 1, 1, 100, 1}, 6);
        test(new int[] {2, 9, 13, 7, 31, 52, 1, 52, 100, 1, 1, 1, 37, 100, 1, 1, 100, 1}, 141);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
