import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC122: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/
//
// Say you have an array for which the ith element is the price of a given stock
// on day i. Design an algorithm to find the maximum profit. You may complete as
// many transactions as you like.
public class BestTransaction2 {
    // beats 11.51%(2 ms)
    public int maxProfit(int[] prices) {
        if (prices.length == 0) return 0;

        int profit = 0;
        int last = prices[0];
        int low = prices[0];
        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            if (price < last) {
                profit += last - low;
                low = price;
            }
            last = price;
        }
        return profit + last - low;
    }

    // Solution of Choice
    // beats 86.18%(1 ms)
    public int maxProfit2(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            profit += Math.max(0, prices[i] - prices[i - 1]);
        }
        return profit;
    }

    void test(Function<int[], Integer> maxProfit, int expected, int ... prices) {
        assertEquals(expected, (int)maxProfit.apply(prices));
    }

    void test(int expected, int ... prices) {
        BestTransaction2 b = new BestTransaction2();
        test(b::maxProfit, expected, prices);
        test(b::maxProfit2, expected, prices);
    }

    @Test
    public void test1() {
        test(13, 20, 10, 9, 2, 8, 15);
        test(9, 20, 10, 9, 5, 8, 6, 12);
        test(0, 20, 10, 9, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BestTransaction2");
    }
}
