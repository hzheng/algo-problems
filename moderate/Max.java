import static java.lang.Integer.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 17.4:
 * Finds the maximum of numbers without if-else or any other comparison operator
 */
public class Max {
    // glitch: (a + b) or (a - b) may be overflow
    public static int max1(int a, int b) {
        return ((a + b) + (a - b) * sgn(a - b)) / 2;
    }

    private static int sgn(int a) {
        return 1 - ((a >> 31) & 1) * 2;
    }

    public static int max2(int a, int b) {
        int difSgn = differenceSgn(a, b);
        return a * difSgn + b * (1 - difSgn);
    }

    private static int sgn2(int a) {
        return 1 - ((a >> 31) & 1);
    }

    // sgn(a - b) avoiding overflow
    private static int differenceSgn(int a, int b) {
        // int diffSign = (a >> 31) ^ (b >> 31);
        int diffSign = sgn2(a) ^ sgn2(b);
        int sameSign = 1 - diffSign;
        return sameSign * sgn2(a - b) + diffSign * sgn2(a);
    }

    // from the book
    /* Flips a 1 to a 0 and a 0 to a 1 */
    public static int flip(int bit) {
        return 1 ^ bit;
    }

    /* Returns 1 if a is positive, and 0 if a is negative */
    public static int sign(int a) {
        return flip((a >> 31) & 0x1);
    }

    public static int getMaxNaive(int a, int b) {
        int k = sign(a - b);
        int q = flip(k);
        return a * k + b * q;
    }

    public static int getMax(int a, int b) {
        int c = a - b;

        int sa = sign(a); // if a >= 0, then 1 else 0
        int sb = sign(b); // if b >= 1, then 1 else 0
        int sc = sign(c); // depends on whether or not a - b overflows

        /* We want to define a value k which is 1 if a > b and 0 if a < b.
         * (if a = b, it doesn't matter what value k is) */

        int use_sign_of_a = sa ^ sb; // If a and b have different signs, then k = sign(a)
        int use_sign_of_c = flip(sa ^ sb); // If a and b have the same sign, then k = sign(a - b)

        /* We can't use a comparison operator, but we can multiply values by 1 or 0 */
        int k = use_sign_of_a * sa + use_sign_of_c * sc;
        int q = flip(k); // opposite of k

        return a * k + b * q;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(Function<Integer, Integer, Integer> max,
                      int expected, int a, int b) {
        assertEquals(expected, (int)max.apply(a, b));
    }

    private void test(int a, int b, int expected) {
        test(Max::max1, expected, a, b);
        test(Max::max2, expected, a, b);
        test(Max::getMaxNaive, expected, a, b);
        test(Max::getMax, expected, a, b);
    }

    private void testFail(Function<Integer, Integer, Integer> max,
                      int expected, int a, int b) {
        assertNotEquals(expected, (int)max.apply(a, b));
    }

    private void testOverflow(int a, int b, int expected) {
        test(Max::max2, expected, a, b);
        test(Max::getMax, expected, a, b);
        testFail(Max::max1, expected, a, b);
        testFail(Max::getMaxNaive, expected, a, b);
    }

    @Test
    public void test(){
        test(5, 3, 5);
        test(3, 5, 5);
        test(3, 3, 3);
        test(0, 0, 0);
        test(1, -1, 1);
        test(-2, -1, -1);
        test(2, -1, 2);
        test(0, -1, 0);
        test(1, 0, 1);
        testOverflow(MAX_VALUE - 2, -15, MAX_VALUE - 2);
        testOverflow(-15, MAX_VALUE - 2, MAX_VALUE - 2);
        testOverflow(MIN_VALUE + 2, 15, 15);
        testOverflow(3, MIN_VALUE + 2, 3);
        // test(2, MIN_VALUE + 2, 2); // only getMaxNaive fail
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Max");
    }
}
