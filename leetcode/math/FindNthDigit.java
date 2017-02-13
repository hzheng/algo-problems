import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC400: https://leetcode.com/problems/nth-digit/
//
// Find the nth digit of the infinite integer sequence 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ...
// n is positive and will fit within the range of a 32-bit signed integer (n < 2 ^ 31).
public class FindNthDigit {
    // beats 38.51%(6 ms for 70 tests)
    public int findNthDigit(int n) {
        long power = 1;
        int digits = 1;
        long offset = n - 1;
        for (;; power *= 10, digits++) {
            long groupLen = digits * (power * 9);
            if (offset >= groupLen) {
                offset -= groupLen;
            } else break;
        }
        int res = 0;
        for (long i = offset % digits, j = power + offset / digits; i < digits; i++, j /= 10) {
            res = (int)(j % 10);
        }
        return res;
    }

    // Solution of Choice
    // beats 38.51%(6 ms for 70 tests)
    public int findNthDigit2(int n) {
        int digits = 1;
        int power = 1;
        int offset = n - 1;
        for (long count = 9; offset > digits * count; count *= 10, power *= 10, digits++) {
            offset -= digits * count;
        }
        return Integer.toString(power + offset / digits).charAt(offset % digits) - '0';
    }

    // beats 17.68%(7 ms for 70 tests)
    public int findNthDigit3(int n) {
        int digits = 1;
        int power = 1;
        int offset = n - 1;
        for (; offset / 9 / power / digits >= 1; power *= 10, digits++) {
            offset -= 9 * power * digits;
        }
        return Integer.toString(power + offset / digits).charAt(offset % digits) - '0';
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
