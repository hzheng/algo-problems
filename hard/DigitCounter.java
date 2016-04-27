import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.4:
 *  Write a method to count the number of 2s between 0 and n.
 */
public class DigitCounter {
    public static int count2sInRangeEasy(int n) {
        int count = 0;
        for (int i = 2; i <= n; i++) {
            count += count2s(i);
        }
        return count;
    }

    private static int count2s(int n) {
        int count = 0;
        for (; n > 0; n /= 10) {
            if ((n % 10) == 2) {
                count++;
            }
        }
        return count;
    }

    public static int count2sInRange(int n) {
        if (n < 2) return 0;
        if (n < 10) return 1;

        int digits = 0; // decimal digits
        for (int m = n; m > 0; m /= 10, digits++) {}
        // assert (digits > 1);

        int secondHighPower = (int)Math.pow(10, digits - 2);
        int firstHighPower = secondHighPower * 10;
        int highestDigit = n / firstHighPower;
        int lowerValue = n % firstHighPower;
        // assert (highestDigit > 0 && highestDigit < 10);

        // each bit(except the highest one) have same # of 2
        int count = secondHighPower * (digits - 1) * highestDigit;
        if (highestDigit == 2) {
            count += lowerValue + 1; // 200..0 -> 2xx..x
        } else if (highestDigit > 2) {
            // starting with 2
            count += firstHighPower;
        }
        // recursively count the lower digits
        return count + count2sInRange(lowerValue);
    }

    // from the book
    private static int count2sInRangeAtDigit(int number, int d) {
        int powerOf10 = (int)Math.pow(10, d);
        // BUG: if d >= 9, nextPowerOf10 will be overflow!
        int nextPowerOf10 = powerOf10 * 10;
        int right = number % powerOf10;

        int roundDown = number - number % nextPowerOf10;
        int roundUp = roundDown + nextPowerOf10;

        int digit = (number / powerOf10) % 10;
        if (digit < 2) { // if the digit in spot digit is
            return roundDown / 10;
        } else if (digit == 2) {
            return roundDown / 10 + right + 1;
        } else {
            return roundUp / 10;
        }
    }

    public static int count2sInRange2(int number) {
        int count = 0;
        int len = String.valueOf(number).length();
        for (int digit = 0; digit < len; digit++) {
            count += count2sInRangeAtDigit(number, digit);
        }
        return count;
    }

    private int test(Function<Integer, Integer> count2s, String name, int n) {
        long t1 = System.nanoTime();
        int count = count2s.apply(n);
        if (n > 100000) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
        return count;
    }

    private void test(int n) {
        int c1 = test(DigitCounter::count2sInRangeEasy, "count2sInRangeEasy", n);
        int c2 = test(DigitCounter::count2sInRange, "count2sInRange", n);
        int c3 = test(DigitCounter::count2sInRange2, "count2sInRange2", n);
        assertEquals(c1, c2);
        assertEquals(c1, c3);
    }

    @Test
    public void test1() {
        int n = 10000;
        for (int i = 0; i < n; ++i) {
            test(i);
        }
    }

    @Test
    public void test2() {
        test(1410065408); // count2sInRange2 first break point(overflow)
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DigitCounter");
    }
}
