import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1833: https://leetcode.com/problems/maximum-ice-cream-bars/
//
// It is a sweltering summer day, and a boy wants to buy some ice cream bars.
// At the store, there are n ice cream bars. You are given an array costs of length n, where
// costs[i] is the price of the ith ice cream bar in coins. The boy initially has coins coins to
// spend, and he wants to buy as many ice cream bars as possible.
// Return the maximum number of ice cream bars the boy can buy with coins coins.
//
// Note: The boy can buy the ice cream bars in any order.
//
// Constraints:
// costs.length == n
// 1 <= n <= 10^5
// 1 <= costs[i] <= 10^5
// 1 <= coins <= 10^8
public class MaxIceCream {
    // Sort + Greedy
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 47 ms(75.00%), 69.3 MB(25.00%) for 61 tests
    public int maxIceCream(int[] costs, int coins) {
        Arrays.sort(costs);
        int n = costs.length;
        for (int i = 0; i < n; i++) {
            if ((coins -= costs[i]) < 0) { return i; }
        }
        return n;
    }

    private void test(int[] costs, int coins, int expected) {
        assertEquals(expected, maxIceCream(costs, coins));
    }

    @Test public void test() {
        test(new int[] {1, 3, 2, 4, 1}, 7, 4);
        test(new int[] {10, 6, 8, 7, 7, 8}, 5, 0);
        test(new int[] {1, 6, 3, 1, 2, 5}, 20, 6);
        test(new int[] {49027, 64023, 1133, 54326, 51946, 96986, 55570, 49385, 93489, 85082, 51557,
                        83066, 44042, 57518, 99318, 22605, 14746, 31556, 39042, 51782, 68452, 77778,
                        60504, 92727, 296, 82474, 47874, 57452, 50002, 53124, 14778, 17218, 77142,
                        95610, 84812, 49610, 16080, 60930, 36260, 72438, 55732, 95007, 72998, 1026,
                        76316, 40742, 59220, 87422, 51666, 66714, 47514, 24139, 13884, 23046,
                        84986}, 34233545, 55);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
