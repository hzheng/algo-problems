import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1599: https://leetcode.com/problems/maximum-profit-of-operating-a-centennial-wheel/
//
// You are the operator of a Centennial Wheel that has four gondolas, and each gondola has room for
// up to four people. You have the ability to rotate the gondolas counterclockwise, which costs you
// runningCost dollars.
// You are given an array customers of length n where customers[i] is the number of new customers
// arriving just before the ith rotation (0-indexed). This means you must rotate the wheel i times
// before customers[i] arrive. Each customer pays boardingCost dollars when they board on the
// gondola closest to the ground and will exit once that gondola reaches the ground again.
// You can stop the wheel at any time, including before serving all customers. If you decide to stop
// serving customers, all subsequent rotations are free in order to get all the customers down
// safely. Note that if there are currently more than four customers waiting at the wheel, only
// four will board the gondola, and the rest will wait for the next rotation.
// Return the minimum number of rotations you need to perform to maximize your profit. If there is
// no scenario where the profit is positive, return -1.
// Constraints:
// n == customers.length
// 1 <= n <= 105
// 0 <= customers[i] <= 50
// 1 <= boardingCost, runningCost <= 100
public class CentennialWheel {
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(86.33%), 49.1 MB(79.38%) for 136 tests
    public int minOperationsMaxProfit(int[] customers, int boardingCost, int runningCost) {
        int n = customers.length;
        int maxProfit = 0;
        int maxRound = -1;
        for (int i = 0, wait = 0, profit = 0, round = 1; wait > 0 || i < n; round++) {
            if (i < n) {
                wait += customers[i++];
            }
            int board = Math.min(wait, 4);
            wait -= board;
            profit += board * boardingCost - runningCost;
            if (profit > maxProfit) {
                maxProfit = profit;
                maxRound = round;
            }
        }
        return maxProfit > 0 ? maxRound : -1;
    }

    void test(int[] customers, int boardingCost, int runningCost, int expected) {
        assertEquals(expected, minOperationsMaxProfit(customers, boardingCost, runningCost));
    }

    @Test public void test() {
        test(new int[] {8, 3}, 5, 6, 3);
        test(new int[] {10, 9, 6}, 6, 4, 7);
        test(new int[] {3, 4, 0, 5, 1}, 1, 92, -1);
        test(new int[] {10, 10, 6, 4, 7}, 3, 8, 9);
        test(new int[] {19, 42, 25, 18, 34, 24, 22, 11, 38, 33, 50, 33, 50, 26, 10, 4, 46, 5, 3, 50,
                        38, 20, 13, 1, 28, 6, 37, 11, 1, 9, 39, 13, 4, 14, 3, 39, 39, 0, 16, 11, 12,
                        16, 2, 28, 46, 33, 49, 43, 9, 23, 43, 40}, 35, 96, 307);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
