import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1137: https://leetcode.com/problems/n-th-tribonacci-number/
//
// The Tribonacci sequence Tn is defined as follows:
// T0 = 0, T1 = 1, T2 = 1, and Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.
// Given n, return the value of Tn.
//
// Constraints:
// 0 <= n <= 37
// The answer is guaranteed to fit within a 32-bit integer, ie. answer <= 2^31 - 1.
public class Tribonacci {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 37.7 MB(5.60%) for 39 tests
    public int tribonacci(int n) {
        if (n <= 1) { return n; }

        int a = 0;
        int b = 1;
        int c = 1;
        for (int i = n - 2; i > 0; i--) {
            int sum = a + b + c;
            a = b;
            b = c;
            c = sum;
        }
        return c;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 35.9 MB(42.50%) for 39 tests
    public int tribonacci2(int n) {
        int[] dp = new int[] {0, 1, 1};
        for (int i = 3; i <= n; i++) {
            dp[i % 3] = dp[0] + dp[1] + dp[2];
        }
        return dp[n % 3];
    }

    // TODO: matrix log(N) method

    private void test(int n, int expected) {
        assertEquals(expected, tribonacci(n));
        assertEquals(expected, tribonacci2(n));
    }

    @Test public void test() {
        test(2, 1);
        test(4, 4);
        test(25, 1389537);
        test(37, 2082876103);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
