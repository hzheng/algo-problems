import java.util.*;
import java.math.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC633: https://leetcode.com/problems/sum-of-square-numbers/
//
// Given a non-negative integer c, your task is to decide whether there're two
// integers a and b such that a ^ 2 + b ^ 2 = c.
public class SumOfSquareNumbers {
    // time complexity: O(N ^ 1/2) assuming Math.sqrt is O(1)
    // beats 52.69%(20 ms for 124 tests)
    public boolean judgeSquareSum(int c) {
        for (int a = 0; a * a <= c / 2; a++) {
            // if (isPerfect(c - a * a)) return true;
            double b = Math.sqrt(c - a * a);
            if (b == (int)b) return true;
        }
        return false;
    }

    // Binary Search
    // time complexity: O(N ^ 1/2 * log(N))
    // beats 21.57%(71 ms for 124 tests)
    public boolean judgeSquareSum2(int c) {
        for (int a = 0; a * a <= c / 2; a++) {
            int b = c - a * a;
            if (binarySearch(0, b, b)) return true;
        }
        return false;
    }

    private boolean binarySearch(long low, long high, int n) {
        if (low > high) return false;

        long mid = low + (high - low) / 2;
        if (mid * mid == n) return true;

        return mid * mid > n ? binarySearch(low, mid - 1, n)
               : binarySearch(mid + 1, high, n);
    }

    // https://en.wikipedia.org/wiki/Fermat%27s_theorem_on_sums_of_two_squares
    // Math(Fermat Theorem)
    // time complexity: O(N ^ 1/2 * log(N))
    // beats 100.00%(10 ms for 124 tests)
    public boolean judgeSquareSum3(int c) {
        for (int i = 2; i * i <= c; i++) {
            if (c % i == 0) {
                int count = 0;
                for (; c % i == 0; count++, c /= i) {}
                if (i % 4 == 3 && count % 2 != 0) return false;
            }
        }
        return c % 4 != 3;
    }

    // Two Pointers
    // time complexity: O(N ^ 1/2)
    // beats 76.81%(16 ms for 124 tests)
    public boolean judgeSquareSum4(int c) {
        for (int a = 0, b = (int)Math.sqrt(c); a <= b; ) {
            int squareSum = a * a + b * b;
            if (squareSum < c) {
                a++;
            } else if (squareSum > c) {
                b--;
            } else return true;
        }
        return false;
    }

    void test(int c, boolean expected) {
        assertEquals(expected, judgeSquareSum(c));
        assertEquals(expected, judgeSquareSum2(c));
        assertEquals(expected, judgeSquareSum3(c));
        assertEquals(expected, judgeSquareSum4(c));
    }

    @Test
    public void test() {
        test(3, false);
        test(5, true);
        test(6, false);
        test(25, true);
        test(30, false);
        test(169, true);
        test(1000000, true);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
