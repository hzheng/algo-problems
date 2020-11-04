import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC509: https://leetcode.com/problems/fibonacci-number/
//
// The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence, such
// that each number is the sum of the two preceding ones, starting from 0 and 1.
// Given N, calculate F(N).
// Note:
// 0 ≤ N ≤ 30.
public class Fibonacci {
    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 35.6 MB(11.58%) for 31 tests
    public int fib(int N) {
        int[] dp = new int[N + 2];
        dp[1] = 1;
        for (int i = 2; i <= N; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[N];
    }

    // 0-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(11.58%) for 31 tests
    public int fib2(int N) {
        if (N <= 0) { return 0; }

        int a = 0;
        int b = 1;
        for (int i = N; i > 1; i--) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    // Recursion
    // time complexity: O(2^(N/2)), space complexity: O(1)
    // 6 ms(32.31%), 35.8 MB(11.58%) for 31 tests
    public int fib3(int N) {
        return (N < 2) ? N : fib(N - 1) + fib(N - 2);
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36 MB(11.58%) for 31 tests
    public int fib4(int N) {
        return fib(N, new int[N + 1]);
    }

    private int fib(int N, int[] dp) {
        if (N < 2) { return N; }

        return (dp[N] != 0) ? dp[N] : (dp[N] = fib(N - 1, dp) + fib(N - 2, dp));
    }

    // Math (https://en.wikipedia.org/wiki/Fibonacci_number#Matrix_form)
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 36 MB(11.58%) for 31 tests
    public int fib5(int N) {
        if (N < 2) { return N; }

        int[][] F = new int[][] {{1, 1}, {1, 0}};
        int[][] I = new int[][] {{1, 0}, {0, 1}};
        for (int n = N - 1; n > 0; n >>= 1) {
            if ((n & 1) == 1) {
                I = multiply(I, F);
            }
            F = multiply(F, F);
        }
        return multiply(new int[][] {{1, 0}}, I)[0][0];
    }

    private int[][] multiply(int[][] A, int[][] B) {
        int[][] product = new int[A.length][B[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int k = 0; k < A[0].length; k++) {
                for (int j = 0; j < B[0].length; j++) {
                    product[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return product;
    }

    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.6 MB(11.58%) for 31 tests
    public int fib6(int N) {
        double sqrt5 = Math.sqrt(5);
        double phi = (sqrt5 + 1) / 2;
        return (int)(Math.pow(phi, N) / sqrt5 + 0.5);
    }

    private void test(int N, int expected) {
        assertEquals(expected, fib(N));
        assertEquals(expected, fib2(N));
        assertEquals(expected, fib3(N));
        assertEquals(expected, fib4(N));
        assertEquals(expected, fib5(N));
        assertEquals(expected, fib6(N));
    }

    @Test public void test() {
        test(0, 0);
        test(1, 1);
        test(2, 1);
        test(3, 2);
        test(4, 3);
        test(5, 5);
        test(6, 8);
        test(7, 13);
        test(8, 21);
        test(9, 34);
        test(10, 55);
        test(20, 6765);
        test(30, 832040);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
