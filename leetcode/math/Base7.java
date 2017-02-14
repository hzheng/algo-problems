import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC504: https://leetcode.com/problems/base-7/
//
// Given an integer, return its base 7 string representation.
public class Base7 {
    // beats 0.26%(19 ms for 241 tests)
    public String convertToBase7(int num) {
        if (num == 0) return "0";

        boolean negative = false;
        if (num < 0) {
            if (num == Integer.MIN_VALUE) return convertToBase7(num / 7) + (-(num % 7));

            num = -num;
            negative = true;
        }
        StringBuilder sb = new StringBuilder();
        for (int n = num; n > 0; n /= 7) {
            sb.append(n % 7);
        }
        if (negative) {
            sb.append("-");
        }
        return sb.reverse().toString();
    }

    // beats 0.26%(17 ms for 241 tests)
    public String convertToBase7_2(int num) {
        return Integer.toString(num, 7);
    }

    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    // from openjdk's source code
    // beats 0.26%(18 ms for 241 tests)
    public String convertToBase7_3(int num) {
        char buf[] = new char[33];
        boolean negative = (num < 0);
        if (!negative) {
            num = -num;
        }
        final int radix = 7;
        int charPos = 32;
        for ( ; num <= -radix; num /= radix) {
            buf[charPos--] = DIGITS[-(num % radix)];
        }
        buf[charPos] = DIGITS[-num];
        if (negative) {
            buf[--charPos] = '-';
        }
        return new String(buf, charPos, (33 - charPos));
    }

    void test(int num, String expected) {
        assertEquals(expected, convertToBase7(num));
        assertEquals(expected, convertToBase7_2(num));
        assertEquals(expected, convertToBase7_3(num));
    }

    @Test
    public void test() {
        test(0, "0");
        test(7, "10");
        test(100, "202");
        test(-7, "-10");
        test(Integer.MAX_VALUE, "104134211161");
        test(Integer.MIN_VALUE, "-104134211162");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Base7");
    }
}
