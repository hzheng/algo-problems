import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1009: https://leetcode.com/problems/complement-of-base-10-integer/
//
// Every non-negative integer N has a binary representation. Note that except for N = 0, there are
// no leading zeroes in any binary representation. The complement of a binary representation is the
// number in binary you get when changing every 1 to a 0 and 0 to a 1.
// For a given number N in base-10, return the complement of it's binary representation as a
// base-10 integer.
public class BitwiseComplement {
    // time complexity: O(log(N)), space complexity: O(log(N))
    // 1 ms(32.33%), 32.2 MB(12.06%) for 128 tests
    public int bitwiseComplement(int N) {
        char[] s = Integer.toBinaryString(N).toCharArray();
        for (int i = 0; i < s.length; i++) {
            s[i] = (s[i] == '0') ? '1' : '0';
        }
        return Integer.valueOf(String.valueOf(s), 2);
    }

    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 32.2 MB(12.06%) for 128 tests
    public int bitwiseComplement2(int N) {
        int mask = 1;
        for (; mask < N; mask = (mask << 1) + 1) {}
        return N ^ mask; // or: mask - N
    }

    void test(int N, int expected) {
        assertEquals(expected, bitwiseComplement(N));
        assertEquals(expected, bitwiseComplement2(N));
    }

    @Test
    public void test() {
        test(5, 2);
        test(7, 0);
        test(10, 5);
        test(100, 27);
        test(999, 24);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
