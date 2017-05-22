import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC568: https://leetcode.com/problems/maximum-vacation-days/
//
// You travel among N cities, you could take vacations in some particular cities
// and weeks. Your job is to schedule the traveling to maximize the number of vacation
// days you could take, but there are certain rules and restrictions you need to follow.
// You can only travel among N cities, represented by indexes from 0 to N-1. Initially,
// you are in the city indexed 0 on Monday. The cities are connected by flights. The
// flights are represented as a N*N matrix, called flights representing the airline
// status from the city i to the city j. Flights[i][i] = 0 for all i.
// You totally have K weeks to travel. You can only take flights at most once per day
// and can only take flights on each week's Monday morning. We don't consider the impact
// of flight time. For each city, you can only have restricted vacation days in different
// weeks, given an N*K matrix called days representing this relationship. For the value
// of days[i][j], it represents the maximum days you could take vacation in the city i
// in the week j. You're given the flights matrix and days matrix, and you need to output
// the maximum vacation days you could take during K weeks.
// Note:
// N and K are positive integers, which are in the range of [1, 100].
// In the matrix flights, all the values are integers in the range of [0, 1].
// In the matrix days, all the values are integers in the range [0, 7].
// You could stay at a city beyond the number of vacation days, but you should work on
// the extra days, which won't be counted as vacation days.
// If you fly from the city A to the city B and take the vacation on that day, the deduction
// towards vacation days will count towards the vacation days of city B in that week.
public class MaxVacationDays {
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2 * K), space complexity: O(N * K)
    // beats 96.86%(80 ms for 57 tests)
    public int maxVacationDays(int[][] flights, int[][] days) {
        int N = flights.length;
        int K = days[0].length;
        int[][] dp = new int[N][K];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dp[i], Integer.MIN_VALUE);
            if (i == 0 || flights[0][i] == 1) {
                dp[i][0] = days[i][0];
            }
        }
        for (int k = 1; k < K; k++) {
            for (int i = 0; i < N; i++) {
                int prev = dp[i][k - 1];
                if (prev < 0) continue;

                for (int j = 0; j < N; j++) {
                    if (i == j || flights[i][j] == 1) {
                        dp[j][k] = Math.max(dp[j][k], prev + days[j][k]);
                    }
                }
            }
        }
        int max = 0;
        for (int i = 0; i < N; i++) {
            max = Math.max(max, dp[i][K - 1]);
        }
        return max;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2 * K), space complexity: O(N * K)
    // beats 97.87%(77 ms for 57 tests)
    public int maxVacationDays2(int[][] flights, int[][] days) {
        int N = flights.length;
        int K = days[0].length;
        int[][] dp = new int[N][K + 1];
        for (int k = K - 1; k >= 0; k--) {
            for (int i = 0; i < N; i++) {
                dp[i][k] = days[i][k] + dp[i][k + 1];
                for (int j = 0; j < N; j++) {
                    if (flights[i][j] == 1) {
                        dp[i][k] = Math.max(days[j][k] + dp[j][k + 1], dp[i][k]);
                    }
                }
            }
        }
        return dp[0][0];
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2 * K), space complexity: O(N)
    // beats 98.63%(74 ms for 57 tests)
    public int maxVacationDays3(int[][] flights, int[][] days) {
        int N = flights.length;
        int K = days[0].length;
        int[] dp = new int[N];
        for (int k = K - 1; k >= 0; k--) {
            int[] tmp = new int[N];
            for (int i = 0; i < N; i++) {
                tmp[i] = days[i][k] + dp[i];
                for (int j = 0; j < N; j++) {
                    if (flights[i][j] == 1) {
                        tmp[i] = Math.max(days[j][k] + dp[j], tmp[i]);
                    }
                }
            }
            dp = tmp;
        }
        return dp[0];
    }

    // DFS + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 2 * K), space complexity: O(N * K)
    // beats 96.86%(80 ms for 57 tests)
    public int maxVacationDays4(int[][] flights, int[][] days) {
        int[][] dp = new int[flights.length][days[0].length];
        for (int[] a : dp) {
            Arrays.fill(a, Integer.MIN_VALUE);
        }
        return dfs(flights, days, 0, 0, dp);
    }

    private int dfs(int[][] flights, int[][] days, int city, int week, int[][] dp) {
        if (week == days[0].length) return 0;

        if (dp[city][week] != Integer.MIN_VALUE) return dp[city][week];

        int max = 0;
        for (int i = 0; i < flights.length; i++) {
            if (flights[city][i] == 1 || i == city) {
                max = Math.max(max, days[i][week] + dfs(flights, days, i, week + 1, dp));
            }
        }
        return dp[city][week] = max;
    }

    void test(int[][] flights, int[][] days, int expected) {
        assertEquals(expected, maxVacationDays(flights, days));
        assertEquals(expected, maxVacationDays2(flights, days));
        assertEquals(expected, maxVacationDays3(flights, days));
        assertEquals(expected, maxVacationDays4(flights, days));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}, new int[][] {{1, 1, 1}, {7, 7, 7}, {7, 7, 7}}, 3);
        test(new int[][] {{0, 0, 1, 0, 0}, {0, 0, 0, 1, 1}, {0, 0, 0, 0, 1}, {0, 1, 1, 0, 0}, {0, 1, 0, 1, 0}},
             new int[][] {{3, 1, 6, 2, 2}, {1, 3, 5, 6, 5}, {3, 2, 5, 0, 0}, {2, 3, 5, 4, 3}, {3, 3, 1, 5, 4}}, 22);
        test(new int[][] {{0, 1, 1}, {1, 0, 1}, {1, 1, 0}}, new int[][] {{1, 3, 1}, {6, 0, 3}, {3, 3, 3}}, 12);
        test(new int[][] {{0, 1, 1}, {1, 0, 1}, {1, 1, 0}}, new int[][] {{7, 0, 0}, {0, 7, 0}, {0, 0, 7}}, 21);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
