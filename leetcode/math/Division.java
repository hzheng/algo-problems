import org.junit.Test;
import static org.junit.Assert.*;

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

    // beats 75.64% (only 8.94% if use countBits)
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

    // TODO: binary search

    // dumped
    int countBits(int n) {
        int bits = 0;
        for (; n > 0; n >>= 1) bits++;
        return bits;
    }

    void test(int a, int b) {
        // assertEquals(a / b, divide(a, b));
        assertEquals(a / b, divide(a, b));
    }

    @Test
    public void test1() {
        for (int i = 1; i < 100; i++) {
            for (int j = 1; j < 20; j++) {
                test(i, j);
            }
        }
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
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Division");
    }
}
