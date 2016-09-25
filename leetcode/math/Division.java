import org.junit.Test;
import static org.junit.Assert.*;

// LC029: https://leetcode.com/problems/divide-two-integers/
//
// Divide two integers without using multiplication, division and mod operator.
public class Division {
    // Time Limit Exceeded and overflow unhandled
    public int divideFlawed(int dividend, int divisor) {
        if (divisor == 0) return Integer.MAX_VALUE;
        if (dividend == 0) return 0;

        if (divisor < 0) {
            divisor = -divisor;
            dividend = -dividend;
        }
        if (divisor == 1) return dividend;

        int sign = 1;
        if (dividend < 0) {
            sign = -1;
            dividend = -dividend;
        }

        int res = -1;
        for (; dividend >= 0; dividend -= divisor) {
            res++;
        }
        return sign > 0 ? res : -res;
    }

    // beats 75.64% (2ms) // only 8.94%(3 ms) if use countBits
    // beats 4.70%(55 ms) // new percentage
    public int divide(int dividend, int divisor) {
        if (divisor == 0) return Integer.MAX_VALUE;
        if (dividend == divisor) return 1;
        if (dividend == 0 || divisor == Integer.MIN_VALUE) return 0;

        int overflow = 0;
        if (dividend == Integer.MIN_VALUE) {
            if (divisor == -1) return Integer.MAX_VALUE;
            if (divisor == 1) return dividend;

            if (divisor < 0) {
                dividend -= divisor;
            } else {
                dividend += divisor;
            }
            overflow = 1;
        }

        if (divisor < 0) {
            divisor = -divisor;
            dividend = -dividend;
        }
        if (divisor == 1) return dividend;

        int sign = 1;
        if (dividend < 0) {
            sign = -1;
            dividend = -dividend;
        }
        if (dividend < divisor) return sign > 0 ? overflow : -overflow;

        int adjust = 1;
        int max = Integer.MAX_VALUE >> 1; // avoid overflow
        int shift = -1;
        for (; divisor < dividend; divisor <<= 1) {
            shift++;
            if (divisor >= max) {
                adjust = 0;
                break;
            }
        }
        divisor >>= adjust;
        dividend -= divisor;
        divisor >>= shift;
        int res = (1 << shift) + divide(dividend, divisor) + overflow;
        return sign > 0 ? res : -res;
    }

    // Solution of Choice
    // Binary Search
    // beats 42.45%(36 ms)
    public int divide2(int dividend, int divisor) {
        if (divisor == 0 || (dividend == Integer.MIN_VALUE && divisor == -1)) {
            return Integer.MAX_VALUE;
        }

        int sign = (dividend > 0 ^ divisor > 0) ? -1 : 1;
        long target = Math.abs((long)dividend);
        long upper = Math.abs((long)divisor);
        long power = 1;
        for (; upper < target; upper <<= 1, power <<= 1) {}
        if (upper == target) return (int)(sign > 0 ? power : -power);

        power >>= 1;
        for (long res = 0, lower = 0; ; power >>= 1) {
            long mid = (lower + upper) >> 1;
            if (mid > target) {
                upper = mid;
            } else {
                res += power;
                lower = mid;
            }
            if (mid == target || power == 0) return (int)(sign > 0 ? res : -res);
        }
    }

    // beats 39.32%(37 ms)
    public int divide3(int dividend, int divisor) {
        if (divisor == 0 || (dividend == Integer.MIN_VALUE && divisor == -1)) {
            return Integer.MAX_VALUE;
        }
        int sign = ((dividend < 0) ^ (divisor < 0)) ? -1 : 1;
        long res = 0;
        for (long p = Math.abs((long)dividend), q = Math.abs((long)divisor),
             power = 1; p >= q; res += power) {
            long base = q;
            for (power = 1; p >= (base << 1); base <<= 1, power <<= 1) {}
            p -= base;
        }
        return (int)(sign == 1 ? res : -res);
    }

    // dumped
    int countBits(int n) {
        int bits = 0;
        for (; n > 0; n >>= 1) bits++;
        return bits;
    }

    void test(int a, int b) {
        assertEquals(a / b, divide(a, b));
        assertEquals(a / b, divide2(a, b));
        assertEquals(a / b, divide3(a, b));
    }

    void test(int a, int b, int expected) {
        assertEquals(expected, divide(a, b));
        assertEquals(expected, divide2(a, b));
        assertEquals(expected, divide3(a, b));
    }

    @Test
    public void test1() {
        for (int i = 1; i < 100; i++) {
            for (int j = 1; j < 20; j++) {
                test(i, j);
            }
        }
        test(0, 2);
        test(10, -2);
        test(-10, 2);
        test(-2, 10);
        test(2, -10);
    }

    @Test
    public void test2() {
        test(1000000, 3);
        test(-1010369383, -2147483648);
        test(-1010369383, 2147483647);
        test(-2147483648, 2);
        test(-690731771, -1401361801);
        test(-2147483648, 1262480350);
        test(-2147483648, -2147483648);
        test(-2147483648, -1, Integer.MAX_VALUE);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Division");
    }
}
