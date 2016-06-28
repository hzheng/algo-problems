import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/bitwise-and-of-numbers-range/
//
// Given a range [m, n] where 0 <= m <= n <= 2147483647, return the bitwise AND
// of all numbers in this range, inclusive.
public class RangeBitwiseAnd {
    // beats 88.39%(7 ms)
    public int rangeBitwiseAnd(int m, int n) {
        if (m >= n) return m;

        int diff = n - m;
        if (diff >= m) return 0;

        long power = 1;
        for (; power <= diff; power <<= 1);
        long diffPower = power;
        for (; power <= m; power <<= 1);
        if (n >= power) return 0;

        int res = 0;
        for (power >>= 1; power >= diffPower; power >>= 1) {
            if ((m & power) != 0 && (n & power) != 0) {
                res |= power;
            }
        }
        return res;
    }

    // beats 59.66%(8 ms)
    public int rangeBitwiseAnd2(int m, int n) {
        int mask = Integer.MAX_VALUE;
        while ((m & mask) != (n & mask)) {
            mask <<= 1;
        }
        return m & mask;
    }

    // beats 22.37(9 ms)
    public int rangeBitwiseAnd3(int m, int n) {
        int shift = 0;
        for (; m != n; m >>= 1, n >>= 1, shift++);
        return m << shift;
    }

    // beats 22.37(9 ms)
    public int rangeBitwiseAnd4(int m, int n) {
        int mask = n;
        for (; mask > m; mask &= (mask - 1));
        return m & mask;
    }

    // recursion
    // beats 22.37(9 ms)
    public int rangeBitwiseAnd5(int m, int n) {
        if (m >= n) return m;

        if (n == m + 1) return m & n;

        return rangeBitwiseAnd5(m >> 1, n >> 1) << 1;
    }

    void test(int m, int n, int expected) {
        assertEquals(expected, rangeBitwiseAnd(m, n));
        assertEquals(expected, rangeBitwiseAnd2(m, n));
        assertEquals(expected, rangeBitwiseAnd3(m, n));
        assertEquals(expected, rangeBitwiseAnd4(m, n));
        assertEquals(expected, rangeBitwiseAnd5(m, n));
    }

    @Test
    public void test1() {
        test(11, 12, 8);
        test(5, 6, 4);
        test(2, 3, 2);
        test(3, 4, 0);
        test(5, 7, 4);
        test(5, 8, 0);
        test(98, 125, 96);
        test(98, 199, 0);
        test(2147483646, 2147483647, 2147483646);
        test(2147483645, 2147483647, 2147483644);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RangeBitwiseAnd");
    }
}
