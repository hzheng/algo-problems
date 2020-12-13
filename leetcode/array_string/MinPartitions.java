import org.junit.Test;

import static org.junit.Assert.*;

// LC1689: https://leetcode.com/problems/partitioning-into-minimum-number-of-deci-binary-numbers/
//
// A decimal number is called deci-binary if each of its digits is either 0 or 1 without any leading
// zeros. For example, 101 and 1100 are deci-binary, while 112 and 3001 are not.
// Given a string n that represents a positive decimal integer, return the minimum number of
// positive deci-binary numbers needed so that they sum up to n.
//
// Constraints:
// 1 <= n.length <= 10^5
// n consists of only digits.
// n does not contain any leading zeros and represents a positive integer.
public class MinPartitions {
    // time complexity: O(N), space complexity: O(1)
    // 11 ms(14.29%), 39 MB(100.00%) for 94 tests
    public int minPartitions(String n) {
        int res = 0;
        for (int i = 0, len = n.length(), max = 0; i < len; i++) {
            int d = n.charAt(i) - '0';
            res += Math.max(0, d - max);
            max = Math.max(max, d);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 10 ms(28.57%), 51.4 MB(28.57%) for 94 tests
    public int minPartitions2(String n) {
        int res = 0;
        for (int i = 0, len = n.length(); i < len; i++) {
            res = Math.max(res, n.charAt(i));
        }
        return res - '0';
    }

    // time complexity: O(N), space complexity: O(1)
    // 4 ms(100.00%), 39.6 MB(42.86%) for 94 tests
    public int minPartitions3(String n) {
        int res = 0;
        for (int i = 0, len = n.length(); i < len; i++) {
            res = Math.max(res, n.charAt(i));
            if (res == '9') { break; }
        }
        return res - '0';
    }

    // time complexity: O(N), space complexity: O(1)
    // 19 ms(14.29%), 39.1 MB(100.00%) for 94 tests
    public int minPartitions4(String n) {
        return n.chars().max().getAsInt() - '0';
    }

    private void test(String n, int expected) {
        assertEquals(expected, minPartitions(n));
        assertEquals(expected, minPartitions2(n));
        assertEquals(expected, minPartitions3(n));
        assertEquals(expected, minPartitions4(n));
    }

    @Test public void test() {
        test("32", 3);
        test("82734", 8);
        test("27346209830709182346", 9);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
