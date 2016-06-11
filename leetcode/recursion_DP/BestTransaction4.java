import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Say you have an array for which the ith element is the price of a given stock
// on day i. If you were only permitted to complete at most k transactions,
// design an algorithm to find the maximum profit.
public class BestTransaction4 {
    // beats 60.27%
    public int maxProfit(int k, int[] prices) {
        // if (k >= prices.length / 2) { // beat rate will drop to 40.92
        if (k >= prices.length) {
            int profit = 0;
            for (int i = 1; i < prices.length; i++) {
                int diff = prices[i] - prices[i - 1];
                if (diff > 0) {
                    profit += diff;
                }
            }
            return profit;
        }

        int[] local = new int[k + 1];
        int[] global = new int[k + 1];
        for (int i = 0; i < prices.length - 1; i++) {
            int diff = prices[i + 1] - prices[i];
            for (int j = k; j > 0; j--) {
                local[j] = Math.max(global[j - 1], local[j]) + diff;
                global[j] = Math.max(local[j], global[j]);
            }
        }
        return global[k];
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, int[], Integer> maxProfit, int expected,
              int k, int ... prices) {
        assertEquals(expected, (int)maxProfit.apply(k, prices));
    }

    void test(int expected, int k, int ... prices) {
        BestTransaction4 b = new BestTransaction4();
        test(b::maxProfit, expected, k, prices);
    }

    @Test
    public void test1() {
        test(13, 2, 20, 10, 9, 2, 8, 15);
        test(0, 2, 20, 10, 9, 2);
        test(7, 2, 9, 2, 6, 3, 5, 4, 6, 3);
        test(23, 2, 9, 3, 6, 7, 8, 2, 0, 3, 8, 7, 5, 9, 10, 12, 8, 5, 4, 8, 10,
             13, 15, 10, 8, 2, 3, 9);
        test(30, 3, 9, 3, 6, 7, 8, 2, 0, 3, 8, 7, 5, 9, 10, 12, 8, 5, 4, 8, 10,
             13, 15, 10, 8, 2, 3, 9);
        test(35, 4, 9, 3, 6, 7, 8, 2, 0, 3, 8, 7, 5, 9, 10, 12, 8, 5, 4, 8, 10,
             13, 15, 10, 8, 2, 3, 9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BestTransaction4");
    }
}
