import org.junit.Test;
import static org.junit.Assert.*;

// LC461: https://leetcode.com/problems/hamming-distance/
//
// Given two integers x and y, calculate the Hamming distance.
public class HammingDistance {
    // beats N/A(10 ms for 149 tests)
    public int hammingDistance(int x, int y) {
        int count = 0;
        for (int a = x ^ y; a != 0; a &= (a - 1)) {
            count++;
        }
        return count;
    }

    // beats N/A(11 ms for 149 tests)
    public int hammingDistance2(int x, int y) {
        return Integer.bitCount(x ^ y);
    }

    // beats N/A(11 ms for 149 tests)
    // implementation of Integer.bitCount
    public int hammingDistance3(int x, int y) {
        int i = x ^ y;
        i = i - ((i >>> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        i = (i + (i >>> 4)) & 0x0f0f0f0f;
        i += (i >>> 8);
        i += (i >>> 16);
        return i & 0x3f;
    }

    void test(int x, int y, int expected) {
        assertEquals(expected, hammingDistance(x, y));
        assertEquals(expected, hammingDistance2(x, y));
        assertEquals(expected, hammingDistance3(x, y));
    }

    @Test
    public void test() {
        test(1, 4, 2);
        test(0, 2, 1);
        test(11, 13, 2);
        test(0b10001, 0b1110, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HammingDistance");
    }
}
