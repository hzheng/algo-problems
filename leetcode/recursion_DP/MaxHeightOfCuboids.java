import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.Utils;

// LC1691: https://leetcode.com/problems/maximum-height-by-stacking-cuboids/
//
// Given n cuboids where the dimensions of the ith cuboid is cuboids[i] = [widthi, lengthi, heighti]
// (0-indexed). Choose a subset of cuboids and place them on each other. You can place cuboid i on
// cuboid j if widthi <= widthj and lengthi <= lengthj and heighti <= heightj. You can rearrange any
// cuboid's dimensions by rotating it to put it on another cuboid.
// Return the maximum height of the stacked cuboids.
//
// Constraints:
// n == cuboids.length
// 1 <= n <= 100
// 1 <= widthi, lengthi, heighti <= 100
public class MaxHeightOfCuboids {
    // 1-D Dynamic Programming(Bottom-Up) + Sort
    // time complexity: O(N^2), space complexity: O(N)
    // 6 ms(100%), 38.5 MB(100%) for 80 tests
    public int maxHeight(int[][] cuboids) {
        for (int[] c : cuboids) {
            Arrays.sort(c); // make the height largest
        }
        Arrays.sort(cuboids, (a, b) -> (a[0] + a[1] + a[2] - b[0] - b[1] - b[2]));
        int n = cuboids.length;
        int[] dp = new int[n];
        int res = 0;
        for (int i = 0; i < n; i++) {
            int[] c1 = cuboids[i];
            dp[i] = c1[2];
            for (int j = i - 1; j >= 0; j--) {
                int[] c2 = cuboids[j];
                if (c2[2] > c1[2]) { continue; }

                if ((c2[0] <= c1[0] && c2[1] <= c1[1]) || (c2[0] <= c1[1] && c2[1] <= c1[0])) {
                    dp[i] = Math.max(dp[i], dp[j] + c1[2]);
                }
            }
            res = Math.max(res, dp[i]);
        }
        return res;
    }

    // 1-D Dynamic Programming(Bottom-Up) + Sort
    // time complexity: O(N^2), space complexity: O(N)
    // 5 ms(100%), 38.3 MB(100%) for 80 tests
    public int maxHeight2(int[][] cuboids) {
        for (int[] c : cuboids) {
            Arrays.sort(c); // make the height largest
        }
        Arrays.sort(cuboids, (a, b) -> {
            if (a[0] != b[0]) { return a[0] - b[0]; }
            return (a[1] != b[1]) ? a[1] - b[1] : a[2] - b[2];
        });
        int n = cuboids.length;
        int[] dp = new int[n];
        int res = 0;
        for (int i = 0; i < n; i++) {
            int[] c1 = cuboids[i];
            dp[i] = c1[2];
            for (int j = i - 1; j >= 0; j--) {
                int[] c2 = cuboids[j];
                if (c2[0] <= c1[0] && c2[1] <= c1[1] && c2[2] <= c1[2]) {
                    dp[i] = Math.max(dp[i], dp[j] + c1[2]);
                }
            }
            res = Math.max(res, dp[i]);
        }
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down) + Sort
    // time complexity: O(N^2), space complexity: O(N)
    // 5 ms(100%), 38.7 MB(100%) for 80 tests
    public int maxHeight3(int[][] cuboids) {
        for (int[] c : cuboids) {
            Arrays.sort(c); // make the height largest
        }
        Arrays.sort(cuboids, (a, b) -> {
            if (a[0] != b[0]) { return a[0] - b[0]; }
            return (a[1] != b[1]) ? a[1] - b[1] : a[2] - b[2];
        });
        int n = cuboids.length;
        return dfs(cuboids, n - 1, n, new int[n][n + 1]);
    }

    private int dfs(int[][] cuboids, int cur, int prev, int[][] dp) {
        if (cur < 0) { return 0; }
        if (dp[cur][prev] > 1) { return dp[cur][prev] - 1; }

        int res = dfs(cuboids, cur - 1, prev, dp);
        int[] c1 = cuboids[cur];
        int[] c2 = (prev == cuboids.length) ? null : cuboids[prev];
        if (prev == cuboids.length || (c1[0] <= c2[0] && c1[1] <= c2[1] && c1[2] <= c2[2])) {
            res = Math.max(res, c1[2] + dfs(cuboids, cur - 1, cur, dp));
        }
        dp[cur][prev] = res + 1;
        return res;
    }

    private void test(int[][] cuboids, int expected) {
        assertEquals(expected, maxHeight(Utils.clone(cuboids)));
        assertEquals(expected, maxHeight2(Utils.clone(cuboids)));
        assertEquals(expected, maxHeight3(Utils.clone(cuboids)));
    }

    @Test public void test() {
        test(new int[][] {{38, 25, 45}, {76, 35, 3}}, 76);
        test(new int[][] {{7, 11, 17}, {7, 17, 11}, {11, 7, 17}, {11, 17, 7}, {17, 7, 11},
                          {17, 11, 7}}, 102);
        test(new int[][] {{12, 76, 13}, {68, 55, 30}, {48, 85, 52}, {91, 7, 41}, {29, 65, 35}},
             161);
        test(new int[][] {{50, 45, 20}, {95, 37, 53}, {45, 23, 12}}, 190);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
