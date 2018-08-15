import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC518: https://leetcode.com/problems/coin-change-2/
//
// You are given coins of different denominations and a total amount of money.
// Compute the number of combinations that make up that amount. Assume that you
// have infinite number of each kind of coin.
// Note:
// 0 <= amount <= 5000
// 1 <= coin <= 5000
// the number of coins is less than 500
// the answer is guaranteed to fit into signed 32-bit integer
public class CoinChange2 {
    // Dynamic Programming
    // beats 100.00%(3 ms for 27 tests)
    // time complexity: O(N * M), space complexity: O(N)
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        return dp[amount];
    }

    void test(int amount, int[] coins, int expected) {
        assertEquals(expected, change(amount, coins));
    }

    @Test
    public void test() {
        test(5, new int[] { 1, 2, 5 }, 4);
        test(3, new int[] { 2 }, 0);
        test(10, new int[] { 10 }, 1);
        test(13, new int[] { 1, 2, 5 }, 14);
        test(35, new int[] { 1, 2, 5, 10 }, 140);
        test(835, new int[] { 1, 2, 3, 5, 7, 10 }, 1750022012);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
