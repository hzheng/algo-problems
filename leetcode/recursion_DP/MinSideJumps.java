import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

// LC1824: https://leetcode.com/problems/minimum-sideway-jumps/
//
// There is a 3 lane road of length n that consists of n + 1 points labeled from 0 to n. A frog
// starts at point 0 in the second lane and wants to jump to point n. However, there could be
// obstacles along the way.
// You are given an array obstacles of length n + 1 where each obstacles[i] (ranging from 0 to 3)
// describes an obstacle on the lane obstacles[i] at point i. If obstacles[i] == 0, there are no
// obstacles at point i. There will be at most one obstacle in the 3 lanes at each point.
// For example, if obstacles[2] == 1, then there is an obstacle on lane 1 at point 2.
// The frog can only travel from point i to point i + 1 on the same lane if there is not an obstacle
// on the lane at point i + 1. To avoid obstacles, the frog can also perform a side jump to jump to
// another lane (even if they are not adjacent) at the same point if there is no obstacle on the new
// lane.
// For example, the frog can jump from lane 3 at point 3 to lane 1 at point 3.
// Return the minimum number of side jumps the frog needs to reach any lane at point n starting from
// lane 2 at point 0.
// Note: There will be no obstacles on points 0 and n.
//
// Constraints:
// obstacles.length == n + 1
// 1 <= n <= 5 * 10^5
// 0 <= obstacles[i] <= 3
// obstacles[0] == obstacles[n] == 0
public class MinSideJumps {
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 85 ms(55.03%), 95.7 MB(96.15%) for 89 tests
    public int minSideJumps(int[] obstacles) {
        int n = obstacles.length;
        int[][] dp = new int[n][3];
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j < 3; j++) {
                dp[i][j] = Integer.MAX_VALUE / 3;
                if (obstacles[i] == j + 1) { continue; }

                for (int k = 0; k < 3; k++) {
                    if (obstacles[i] != k + 1) {
                        dp[i][j] = Math.min(dp[i][j], dp[i + 1][k] + ((j == k) ? 0 : 1));
                    }
                }
            }
        }
        return dp[0][1];
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(1)
    // 40 ms(84.22%), 95.7 MB(96.15%) for 89 tests
    public int minSideJumps2(int[] obstacles) {
        int n = obstacles.length;
        int[] dp = new int[3];
        for (int i = n - 2; i >= 0; i--) {
            int[] dp2 = new int[3];
            for (int j = 0; j < 3; j++) {
                dp2[j] = Integer.MAX_VALUE / 3;
                if (obstacles[i] == j + 1) { continue; }

                for (int k = 0; k < 3; k++) {
                    if (obstacles[i] != k + 1) {
                        dp2[j] = Math.min(dp2[j], dp[k] + ((j == k) ? 0 : 1));
                    }
                }
            }
            dp = dp2;
        }
        return dp[1];
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(1)
    // 37 ms(85.47%), 129.2 MB(71.43%) for 89 tests
    public int minSideJumps3(int[] obstacles) {
        int[] dp = new int[] {1, 0, 1};
        for (int ob : obstacles) {
            if (ob > 0) {
                dp[ob - 1] = Integer.MAX_VALUE / 3;
            }
            for (int i = dp.length - 1; i >= 0; i--) {
                if (ob != i + 1) {
                    dp[i] = Math.min(dp[i], Math.min(dp[(i + 1) % 3], dp[(i + 2) % 3]) + 1);
                }
            }
        }
        return Math.min(dp[0], Math.min(dp[1], dp[2]));
    }

    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // 8 ms(100.00%), 129.6 MB(71.43%) for 89 tests
    public int minSideJumps4(int[] obstacles) {
        int res = 0;
        for (int i = 0, cur = 2, n = obstacles.length; i < n - 1; i++) {
            if (cur != obstacles[i + 1]) { continue; }

            res++;
            for (int flag = 0; i < n; i++) {
                flag |= (1 << obstacles[i]);
                if ((flag >> 1) == 7) { break; }
            }
            if (i == n) { break; }

            cur = obstacles[i]; // furthermost obstacles
            i -= 2;
        }
        return res;
    }

    private void test(int[] obstacles, int expected) {
        assertEquals(expected, minSideJumps(obstacles));
        assertEquals(expected, minSideJumps2(obstacles));
        assertEquals(expected, minSideJumps3(obstacles));
        assertEquals(expected, minSideJumps4(obstacles));
    }

    @Test public void test() {
        test(new int[] {0, 1, 2, 3, 0}, 2);
        test(new int[] {0, 1, 1, 3, 3, 0}, 0);
        test(new int[] {0, 2, 1, 0, 3, 0}, 2);
        test(new int[] {0, 1, 0, 1, 3, 1, 1, 1, 0, 2, 0}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
