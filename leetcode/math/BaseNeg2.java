import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1017: https://leetcode.com/problems/convert-to-base-2
//
// Given a number N, return a string consisting of "0"s and "1"s that represents its value in
// base -2 (negative two).
// The returned string must have no leading zeroes, unless the string is "0".
// Note:
// 0 <= N <= 10^9
public class BaseNeg2 {
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 1 ms(97.36%), 33.5 MB(100%) for 170 tests
    public String baseNeg2(int N) {
        String res = "";
        for (int n = N; n != 0; n = -(n >> 1)) {
            res = (n & 1) + res;
        }
        return (res == "") ? "0" : res;
    }

    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100.00%), 33.5 MB(100%) for 170 tests
    public String baseNeg2_2(int N) {
        int upperBound01 = 1;
        for (; upperBound01 < N; upperBound01 = (upperBound01 << 2) + 1) {}
        return Integer.toBinaryString(upperBound01 ^ (upperBound01 - N));
    }

    // time complexity: O(log(N)), space complexity: O(log(N))
    // 0 ms(100.00%), 33.5 MB(100%) for 170 tests
    public String baseNeg2_3(int N) {
        int upperBound01 = 0b1010101010101010101010101010101;
        return Integer.toBinaryString(upperBound01 ^ (upperBound01 - N));
    }

    // Recursion
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 1 ms(97.36%), 33.6 MB(100%) for 170 tests
    public String baseNeg2_4(int N) {
        return (N == 0 || N == 1) ? Integer.toString(N) : baseNeg2_4(-(N >> 1)) + (N & 1);
    }

    void test(int N, String expected) {
        assertEquals(expected, baseNeg2(N));
        assertEquals(expected, baseNeg2_2(N));
        assertEquals(expected, baseNeg2_3(N));
        assertEquals(expected, baseNeg2_4(N));
    }

    @Test
    public void test() {
        test(0, "0");
        test(1, "1");
        test(2, "110");
        test(3, "111");
        test(4, "100");
        test(16, "10000");
        test(100, "110100100");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
