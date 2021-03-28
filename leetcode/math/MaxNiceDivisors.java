import org.junit.Test;

import static org.junit.Assert.*;

// LC1808: https://leetcode.com/problems/maximize-number-of-nice-divisors/
//
// You are given a positive integer primeFactors. You are asked to construct a positive integer n
// that satisfies the following conditions:
// The number of prime factors of n (not necessarily distinct) is at most primeFactors.
// The number of nice divisors of n is maximized. Note that a divisor of n is nice if it is
// divisible by every prime factor of n. For example, if n = 12, then its prime factors are [2,2,3],
// then 6 and 12 are nice divisors, while 3 and 4 are not. Return the number of nice divisors of n.
// Since that number can be too large, return it modulo 10^9 + 7.
// The prime factors of a number n is a list of prime numbers such that their product equals n.
//
// Constraints:
// 1 <= primeFactors <= 10^9
public class MaxNiceDivisors {
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 35.4 MB(100.00%) for 213 tests
    private static final int MOD = 1_000_000_007;

    public int maxNiceDivisors(int primeFactors) {
        if (primeFactors < 3) { return primeFactors; }

        int multiple3 = primeFactors / 3;
        int mod3 = primeFactors % 3;
        if (mod3 == 1) {
            multiple3--;
        }
        long res = power(3, multiple3);
        if (mod3 == 1) {
            res <<= 2;
        } else if (mod3 == 2) {
            res <<= 1;
        }
        return (int)(res % MOD);
    }

    private int power(int x, int y) {
        long res = 1;
        for (long base = x, exp = y; exp > 0; exp >>= 1, base = (base * base) % MOD) {
            if ((exp & 1) == 1) {
                res = (res * base) % MOD;
            }
        }
        return (int)res;
    }

    public int maxNiceDivisors2(int primeFactors) {
        if (primeFactors < 4) { return primeFactors; }

        switch (primeFactors % 3) {
        case 0:
            return power(3, primeFactors / 3);
        case 1:
            return (int)(power(3, (primeFactors - 4) / 3) * 4L % MOD);
        default:
            return (int)(power(3, primeFactors / 3) * 2L % MOD);
        }
    }

    private void test(int primeFactors, int expected) {
        assertEquals(expected, maxNiceDivisors(primeFactors));
        assertEquals(expected, maxNiceDivisors2(primeFactors));
    }

    @Test public void test() {
        test(5, 6);
        test(8, 18);
        test(88, 169179295);
        test(98, 351761402);
        test(351761402, 191515467);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
