import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC122: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/
//
// Say you have an array for which the ith element is the price of a given stock
// on day i. Design an algorithm to find the maximum profit. You may complete as
// many transactions as you like.
public class BestTransaction2 {
    // beats 7.73%(2 ms for 201 tests)
    public int maxProfit(int[] prices) {
        if (prices.length == 0) return 0;

        int profit = 0;
        int low = prices[0];
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < prices[i - 1]) {
                profit += prices[i - 1] - low;
                low = prices[i];
            }
        }
        return profit + prices[prices.length - 1] - low;
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

    // beats 99.96%(1 ms for 201 tests)
    public int maxProfit3(int[] prices) {
        int n = prices.length;
        if (n == 0) return 0;

        int res = 0;
        for (int i = 1, min = prices[0]; i < n; ) {
            for (; i < n && prices[i] <= prices[i - 1]; i++) {
                min = prices[i]; 
            }
            int max = min;
            for (; i < n && prices[i] >= prices[i - 1]; i++) {
                max = prices[i]; 
            }
            res += max - min;
        }
        return res;
    }

    void test(Function<int[], Integer> maxProfit, int expected, int ... prices) {
        assertEquals(expected, (int)maxProfit.apply(prices));
    }

    void test(int expected, int ... prices) {
        BestTransaction2 b = new BestTransaction2();
        test(b::maxProfit, expected, prices);
        test(b::maxProfit2, expected, prices);
        test(b::maxProfit3, expected, prices);
    }

    @Test
    public void test1() {
        test(13, 20, 10, 9, 2, 8, 15);
        test(9, 20, 10, 9, 5, 8, 6, 12);
        test(0, 20, 10, 9, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
