import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/integer-break/
//
// Given a positive integer n, break it into the sum of at least two positive
// integers and maximize the product of those integers. Return the maximum
// product you can get.
// Note: You may assume that n is not less than 2 and not larger than 58.
public class IntegerBreak {
    // Recursion
    // Time Limit Exceeded
    public int integerBreak(int n) {
        if (n == 2) return 1;

        if (n == 3) return 2;

        return intBreak(n);
    }

    private int intBreak(int n) {
        if (n <= 4) return n;

        int max = 0;
        for (int i = 1; i <= n / 2; i++) {
            max = Math.max(max, intBreak(i) * intBreak(n - i));
        }
        return max;
    }

    // Recursion + Memoization
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 7.67%(3 ms)
    public int integerBreak2(int n) {
        if (n == 2) return 1;

        if (n == 3) return 2;

        return intBreak(n, new HashMap<Integer, Integer>());
    }

    private int intBreak(int n, Map<Integer, Integer> memo) {
        if (n <= 4) return n;

        if (memo.containsKey(n)) return memo.get(n);

        int max = 0;
        for (int i = 1; i <= n / 2; i++) {
            max = Math.max(max, intBreak(i, memo) * intBreak(n - i, memo));
        }
        memo.put(n, max);
        return max;
    }

    // Math
    // time complexity: O(N), space complexity: O(1)
    // beats 39.81%(0 ms)
    public int integerBreak3(int n) {
        if (n < 4) return n - 1;

        int max = 1;
        while (n > 4) {
            max *= 3;
            n -= 3;
        }
        return max * n;
    }

    // Math
    // time complexity: O(log(N)), space complexity: O(1)
    // beats 39.81%(0 ms)
    public int integerBreak4(int n) {
        if (n < 4) return n - 1;

        int times = (n - 2) / 3;
        return (int)Math.pow(3, times) * (n - times * 3);
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 28.65%(1 ms)
    public int integerBreak5(int n) {
        int[] dp = new int[n + 1];
        dp[2] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j * 2 <= i; j++) {
                dp[i] = Math.max(dp[i], Math.max(i - j, dp[i - j]) * j);
            }
        }
        return dp[n];
    }

    void test(Function<Integer, Integer> integerBreak, String name,
              int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)integerBreak.apply(n));
        if (n > 20) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int n, int expected) {
        IntegerBreak i = new IntegerBreak();
        if (n < 30) {
            test(i::integerBreak, "integerBreak", n, expected);
        }
        test(i::integerBreak2, "integerBreak2", n, expected);
        test(i::integerBreak3, "integerBreak3", n, expected);
        test(i::integerBreak4, "integerBreak4", n, expected);
        test(i::integerBreak5, "integerBreak5", n, expected);
    }

    @Test
    public void test1() {
        test(2, 1);
        test(3, 2);
        test(4, 4);
        test(5, 6);
        test(6, 9);
        test(7, 12);
        test(8, 18);
        test(9, 27);
        test(10, 36);
        test(15, 243);
        test(25, 8748);
        test(35, 354294);
        test(45, 14348907);
        test(55, 516560652);
        test(57, 1162261467);
        test(58, 1549681956);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IntegerBreak");
    }
}
