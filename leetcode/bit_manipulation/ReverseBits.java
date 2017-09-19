import org.junit.Test;
import static org.junit.Assert.*;

// LC190: https://leetcode.com/problems/reverse-bits/
//
// Reverse bits of a given 32 bits unsigned integer.
// Follow up:
// If this function is called many times, how would you optimize it?
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

    // Solution of Choice
    // beats 12.09%(3 ms for 600 tests)
    public int reverseBits2(int n) {
        for (int i = 0, j = 31; i < j; i++, j--) {
            if (((n >> i) & 1) != ((n >> j) & 1)) {
            // if ((((n >> i) & 1) ^ ((n >> j) & 1)) != 0) {
                n ^= (1 << i) | (1 << j);
            }
        }
        return n;
    }

    // Solution of Choice
    // http://www.docjar.com/html/api/java/lang/Integer.java.html
    // beats 43.92%(2 ms for 600 tests)
    public int reverseBits3(int n) {
        n = (n & 0x55555555) << 1 | (n >>> 1) & 0x55555555;
        n = (n & 0x33333333) << 2 | (n >>> 2) & 0x33333333;
        n = (n & 0x0f0f0f0f) << 4 | (n >>> 4) & 0x0f0f0f0f;
        return (n << 24) | ((n & 0xff00) << 8) | ((n >>> 8) & 0xff00) | (n >>> 24);
    }

    // Solution of Choice
    // https://discuss.leetcode.com/topic/9811/o-1-bit-operation-c-solution-8ms
    // Divide & Conquer
    // beats 43.92%(2 ms for 600 tests)
    public int reverseBits4(int n) {
        n = (n >>> 16) | (n << 16);
        n = ((n & 0xff00ff00) >>> 8) | ((n & 0x00ff00ff) << 8);
        n = ((n & 0xf0f0f0f0) >>> 4) | ((n & 0x0f0f0f0f) << 4);
        n = ((n & 0xcccccccc) >>> 2) | ((n & 0x33333333) << 2);
        n = ((n & 0xaaaaaaaa) >>> 1) | ((n & 0x55555555) << 1);
        return n;
    }

    // beats 50.50%(2 ms)
    public int reverseBits5(int n) {
        int reversed = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (1 << i)) != 0) {
                reversed |= 1 << (31 - i);
            }
        }
        return reversed;
    }

    // beats 50.50%(2 ms)
    public int reverseBits6(int n) {
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
        assertEquals(expected, reverseBits6(x));
    }

    @Test
    public void test1() {
        test(43261596, 964176192);
        test(1, -2147483648);
        test(2147483647, -2);
        test(-1, -1);
        test(2, 1073741824);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseBits");
    }
}
