import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC248: https://leetcode.com/problems/strobogrammatic-number-iii/
//
// A strobogrammatic number is a number that looks the same when rotated 180 degrees.
// Write a function to count the total strobogrammatic numbers that exist in the range of low <= num <= high.
public class StrobogrammaticNumber3 {
    private static final char[] PAIR_MAP = {'0', '1', 0, 0, 0, 0, '9', 0, '8', '6'};
    private static final char[] VALID_CHARS = {1, 1, 0, 0, 0, 0, 1, 0, 1, 1};
    private static final int[] GREATER_COUNTS1 = {3, 2, 1, 1, 1, 1, 1, 1, 1, 0};
    private static final int[] GREATER_COUNTS2 = {4, 3, 3, 3, 3, 3, 2, 2, 1, 0};

    // beats95.78%(0 ms for 16 tests)
    public int strobogrammaticInRange(String low, String high) {
        int m = low.length();
        int n = high.length();
        if (n < m || n == m && low.compareTo(high) > 0) return 0;

        int res = 0;
        for (int i = m + 1; i < n; i++) {
            res += strobogrammaticCount(i, 4);
        }
        res += strobogrammaticGreaterCount(low);
        res += strobogrammaticCount(n, 4) - strobogrammaticGreaterCount(high) + 1;
        for (int i = 0, j = n - 1; i <= j; i++, j--) {
            if (PAIR_MAP[high.charAt(i) - '0'] != high.charAt(j)) {
                res--;
                break;
            }
        }
        return (m == n) ? res - strobogrammaticCount(m, 4) : res;
    }

    private int strobogrammaticGreaterCount(String num) {
        int m = num.length();
        if (m == 0) return 1;
        if (m == 1) return GREATER_COUNTS1[num.charAt(0) - '0'];

        int count = 0;
        int i = 0;
        for (; i < m; i++) {
            int bigs = GREATER_COUNTS2[num.charAt(i) - '0'];
            if (bigs > 0) {
                if (m < (i + 1) * 2) return m == i * 2 ? 1 : 0;

                count += bigs * strobogrammaticCount(m - (i + 1) * 2, 5);
                break;
            }
        }
        if (i < m && VALID_CHARS[num.charAt(i) - '0'] != 0) { // same prefix digits
            String middle = num.substring(i + 1, m - (i + 1));
            count += strobogrammaticGreaterCount(middle);
            if (!middle.isEmpty() && !isMax(middle)) return count;

            for (int j = i; j >= 0; j--) {
                int diff = PAIR_MAP[num.charAt(j) - '0'] - num.charAt(m - j - 1);
                if (diff > 0) return count;
                if (diff < 0) return count - 1;
            }
        }
        return count;
    }

    private boolean isMax(String num) {
        int m = num.length();
        int i = 0;
        for (; i < m / 2; i++) {
            if (num.charAt(i) != '9') return false;
        }
        if (m % 2 == 1) {
            if (num.charAt(i++) < '8') return false;
        }
        for (; i < m; i++) {
            if (num.charAt(i) < '6') return false;
        }
        return true;
    }

    private int strobogrammaticCount(int n, int first) {
        if (n == 0) return 1;
        if (n % 2 == 1) return strobogrammaticCount(n - 1, first) * 3;

        int count = first;
        for (int i = n / 2 - 1; i > 0; i--) {
            count *= 5;
        }
        return count;
    }

    void test(String low, String high, int expected) {
        assertEquals(expected, strobogrammaticInRange(low, high));
    }

    @Test
    public void test() {
        test("0", "0", 1);
        test("1", "0", 0);
        test("1", "1", 1);
        test("1", "9", 2);
        test("11", "69", 2);
        test("10", "69", 2);
        test("50", "100", 3);
        test("888", "999", 4);
        test("888", "10000", 24);
        test("789", "10000", 26);
        test("0", "1000", 19);
        test("0", "10000", 39);
        test("10", "10000", 36);
        test("889", "999", 3);
        test("889", "999000", 182);
        test("1001", "11111", 25);
        test("10000", "11111", 5);
        test("1001", "9999", 20);
        test("1000", "9999", 20);
        test("10000", "99999", 60);
        test("1000", "999000", 179);
        test("100000", "999999", 100);
        test("999000", "999999", 1);
        test("100000", "999000", 99);
        test("100000", "999999", 100);
        test("190010", "999999", 80);
        test("960010", "999999", 15);
        test("96600910", "99999999", 65);
        test("16600118", "99999999", 440);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrobogrammaticNumber3");
    }
}
