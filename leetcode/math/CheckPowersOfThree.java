import org.junit.Test;

import static org.junit.Assert.*;

// LC1780: https://leetcode.com/problems/check-if-number-is-a-sum-of-powers-of-three/
//
// Given an integer n, return true if it is possible to represent n as the sum of distinct powers of
// three. Otherwise, return false.
// An integer y is a power of three if there exists an integer x such that y == 3^x.
//
// Constraints:
// 1 <= n <= 10^7
public class CheckPowersOfThree {
    // time complexity: O(N), space complexity: O(1)
    // 44 ms(66.67%), 35.5 MB(100.00%) for 120 tests
    public boolean checkPowersOfThree(int n) {
        int p = (int)(Math.log(n) / Math.log(3)) + 1;
        for (int mask = (1 << p) - 1; mask > 0; mask--) {
            int num = 0;
            for (int i = 0, power3 = 1; i < p; power3 *= 3, i++) {
                if (((mask >> i) & 1) != 0) {
                    num += power3;
                }
            }
            if (num == n) { return true; }
        }
        return false;
    }

    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(66.67%) for 120 tests
    public boolean checkPowersOfThree2(int n) {
        for (int a = n; a > 0; a /= 3) {
            if (a % 3 == 2) { return false; }
        }
        return true;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(log(N))
    // 4 ms(66.67%), 35.8 MB(66.67%) for 120 tests
    public boolean checkPowersOfThree3(int n) {
        return checkPowersOfThree(n,  1);
    }

    private boolean checkPowersOfThree(int n, int p) {
        if (n == 0) { return true; }
        if (p > n) { return false; }
        return checkPowersOfThree(n, p * 3) || checkPowersOfThree(n - p, p * 3);
    }

    private void test(int n, boolean expected) {
        assertEquals(expected, checkPowersOfThree(n));
        assertEquals(expected, checkPowersOfThree2(n));
        assertEquals(expected, checkPowersOfThree3(n));
    }

    @Test public void test() {
        test(12, true);
        test(91, true);
        test(21, false);
        test(31, true);
        test(932, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
