import org.junit.Test;

import static org.junit.Assert.*;

// LC1952: https://leetcode.com/problems/three-divisors/
//
// Given an integer n, return true if n has exactly 3 positive divisors. Otherwise, return false.
// An integer m is a divisor of n if there exists an integer k such that n = k * m.
//
// Constraints:
// 1 <= n <= 10^4
public class ThreeDivisors {
    // time complexity: O(N^0.5), space complexity: O(1)
    // 0 ms(100.00%), 35.6 MB(94.36%) for 228 tests
    public boolean isThree(int n) {
        for (int i = 2; i * i <= n; i++) {
            if ((n % i) == 0) {
                return i == n / i;
            }
        }
        return false;
    }

    // time complexity: O(N^0.25), space complexity: O(1)
    // 0 ms(100.00%), 35.6 MB(94.36%) for 228 tests
    public boolean isThree2(int n) {
        int sqrt = (int)Math.sqrt(n);
        return (n != 1) && (sqrt * sqrt == n) && isPrime(sqrt);
    }

    private boolean isPrime(int n) {
        if (n == 1) { return false; }

        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) { return false; }
        }
        return true;
    }

    private void test(int n, boolean expected) {
        assertEquals(expected, isThree(n));
        assertEquals(expected, isThree2(n));
    }

    @Test public void test1() {
        test(2, false);
        test(4, true);
        test(9, true);
        test(36, false);
        test(81, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
