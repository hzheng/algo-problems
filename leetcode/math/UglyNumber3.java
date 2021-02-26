import org.junit.Test;

import static org.junit.Assert.*;

// LC1201: https://leetcode.com/problems/ugly-number-iii/
//
// Given four integers n, a, b, and c, return the nth ugly number.
// Ugly numbers are positive integers that are divisible by a, b, or c.
//
// Constraints:
//
// 1 <= n, a, b, c <= 10^9
// 1 <= a * b * c <= 10^18
//It is guaranteed that the result will be in range [1, 2 * 10^9].
public class UglyNumber3 {
    // Binary Search
    // time complexity: O(log(MAX)), space complexity: O(1)
    // 0 ms(100%), 35.6 MB(86.87%) for 51 tests
    public int nthUglyNumber(int n, int a, int b, int c) {
        long ab = lcm(a, b);
        long bc = lcm(b, c);
        long ca = lcm(c, a);
        long abc = lcm(ab, c);
        int low = 1;
        for (int high = Integer.MAX_VALUE; low < high; ) {
            int mid = (low + high) >>> 1;
            long count = mid / a + mid / b + mid / c - mid / ab - mid / bc - mid / ca + mid / abc;
            if (count < n) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    private long gcd(long a, long b) {
        if (a > b) { return gcd(b, a); }

        return a == 0 ? b : gcd(b % a, a);
    }

    private void test(int n, int a, int b, int c, int expected) {
        assertEquals(expected, nthUglyNumber(n, a, b, c));
    }

    @Test public void test() {
        test(3, 2, 3, 5, 4);
        test(4, 2, 3, 4, 6);
        test(5, 2, 11, 13, 10);
        test(1000000000, 2, 217983653, 336916467, 1999999984);
        test(26, 14, 25, 14, 238);
        test(16, 7, 1, 7, 16);
        test(16, 7, 2, 7, 28);
        test(16, 2, 7, 7, 28);
        test(7, 7, 7, 7, 49);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
