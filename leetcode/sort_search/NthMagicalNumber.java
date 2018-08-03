import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC878: https://leetcode.com/problems/nth-magical-number/
//
// A positive integer is magical if it is divisible by either A or B. Return the
// N-th magical number by modulo 10^9 + 7.
public class NthMagicalNumber {
    // Binary Search
    // time complexity: O(Log(N * max(A,B)), space complexity: O(1)
    // beats 100%(3 ms for 66 tests)
    public int nthMagicalNumber(int N, int A, int B) {
        final int MOD = (int) 1e9 + 7;
        int factor = gcd(A, B);
        int lcm = A / factor * B;
        long low = 1;
        for (long high = Long.MAX_VALUE; low < high; ) {
            long mid = (low + high) >>> 1;
            if (mid / A + mid / B - mid / lcm < N) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return (int) (low % MOD);
    }

    private int gcd(int a, int b) {
        return (a == 0) ? b : gcd(b % a, a);
    }

    // Math
    // time complexity: O(A + B), space complexity: O(1)
    // beats 51.59%(5 ms for 66 tests)
    public int nthMagicalNumber2(int N, int A, int B) {
        final int MOD = 1_000_000_007;
        int lcm = A / gcd(A, B) * B;
        int period = lcm / A + lcm / B - 1; // magical numbers <= lcm
        long res = (long) (N / period) * lcm;
        int r = N % period;
        if (r > 0) {
            int a = A;
            int b = B;
            for (; r > 1; r--) {
                if (a <= b) {
                    a += A;
                } else {
                    b += B;
                }
            }
            res += Math.min(a, b);
        }
        return (int) (res % MOD);
    }

    // Math
    // time complexity: O(Log(min(a, b))), space complexity: O(1) 
    // beats 100%(3 ms for 66 tests)
    public int nthMagicalNumber3(int N, int A, int B) {
        final int MOD = 1_000_000_007;
        int lcm = A / gcd(A, B) * B;
        long period = lcm / A + lcm / B - 1; // magical numbers <= lcm
        double nearest = N % period / (1. / A + 1. / B);
        long res = (long) (N / period) * lcm;
        res += (int)Math.min(Math.ceil(nearest / A) * A, 
                             Math.ceil(nearest / B) * B);
        return (int) (res % MOD);
    }

    void test(int N, int A, int B, int expected) {
        assertEquals(expected, nthMagicalNumber(N, A, B));
        assertEquals(expected, nthMagicalNumber2(N, A, B));
        assertEquals(expected, nthMagicalNumber3(N, A, B));
    }

    @Test
    public void test() {
        test(1, 2, 3, 2);
        test(4, 2, 3, 6);
        test(3, 6, 4, 8);
        test(5, 2, 4, 10);
        test(589, 211, 400, 81446);
        test(9589, 7211, 9400, 39132200);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
