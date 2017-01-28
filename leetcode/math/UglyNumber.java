import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC263: https://leetcode.com/problems/ugly-number/
//
// Ugly numbers are positive numbers whose prime factors only include 2, 3, 5.
// Note that 1 is typically treated as an ugly number.
public class UglyNumber {
    // beats 19.59%(2 ms for 1012 tests)
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

    // Solution of Choice
    // beats 19.59%(2 ms for 1012 tests)
    public boolean isUgly2(int num) {
        if (num <= 0) return false;

        int x = num / (((num ^ (num - 1)) >> 1) + 1);
        x = divideRepeately(x, 3);
        return divideRepeately(x, 5) == 1;
    }

    // beats 19.59%(2 ms for 1012 tests)
    public boolean isUgly3(int num) {
        if (num <= 0) return false;

        for (int factor : new int[] {2, 3, 5}) {
            for (; num % factor == 0; num /= factor) {}
        }
        return num == 1;
    }

    // Recursion
    // beats 5.19%(3 ms for 1012 tests)
    public boolean isUgly4(int num) {
        if (num <= 0) return false;
        if (num == 1) return true;

        if (num % 2 == 0) return isUgly4(num / 2);
        if (num % 3 == 0) return isUgly4(num / 3);
        if (num % 5 == 0) return isUgly4(num / 5);
        return false;
    }

    void test(int x, boolean expected) {
        assertEquals(expected, isUgly(x));
        assertEquals(expected, isUgly2(x));
        assertEquals(expected, isUgly3(x));
        assertEquals(expected, isUgly4(x));
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
