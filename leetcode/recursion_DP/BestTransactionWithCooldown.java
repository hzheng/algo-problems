import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/

//Say you have an array for which the ith element is the price of a given stock
// on day i. Design an algorithm to find the maximum profit. You may complete as
// many transactions as you like (ie, buy one and sell one share of the stock
// multiple times) with the following restrictions:
// You may not engage in multiple transactions at the same time.
// After you sell your stock, you cannot buy stock on next day.
public class BestTransactionWithCooldown {
    // beats 50.82%
    public int maxProfit(int[] prices) {
        int n = prices.length;
        // profits[i] is the max profit ending before day i
        int[] profits = new int[n];
        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            int diff = price - prices[i - 1];
            if (diff <= 0) {
                profits[i] = profits[i - 1];
            } else if (i < 3) {
                profits[i] = profits[i - 1] + diff;
            } else {
                // ignore day i; day (i - 2) cooldown
                profits[i] = Math.max(profits[i - 1], profits[i - 3] + diff);
                for (int j = i; j > i - 3; j--) { // extend day (j - 1)
                    if (j < 2 || prices[j - 1] > prices[j - 2]) {
                        diff = price - prices[j - 1];
                        profits[i] = Math.max(profits[i], profits[j - 1] + diff);
                    }
                }
            }
        }
        return n == 0 ? 0 : profits[n - 1];
    }

    void test(Function<int[], Integer> maxProfit, int expected, int ... prices) {
        assertEquals(expected, (int)maxProfit.apply(prices));
    }

    void test(int expected, int ... prices) {
        BestTransactionWithCooldown b = new BestTransactionWithCooldown();
        test(b::maxProfit, expected, prices);
    }

    @Test
    public void test1() {
        test(3, 1, 2, 1, 0, 1, 2);
        test(3, 2, 1, 2, 1, 0, 1, 2);
        test(6, 6, 1, 3, 2, 4, 7);
        test(15, 2, 6, 8, 7, 8, 7, 9, 4, 1, 2, 4, 5, 8);
        test(10, 2, 5, 8, 3, 8, 2, 6);
        test(10, 8, 6, 4, 3, 3, 2, 3, 5, 8, 3, 8, 2, 6);
        test(8, 2, 8, 7, 8, 7, 9);
        test(15, 2, 8, 7, 8, 7, 9, 4, 1, 8);
        test(7, 3, 2, 6, 5, 0, 3);
        test(3, 1, 2, 3, 0, 2);
        test(1, 2, 1, 2, 0, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BestTransactionWithCooldown");
    }
}
