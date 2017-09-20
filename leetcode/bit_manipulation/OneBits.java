import org.junit.Test;
import static org.junit.Assert.*;

// LC191: https://leetcode.com/problems/number-of-1-bits/
//
// Write a function that takes an unsigned integer and returns the number of 1
// bits it has(also known as the Hamming weight).
public class OneBits {
    // beats 89.42%(1 ms for 600 tests)
    public int hammingWeight(int n) {
        int count = 0;
        for (int i = n; i != 0; i >>>= 1) {
            count += (i & 1);
        }
        return count;
    }

    // Solution of Choice
    // beats 89.42%(1 ms for 600 tests)
    public int hammingWeight2(int n) {
        int count = 0;
        for (int i = n; i != 0; i &= (i - 1)) {
            count++;
        }
        return count;
    }

    // https://en.wikipedia.org/wiki/Hamming_weight
    // beats 16.06%(2 ms for 600 tests)
    public int hammingWeight3(int n) {
        n = (n & 0x55555555) + (n >>>  1 & 0x55555555); // put count of each  2 bits into those 2 bits
        n = (n & 0x33333333) + (n >>>  2 & 0x33333333);// put count of each  4 bits into those  4 bits
        n = (n & 0x0F0F0F0F) + (n >>>  4 & 0x0F0F0F0F);// put count of each  8 bits into those  8 bits
        n = (n & 0x00FF00FF) + (n >>>  8 & 0x00FF00FF);// put count of each 16 bits into those 16 bits
        n = (n & 0x0000FFFF) + (n >>> 16 & 0x0000FFFF); // put count of each 32 bits into those 32 bits
        return n;
    }

    // https://en.wikipedia.org/wiki/Hamming_weight
    // beats 16.06%(2 ms for 600 tests)
    public int hammingWeight4(int n) {
        n -= (n >>> 1) & 0x55555555; //put count of each 2 bits into those 2 bits
        n = (n & 0x33333333) + (n >>> 2 & 0x33333333); //put count of each 4 bits into those 4 bits
        n = (n + (n >>> 4)) & 0x0F0F0F0F; //put count of each 8 bits into those 8 bits
        n += n >>> 8; // put count of each 16 bits into those 8 bits
        n += n >>> 16; // put count of each 32 bits into those 8 bits
        return n & 0xFF;
    }

    // https://en.wikipedia.org/wiki/Hamming_weight
    // beats 16.06%(2 ms for 600 tests)
    public int hammingWeight5(int n) {
        n -= (n >>> 1) & 0x55555555; // put count of each 2 bits into those 2 bits
        n = (n & 0x33333333) + (n >>> 2 & 0x33333333); // put count of each 4 bits into those 4 bits
        n = (n + (n >>> 4)) & 0x0F0F0F0F; // put count of each 8 bits into those 8 bits
        return n * 0x01010101 >>> 24; // returns left 8 bits of x + (x<<8) + (x<<16) + (x<<24)
    }

    void test(int n, int expected) {
        assertEquals(expected, hammingWeight(n));
        assertEquals(expected, hammingWeight2(n));
        assertEquals(expected, hammingWeight3(n));
        assertEquals(expected, hammingWeight4(n));
        assertEquals(expected, hammingWeight5(n));
    }

    @Test
    public void test1() {
        test(11, 3);
        test(45, 4);
        test(-1, 32);
        test(Integer.MIN_VALUE, 1);
        test(0, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("OneBits");
    }
}
