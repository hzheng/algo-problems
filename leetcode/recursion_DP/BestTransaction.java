import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Say you have an array for which the ith element is the price of a given stock
// on day i. If you were only permitted to complete at most one transaction,
// design an algorithm to find the maximum profit.
public class BestTransaction {
    // beats 51.18%
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

    // beats 94.51%
    public int maxProfit2(int[] prices) {
        int min = Integer.MAX_VALUE;
        int profit = 0;
        for (int price : prices) {
            min = price < min ? price : min;
            profit = Math.max(profit, price - min);
        }
        return profit;
    }

    void test(Function<int[], Integer> maxProfit, int expected, int ... prices) {
        assertEquals(expected, (int)maxProfit.apply(prices));
    }

    void test(int expected, int ... prices) {
        BestTransaction b = new BestTransaction();
        test(b::maxProfit, expected, prices);
        test(b::maxProfit2, expected, prices);
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
