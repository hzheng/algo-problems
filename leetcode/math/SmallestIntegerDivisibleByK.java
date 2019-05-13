import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC1015: https://leetcode.com/problems/smallest-integer-divisible-by-k/
//
// Given a positive integer K, you need find the smallest positive integer N such that N is
// divisible by K, and N only contains the digit 1.
// Return the length of N.  If there is no such N, return -1.
public class SmallestIntegerDivisibleByK {
    // time complexity: O(K), space complexity: O(1)
    // 1 ms(99.93%), 32.1 MB(100%) for 70 tests
    public int smallestRepunitDivByK(int K) {
        if (K % 2 == 0 || K % 5 == 0) {
            return -1;
        }

        for (int k = 1, x = 0; k <= K; k++) {
            x = (x * 10 + 1) % K;
            if (x == 0) {
                return k; // always come here by pigeon hole principle
            }
        }
        return -1; // unreachable
    }

    void test(int K, int expected) {
        assertEquals(expected, smallestRepunitDivByK(K));
    }

    @Test
    public void test() {
        test(1, 1);
        test(2, -1);
        test(3, 3);
        test(3, 3);
        test(10, -1);
        test(101, 4);
        test(105, -1);
        test(1019, 1018);
        test(10129, 1446);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
