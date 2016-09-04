import org.junit.Test;
import static org.junit.Assert.*;

// LC007: https://leetcode.com/problems/reverse-integer/
//
// Reverse digits of an integer.
public class ReverseNumber {
    // beats 48.48%(2 ms)
    public int reverse(int x) {
        if (x == 0) return 0;

        int sign = 1;
        if (x < 0) {
            sign = -1;
            x = -x;
        }
        int y = 0;
        for (; x > 9; x /= 10) {
            y *= 10;
            y += x % 10;
        }
        int firstDigt = x % 10;
        if (y > (Integer.MAX_VALUE - firstDigt) / 10) {
            return 0;
        }
        y *= 10;
        y += firstDigt;
        return y * sign;
    }

    // beats 20.05%(3 ms)
    public int reverse2(int x) {
        int res = 0;
        for (; x != 0; x /= 10) {
            int lastDigit = x % 10;
            int nextRes = res * 10 + lastDigit;
            if ((nextRes - lastDigit) / 10 != res) return 0;

            res = nextRes;
        }
        return res;
    }

    // Solution of Choice
    // beats 48.48%(2 ms)
    public int reverse3(int x) {
        long res = 0;
        for (; x != 0; x /= 10) {
            res = res * 10 + x % 10;
        }
        return res > Integer.MAX_VALUE || res < Integer.MIN_VALUE ? 0 : (int)res;
    }

    void test(int x, int expected) {
        assertEquals(expected, reverse(x));
        assertEquals(expected, reverse2(x));
        assertEquals(expected, reverse3(x));
    }

    @Test
    public void test1() {
        test(-2, -2);
        test(-12, -21);
        test(-123, -321);
        test(-12345, -54321);
        test(12345, 54321);
        test(1463847412, 2147483641);
        test(-2147483648, 0);
        test(1534236469, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseNumber");
    }
}
