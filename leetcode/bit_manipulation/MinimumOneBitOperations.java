import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1611: https://leetcode.com/problems/minimum-one-bit-operations-to-make-integers-zero/
//
// Given an integer n, you must transform it into 0 using the following operations any number of
// times:
// Change the rightmost (0th) bit in the binary representation of n.
// Change the ith bit in the binary representation of n if the (i-1)th bit is set to 1 and the
// (i-2)th through 0th bits are set to 0.
// Return the minimum number of operations to transform n into 0.
//
// Constraints:
// 0 <= n <= 10^9
public class MinimumOneBitOperations {
    // BFS + Queue
    // Time Limit Exceeded
    public int minimumOneBitOperations0(int n) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(n);
        Set<Integer> visited = new HashSet<>();
        for (int level = 0; ; level++) {
            for (int size = queue.size(); size > 0; size--) {
                int cur = queue.poll();
                if (cur == 0) { return level; }
                if (!visited.add(cur)) { continue; }

                queue.offer(cur ^ 1);
                int mask = (cur & (-cur)) << 1;
                if (Integer.highestOneBit(cur) >= mask) { // optimize
                    queue.offer(cur ^ mask);
                }
            }
        }
    }

    // Bit Manipulation + Recursion
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100%), 36.1 MB(24.44%) for 308 tests
    public int minimumOneBitOperations(int n) {
        if (n <= 1) { return n; }

        int hBit = Integer.highestOneBit(n);
        return ((hBit << 1) - 1) - minimumOneBitOperations5(n - hBit);
    }

    // Bit Manipulation + Recursion
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100%), 35.8 MB(55.56%) for 308 tests
    public int minimumOneBitOperations2(int n) {
        if (n == 0) { return 0; }

        int hBit = Integer.highestOneBit(n);
        // f(a, b) = f(a^x, b^x), where f is the min operations from a to b
        // letting x = b, we have f(a, b) = f(a^b, 0)
        // f(1..., 110..0) = f(1... ^ 110...0, 0)
        // f(110...0, 10...0) = 1
        // f(10...0, 0) = hBit - 1
        return minimumOneBitOperations2((hBit >> 1) ^ hBit ^ n) + hBit;
    }

    // Bit Manipulation + Recursion
    // time complexity: O(log(N)), space complexity: O(1) // tail recursion
    // 0 ms(100%), 35.6 MB(86.67%) for 308 tests
    public int minimumOneBitOperations3(int n) {
        return minOperations(n, 0);
    }

    private int minOperations(int n, int res) {
        if (n == 0) { return res; }

        int hBit = Integer.highestOneBit(n);
        return minOperations((hBit >> 1) ^ hBit ^ n, res + hBit);
    }

    // Bit Manipulation
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100%), 35.8 MB(63.33%) for 308 tests
    public int minimumOneBitOperations4(int n) {
        int res = 0;
        for (int sign = 1, x = n; x > 0; x &= x - 1, sign *= -1) {
            res += x ^ (x - 1) * sign;
        }
        return Math.abs(res);
    }

    // Bit Manipulation
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100%), 36 MB(40.00%) for 308 tests
    public int minimumOneBitOperations5(int n) {
        int res = n;
        for (int x = n; (x >>= 1) > 0; res ^= x) {}
        return res;
    }

    // https://en.wikipedia.org/wiki/Gray_code
    // Bit Manipulation
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100%), 35.7 MB(75.56%) for 308 tests
    public int minimumOneBitOperations6(int n) {
        int res = 0;
        for (int x = n; x > 0; res ^= x, x >>= 1) {}
        return res;
    }

    // https://oeis.org/A006068 (gray code)
    // Bit Manipulation + Recursion
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100%), 35.8 MB(63.33%) for 308 tests
    public int minimumOneBitOperations7(int n) {
        if (n <= 1) { return n; }

        int m = minimumOneBitOperations7(n >> 1);
        return (m << 1) + ((n + m) & 1);
    }

    private void test(int n, int expected) {
        if (n < 500000) {
            assertEquals(expected, minimumOneBitOperations0(n));
        }
        assertEquals(expected, minimumOneBitOperations(n));
        assertEquals(expected, minimumOneBitOperations2(n));
        assertEquals(expected, minimumOneBitOperations3(n));
        assertEquals(expected, minimumOneBitOperations4(n));
        assertEquals(expected, minimumOneBitOperations5(n));
        assertEquals(expected, minimumOneBitOperations6(n));
        assertEquals(expected, minimumOneBitOperations7(n));
    }

    @Test public void test1() {
        test(0, 0);
        test(3, 2);
        test(6, 4);
        test(9, 14);
        test(333, 393);
        test(343221, 403673);
        test(422349, 286345);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
