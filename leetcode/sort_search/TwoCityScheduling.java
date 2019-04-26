import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC1029: https://leetcode.com/problems/two-city-scheduling/
//
// There are 2N people a company is planning to interview. The cost of flying 
// the i-th person to city A is costs[i][0], and the cost of flying the i-th 
// person to city B is costs[i][1].
// Return the minimum cost to fly every person to a city such that exactly N
// people arrive in each city.
// Note:
// 1 <= costs.length <= 100
// It is guaranteed that costs.length is even.
// 1 <= costs[i][0], costs[i][1] <= 1000
public class TwoCityScheduling {
    // Heap
    // time complexity: O(N * log(N)), space complexity: O(1)
    // 1 ms(99.62%), 38.5 MB(100%) for 49 tests
    public int twoCitySchedCost(int[][] costs) {
        int n = costs.length;
        int res = 0;
        int countA = 0;
        int countB = 0;
        PriorityQueue<Integer> pqA = new PriorityQueue<>();
        PriorityQueue<Integer> pqB = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            int diff = Math.abs(costs[i][0] - costs[i][1]);
            res += Math.min(costs[i][0], costs[i][1]);
            if (costs[i][0] < costs[i][1]) {
                countA++;
                pqA.offer(diff);
            } else {
                countB++;
                pqB.offer(diff);
            }
        }
        while (countA > countB) {
            res += pqA.poll();
            countA--;
            countB++;
        }
        while (countA < countB) {
            res += pqB.poll();
            countB--;
            countA++;
        }
        return res;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // 30 ms(39.86%), 36.9 MB(100%) for 49 tests
    public int twoCitySchedCost2(int[][] costs) {
        Arrays.sort(costs, (a, b) -> (a[0] - a[1]) - (b[0] - b[1]));
        int res = 0;
        int n = costs.length / 2;
        for (int i = n - 1; i >= 0; i--) {
            res += costs[i][0] + costs[i + n][1];
        }
        return res;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // 31 ms(39.64%), 39.4 MB(100%) for 49 tests
    public int twoCitySchedCost3(int[][] costs) {
        Arrays.sort(costs, (a, b) -> (b[0] - b[1]) - (a[0] - a[1]));
        int res = 0;
        for (int[] cost : costs) {
            res += cost[0];
        }
        int n = costs.length / 2;
        for (int i = n - 1; i >= 0; i--) {
            res += costs[i][1] - costs[i][0];
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // 31 ms(39.64%), 39.4 MB(100%) for 49 tests
    public int twoCitySchedCost4(int[][] costs) {
        int N = costs.length / 2;
        int[][] dp = new int[N + 1][N + 1];
        for (int i = 1; i <= N; i++) {
            dp[i][0] = dp[i - 1][0] + costs[i - 1][0];
        }
        for (int j = 1; j <= N; j++) {
            dp[0][j] = dp[0][j - 1] + costs[j - 1][1];
        }
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                dp[i][j] = Math.min(dp[i - 1][j] + costs[i + j - 1][0],
                                    dp[i][j - 1] + costs[i + j - 1][1]);
            }
        }
        return dp[N][N];
    }

    void test(int[][] costs, int expected) {
        assertEquals(expected, twoCitySchedCost(costs));
        assertEquals(expected, twoCitySchedCost2(costs));
        assertEquals(expected, twoCitySchedCost3(costs));
        assertEquals(expected, twoCitySchedCost4(costs));
    }

    @Test
    public void test() {
        test(new int[][] {{10, 20}, {30, 200}, {400, 50}, {30, 20}}, 110);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
