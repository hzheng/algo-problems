import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 5.1:
 * Given two 32-bit numbers, N and M, and two bit positions, i and j.
 * Insert M into N such that M starts at bit j and ends at bit i.
 * Assume that the bits j through i have enough space to fit all of M.
 * i.e,if M = 10011, assume that there are at least 5 bits between j and i.
 */
public class BitInsertion {
    public static int insertBits(int n, int m, int i, int j) {
        int mask = ~(((1 << (j - i + 1)) - 1) << i);
        return (n & mask) | (m << i);
    }

    public static int insertBits2(int n, int m, int i, int j) {
        int left = ~0 << (j + 1);
        int right = ((1 << i) - 1);
        int mask = left | right;
        return (n & mask) | (m << i);
    }

    void test(String nStr, String mStr, int i, int j, String expected) {
        int n = Integer.parseInt(nStr, 2);
        int m = Integer.parseInt(mStr, 2);
        assertEquals(expected, Integer.toBinaryString(insertBits(n, m, i, j)));
        assertEquals(expected, Integer.toBinaryString(insertBits2(n, m, i, j)));
    }

    @Test
    public void test1() {
        test("10000000000", "10011", 2, 6, "10001001100");
        test("10001011100", "10011", 2, 6, "10001001100");
        test("10001011101", "10011", 2, 6, "10001001101");
        test("10111011101", "10011", 2, 7, "10101001101");
        test("10111011101", "10011", 2, 8, "10001001101");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BitInsertion");
    }
}
