import org.junit.Test;

import static org.junit.Assert.*;

// LC1881: https://leetcode.com/problems/maximum-value-after-insertion/
//
// You are given a very large integer n, represented as a string, and an integer digit x. The digits
// in n and the digit x are in the inclusive range [1, 9], and n may represent a negative number.
// You want to maximize n's numerical value by inserting x anywhere in the decimal representation of
// n. You cannot insert x to the left of the negative sign.
// For example, if n = 73 and x = 6, it would be best to insert it between 7 and 3, making n = 763.
// If n = -55 and x = 2, it would be best to insert it before the first 5, making n = -255.
// Return a string representing the maximum value of n after the insertion.
//
// Constraints:
// 1 <= n.length <= 105
// 1 <= x <= 9
// The digits in n are in the range [1, 9].
// n is a valid representation of an integer.
// In the case of a negative n, it will begin with '-'.
public class MaxAfterInsertion {
    // time complexity: O(N), space complexity: O(1)
    // 21 ms(49.21%), 71.5 MB(49.22%) for 97 tests
    public String maxValue(String n, int x) {
        int sign = (n.charAt(0) == '-') ? 1 : -1;
        for (int i = (sign + 1) / 2, len = n.length(); i < len; i++) {
            if ((n.charAt(i) - '0' - x) * sign > 0) {
                return n.substring(0, i) + x + n.substring(i);
            }
        }
        return n + x;
    }

    private void test(String n, int x, String expected) {
        assertEquals(expected, maxValue(n, x));
    }

    @Test public void test1() {
        test("99", 9, "999");
        test("-13", 2, "-123");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
