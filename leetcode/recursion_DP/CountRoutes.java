import org.junit.Test;

import static org.junit.Assert.*;

// LC1575: https://leetcode.com/problems/count-all-possible-routes/
//
// You are given an array of distinct positive integers locations where locations[i] represents the
// position of city i. You are also given integers start, finish and fuel representing the starting
// city, ending city, and the initial amount of fuel you have, respectively.
// At each step, if you are at city i, you can pick any city j such that j != i and
// 0 <= j < locations.length and move to city j. Moving from city i to city j reduces the amount of
// fuel you have by |locations[i] - locations[j]|.
// Notice that fuel cannot become negative at any point in time, and that you are allowed to visit
// any city more than once (including start and finish).
// Return the count of all possible routes from start to finish.
// Since the answer may be too large, return it modulo 10^9 + 7.
//
// Constraints:
// 2 <= locations.length <= 100
// 1 <= locations[i] <= 10^9
// All integers in locations are distinct.
// 0 <= start, finish < locations.length
// 1 <= fuel <= 200
public class CountRoutes {
    private static final int MOD = 1_000_000_007;

    // DFS + Recursion + 2D-Dynamic Programming(Top-Down)
    // time complexity: O(N^2*F), space complexity: O(N*F)
    // 55 ms(83.23%), 38.4 MB(77.02%) for 100 tests
    public int countRoutes(int[] locations, int start, int finish, int fuel) {
        return dfs(locations, start, finish, fuel, new Integer[locations.length][fuel + 1]);
    }

    private int dfs(int[] locations, int cur, int finish, int fuel, Integer[][] dp) {
        if (dp[cur][fuel] != null) { return dp[cur][fuel]; }

        int res = (cur == finish) ? 1 : 0;
        for (int i = 0, n = locations.length; i < n; i++) {
            int d = Math.abs(locations[cur] - locations[i]);
            if (d > 0 && d <= fuel) {
                res = (res + dfs(locations, i, finish, fuel - d, dp)) % MOD;
            }
        }
        return dp[cur][fuel] = res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2*F), space complexity: O(N*F)
    // 166 ms(41.62%), 38.3 MB(81.99%) for 100 tests
    public int countRoutes2(int[] locations, int start, int finish, int fuel) {
        int n = locations.length;
        int[][] dp = new int[n][fuel + 1];
        for (int f = 0; f <= fuel; f++) {
            dp[finish][f] = 1;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int d = Math.abs(locations[i] - locations[j]);
                    if (d > 0 && d <= f) {
                        dp[i][f] = (dp[i][f] + dp[j][f - d]) % MOD;
                    }
                }
            }
        }
        return dp[start][fuel];
    }

    private void test(int[] locations, int start, int finish, int fuel, int expected) {
        assertEquals(expected, countRoutes(locations, start, finish, fuel));
        assertEquals(expected, countRoutes2(locations, start, finish, fuel));
    }

    @Test public void test1() {
        test(new int[] {2, 3, 6, 8, 4}, 1, 3, 5, 4);
        test(new int[] {4, 3, 1}, 1, 0, 6, 5);
        test(new int[] {5, 2, 1}, 0, 2, 3, 0);
        test(new int[] {2, 1, 5}, 0, 0, 3, 2);
        test(new int[] {1, 2, 3}, 0, 2, 40, 615088286);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
