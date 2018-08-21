import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC123: https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/
//
// Say you have an array for which the ith element is the price of a given stock
// on day i. If you were only permitted to complete at most two transactions,
// design an algorithm to find the maximum profit.
public class BestTransaction3 {
    // beats 37.83%(5 ms)
    public int maxProfit(int[] prices) {
        int n = prices.length;
        if (n == 0) return 0;

        int[] leftProfits = new int[n];
        int min = prices[0];
        for (int i = 1; i < n; i++) {
            int price = prices[i];
            min = price < min ? price : min;
            leftProfits[i] = Math.max(leftProfits[i - 1], price - min);
        }
        int[] rightProfits = new int[n];
        int max = prices[n - 1];
        for (int i = n - 2; i > 0; i--) {
            int price = prices[i];
            max = price > max ? price : max;
            rightProfits[i] = Math.max(rightProfits[i + 1], max - price);
        }
        int maxProfit = leftProfits[n - 1];
        for (int i = 1; i < n - 1; i++) {
            maxProfit = Math.max(maxProfit, leftProfits[i] + rightProfits[i + 1]);
        }
        return maxProfit;
    }

    // one less for-loop
    // beats 13.02%(7 ms)
    public int maxProfit2(int[] prices) {
        int n = prices.length;
        if (n == 0) return 0;

        int[] leftProfits = new int[n];
        int min = prices[0];
        for (int i = 1; i < n; i++) {
            int price = prices[i];
            min = price < min ? price : min;
            leftProfits[i] = Math.max(leftProfits[i - 1], price - min);
        }
        int[] rightProfits = new int[n];
        int max = prices[n - 1];
        int maxProfit = leftProfits[n - 1];
        for (int i = n - 2; i > 0; i--) {
            int price = prices[i];
            max = price > max ? price : max;
            rightProfits[i] = Math.max(rightProfits[i + 1], max - price);
            maxProfit = Math.max(maxProfit, leftProfits[i] + rightProfits[i + 1]);
        }
        return maxProfit;
    }

    // one less array
    // beats 13.02%(7 ms)
    public int maxProfit3(int[] prices) {
        int n = prices.length;
        if (n == 0) return 0;

        int[] profits = new int[n];
        int min = prices[0];
        for (int i = 1; i < n; i++) {
            int price = prices[i];
            min = price < min ? price : min;
            profits[i] = Math.max(profits[i - 1], price - min);
        }
        int maxProfit = profits[n - 1];
        int max = Integer.MIN_VALUE;
        int sum = max;
        for (int i = n - 2; i > 0; i--) {
            max = Math.max(max, prices[i + 1]);
            sum = Math.max(sum, max - prices[i]);
            if (sum > 0) {
                profits[i] += sum;
                maxProfit = Math.max(maxProfit, profits[i]);
            }
        }
        return maxProfit;
    }

    // Solution of Choice
    /// http://blog.csdn.net/linhuanmars/article/details/23236995
    // can be generalized to k > 2 (generalized Kadane's algorithm)
    // beats 69.45%(2 ms)
    public int maxProfit4(int[] prices) {
        final int k = 2;
        int[] local = new int[k + 1];
        int[] global = new int[k + 1];
        for (int i = 1; i < prices.length; i++) {
            int diff = prices[i] - prices[i - 1];
            for (int j = k; j > 0; j--) {
                // local[i][j]=max(global[i-1][j-1]+max(diff,0),local[i-1][j]+diff)
                local[j] = Math.max(global[j - 1], local[j] + diff);
                // global[i][j]=max(local[i][j],global[i-1][j])
                global[j] = Math.max(local[j], global[j]);
            }
        }
        return global[k];
    }

    // Solution of Choice
    // 47.10%(4 ms)
    public int maxProfit5(int[] prices) {
        int buy1 = Integer.MIN_VALUE;
        int buy2 = Integer.MIN_VALUE;
        int sell1 = 0;
        int sell2 = 0;
        for (int price : prices) {
            buy1 = Math.max(buy1, -price);
            sell1 = Math.max(sell1, buy1 + price);
            buy2 = Math.max(buy2, sell1 - price);
            sell2 = Math.max(sell2, buy2 + price);
        }
        return sell2;
    }

    // Solution of Choice
    // beats 33.19%(5 ms)
    public int maxProfit6(int[] prices) {
        int buy1 = Integer.MAX_VALUE;
        int buy2 = Integer.MAX_VALUE;
        int profit1 = 0;
        int profit2 = 0;
        for (int price : prices) {
            buy1 = Math.min(buy1, price);
            profit1 = Math.max(profit1, price - buy1);
            buy2 = Math.min(buy2, price - profit1);
            profit2 = Math.max(profit2, price - buy2);
        }
        return profit2;
    }

    void test(Function<int[], Integer> maxProfit, int expected, int ... prices) {
        assertEquals(expected, (int)maxProfit.apply(prices));
    }

    void test(int expected, int ... prices) {
        BestTransaction3 b = new BestTransaction3();
        test(b::maxProfit, expected, prices);
        test(b::maxProfit2, expected, prices);
        test(b::maxProfit3, expected, prices);
        test(b::maxProfit4, expected, prices);
        test(b::maxProfit5, expected, prices);
        test(b::maxProfit6, expected, prices);
    }

    @Test
    public void test1() {
        test(13, 20, 10, 9, 2, 8, 15);
        test(0, 20, 10, 9, 2);
        test(7, 9, 2, 6, 3, 5, 4, 6, 3);
        test(23, 9, 3, 6, 7, 8, 2, 0, 3, 8, 7, 5, 9, 10, 12, 8, 5, 4, 8, 10,
             13, 15, 10, 8, 2, 3, 9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BestTransaction3");
    }
}
