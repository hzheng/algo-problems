import org.junit.Test;

import static org.junit.Assert.*;

// LC1486: https://leetcode.com/problems/xor-operation-in-an-array/
//
// Given an integer n and an integer start. Define an array nums where nums[i] = start + 2*i
// (0-indexed) and n == nums.length.
// Return the bitwise XOR of all elements of nums.
public class XorOperation {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 36.3 MB(100%) for 54 tests
    public int xorOperation(int n, int start) {
        int res = 0;
        for (int i = 0; i < n; i++) {
            res ^= start + i * 2;
        }
        return res;
    }

    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100%), 36.3 MB(100%) for 54 tests
    public int xorOperation2(int n, int start) {
        return (n & start & 1) + xorConsecutive(n, start >> 1) * 2;
    }

    private int xorConsecutive(int n, int start) {
        if ((start & 1) != 0) {
            return (start - 1) ^ xorConsecutiveWithEvenStart(n + 1, start - 1);
        }
        return xorConsecutiveWithEvenStart(n, start);
    }

    // (2a)^(2a+1) = 1
    private int xorConsecutiveWithEvenStart(int n, int start) {
        return (n >> 1) & 1 ^ (((n & 1) == 0) ? 0 : (start + n - 1));
    }

    void test(int n, int start, int expected) {
        assertEquals(expected, xorOperation(n, start));
        assertEquals(expected, xorOperation2(n, start));
    }

    @Test public void test() {
        test(5, 0, 8);
        test(4, 3, 8);
        test(1, 7, 7);
        test(10, 5, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
