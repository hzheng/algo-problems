import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/power-of-three/
//
// Given an integer, write a function to determine if it is a power of three.
// Could you do it without using any loop / recursion?
public class PowerOf3 {
    // recursive
    // beats 87.73%(16 ms)
    public boolean isPowerOfThree(int n) {
        return n == 1 || n > 0 && (n % 3 == 0) && isPowerOfThree(n / 3);
    }

    // iterative
    // beats 60.90%(18 ms)
    public boolean isPowerOfThree2(int n) {
        int i = n;
        for (; i > 1; i /= 3) {
            if (i % 3 != 0) return false;
        }
        return i == 1;
    }

    // beats 76.96%(17 ms)
    public boolean isPowerOfThree3(int n) {
        if (n < 1) return false;

        double log = Math.log10(n) / Math.log10(3);
        double delta = log - (int)log;
        // final double e = 1E-6 / n;
        final double e = 1E-10;
        return delta < e || (1 - delta) < e;
    }

    // beats 93.72%(15 ms)
    public boolean isPowerOfThree4(int n) {
        if (n < 1) return false;

        // double log = Math.log(n) / Math.log(3); // log10 is faster
        double log = Math.log10(n) / Math.log10(3);
        // or : return Math.abs(log - Math.rint(log)) < 1E-10;
        return Math.abs(log - (int)(log + 0.5)) < 1E-10;
    }

    // beats 9.28%(24 ms)
    public boolean isPowerOfThree5(int n) {
        return n > 0 && n == Math.pow(3, Math.round(Math.log(n) / Math.log(3)));
    }

    // beats 43.05(19 ms)
    public boolean isPowerOfThree6(int n) {
        return n > 0 && 1162261467 % n == 0; //1162261467 = 3^19
    }

    // beats 3.08%(78 ms)
    public boolean isPowerOfThree7(int n) {
        return n > 0 && Integer.toString(n, 3).matches("10*");
        // or:
        // return Integer.toString(n, 3).matches("^10*$");
    }

    // beats 19.68%(21 ms)
    public boolean isPowerOfThree8(int n) {
        int[] list = {1, 3, 9, 27, 81, 243, 729, 2187, 6561, 19683, 59049,
                      177147, 531441, 1594323, 4782969, 14348907, 43046721,
                      129140163, 387420489, 1162261467};
        for (int powerOf3 : list) {
            if (n == powerOf3) return true;
        }
        return false;
    }

    // beats 19.68%(21 ms)
    public boolean isPowerOfThree9(int n) {
        return Math.log10(n) / Math.log10(3) % 1 == 0;
        // safer version:
        // return (Math.log(n) / Math.log(3) + epsilon) % 1 <= 2 * epsilon;
    }

    void test(int n, boolean expected) {
        assertEquals(expected, isPowerOfThree(n));
        assertEquals(expected, isPowerOfThree2(n));
        assertEquals(expected, isPowerOfThree3(n));
        assertEquals(expected, isPowerOfThree4(n));
        assertEquals(expected, isPowerOfThree5(n));
        assertEquals(expected, isPowerOfThree6(n));
        assertEquals(expected, isPowerOfThree7(n));
        assertEquals(expected, isPowerOfThree8(n));
        assertEquals(expected, isPowerOfThree9(n));
    }

    void test(int n) {
        test(n, true);
        test(n + 1, false);
        test(n - 1, false);
    }

    @Test
    public void test1() {
        for (int i = 0; i < 1000; i++) {
            test(i, i == 1 || i == 3 || i == 9 || i == 27 || i == 81
                 || i == 243 || i == 729);
        }
    }

    @Test
    public void test2() {
        test(6561);
        test(19683);
        test(177147);
        test(4782969);
        test(129140163);
        test(1162261467);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PowerOf3");
    }
}
