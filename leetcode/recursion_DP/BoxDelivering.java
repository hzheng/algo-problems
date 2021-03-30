import org.junit.Test;

import static org.junit.Assert.*;

// LC1687: https://leetcode.com/problems/delivering-boxes-from-storage-to-ports/
//
// You have the task of delivering some boxes from storage to their ports using only one ship.
// However, this ship has a limit on the number of boxes and the total weight that it can carry.
// You are given an array boxes, where boxes[i] = [ports, weighti], and three integers portsCount,
// maxBoxes, and maxWeight.
// ports is the port where you need to deliver the ith box and weightsi is the weight of the ith box.
// portsCount is the number of ports.
// maxBoxes and maxWeight are the respective box and weight limits of the ship.
// The boxes need to be delivered in the order they are given. The ship will follow these steps:
// The ship will take some number of boxes from the boxes queue, not violating the maxBoxes and
// maxWeight constraints.
// For each loaded box in order, the ship will make a trip to the port the box needs to be delivered
// to and deliver it. If the ship is already at the correct port, no trip is needed, and the box can
// immediately be delivered.
// The ship then makes a return trip to storage to take more boxes from the queue.
// The ship must end at storage after all the boxes have been delivered.
// Return the minimum number of trips the ship needs to make to deliver all boxes to their
// respective ports.
//
// Constraints:
// 1 <= boxes.length <= 10^5
// 1 <= portsCount, maxBoxes, maxWeight <= 10^5
// 1 <= ports <= portsCount
// 1 <= weightsi <= maxWeight
public class BoxDelivering {
    // Dynamic Programming + Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 10 ms(51.22%), 87.1 MB(37.8%) for 41 tests
    public int boxDelivering(int[][] boxes, int portsCount, int maxBoxes, int maxWeight) {
        int n = boxes.length;
        int[] dp = new int[n + 1];
        for (int i = 0, j = 0, k = 0, trip = 0, b = maxBoxes, w = maxWeight; i < n; i++) {
            for (; j < n && b > 0 && w >= boxes[j][1]; b--, w -= boxes[j++][1]) {
                if (j == 0 || boxes[j][0] != boxes[j - 1][0]) {
                    k = j;
                    trip++;
                }
                dp[j + 1] = Integer.MAX_VALUE / 2;
            }
            dp[j] = Math.min(dp[j], dp[i] + trip + 1);
            dp[k] = Math.min(dp[k], dp[i] + trip);
            b++;
            w += boxes[i][1];
            if (i == n - 1 || boxes[i][0] != boxes[i + 1][0]) {
                trip--;
            }
        }
        return dp[n];
    }

    // Solution of Choice
    // Dynamic Programming + Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(100.00%), 86.9 MB(50.00%) for 41 tests
    public int boxDelivering2(int[][] boxes, int portsCount, int maxBoxes, int maxWeight) {
        int n = boxes.length;
        int[] dp = new int[n + 1];
        for (int i = 0, j = 0, w = 0, extraCost = 1; j < n; j++) {
            w += boxes[j][1];
            extraCost += (j == 0 || boxes[j - 1][0] != boxes[j][0]) ? 1 : 0;
            for (; (j - i >= maxBoxes) || (w > maxWeight) || (i < j && dp[i] == dp[i + 1]); i++) {
                w -= boxes[i][1];
                extraCost -= (boxes[i][0] != boxes[i + 1][0]) ? 1 : 0;
            }
            dp[j + 1] = dp[i] + extraCost;
        }
        return dp[n];
    }

    private void test(int[][] boxes, int portsCount, int maxBoxes, int maxWeight, int expected) {
        assertEquals(expected, boxDelivering(boxes, portsCount, maxBoxes, maxWeight));
        assertEquals(expected, boxDelivering2(boxes, portsCount, maxBoxes, maxWeight));
    }

    @Test public void test() {
        test(new int[][] {{1, 1}, {2, 1}, {1, 1}}, 2, 3, 3, 4);
        test(new int[][] {{1, 2}, {3, 3}, {3, 1}, {3, 1}, {2, 4}}, 3, 3, 6, 6);
        test(new int[][] {{1, 4}, {1, 2}, {2, 1}, {2, 1}, {3, 2}, {3, 4}}, 3, 6, 7, 6);
        test(new int[][] {{2, 4}, {2, 5}, {3, 1}, {3, 2}, {3, 7}, {3, 1}, {4, 4}, {1, 3}, {5, 2}},
             5, 5, 7, 14);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
