import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 5.3:
 * Given a positive integer, find the next smallest and the next largest number
 * that have the same number of 1 bits in their binary representation.
 */
public class NextNumber {
    // find lowest '01' sequence
    public static int getNext(int n) {
        if (n <= 0) return -1;

        int seekBit = 1;
        int i = 0;
        for (;; ++i) {
            int bit = (n & (1 << i)) > 0 ? 1 : 0;
            if (bit != seekBit) continue;

            // found a desired bit
            if (seekBit == 0) {
                break; // done
            } else {
                seekBit = 0;
            }
        }
        // error check
        if (i == 31) return -1;

        // after i-th bit, must be form of some 1's followed by some or zero 0's
        int hiBits = n & (~0 << (i + 1));
        int lowBits = n & ((1 << (i - 1)) - 1);
        while (lowBits > 0 && (lowBits & 1) == 0) {
            // shift all tailing 0's to the left
            lowBits >>= 1;
        }
        return hiBits | lowBits | (1 << i);
    }

    // find first non-trailing 0
    public static int getNext2(int n) {
        if (n <= 0) return -1;

        int i = 0;
        // skip trailing 0's
        for (; (n & (1 << i)) == 0; ++i) {}
        // skip 1's
        for (; (n & (1 << i)) > 0; ++i) {}
        // found the desired 0, set it to 1
        n |= (1 << i);
        // error check
        if (i == 31) return -1;

        // set the following 1 to 0
        n &= ~(1 << (i - 1));

        // push all 1's to the end(i.e. shift all tailing 0's)
        int lowBits = n & ((1 << (i - 1)) - 1);
        if (lowBits > 0) {
            while ((lowBits & 1) == 0) {
                lowBits >>= 1;
            }
        }
        return n & (~0 << (i - 1)) | lowBits;
    }

    // find first non-trailing 0(adapt from the book)
    public static int getNext0(int n) {
        if (n <= 0) return -1;

        // remove trailing 0's
        int trailing0 = 0;
        for (; ((n & 1) == 0) && (n != 0); ++trailing0) {
            n >>= 1;
        }
        // remove tailing 1's
        int trailing1 = 0;
        for (; (n & 1) == 1; ++trailing1) {
            n >>= 1;
        }

        int shifted = trailing0 + trailing1;
        // error check
        if (shifted == 31) return -1;

        // found the desired 0, set it to 1
        n |= 1;
        // shift back
        n <<= shifted;
        // add 1's back
        return n | ((1 << (trailing1 - 1)) - 1);
    }

    // find first non-trailing 0(adapt from the book)
    public static int getNextArimetic(int n) {
        if (n <= 0) return -1;

        int m = n;
        // remove trailing 0's
        int trailing0 = 0;
        for (; ((n & 1) == 0) && (n != 0); ++trailing0) {
            n >>= 1;
        }
        // remove tailing 1's
        int trailing1 = 0;
        for (; (n & 1) == 1; ++trailing1) {
            n >>= 1;
        }

        // error check
        if (trailing0 + trailing1 == 31) return -1;

        // calculate the difference between next
        return m + (1 << trailing0) + (1 << (trailing1 - 1)) - 1;
    }

    // find first non-trailing 1(symmetric to the getNext0)
    public static int getPrev(int n) {
        // remove tailing 1's
        int trailing1 = 0;
        for (; (n & 1) == 1; ++trailing1) {
            n >>= 1;
        }
        // error check
        if (n == 0) return -1;

        // remove trailing 0's
        int trailing0 = 0;
        for (; ((n & 1) == 0) && (n != 0); ++trailing0) {
            n >>= 1;
        }

        // found the desired 1, set it to 0
        n = n >> 1 << 1;
        // add 1's back
        n <<= (trailing1 + 1);
        n |= (1 << (trailing1 + 1)) - 1;
        // fill trailing 0's
        return n << (trailing0 - 1);
    }

    private static int flip(int n) {
        return ~n & ((1 << 31) - 1);
    }

    // just take use of getNext
    public static int getPrev2(int n) {
        int flipNext = getNext0(flip(n));
        return (flipNext > 0) ? flip(flipNext) : -1;
    }

    void testNext(String nStr, String expectedNext) {
        int n = Integer.parseInt(nStr, 2);
        assertEquals(expectedNext, Integer.toBinaryString(getNext(n)));
        assertEquals(expectedNext, Integer.toBinaryString(getNext2(n)));
        assertEquals(expectedNext, Integer.toBinaryString(getNext0(n)));
        assertEquals(expectedNext, Integer.toBinaryString(getNextArimetic(n)));
    }

    void testPrev(String nStr, String expectedPrev) {
        int n = Integer.parseInt(nStr, 2);
        assertEquals(expectedPrev, Integer.toBinaryString(getPrev(n)));
        assertEquals(expectedPrev, Integer.toBinaryString(getPrev2(n)));
    }

    @Test
    public void test1() {
        testNext("100101", "100110");
        testNext("10010110", "10011001");
        testNext("1111", "10111");
        testNext("10000", "100000");
        testNext("111111111111111111111111111000",
                 "1000011111111111111111111111111");
        // error: -1
        testNext("0",
                 "11111111111111111111111111111111");
        testNext("1111111111111111111111111111000",
                 "11111111111111111111111111111111");
        testNext("1111111111111111111111111111111",
                 "11111111111111111111111111111111");
    }

    @Test
    public void test2() {
        // testPrev("1000", "100");
        testPrev("10011", "1110");
        testPrev("100101", "100011");
        testPrev("10010", "10001");
        // error: -1
        testNext("0",
                 "11111111111111111111111111111111");
        testPrev("1111", "11111111111111111111111111111111");
        testPrev("1111111111111111111111111111111",
                 "11111111111111111111111111111111");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NextNumber");
    }
}
