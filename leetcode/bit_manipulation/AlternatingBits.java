import org.junit.Test;
import static org.junit.Assert.*;

// LC693: https://leetcode.com/problems/binary-number-with-alternating-bits/description/
//
// Given a positive integer, check whether it has alternating bits: namely,
// if two adjacent bits will always have different values.
public class AlternatingBits {
    // beats 18.07%(16 ms for 204 tests)
    public boolean hasAlternatingBits(int n) {
        for (int i = n >> 1, lastBit = n & 1; i > 0; lastBit ^= 1, i >>= 1) {
            if ((i & 1) == lastBit) return false;
        }
        return true;
    }

    // beats 50.60%(14 ms for 204 tests)
    public boolean hasAlternatingBits2(int n) {
        int i = n;
        for (int lastBit = n & 1; (i & 1) == lastBit; lastBit ^= 1, i >>= 1) {}
        return i == 0;
    }

    // beats 50.60%(14 ms for 204 tests)
    public boolean hasAlternatingBits3(int n) {
        for (int i = n >> 1, lastBit = n & 1; i > 0; lastBit = i & 1, i >>= 1) {
            if ((i & 1) == lastBit) return false;
        }
        return true;
    }

    // beats 75.82%(13 ms for 204 tests)
    public boolean hasAlternatingBits4(int n) {
        int last2Bits = n & 3;
        if (last2Bits == 0 || last2Bits == 3) return false;

        int i = n >> 2;
        for (; (i & 3) == last2Bits; i >>= 2) {}
        return i == 0;
    }

    // beats 75.82%(13 ms for 204 tests)
    // If n has alternating bits, then (n>>1) + n must be like 111...11.
    public boolean hasAlternatingBits5(int n) {
        // return (((long)n + (n >> 1) + 1) & ((long)n + (n >> 1))) == 0;
        return ((n + (n >> 1) + 1) & (n + (n >> 1))) == 0;
    }

    // beats 33.98%(15 ms for 204 tests)
    public boolean hasAlternatingBits6(int n) {
        return ((n ^= (n >> 1)) & (n + 1)) == 0;
    }

    // beats 96.95%(12 ms for 204 tests)
    public boolean hasAlternatingBits7(int n) {
        return ((n ^= (n >> 2)) & (n - 1)) == 0;
    }

    // beats 75.82%(13 ms for 204 tests)
    public boolean hasAlternatingBits8(int n) {
        String bits = Integer.toBinaryString(n);
        for (int i = bits.length() - 2; i >= 0; i--) {
            if (bits.charAt(i) == bits.charAt(i + 1)) return false;
        }
        return true;
    }

    // beats 2.81%(23 ms for 204 tests)
    public boolean hasAlternatingBits9(int n) {
        return Integer.toBinaryString(n).matches("(10)*1?");
    }

    // beats 4.26%(21 ms for 204 tests)
    public boolean hasAlternatingBits10(int n) {
        return Integer.toBinaryString(n ^ (n >> 1)).matches("1+");
    }

    // beats 33.58%(15 ms for 204 tests)
    public boolean hasAlternatingBits11(int n) {
        int i = n;
        if ((i & 1) == 1) {
            i >>= 1;
        }
        for (; i > 0 && ((i & 3) == 2); i >>= 2) {}
        return i == 0;
    }

    void test(int n, boolean expected) {
        assertEquals(expected, hasAlternatingBits(n));
        assertEquals(expected, hasAlternatingBits2(n));
        assertEquals(expected, hasAlternatingBits3(n));
        assertEquals(expected, hasAlternatingBits4(n));
        assertEquals(expected, hasAlternatingBits5(n));
        assertEquals(expected, hasAlternatingBits6(n));
        assertEquals(expected, hasAlternatingBits7(n));
        assertEquals(expected, hasAlternatingBits8(n));
        assertEquals(expected, hasAlternatingBits9(n));
        assertEquals(expected, hasAlternatingBits10(n));
        assertEquals(expected, hasAlternatingBits11(n));
    }

    @Test
    public void test() {
        test(1, true);
        test(3, false);
        test(5, true);
        test(10, true);
        test(21, true);
        test(7, false);
        test(22, false);
        test(0b1010, true);
        test(0b10100, false);
        test(0b10101, true);
        test(0b101010, true);
        test(0b1010101, true);
        test(0b10101010, true);
        test(0b101010101, true);
        test(0b10101001, false);
        test(Integer.MAX_VALUE, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
