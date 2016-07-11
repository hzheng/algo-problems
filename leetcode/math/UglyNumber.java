import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/ugly-number/
//
// Ugly numbers are positive numbers whose prime factors only include 2, 3, 5.
// Note that 1 is typically treated as an ugly number.
public class UglyNumber {
    // beats 21.55%(2 ms)
    public boolean isUgly(int num) {
        if (num <= 0) return false;

        int x = divideRepeately(num, 2);
        x = divideRepeately(x, 3);
        return divideRepeately(x, 5) == 1;
    }

    private int divideRepeately(int num, int divisor) {
        int x = num;
        while (x % divisor == 0) {
            x /= divisor;
        }
        return x;
    }

    // beats 21.55%(2 ms)
    public boolean isUgly2(int num) {
        if (num <= 0) return false;

        int x = num / (((num ^ (num - 1)) >> 1) + 1);
        x = divideRepeately(x, 3);
        return divideRepeately(x, 5) == 1;
    }

    void test(int x, boolean expected) {
        assertEquals(expected, isUgly(x));
        assertEquals(expected, isUgly2(x));
    }

    @Test
    public void test1() {
        test(1, true);
        test(6, true);
        test(8, true);
        test(30, true);
        test(14, false);
        test(31, false);
        test(64, true);
        test(14400, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UglyNumber");
    }
}
