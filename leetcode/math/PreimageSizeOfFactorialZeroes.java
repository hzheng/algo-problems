import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC754: https://leetcode.com/problems/reach-a-number/
//
// You are standing at position 0 on an infinite number line. There is a goal at position target.
// On each move, you can either go left or right. During the n-th move (starting from 1), you take
// n steps. Return the minimum number of steps required to reach the destination.
//
// Note:
// target will be a non-zero integer in the range [-10^9, 10^9].
public class PreimageSizeOfFactorialZeroes {
    // Math + Binary Search
    // time complexity: O(log(K)^2), space complexity: O(1)
    // 0 ms(100.00%), 36.1 MB(6.25%) for 44 tests
    public int preimageSizeFZF(int K) {
        // find greatest n s.t. 5^0+5^1+...+5^n <= K
        int n = (int)Math.floor(Math.log(4L * K + 1) / Math.log(5)) - 1;
        long start = (long)Math.pow(5, n);
        long end = 5L * (K + 1);
        for (long low = start, high = end; low < high; ) {
            long mid = (low + high) >>> 1;
            int k = trailingZeros(mid);
            if (k == K) { return 5; }

            if (k < K) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return 0;
    }

    // Binary Search
    // time complexity: O(log(K)^2), space complexity: O(1)
    // 0 ms(100.00%), 36.1 MB(6.25%) for 44 tests
    public int preimageSizeFZF2(int K) {
        return (int)(count(K) - count(K - 1));
    }

    private long count(int K) {
        long high = 5L * (K + 1);
        for (long low = 0; low <= high; ) {
            long mid = (low + high) >>> 1;
            if (trailingZeros(mid) <= K) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return high;
    }

    // Binary Search
    // time complexity: O(log(K)^2), space complexity: O(1)
    // 0 ms(100.00%), 36.1 MB(6.25%) for 44 tests
    public int preimageSizeFZF3(int K) {
        for (long low = 0, high = 5L * (K + 1); low <= high; ) {
            long mid = (low + high) >>> 1;
            long k = trailingZeros(mid);
            if (k < K) {
                low = mid + 1;
            } else if (k > K) {
                high = mid - 1;
            } else {
                return 5;
            }
        }
        return 0;
    }

    // Math
    // time complexity: O(log(K)), space complexity: O(1)
    // 0 ms(100.00%), 36.1 MB(6.25%) for 44 tests
    public int preimageSizeFZF4(int K) {
        // for any integer x = a0 * 5^0 + a1 * 5^1 + a2 * 5^2 + ... a(k) * 5^k (0<a(i)<5)
        // let f = trailingZeros
        // f(x) = a1 * 5^0 + a2 * (5^0 + 5^1) + a3 * (5^0 + 5^1 + 5^2) + ...
        //      = a1 * g(1) + a2 * g(2) + a3 * g(3) + ...
        Stack<Integer> g = new Stack<>();
        for (g.push(0); ; ) {
            long next = g.peek() * 5L + 1;
            if (next > Integer.MAX_VALUE) { break; }

            g.push((int)next);
        }
        for (int k = K, x; ; k %= x) {
            x = g.pop();
            if (x == 0) { return 5; } // top of stack
            if (k == 5 * x) { return 0; } // only [0,4] is valid
        }
    }

    // Math
    // time complexity: O(log(K)), space complexity: O(1)
    // 0 ms(100.00%), 36.1 MB(6.25%) for 44 tests
    public int preimageSizeFZF5(int K) {
        final int max = 305175781; // second-top of stack in above solution (less than 10^9)
        for (int f = max, k = K; f > 0; k %= f, f = (f - 1) / 5) {
            if (k == 5 * f) { return 0; }
        }
        return 5;
    }

    private int trailingZeros(long n) {
        int res = 0;
        for (long i = 5; i <= n; i *= 5) {
            res += n / i;
        }
        return res;
    }

    private void test(int K, int expected) {
        assertEquals(expected, preimageSizeFZF(K));
        assertEquals(expected, preimageSizeFZF2(K));
        assertEquals(expected, preimageSizeFZF3(K));
        assertEquals(expected, preimageSizeFZF4(K));
        assertEquals(expected, preimageSizeFZF5(K));
    }

    @Test public void test() {
        test(28, 5);
        test(29, 0);
        test(30, 0);
        test(31, 5);
        test(0, 5);
        test(1, 5);
        test(2, 5);
        test(3, 5);
        test(4, 5);
        test(5, 0);
        test(6, 5);
        test(7, 5);
        test(8, 5);
        test(9, 5);
        test(10, 5);
        test(11, 0);
        test(16, 5);
        test(17, 0);
        test(18, 5);
        test(22, 5);
        test(23, 0);
        test(24, 5);
        test(28, 5);
        test(29, 0);
        test(30, 0);
        test(31, 5);
        test(41, 5);
        test(42, 0);
        test(43, 5);
        test(47, 5);
        test(48, 0);
        test(49, 5);
        test(1000000000, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
