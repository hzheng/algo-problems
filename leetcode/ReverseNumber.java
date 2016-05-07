import org.junit.Test;
import static org.junit.Assert.*;

public class ReverseNumber {
    // beats 45.49%
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

    void test(int x, int expected) {
        assertEquals(expected, reverse(x));
    }

    @Test
    public void test1() {
        test(-2, -2);
        test(-12, -21);
        test(1463847412, 2147483641);
        test(-2147483648, 0);
        test(1534236469, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseNumber");
    }
}
