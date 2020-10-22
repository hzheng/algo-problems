import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1595: https://leetcode.com/problems/minimum-cost-to-connect-two-groups-of-points/
//
// You are given two groups of points where the first group has size1 points, the second group has
// size2 points, and size1 >= size2. The cost of the connection between any two points are given in
// an size1 x size2 matrix where cost[i][j] is the cost of connecting point i of the first group and
// point j of the second group. The groups are connected if each point in both groups is connected
// to one or more points in the opposite group. In other words, each point in the first group must
// be connected to at least one point in the second group, and each point in the second group must
// be connected to at least one point in the first group.
// Return the minimum cost it takes to connect the two groups.
// Constraints:
// size1 == cost.length
// size2 == cost[i].length
// 1 <= size1, size2 <= 12
// size1 >= size2
// 0 <= cost[i][j] <= 100
public class ConnectTwoGroups {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(M * N * 2 ^ N), space complexity: O(M*2^N)
    // 11 ms(97.27%), 38.4 MB(5.23%) for 81 tests
    public int connectTwoGroups(List<List<Integer>> cost) {
        int m = cost.size();
        int n = cost.get(0).size();
        int[] min = new int[n];
        for (int i = 0; i < n; i++) {
            min[i] = Integer.MAX_VALUE;
            for (List<Integer> c : cost) {
                min[i] = Math.min(min[i], c.get(i));
            }
        }
        return dfs(cost, min, 0, 0, new int[m + 1][1 << n]);
    }

    private int dfs(List<List<Integer>> cost, int[] min, int cur, int mask, int[][] dp) {
        if (dp[cur][mask] > 0) { return dp[cur][mask]; }

        int res = 0;
        int n = cost.get(0).size();
        if (cur == cost.size()) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    res += min[i];
                }
            }
        } else {
            res = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                res = Math.min(res,
                               cost.get(cur).get(i) + dfs(cost, min, cur + 1, mask | (1 << i), dp));
            }
        }
        return dp[cur][mask] = res;
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M * N * 2 ^ N), space complexity: O(2^M)
    // 14 ms(89.02%), 38.2 MB(5.19%) for 81 tests
    public int connectTwoGroups2(List<List<Integer>> cost) {
        int n = cost.get(0).size();
        int totalMask = 1 << n;
        int[] dp = new int[totalMask];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        for (List<Integer> c : cost) {
            int[] ndp = new int[totalMask];
            Arrays.fill(ndp, Integer.MAX_VALUE / 2);
            for (int mask = 0; mask < totalMask; mask++) {
                for (int i = 0; i < n; i++) {
                    int nMask = mask | (1 << i);
                    ndp[nMask] = Math.min(ndp[nMask], Math.min(dp[mask], ndp[mask]) + c.get(i));
                }
            }
            dp = ndp;
        }
        return dp[totalMask - 1];
    }

    // TODO: Bipartite

    private void test(Integer[][] costs, int expected) {
        List<List<Integer>> cost = Utils.toList(costs);
        assertEquals(expected, connectTwoGroups(cost));
        assertEquals(expected, connectTwoGroups2(cost));
    }

    @Test public void test() {
        test(new Integer[][] {{15, 96}, {36, 2}}, 17);
        test(new Integer[][] {{1, 3, 5}, {4, 1, 1}, {1, 5, 3}}, 4);
        test(new Integer[][] {{2, 5, 1}, {3, 4, 7}, {8, 1, 2}, {6, 2, 4}, {3, 8, 8}}, 10);
        test(new Integer[][] {{3, 79, 86, 68, 68, 95, 56}, {4, 60, 37, 68, 48, 91, 1},
                              {48, 10, 95, 10, 48, 43, 53}, {76, 88, 81, 63, 47, 63, 32},
                              {4, 23, 70, 79, 35, 95, 37}, {89, 51, 42, 79, 23, 94, 29},
                              {14, 35, 3, 49, 67, 20, 16}, {26, 76, 64, 82, 12, 77, 19},
                              {99, 49, 90, 27, 85, 86, 74}, {1, 11, 89, 5, 63, 23, 68},
                              {29, 37, 90, 66, 1, 12, 23}}, 128);
        test(new Integer[][] {{42, 99, 99, 56, 68, 94, 84}, {52, 77, 29, 68, 46, 62, 99},
                              {4, 41, 42, 4, 35, 29, 73}, {82, 58, 68, 20, 24, 23, 29},
                              {40, 64, 61, 94, 39, 83, 28}, {83, 90, 10, 78, 42, 63, 3},
                              {92, 11, 80, 30, 2, 58, 80}}, 142);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
