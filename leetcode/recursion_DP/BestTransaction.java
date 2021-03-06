import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC121: https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
//
// Say you have an array for which the ith element is the price of a given stock
// on day i. If you were only permitted to complete at most one transaction,
// design an algorithm to find the maximum profit.
public class BestTransaction {
    // beats 51.18%(2 ms)
    public int maxProfit(int[] prices) {
        if (prices.length == 0) return 0;

        int profit = 0;
        int low = prices[0];
        int high = prices[0];
        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            if (price > high) {
                high = price;
                profit = Math.max(profit, high - low);
            } else if (price < low) {
                low = price;
                high = low;
            }
        }
        return profit;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 85.40%(1 ms)
    public int maxProfit2(int[] prices) {
        int min = Integer.MAX_VALUE;
        int profit = 0;
        for (int price : prices) {
            min = Math.min(min, price);
            profit = Math.max(profit, price - min);
        }
        return profit;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // beats 99.91%(1 ms for 200 tests)
    public int maxProfit3(int[] prices) {
        int min = Integer.MAX_VALUE;
        int profit = 0;
        for (int price : prices) {
            if (price < min) {
                min = price;
            } else if (price > min + profit) {
                profit = price - min;
            }
        }
        return profit;
    }

    // Kadane's algorithm
    // time complexity: O(N), space complexity: O(1)
    // beats 99.91%(1 ms for 200 tests)
    public int maxProfit4(int[] prices) {
        int max = 0; // global max
        for (int i = 1, localMax = 0; i < prices.length; i++) {
            localMax = Math.max(0, localMax + prices[i] - prices[i - 1]);
            max = Math.max(localMax, max);
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 80.02%(1 ms for 200 tests)
    public int maxProfit5(int[] prices) {
        int n = prices.length;
        int[] minFromLeft = new int[n + 1];
        minFromLeft[0] = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            minFromLeft[i + 1] = Math.min(minFromLeft[i], prices[i]);
        }
        int max = 0;
        for (int i = n - 1; i > 0; i--) {
            max = Math.max(max, prices[i] - minFromLeft[i]);
        }
        return max;
    }

    void test(Function<int[], Integer> maxProfit, int expected, int... prices) {
        assertEquals(expected, (int)maxProfit.apply(prices));
    }

    void test(int expected, int... prices) {
        BestTransaction b = new BestTransaction();
        test(b::maxProfit, expected, prices);
        test(b::maxProfit2, expected, prices);
        test(b::maxProfit3, expected, prices);
        test(b::maxProfit4, expected, prices);
        test(b::maxProfit5, expected, prices);
    }

    @Test
    public void test1() {
        test(13, 20, 10, 9, 2, 8, 15);
        test(0, 20, 10, 9, 2);
        test(0);
        test(1, 1, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
