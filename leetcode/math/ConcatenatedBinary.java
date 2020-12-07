import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1680: https://leetcode.com/problems/concatenation-of-consecutive-binary-numbers/
//
// Given an integer n, return the decimal value of the binary string formed by concatenating the
// binary representations of 1 to n in order, modulo 10^9 + 7.
//
// Constraints:
// 1 <= n <= 10^5
public class ConcatenatedBinary {
    private static final int MOD = 1_000_000_000 + 7;

    // time complexity: O(N), space complexity: O(1)
    // 81 ms(20.00%), 37.9 MB(40.00%) for 403 tests
    public int concatenatedBinary(int n) {
        int bits = Integer.SIZE - Integer.numberOfLeadingZeros(n);
        long res = 0;
        for (int b = 1; b < bits; b++) {
            for (int i = 1 << (b - 1), end = (1 << b); i < end; i++) {
                res = ((res << b) | i) % MOD;
            }
        }
        for (int i = 1 << (bits - 1); i <= n; i++) {
            res = ((res << bits) | i) % MOD;
        }
        return (int)(res);
    }

    // time complexity: O(N), space complexity: O(1)
    // 88 ms(20.00%), 38 MB(40.00%) for 403 tests
    public int concatenatedBinary2(int n) {
        long res = 0;
        for (int i = 1; i <= n; i++) {
            int bits = Integer.SIZE - Integer.numberOfLeadingZeros(i);
            res = ((res << bits) | i) % MOD;
        }
        return (int)(res);
    }

    // time complexity: O(N), space complexity: O(1)
    // 81 ms(20.00%), 35.5 MB(100.00%) for 403 tests
    public int concatenatedBinary3(int n) {
        long res = 0;
        for (int i = 1, bits = 0; i <= n; i++) {
            if ((i & (i - 1)) == 0) {
                bits++;
            }
            res = ((res << bits) | i) % MOD;
        }
        return (int)res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 84 ms(20.00%), 37.6 MB(40.00%) for 403 tests
    public int concatenatedBinary4(int n) {
        long res = 0;
        for (int i = 1, bits = 0, power = 1; i <= n; i++) {
            if (i == power) {
                power <<= 1;
                bits++;
            }
            res = ((res << bits) | i) % MOD;
        }
        return (int)res;
    }

    private void test(int n, int expected) {
        assertEquals(expected, concatenatedBinary(n));
        assertEquals(expected, concatenatedBinary2(n));
        assertEquals(expected, concatenatedBinary3(n));
        assertEquals(expected, concatenatedBinary4(n));
    }

    @Test public void test() {
        test(1, 1);
        test(2, 6);
        test(3, 27);
        test(4, 220);
        test(5, 1765);
        test(12, 505379714);
        test(42, 727837408);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
