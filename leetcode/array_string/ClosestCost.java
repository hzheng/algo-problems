import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1774: https://leetcode.com/problems/closest-dessert-cost/
//
// You would like to make dessert and are preparing to buy the ingredients. You have n ice cream
// base flavors and m types of toppings to choose from. You must follow these rules when making your
// dessert:
// There must be exactly one ice cream base.
// You can add one or more types of topping or have no toppings at all.
// There are at most two of each type of topping.
// You are given three inputs:
// baseCosts, an integer array of length n, where each baseCosts[i] represents the price of the ith
// ice cream base flavor.
// toppingCosts, an integer array of length m, where each toppingCosts[i] is the price of one of the
// ith topping.
// target, an integer representing your target price for dessert.
// You want to make a dessert with a total cost as close to target as possible.
// Return the closest possible cost of the dessert to target. If there are multiple, return the
// lower one.
//
// Constraints:
// n == baseCosts.length
// m == toppingCosts.length
// 1 <= n, m <= 10
// 1 <= baseCosts[i], toppingCosts[i] <= 10^4
// 1 <= target <= 10^4
public class ClosestCost {
    // Combinatorics
    // time complexity: O(B*3^T), space complexity: O(1)
    // 7 ms(83.33%), 35.8 MB(100.00%) for 65 tests
    public int closestCost(int[] baseCosts, int[] toppingCosts, int target) {
        int max = (int)Math.pow(3, toppingCosts.length) - 1;
        int res = Integer.MAX_VALUE;
        for (int baseCost : baseCosts) {
            for (int choice = max; choice >= 0; choice--) {
                int cost = baseCost;
                for (int c = choice, i = 0; c > 0 && cost < target; c /= 3) {
                    cost += toppingCosts[i++] * (c % 3);
                }
                int minDiff = Math.abs(res - target);
                int curDiff = Math.abs(cost - target);
                if (curDiff < minDiff || curDiff == minDiff && cost < res) {
                    res = cost;
                }
            }
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(B*3^T), space complexity: O(1)
    // 0 ms(100.00%), 36.3 MB(83.33%) for 65 tests
    public int closestCost2(int[] baseCosts, int[] toppingCosts, int target) {
        int[] res = {baseCosts[0]};
        for (int baseCost : baseCosts) {
            dfs(res, baseCost, 0, toppingCosts, target);
        }
        return res[0];
    }

    private void dfs(int[] res, int cost, int index, int[] toppingCosts, int target) {
        int diff = Math.abs(target - cost) - Math.abs(target - res[0]);
        if (diff < 0 || diff == 0 && cost < res[0]) {
            res[0] = cost;
        }
        if (index < toppingCosts.length && cost < target) {
            for (int i = 0; i < 3; i++) {
                dfs(res, cost + toppingCosts[index] * i, index + 1, toppingCosts, target);
            }
        }
    }

    private void test(int[] baseCosts, int[] toppingCosts, int target, int expected) {
        assertEquals(expected, closestCost(baseCosts, toppingCosts, target));
        assertEquals(expected, closestCost2(baseCosts, toppingCosts, target));
    }

    @Test public void test() {
        test(new int[] {1, 7}, new int[] {3, 4}, 10, 10);
        test(new int[] {2, 3}, new int[] {4, 5, 100}, 18, 17);
        test(new int[] {3, 10}, new int[] {2, 5}, 9, 8);
        test(new int[] {10}, new int[] {1}, 1, 10);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
