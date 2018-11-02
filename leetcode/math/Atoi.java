import org.junit.Test;
import static org.junit.Assert.*;

// LC008: https://leetcode.com/problems/string-to-integer-atoi/
//
// Implement atoi to convert a string to an integer.
public class Atoi {
    // beats 14.48% (5 ms) (35.97% 4 ms if use str.trim)
    public int myAtoi(String str) {
        int len = str.length();
        if (len == 0) return 0;

        int sign = 1;
        int i = 0;
        int start = 0;
        char c = str.charAt(0);
        boolean allowSpace = true;
        for (; i < len; i++) {
            c = str.charAt(i);
            if (c > '0' && c <= '9') break;

            if (c == '-') {
                if (i > start) return 0;
                sign = -1;
                allowSpace = false;
            } else if (c == '+') {
                if (i > start) return 0;
                allowSpace = false;
            } else if (Character.isWhitespace(c)) {
                if (!allowSpace) return 0;
                start++;
            } else if (c != '0') {
                return 0;
            }
        }

        int x = 0;
        boolean limited = true;
        for (int actualDigits = 0; i < len; i++, actualDigits++) {
            c = str.charAt(i);
            if (c < '0' || c > '9') break;

            if (actualDigits < 9) {
                x *= 10;
                x += c - '0';
            } else {
                if (i + 1 < len && Character.isDigit(str.charAt(i + 1))) {
                    return sign > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                }
                limited = false;
                break;
            }
        }
        if (limited) return x * sign;

        if (x > Integer.MAX_VALUE / 10) {
            return sign > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }

        int lastDigit = str.charAt(i) - '0';
        if (x == Integer.MAX_VALUE / 10) {
            int maxLastDigit = Integer.MAX_VALUE % 10;
            if (sign > 0) {
                if (lastDigit > maxLastDigit) return Integer.MAX_VALUE;
            } else {
                if (lastDigit >= maxLastDigit + 1) return Integer.MIN_VALUE;
            }
        }
        // now we're safe
        x *= 10;
        x += lastDigit;
        return x * sign;
    }

    // beats 42.47%(32 ms for 1079 tests)
    public int myAtoi2(String str) {
        int len = str.length();
        int i = 0;
        for (; i < len && str.charAt(i) == ' '; i++) {}
        if (i == len) return 0;

        int sign = 1;
        if (str.charAt(i) == '-' || str.charAt(i) == '+') {
            sign = str.charAt(i++) == '-' ? -1 : 1;
        }
        int res = 0;
        final int maxVal = Integer.MAX_VALUE / 10;
        for (; i < len; i++) {
            int digit = str.charAt(i) - '0';
            if (digit < 0 || digit > 9) break;

            if (res > maxVal || (res == maxVal && digit > 7)) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            res = 10 * res + digit;
        }
        return res * sign;
    }

    // beats 54.20%(28 ms for 1079 tests)
    public int myAtoi3(String str) {
        int sign = 0;
        long res = 0;
        for (char c : str.toCharArray()) {
            if (c >= '0' && c <= '9') {
                if (sign == 0) {
                    sign = 1;
                }
                res = res * 10 + (c - '0');
                if (res > Integer.MAX_VALUE) break;
            } else if (sign == 0) {
                if (c == ' ') continue;
                if (c == '+') {
                    sign = 1;
                } else if (c == '-') {
                    sign = -1;
                } else return 0;
            } else break;
        }
        res *= sign;
        if (res > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return (res < Integer.MIN_VALUE) ? Integer.MIN_VALUE : (int)res;
    }

    void test(String x, int expected) {
        assertEquals(expected, myAtoi(x));
        assertEquals(expected, myAtoi2(x));
        assertEquals(expected, myAtoi3(x));
    }

    @Test
    public void test1() {
        test(" 010", 10);
        test("", 0);
        test(" ", 0);
        test("0", 0);
        test("-0", 0);
        test("1", 1);
        test("12", 12);
        test("012", 12);
        test("000012", 12);
        test("-12", -12);
        test("+12", 12);
        test("+1a2", 1);
        test("++2", 0);
        test("0012k23k26", 12);
        test("+", 0);
        test("-", 0);
        test("+0001147483647", 1147483647);
        test("0001147483647", 1147483647);
        test("1147483647", 1147483647);
        test("-1147483647", -1147483647);
        test("-0001147483647", -1147483647);
        test("2147483647", 2147483647);
        test("+2147483647", 2147483647);
        test("2147483648", 2147483647);
        test("2147483649", 2147483647);
        test("-2147483647", -2147483647);
        test("-2147483647a ", -2147483647);
        test("-2147483648ab ", -2147483648);
        test("-2147483649", -2147483648);
        test("12147483649", 2147483647);
        test("9223372036854775808", 2147483647);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
