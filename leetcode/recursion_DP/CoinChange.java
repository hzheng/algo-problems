import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// You are given coins of different denominations and a total amount
// of money amount. Write a function to compute the fewest number of coins that
// you need to make up that amount. If that amount of money cannot be made up
// by any combination of the coins, return -1.
public class CoinChange {
    //  Time Limit Exceeded
    public int coinChange(int[] coins, int amount) {
        int n = coins.length;
        if (n == 0 || amount < 0) return -1;

        if (amount == 0) return 0;

        Arrays.sort(coins);
        return coinChange(coins, n - 1, amount);
    }

    private int coinChange(int[] coins, int last, int amount) {
        int coin = coins[last];
        if (coin == 0) return -1;

        if (amount % coin == 0) return amount / coin;

        if (last == 0) return -1;

        int min = amount + 1;
        for (int i = amount / coin; i >= 0; i--) {
            int changes = coinChange(coins, last - 1, amount - coin * i);
            if (changes >= 0) {
                min = Math.min(min, i + changes);
            }
        }
        return min > amount ? -1 : min;
    }

    // Dynamic Programming
    // Time Limit Exceeded
    public int coinChange2(int[] coins, int amount) {
        int n = coins.length;
        if (n == 0 || amount < 0) return -1;

        if (amount == 0) return 0;

        Arrays.sort(coins);
        return coinChange2(coins, n - 1, amount, new HashMap<>());
    }

    private int coinChange2(int[] coins, int m, int amount,
                            Map<Long, Integer> memo) {
        int coin = coins[m];
        if (coin == 0) return -1;

        long key = ((long)m << 32) + amount;
        if (amount % coin == 0) {
            int res = amount / coin;
            memo.put(key, res);
            return res;
        }

        if (m == 0) return -1;

        if (memo.containsKey(key)) return memo.get(key);

        int min = Integer.MAX_VALUE;
        for (int i = amount / coin; i >= 0; i--) {
            int changes = coinChange2(coins, m - 1, amount - coin * i, memo);
            if (changes > 0) {
                min = Math.min(min, i + changes);
            }
        }
        if (min == Integer.MAX_VALUE) {
            min = -1;
        }
        memo.put(key, min);
        return min;
    }

    // Dynamic Programming
    // beats 3.60%(261 ms)
    public int coinChange3(int[] coins, int amount) {
        int n = coins.length;
        if (n == 0 || amount < 0) return -1;

        if (amount == 0) return 0;

        Arrays.sort(coins);
        int[] dp = new int[amount + 1];
        dp[amount] = -1;
        for (int i = 0; i < n; i++) {
            int coin = coins[i];
            if (coin == 0) continue;
            if (coin > amount) break;

            for (int money = 0; money <= amount; money++) {
                int numbers = dp[money];
                if (money > 0 && numbers == 0) continue;

                for (int j = 1;; j++) {
                    int m = money + coin * j;
                    if (m > amount) break;

                    dp[m] = Math.min(numbers + j, (dp[m] > 0) ? dp[m] : amount);
                }
            }
        }
        return dp[amount];
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, Integer> change, String name,
              int expected, int amount, int ... coins) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)change.apply(coins, amount));
        if (amount > 5000) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int expected, int amount, int ... coins) {
        CoinChange c = new CoinChange();
        coins = coins.clone();
        if (amount <= 1000) {
            test(c::coinChange, "coinChange", expected, amount, coins);
        }
        test(c::coinChange2, "coinChange2", expected, amount, coins);
        test(c::coinChange3, "coinChange3", expected, amount, coins);
    }

    @Test
    public void test1() {
        test(0, 0, 1);
        test(-1, 3, 2);
        test(-1, 2, 2147483647);
        test(3, 11, 1, 2, 5);
        test(7, 50, 2, 5, 11);
        test(11, 100, 2, 5, 11);
        test(14, 211, 2, 13, 17);
        test(31, 500, 2, 13, 17);
        test(60, 1000, 2, 13, 17);
        test(144, 1000, 2, 3, 5, 7);
        test(22, 2168, 261, 27, 78, 61);
        test(20, 6249, 186, 419, 83, 408);
        test(22, 5456, 261, 411, 27, 78, 61);
        test(17, 7066, 284, 260, 393, 494);
        test(29, 8402, 125, 146, 125, 252, 226, 25, 24, 308, 50);
        test(22, 9208, 288, 160, 10, 249, 40, 77, 314, 429);
        test(26, 9794, 77, 82, 84, 80, 398, 286, 40, 136, 162);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CoinChange");
    }
}
