import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1473: https://leetcode.com/problems/paint-house-iii/
//
// There is a row of m houses in a small city, each house must be painted with one of the n colors
// (labeled from 1 to n), some houses that has been painted last summer should not be painted again.
// A neighborhood is a maximal group of continuous houses that are painted with the same color.
// Given an array houses, an m * n matrix cost and an integer target where:
// houses[i]: is the color of the house i, 0 if the house is not painted yet.
// cost[i][j]: is the cost of paint the house i with the color j+1.
// Return the minimum cost of painting all the remaining houses in such a way that there are exactly
// target neighborhoods, if not possible return -1.
// Constraints:
//
//m == houses.length == cost.length
//n == cost[i].length
//1 <= m <= 100
//1 <= n <= 20
//1 <= target <= m
//0 <= houses[i] <= n
//1 <= cost[i][j] <= 10^4
public class PaintHouse3 {
    private static final int MAX = Integer.MAX_VALUE / 2;

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N^2*T), space complexity: O(M*N*T)
    // 48 ms(33.33%), 39.4 MB(100.00%) for 59 tests
    public int minCost(int[] houses, int[][] cost, int m, int n, int target) {
        int[][][] dp = new int[m + 1][n + 1][target + 2];
        for (int[][] x : dp) {
            for (int[] y : x) {
                Arrays.fill(y, MAX);
            }
        }
        dp[0][0][0] = 0;
        for (int i = 0; i < m; i++) {
            int color = houses[i];
            for (int grp = 0; grp <= target; grp++) {
                for (int c1 = 0; c1 <= n; c1++) {
                    if (color > 0) {
                        int grp2 = (c1 == color) ? grp : grp + 1;
                        dp[i + 1][color][grp2] = Math.min(dp[i + 1][color][grp2], dp[i][c1][grp]);
                    } else {
                        for (int c2 = 1; c2 <= n; c2++) {
                            int grp2 = (c1 == c2) ? grp : grp + 1;
                            dp[i + 1][c2][grp2] =
                                    Math.min(dp[i + 1][c2][grp2], dp[i][c1][grp] + cost[i][c2 - 1]);
                        }
                    }
                }
            }
        }
        int res = MAX;
        for (int j = 1; j <= n; j++) {
            res = Math.min(res, dp[m][j][target]);
        }
        return res == MAX ? -1 : res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(M*N^2*T), space complexity: O(M*N*T)
    // 47 ms(22.96%), 45.7 MB(100.00%) for 60 tests
    public int minCost2(int[] houses, int[][] cost, int m, int n, int target) {
        int[][][] dp = new int[m + 1][n + 1][target + 2];
        int res = solve(houses, cost, m, n, target, 0, 0, dp);
        return res < MAX ? res : -1;
    }

    private int solve(int[] houses, int[][] cost, int m, int n, int target, int i, int c1,
                      int[][][] dp) {
        if (i == m || target < 0) {
            return (target == 0) ? 0 : MAX;
        }
        if (dp[i][c1][target] != 0) { return dp[i][c1][target]; }

        int res = MAX;
        if (houses[i] == 0) {
            for (int c2 = 1; c2 <= n; c2++) {
                int tgt = (c1 == c2) ? target : target - 1;
                res = Math.min(res, solve(houses, cost, m, n, tgt, i + 1, c2, dp) + cost[i][c2 - 1]);
            }
        } else {
            int tgt = (houses[i] == c1) ? target : target - 1;
            res = solve(houses, cost, m, n, tgt, i + 1, houses[i], dp);
        }
        return dp[i][c1][target] = res;
    }

    void test(int[] houses, int[][] cost, int m, int n, int target, int expected) {
        assertEquals(expected, minCost(houses, cost, m, n, target));
        assertEquals(expected, minCost2(houses, cost, m, n, target));
    }

    @Test public void test() {
        test(new int[] {0, 0, 0, 0, 0}, new int[][] {{1, 10}, {10, 1}, {10, 1}, {1, 10}, {5, 1}}, 5,
             2, 3, 9);
        test(new int[] {0, 2, 1, 2, 0}, new int[][] {{1, 10}, {10, 1}, {10, 1}, {1, 10}, {5, 1}}, 5,
             2, 3, 11);
        test(new int[] {0, 0, 0, 0, 0}, new int[][] {{1, 10}, {10, 1}, {1, 10}, {10, 1}, {1, 10}},
             5, 2, 5, 5);
        test(new int[] {3, 1, 2, 3}, new int[][] {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, 4, 3,
             3, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
