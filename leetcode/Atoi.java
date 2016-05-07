import org.junit.Test;
import static org.junit.Assert.*;

public class Atoi {
    // beats 14.48% (35.97% if use str.trim)
    public int myAtoi(String str) {
        if (str == null) return 0;
        // str = str.trim();

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

    void test(String x, int expected) {
        assertEquals(expected, myAtoi(x));
    }

    @Test
    public void test1() {
        test(" 010", 10);
        test("", 0);
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
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Atoi");
    }
}
