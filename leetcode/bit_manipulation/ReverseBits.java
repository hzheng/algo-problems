import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/reverse-bits/
//
// Reverse bits of a given 32 bits unsigned integer.
public class ReverseBits {
    // beats 14.96%(3 ms)
    public int reverseBits(int n) {
        for (int leftMask = 1 << 31, rightMask = 1; rightMask < (1 << 16);
             leftMask >>>= 1, rightMask <<= 1) {
            int rightBit = n & rightMask;
            if ((n & leftMask) != 0) {
                n |= rightMask;
            } else {
                n &= ~rightMask;
            }
            if (rightBit != 0) {
                n |= leftMask;
            } else {
                n &= ~leftMask;
            }
        }
        return n;
    }

    // beats 50.50%(2 ms)
    public int reverseBits2(int n) {
        for (int i = 0, j = 31; i < j; i++, j--) {
            if ((((n >> i) & 1) ^ ((n >> j) & 1)) != 0) {
                n ^= (1 << i) | (1 << j);
            }
        }
        return n;
    }

    // http://www.docjar.com/html/api/java/lang/Integer.java.html
    // beats 14.96%(3 ms)
    public int reverseBits3(int n) {
        n = (n & 0x55555555) << 1 | (n >>> 1) & 0x55555555;
        n = (n & 0x33333333) << 2 | (n >>> 2) & 0x33333333;
        n = (n & 0x0f0f0f0f) << 4 | (n >>> 4) & 0x0f0f0f0f;
        n = (n << 24) | ((n & 0xff00) << 8) | ((n >>> 8) & 0xff00) | (n >>> 24);
        return n;
    }

    // beats 50.50%(2 ms)
    public int reverseBits4(int n) {
        int reversed = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (1 << i)) != 0) {
                reversed |= 1 << (31 - i);
            }
        }
        return reversed;
    }

    // beats 50.50%(2 ms)
    public int reverseBits5(int n) {
        int reversed = 0;
        for(int i = 0; i < 32; i++, n >>= 1){
            reversed <<= 1;
            reversed |= (n & 1);
        }
        return reversed;
    }

    void test(int x, int expected) {
        assertEquals(expected, reverseBits(x));
        assertEquals(expected, reverseBits2(x));
        assertEquals(expected, reverseBits3(x));
        assertEquals(expected, reverseBits4(x));
        assertEquals(expected, reverseBits5(x));
    }

    @Test
    public void test1() {
        test(43261596, 964176192);
        test(1, -2147483648);
        test(2147483647, -2);
        test(-1, -1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseBits");
    }
}
