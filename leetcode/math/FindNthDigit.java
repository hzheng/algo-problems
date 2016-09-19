import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC400: https://leetcode.com/problems/nth-digit/
//
// Find the nth digit of the infinite integer sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...
// n is positive and will fit within the range of a 32-bit signed integer (n < 2 ^ 31).
public class FindNthDigit {
    // beats N/A(6 ms)
    public int findNthDigit(int n) {
        if (n < 10) return n;

        long power = 1;
        long digits = 1;
        long l = n - 1;
        for (;; power *= 10, digits++) {
            long group = digits * (power * 9);
            if (l >= group) {
                l -= group;
            } else break;
        }
        long order = l / digits;
        long num = power + order;
        long res = 0;
        for (long i = digits - (l - order * digits) - 1L; i >= 0; i--, num /= 10) {
            res = num % 10L;
        }
        return (int)res;
    }

    // https://discuss.leetcode.com/topic/59314/java-solution
    // beats N/A(6 ms)
    public int findNthDigit2(int n) {
        int len = 1;
        int start = 1;
        for (long count = 9; n > len * count; count *= 10, start *= 10, len++) {
            n -= len * count;
        }
        start += (n - 1) / len;
        String s = Integer.toString(start);
        return Character.getNumericValue(s.charAt((n - 1) % len));
    }

    // https://discuss.leetcode.com/topic/59378/short-python-java
    // beats N/A(6 ms)
    public int findNthDigit3(int n) {
        int digits = 1;
        int first = 1;
        for (n--; n / 9 / first / digits >= 1; first *= 10, digits++) {
            n -= 9 * first * digits;
        }
        return Integer.toString(first + n / digits).charAt(n % digits) - '0';
    }

    void test(int n, int expected) {
        assertEquals(expected, findNthDigit(n));
        assertEquals(expected, findNthDigit2(n));
        assertEquals(expected, findNthDigit3(n));
    }

    @Test
    public void test1() {
        test(1000000000, 1);
        test(1, 1);
        test(2, 2);
        test(10, 1);
        test(11, 0);
        test(12, 1);
        test(15, 2);
        test(16, 1);
        test(17, 3);
        test(27, 8);
        test(29, 9);
        test(30, 2);
        test(31, 0);
        test(171, 0);
        test(189, 9);
        test(190, 1);
        test(191, 0);
        test(192, 0);
        test(193, 1);
        test(194, 0);
        test(195, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindNthDigit");
    }
}
