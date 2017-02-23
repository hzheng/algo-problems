import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC256: https://leetcode.com/problems/paint-house/
//
// There are a row of n houses, each house can be painted with one of the three
// colors: red, blue or green. The cost of painting each house with a certain
// color is different. You have to paint all the houses such that no two adjacent
// houses have the same color. The cost of painting each house with a certain
// color is represented by a n x 3 cost matrix.
// Find the minimum cost to paint all houses.
// All costs are positive integers.
public class PaintHouse {
    // DFS + Recursion
    // Time Limit Exceeded
    public int minCost(int[][] costs) {
        int[] min = new int[]{Integer.MAX_VALUE};
        dfs(costs, 0, -1, 0, min);
        return min[0];
    }

    private void dfs(int[][] costs, int start, int forbid, int sum, int[] min) {
        if (sum >= min[0]) return;

        if (start >= costs.length) {
            min[0] = sum;
            return;
        }

        for (int i = 0; i < 3; i++) {
            if (i != forbid) {
                dfs(costs, start + 1, i, sum + costs[start][i], min);
            }
        }
    }

    // Dynamic Programming
    // space complexity: O(N)
    // beats 17.76%(2 ms for 101 tests)
    public int minCost2(int[][] costs) {
        int n = costs.length;
        int[][] dp = new int[n + 1][3];
        for (int i = 0; i < n; i++) {
            dp[i + 1][0] = Math.min(dp[i][1], dp[i][2]) + costs[i][0];
            dp[i + 1][1] = Math.min(dp[i][0], dp[i][2]) + costs[i][1];
            dp[i + 1][2] = Math.min(dp[i][0], dp[i][1]) + costs[i][2];
        }
        return Math.min(dp[n][0], Math.min(dp[n][1], dp[n][2]));
    }

    // Dynamic Programming
    // space complexity: O(1)
    // beats 58.52%(1 ms for 101 tests)
    public int minCost3(int[][] costs) {
        int n = costs.length;
        int[] mins = new int[3];
        for (int i = 0; i < n; i++) {
            int min0 = Math.min(mins[1], mins[2]) + costs[i][0];
            int min1 = Math.min(mins[0], mins[2]) + costs[i][1];
            mins[2] = Math.min(mins[0], mins[1]) + costs[i][2];
            mins[0] = min0;
            mins[1] = min1;
        }
        return Math.min(mins[0], Math.min(mins[1], mins[2]));
    }

    void test(int[][] costs, int expected) {
        assertEquals(expected, minCost(costs));
        assertEquals(expected, minCost2(costs));
        assertEquals(expected, minCost3(costs));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 3, 4}, {1, 3, 2}, {3, 2, 1}}, 5);
        test(new int[][] {{1, 3, 4}, {1, 3, 2}, {3, 9, 1}}, 5);
        test(new int[][] {{1, 3, 4}, {1, 3, 2}, {3, 9, 1}, {2, 3, 1}}, 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PaintHouse");
    }
}
