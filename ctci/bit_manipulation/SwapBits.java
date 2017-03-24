import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 5.6:
 * Swap odd and even bits in an integer with as few instructions as possible
 * (e.g., bit 0 and bit1 are swapped, bit 2 and bit 3 are swapped, and so on
 */
public class SwapBits {
    public static int swapBits(int n) {
        int swapped = 0;
        for (int bits = 0; n > 0; n >>= 2, bits += 2) {
            int last2 = ((n & 1) << 1) | ((n & 2) >> 1);
            swapped |= last2 << bits;
        }
        return swapped;
    }

    public static int swapBits2(int n) {
        return ((n & 0xaaaaaaaa) >> 1) | ((n & 0x55555555) << 1);
    }

    void test(String nStr, String expected) {
        int n = Integer.parseInt(nStr, 2);
        assertEquals(expected, Integer.toBinaryString(swapBits(n)));
        assertEquals(expected, Integer.toBinaryString(swapBits2(n)));
    }

    @Test
    public void test1() {
        test("11", "11");
        test("1010", "101");
        test("100101", "11010");
        test("1100101", "10011010");
        test("110111001101", "111011001110");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SwapBits");
    }
}
