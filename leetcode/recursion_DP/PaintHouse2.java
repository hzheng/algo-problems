import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC265: https://leetcode.com/problems/paint-house-ii/
//
// There are a row of n houses, each house can be painted with one of the k colors.
// The cost of painting each house with a certain color is different. You have to
// paint all the houses such that no two adjacent houses have the same color.
// The cost of painting each house with a certain color is represented by a n x k cost matrix.
// Find the minimum cost to paint all houses.
// All costs are positive integers.
public class PaintHouse2 {
    // Dynamic Programming
    // time complexity: O(N * K ^ 2) space complexity: O(N * K)
    // beats 3.76%(17 ms for 105 tests)
    public int minCostII(int[][] costs) {
        int n = costs.length;
        if (n == 0) return 0;

        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE;

        int[][] dp = new int[n + 1][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                int min = Integer.MAX_VALUE;
                for (int m = 0; m < k; m++) {
                    if (m != j) {
                        min = Math.min(min, dp[i][m]);
                    }
                }
                dp[i + 1][j] = min + costs[i][j];
            }
        }
        int min = Integer.MAX_VALUE;
        for (int cost : dp[n]) {
            min = Math.min(min, cost);
        }
        return min;
    }

    // Dynamic Programming
    // time complexity: O(N * K ^ 2) space complexity: O(K)
    // beats 12.42%(12 ms for 105 tests)
    public int minCostII2(int[][] costs) {
        int n = costs.length;
        if (n == 0) return 0;

        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE;

        int[] mins = new int[k];
        for (int i = 0; i < n; i++) {
            int[] curMins = new int[k];
            for (int j = 0; j < k; j++) {
                int min = Integer.MAX_VALUE;
                for (int m = 0; m < k; m++) {
                    if (m != j) {
                        min = Math.min(min, mins[m]);
                    }
                }
                curMins[j] = min + costs[i][j];
            }
            System.arraycopy(curMins, 0, mins, 0, k);
        }
        int min = Integer.MAX_VALUE;
        for (int cost : mins) {
            min = Math.min(min, cost);
        }
        return min;
    }

    // Dynamic Programming + Heap
    // time complexity: O(N * K) space complexity: O(N)
    // beats 2.81%(22 ms for 105 tests)
    public int minCostII3(int[][] costs) {
        int n = costs.length;
        if (n == 0) return 0;

        int k = costs[0].length;
        if (k == 1) return n == 1 ? costs[0][0] : Integer.MAX_VALUE;

        if (k < 4) return minCostII(costs);

        int[][][] mins = new int[n + 1][3][2];
        for (int i = 1; i <= n; i++) {
            PriorityQueue<int[]> pq = new PriorityQueue<int[]>(4,
                                                               new Comparator<int[]>() {
                public int compare(int[] a, int[] b) {
                    return b[0] - a[0];
                }
            });
            for (int j = 0; j < k; j++) {
                pq.offer(new int[] {costs[i - 1][j], j});
                if (pq.size() == 4) {
                    pq.poll();
                }
            }
            for (int m = 2; !pq.isEmpty(); m--) {
                mins[i][m] = pq.poll();
            }
        }
        int[][] res = new int[3][2];
        res[0][1] = -1;
        res[1][1] = -1;
        res[2][1] = -1;
        for (int i = 1; i <= n; i++) {
            int[][] temp = new int[3][2];
            for (int j = 0; j < 3; j++) {
                int min = Integer.MAX_VALUE;
                for (int m = 0; m < 3; m++) {
                    if (res[m][1] != mins[i][j][1]) {
                        min = Math.min(min, res[m][0]);
                    }
                }
                temp[j] = new int[] {min + mins[i][j][0], mins[i][j][1]};
            }
            for (int j = 0; j < 3; j++) {
                res[j] = temp[j];
            }
        }
        return Math.min(res[0][0], Math.min(res[1][0], res[2][0]));
    }

    // Dynamic Programming
    // time complexity: O(N * K) space complexity: O(1)
    // beats 47.64%(5 ms for 105 tests)
    public int minCostII4(int[][] costs) {
        int n = costs.length;
        if (n == 0) return 0;

        int k = costs[0].length;
        int[] min = new int[2];
        for (int i = 0, prev = -1, cur = 0; i < n; i++, prev = cur) {
            int min0 = Integer.MAX_VALUE;
            int min1 = Integer.MAX_VALUE;
            for (int j = 0; j < k; j++) {
                int cost = costs[i][j] + (min[j != prev ? 0 : 1]);
                if (cost < min0) {
                    min1 = min0;
                    min0 = cost;
                    cur = j;
                } else if (cost < min1) {
                    min1 = cost;
                }
            }
            min[0] = min0;
            min[1] = min1;
        }
        return min[0];
    }

    void test(int[][] costs, int expected) {
        assertEquals(expected, minCostII(costs));
        assertEquals(expected, minCostII2(costs));
        assertEquals(expected, minCostII3(costs));
        assertEquals(expected, minCostII4(costs));
    }

    @Test
    public void test() {
        test(new int[][] {{8}}, 8);
        test(new int[][] {}, 0);
        test(new int[][] {{3, 8}}, 3);
        test(new int[][] {{1, 3, 4}, {1, 3, 2}, {3, 2, 1}}, 5);
        test(new int[][] {{1, 3, 4}, {1, 3, 2}, {3, 9, 1}}, 5);
        test(new int[][] {{1, 3, 4}, {1, 3, 2}, {3, 9, 1}, {2, 3, 1}}, 7);
        test(new int[][] {{17, 9, 6, 2, 6, 18, 8, 12, 3, 5, 9, 11, 20, 8, 13, 16}}, 2);
        test(new int[][] {{3, 20, 7, 7, 16, 8, 7, 12, 11, 19, 1},
                          {10, 14, 3, 3, 9, 13, 4, 12, 14, 13, 1},
                          {10, 1, 14, 11, 1, 16, 2, 7, 16, 7, 19},
                          {13, 20, 17, 15, 3, 13, 8, 10, 7, 8, 9},
                          {4, 14, 18, 15, 11, 9, 19, 3, 15, 12, 15},
                          {14, 12, 16, 19, 2, 12, 13, 3, 11, 10, 9},
                          {18, 12, 10, 16, 19, 9, 18, 4, 14, 2, 4}}, 15);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PaintHouse2");
    }
}
