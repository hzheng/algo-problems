import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC322: https://leetcode.com/problems/coin-change/
//
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

    // Recursion + Dynamic Programming(Top-Down)
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

    // Sort + Dynamic Programming(Bottom-Up)
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

    // Sort + Dynamic Programming(Bottom-Up)
    // Time complexity : O(S * n). S is amount, n is denomination count.
    // Space complexity: O(S)
    // beats 32.76%(29 ms for 182 tests)
    public int coinChange4(int[] coins, int amount) {
        int n = coins.length;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        Arrays.sort(coins); // to support the following inner loop shortcut
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < n && coins[j] <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // Time complexity : O(S * n). S is amount, n is denomination count.
    // Space complexity: O(S)
    // beats 14.07%(46 ms for 182 tests)
    public int coinChange5(int[] coins, int amount) {
        return coinChange(coins, amount, new int[amount]);
    }

    private int coinChange(int[] coins, int amount, int[] count) {
        if (amount < 0) return -1;

        if (amount == 0) return 0;

        if (count[amount - 1] != 0) return count[amount - 1];

        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int res = coinChange(coins, amount - coin, count);
            if (res >= 0 && res < min) {
                min = res + 1;
            }
        }
        return count[amount - 1] = (min == Integer.MAX_VALUE) ? -1 : min;
    }

    // Recursion(brute force)
    // Time complexity : O(S ^ n). S is amount, n is denomination count.
    // Space complexity: O(n)
    // Time Limit Exceeded
    public int coinChange6(int[] coins, int amount) {
        return coinChange(0, coins, amount);
    }

    private int coinChange(int idxCoin, int[] coins, int amount) {
        if (amount == 0) return 0;

        if (idxCoin >= coins.length || amount <= 0) return -1;

        int maxVal = amount / coins[idxCoin];
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i <= maxVal; i++) {
            if (amount >= i * coins[idxCoin]) {
                int res = coinChange(idxCoin + 1, coins, amount - i * coins[idxCoin]);
                if (res != -1) {
                    minCost = Math.min(minCost, res + i);
                }
            }
        }
        return (minCost == Integer.MAX_VALUE) ? -1 : minCost;
    }

    // Solution of Choice
    // Dynamic Programming(Bottom-Up)
    // compared to coinChange4, loop coin first then amount
    // Time complexity : O(S * n). S is amount, n is denomination count.
    // Space complexity: O(S)
    // beats 94.93%(17 ms for 182 tests)
    public int coinChange7(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // BFS + Queue
    // beats 16.61%(41 ms for 182 tests)
    public int coinChange8(int[] coins, int amount) {
        if (amount == 0) return 0;

        boolean[] visited = new boolean[amount + 1];
        visited[0] = true;
        // although using Set makes runtime to 274ms, it's useful
        // when amount is too large for creating array
        // Set<Integer> visited = new HashSet<>();
        // visited.add(0);
        Queue<Integer> values = new LinkedList<>();
        values.offer(0);
        for (int level = 1; !values.isEmpty(); level++) {
            for (int i = values.size() - 1; i >= 0; i--) {
                int v = values.poll();
                for (int coin : coins) {
                    int val = v + coin;
                    if (val == amount) return level;

                    // if (val <= amount && !visited.contains(val)) {
                        // visited.add(val);
                    if (val <= amount && !visited[val]) {
                        visited[val] = true;
                        values.offer(val);
                    }
                }
            }
        }
        return -1;
    }

    // TODO: two-dimensional DP
    // https://en.wikipedia.org/wiki/Change-making_problem

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
        if (amount <= 1000) {
            test(c::coinChange, "coinChange", expected, amount, coins.clone());
            if (amount <= 100) {
                test(c::coinChange6, "coinChange6", expected, amount, coins.clone());
            }
        }
        test(c::coinChange2, "coinChange2", expected, amount, coins.clone());
        test(c::coinChange3, "coinChange3", expected, amount, coins.clone());
        test(c::coinChange4, "coinChange4", expected, amount, coins.clone());
        test(c::coinChange5, "coinChange5", expected, amount, coins.clone());
        test(c::coinChange7, "coinChange7", expected, amount, coins.clone());
        test(c::coinChange8, "coinChange8", expected, amount, coins.clone());
    }

    @Test
    public void test1() {
        test(0, 0, 1);
        test(-1, 3, 2);
        test(-1, 2, 2147483647);
        test(8, 264, 474, 83, 404, 3);
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
