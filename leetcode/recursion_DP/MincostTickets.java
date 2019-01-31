import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC983: https://leetcode.com/problems/minimum-cost-for-tickets/
//
// In a country popular for train travel, you have planned some train travelling
// one year in advance. The days of the year that you will travel is given as an
// array days.  Each day is an integer from 1 to 365.
// Train tickets are sold in 3 different ways:
// a 1-day pass is sold for costs[0] dollars;
// a 7-day pass is sold for costs[1] dollars;
// a 30-day pass is sold for costs[2] dollars.
// The passes allow that many days of consecutive travel. Return the minimum 
//number of dollars you need to travel every day in the given list of days.
public class MincostTickets {
    // DFS + Recursion
    // beats 0.90%(3435 ms for 64 tests)
    public int mincostTickets(int[] days, int[] costs) {
        int[] res = new int[days.length + 1];
        for (int i = 1; i <= days.length; i++) {
            res[i] = i * costs[0];
        }
        dfs(0, 0, days, costs, res);
        return res[days.length];
    }

    private void dfs(int cur, int cost, int[] days, int[] costs, int[] res) {
        if (cur > days.length) return;
        if (cost > res[cur]) return;

        res[cur] = Math.min(res[cur], cost);
        dfs(cur + 1, cost + costs[0], days, costs, res);
        for (int i = cur;; i++) {
            if (i == days.length || days[i] > days[cur] + 6) {
                dfs(i, cost + costs[1], days, costs, res);
                break;
            }
        }
        for (int i = cur;; i++) {
            if (i == days.length || days[i] > days[cur] + 29) {
                dfs(i, cost + costs[2], days, costs, res);
                break;
            }
        }
    }

    // DFS + Recursion + Dynamic Programming(Top-Down)
    // beats 37.51%(6 ms for 64 tests)
    public int mincostTickets2(int[] days, int[] costs) {
        Set<Integer> daySet = new HashSet<>();
        for (int d : days) {
            daySet.add(d);
        }
        return dfs(1, costs, daySet, new Integer[366]);
    }

    private int dfs(int i, int[] costs, Set<Integer> days, Integer[] dp) {
        if (i >= dp.length) return 0;

        if (dp[i] != null) return dp[i];

        if (!days.contains(i)) return dp[i] = dfs(i + 1, costs, days, dp);

        dp[i] = Math.min(dfs(i + 1, costs, days, dp) + costs[0],
                         dfs(i + 7, costs, days, dp) + costs[1]);
        return dp[i] = Math.min(dp[i], dfs(i + 30, costs, days, dp) + costs[2]);
    }

    // DFS + Recursion + Dynamic Programming(Top-Down)
    // beats 100.00%(3 ms for 64 tests)
    private static final int[] durations = new int[] {1, 7, 30};

    public int mincostTickets3(int[] days, int[] costs) {
        return dfs(0, days, costs, new Integer[days.length]);
    }

    private int dfs(int i, int[] days, int[] costs, Integer[] dp) {
        if (i >= days.length) return 0;

        if (dp[i] != null) return dp[i];

        int res = Integer.MAX_VALUE;
        for (int k = 0, j = i; k < durations.length; k++) {
            for (; j < days.length && days[j] < days[i] + durations[k]; j++) {}
            res = Math.min(res, dfs(j, days, costs, dp) + costs[k]);
        }
        return dp[i] = res;
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // beats 83.97%(4 ms for 64 tests)
    public int mincostTickets4(int[] days, int[] costs) {
        int n = 365;
        int[] dp = new int[n + 1];
        int[] dayCost = new int[n + 1];
        for (int day : days) {
            dayCost[day] = costs[0];
        }
        for (int i = 1; i <= n; i++) {
            int min = dp[i - 1] + dayCost[i];
            if (i >= 7) {
                min = Math.min(min, dp[i - 7] + costs[1]);
                if (i >= 30) {
                    min = Math.min(min, dp[i - 30] + costs[2]);
                }
            }
            dp[i] = min;
        }
        return dp[n];
    }

    void test(int[] days, int[] costs, int expected) {
        assertEquals(expected, mincostTickets(days, costs));
        assertEquals(expected, mincostTickets2(days, costs));
        assertEquals(expected, mincostTickets3(days, costs));
        assertEquals(expected, mincostTickets4(days, costs));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4, 6, 8, 9, 10, 13, 14, 16, 17, 19, 21, 24, 26, 27, 28, 29},
             new int[] {3, 14, 50}, 50);
        test(new int[] {1, 4, 6, 7, 8, 20}, new int[] {2, 7, 15}, 11);
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 30, 31}, new int[] {2, 7, 15}, 17);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
