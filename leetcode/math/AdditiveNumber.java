import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Additive number is a string whose digits can form additive sequence.
// A valid additive sequence should contain at least three numbers. Except for
// the first two numbers, each subsequent number in the sequence must be the
// sum of the preceding two.
public class AdditiveNumber {
    // beats 95.40(1 ms)
    public boolean isAdditiveNumber(String num) {
        int len = num.length();
        int max1 = len / 2;
        for (int j = 1; j <= max1; j++) {
            int max2 = len - 1;
            if (num.charAt(j) == '0') {
                max2 = j + 1;
            }
            for (int k = j + 1; k <= max2; k++) {
                if (isAdditiveNumber(num, 0, j, k, len)) return true;
            }
        }
        return false;
    }

    private boolean isAdditiveNumber(String num, int i, int j, int k, int len) {
        int l = k + Math.max(j - i, k - j);
        if (l > len) return false;

        if (isAdditive(num, i, j, k, l)) {
            if (l == len || isAdditiveNumber(num, j, k, l, len)) return true;
        }

        if (num.charAt(k) != '1') return false;

        if (++l <= len && isAdditive(num, i, j, k, l)) {
            if (l == len || isAdditiveNumber(num, j, k, l, len)) return true;
        }
        return false;
    }

    private boolean isAdditive(String num, int i, int j, int k, int l) {
        int carry = 0;
        int c = l - 1;
        for (int a = j - 1, b = k - 1; a >= i || b >= j; a--, b--, c--) {
            int m = a >= i ? num.charAt(a) - '0' : 0;
            int n = b >= j ? num.charAt(b) - '0' : 0;
            int sum = m + n + carry;
            if (sum % 10 != (num.charAt(c) - '0')) return false;

            carry = sum / 10;
        }
        return (c + 1 == k) && (carry == 0) || (carry == 1);
    }

    // beats 74.34%(2 ms)
    public boolean isAdditiveNumber2(String num) {
        int len = num.length();
        if (len < 3) return false;

        int max1 = len / 2;
        if (num.charAt(0) == '0') {
            max1 = 0;
        }
        for (int i = 0; i <= max1; i++) {
            int max2 = len - i - 2;
            if (num.charAt(i + 1) == '0') {
                max2 = i + 1;
            }
            for (int j = i + 1; j <= max2; j++) {
                String first = num.substring(0, i + 1);
                String second = num.substring(i + 1, j + 1);
                if (isAdditive(num, j + 1, first, second, len)) return true;
            }
        }
        return false;
    }

    private boolean isAdditive(String num, int start, String first,
                               String second, int len) {
        if (start == len) return true;

        long sum = Long.parseLong(first) + Long.parseLong(second);
        int sumLen = Long.toString(sum).length();
        if (start + sumLen > len) return false;

        String third = num.substring(start, start + sumLen);
        return (Long.parseLong(third) == sum) &&
               isAdditive(num, start + sumLen, second, third, len);
    }

    // beats 74.34%(2 ms)
    public boolean isAdditiveNumber3(String num) {
        int len = num.length();
        if (len < 3) return false;

        int max1 = len / 2;
        if (num.charAt(0) == '0') {
            max1 = 1;
        }
        for (int i = 1; i <= max1; ++i) {
            int max2 = len - i;
            if (num.charAt(i) == '0') {
                max2 = i + 1;
            }
            for (int j = i + 1; j <= max2; j++) {
                if (isAdditive(num, 0, i, j)) return true;
            }
        }
        return false;
    }

    private boolean isAdditive(String num, int i, int j, int k) {
        long first = Long.parseLong(num.substring(i, j));
        long second = Long.parseLong(num.substring(j, k));
        String sum = String.valueOf(first + second);
        if (!num.substring(k).startsWith(sum)) return false;

        return (k + sum.length() == num.length())
               || isAdditive(num, j, k, k + sum.length());
    }

    // non-recursion
    // beats 74.34%(2 ms)
    public boolean isAdditiveNumber4(String num) {
        int len = num.length();
        if (len < 3) return false;

        int max1 = len / 2;
        if (num.charAt(0) == '0') {
            max1 = 1;
        }
        for (int i = 1; i <= max1; ++i) {
            int max2 = len - i;
            if (num.charAt(i) == '0') {
                max2 = i + 1;
            }
            long firstNum = Long.parseLong(num.substring(0, i));
            for (int j = i + 1; j <= max2; j++) {
                long first = firstNum;
                long second = Long.parseLong(num.substring(i, j));
                int k = j;
                while (k < len) {
                    long sum = first + second;
                    String sumStr = String.valueOf(sum);
                    if (!num.substring(k).startsWith(sumStr)) break;

                    first = second;
                    second = sum;
                    k += sumStr.length();
                }
                if (k == len) return true;
            }
        }
        return false;
    }

    void test(String num, boolean expected) {
        assertEquals(expected, isAdditiveNumber(num));
        assertEquals(expected, isAdditiveNumber2(num));
        assertEquals(expected, isAdditiveNumber3(num));
        assertEquals(expected, isAdditiveNumber4(num));
    }

    @Test
    public void test1() {
        test("0", false);
        test("0235813", false);
        test("1023", false);
        test("101", true);
        test("011", true);
        test("0112", true);
        test("0145", false);
        test("000", true);
        test("111", false);
        test("123", true);
        test("10203", false);
        test("124", false);
        test("1203", false);
        test("112358", true);
        test("112359", false);
        test("199100199", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AdditiveNumber");
    }
}
