import java.util.*;
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
    // beats 85.40%(1 ms)
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
    // beats 10.50%(3 ms)
    public int maxProfit4(int[] prices) {
        int max = 0; // global max
        for (int i = 1, localMax = 0; i < prices.length; i++) {
            localMax = Math.max(0, localMax + prices[i] - prices[i - 1]);
            max = Math.max(localMax, max);
        }
        return max;
    }

    void test(Function<int[], Integer> maxProfit, int expected, int ... prices) {
        assertEquals(expected, (int)maxProfit.apply(prices));
    }

    void test(int expected, int ... prices) {
        BestTransaction b = new BestTransaction();
        test(b::maxProfit, expected, prices);
        test(b::maxProfit2, expected, prices);
        test(b::maxProfit3, expected, prices);
        test(b::maxProfit4, expected, prices);
    }

    @Test
    public void test1() {
        test(13, 20, 10, 9, 2, 8, 15);
        test(0, 20, 10, 9, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BestTransaction");
    }
}
