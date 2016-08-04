import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/valid-perfect-square/
//
// Given a positive integer num, write a function which returns True if num is
// a perfect square else False.
public class PerfectSquare {
    // Newton method
    // beats 36.67%(0 ms)
    public boolean isPerfectSquare(int num) {
        if (num == 1) return true;

        double a = num / 2d;
        while (true) {
            double b = (a * a - num) / (2 * a);
            a -= b;
            if (b < 1) break;
        }
        int root = (int)a;
        return root * root == num;
    }

    // Newton method
    // https://en.wikipedia.org/wiki/Integer_square_root#Using_only_integer_division
    // beats 36.67%(0 ms)
    public boolean isPerfectSquare2(int num) {
        for (int a = num / 2 + 1;; ) {
            int b = num / a;
            if (a <= b) return a * a == num;

            a = (a + b) / 2;
        }
    }

    // Binary Search
    // beats 36.67%(0 ms)
    public boolean isPerfectSquare3(int num) {
        if (num < 4) return num == 1;

        int low = 1;
        int high = num / 2 + 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int inverse = num / mid; // avoid overflow(optionally, we can use long)
            if (mid == inverse) return num % mid == 0;

            if (mid < inverse) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    // beats 18.23%(1 ms)
    // short but slow
    public boolean isPerfectSquare4(int num) {
        for (int i = 1; num > 0; num -= i, i += 2) {}
        return num == 0;
    }

    void test(int num, boolean expected) {
        assertEquals(expected, isPerfectSquare(num));
        assertEquals(expected, isPerfectSquare2(num));
        assertEquals(expected, isPerfectSquare3(num));
        assertEquals(expected, isPerfectSquare4(num));
    }

    @Test
    public void test1() {
        test(1, true);
        test(2, false);
        test(3, false);
        test(808201, true);
        for (int i = 2; i < 100; i++) {
            int square = i * i;
            test(square, true);
            test(square - 1, false);
            test(square + 1, false);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PerfectSquare");
    }
}
