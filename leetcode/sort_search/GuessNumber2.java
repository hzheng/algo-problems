import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// https://leetcode.com/problems/guess-number-higher-or-lower-ii/
//
// I pick a number from 1 to n. You have to guess which number I picked.
// Every time you guess wrong, I'll tell you whether the number I picked is
// higher or lower. However, when you guess a particular number x, and you guess
// wrong, you pay $x. You win the game when you guess the number I picked.
// Given a particular n ≥ 1, find out how much money you need to have to guarantee a win.
public class GuessNumber2 {
    // Memoization
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 0%(816 ms)
    public int getMoneyAmount(int n) {
        return getMoneyAmount(1, n, new HashMap<>());
    }

    private int getMoneyAmount(int start, int end, Map<Long, Integer> memo) {
        if (start >= end) return 0;
        if (start + 1 == end) return start;

        long key = ((long)start << 32) | end;
        if (memo.containsKey(key)) return memo.get(key);

        int min = Integer.MAX_VALUE;
        for (int i = start; i <= end; i++) {
            min = Math.min(min, i + Math.max(getMoneyAmount(start, i - 1, memo),
                                             getMoneyAmount(i + 1, end, memo)));
        }
        memo.put(key, min);
        return min;
    }

    // beats 35.91%(19 ms)
    public int getMoneyAmount2(int n) {
        return getMoneyAmount2(1, n, new int[n + 1][n + 1]);
    }

    private int getMoneyAmount2(int start, int end, int[][] memo) {
        if (start >= end) return 0;
        if (start + 1 == end) return start;

        if (memo[start][end] > 0) return memo[start][end];

        int min = Integer.MAX_VALUE;
        for (int i = end - 1; i >= start; i--) {
            min = Math.min(min, i + Math.max(getMoneyAmount2(start, i - 1, memo),
                                             getMoneyAmount2(i + 1, end, memo)));
        }
        return memo[start][end] = min;
    }

    public int getMoneyAmount3(int n) {
        if (n < 2) return 0;
        if (n == 2) return 1;

        int[][] dp = new int[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; i <= n; j++) {
                int money = Integer.MAX_VALUE;


            }
        }
        for (int[] a:dp) System.out.println(Arrays.toString(a));

        return dp[1][n];
    }

    // for (int[] a:dp) System.out.println(Arrays.toString(a));
    void test(Function<Integer, Integer> getMoneyAmount, String name,
              int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)getMoneyAmount.apply(n));
        if (n >= 100) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int n, int expected) {
        GuessNumber2 g = new GuessNumber2();
        if (n <= 200) {
            test(g::getMoneyAmount, "getMoneyAmount", n, expected);
        }
        test(g::getMoneyAmount2, "getMoneyAmount2", n, expected);
    }

    @Test
    public void test1() {
        test(20, 49);
        test(0, 0);
        test(1, 0);
        test(2, 1);
        test(3, 2);
        test(4, 4);
        test(5, 6);
        test(6, 8);
        test(7, 10);
        test(8, 12);
        test(9, 14);
        test(10, 16);
        test(50, 172);
        test(100, 400);
        test(200, 952);
        test(500, 2933);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GuessNumber2");
    }
}
