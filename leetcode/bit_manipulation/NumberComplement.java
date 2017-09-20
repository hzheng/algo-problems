import org.junit.Test;
import static org.junit.Assert.*;

// LC476: https://leetcode.com/problems/number-complement/
//
// Given a positive integer, output its complement number. The complement
// strategy is to flip the bits of its binary representation.
public class NumberComplement {
    // beats 82.95%(10 ms for 149 tests)
    public int findComplement(int num) {
        if (num < 0) return ~num;

        int highest = 30;
        for (int mask = 1 << highest; highest >= 0; highest--, mask >>= 1) {
            if ((num & mask) != 0) break;
        }
        return ~num & ((1 << (highest + 1)) - 1);
    }

    // beats 54.58%(11 ms for 149 tests)
    public int findComplement2(int num) {
        return ~num & ((Integer.highestOneBit(num) << 1) - 1);
    }

    // beats 54.58%(11 ms for 149 tests)
    public int findComplement3(int num) {
        return ~num + (Integer.highestOneBit(num) << 1);
    }

    // beats 82.95%(10 ms for 149 tests)
    public int findComplement4(int num) {
        int mask = num;
        mask |= mask >> 1;
        mask |= mask >> 2;
        mask |= mask >> 4;
        mask |= mask >> 8;
        mask |= mask >> 16;
        return num ^ mask;
    }

    public int findComplementWrong(int num) {
        String s = String.format("%32s", Integer.toBinaryString(num));
        char[] chars = new char[32];
        for (int i = 0; i < 32; i++) {
            chars[i] = (s.charAt(i) == '1') ? '0' : '1';
        }
        // NumberFormatException
        return Integer.parseInt(new String(chars), 2);
    }

    void test(int num, int expected) {
        assertEquals(expected, findComplement(num));
        assertEquals(expected, findComplement2(num));
        assertEquals(expected, findComplement3(num));
        assertEquals(expected, findComplement4(num));
    }

    @Test
    public void test() {
        test(Integer.MAX_VALUE, 0);
        test(-1, 0);
        test(-2, 1);
        test(5, 2);
        test(1, 0);
        test(100, 27);
        test(10000, 6383);
        test(-10000, 9999);
        test(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NumberComplement");
    }
}
