import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC231: https://leetcode.com/problems/power-of-two/
//
// Given an integer, write a function to determine if it is a power of two.
public class PowerOfTwo {
    // Solution of Choice
    // beats 20.40%(2 ms)
    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    // beats 20.40%(2 ms)
    public boolean isPowerOfTwo2(int n) {
        if (n <= 0) return false;

        while ((n & 1) == 0) {
            n >>= 1;
        }
        return n == 1;
    }

    // beats 21.02%(2 ms for 1108 tests)
    public boolean isPowerOfTwo3(int n) {
        return n > 0 && ((1 << 30) % n == 0);
    }

    void test(int n, boolean expected) {
        assertEquals(expected, isPowerOfTwo(n));
        assertEquals(expected, isPowerOfTwo2(n));
        assertEquals(expected, isPowerOfTwo3(n));
    }

    @Test
    public void test1() {
        test(0, false);
        for (int i = 0; i < 10; i++) {
            test(1 << i, true);
        }
        for (int i = 1; i < 10; i++) {
            test((1 << i) + 1, false);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PowerOfTwo");
    }
}
