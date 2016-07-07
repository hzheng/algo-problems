import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/power-of-two/
//
// Given an integer, write a function to determine if it is a power of two.
public class PowerOfTwo {
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

    void test(int n, boolean expected) {
        assertEquals(expected, isPowerOfTwo(n));
        assertEquals(expected, isPowerOfTwo2(n));
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
