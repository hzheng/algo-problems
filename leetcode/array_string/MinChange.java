import org.junit.Test;

import static org.junit.Assert.*;

// LC1758: https://leetcode.com/problems/minimum-changes-to-make-alternating-binary-string/
//
// You are given a string s consisting only of the characters '0' and '1'. In one operation, you can
// change any '0' to '1' or vice versa. The string is called alternating if no two adjacent
// characters are equal. For example, the string "010" is alternating, while "0100" is not.
// Return the minimum number of operations needed to make s alternating.
//
// Constraints:
// 1 <= s.length <= 10^4
// s[i] is either '0' or '1'.
public class MinChange {
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(80.00%), 38.7 MB(100.00%) for 89 tests
    public int minOperations(String s) {
        int change = 0;
        int n = s.length();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) - '0' == (i % 2)) {
                change++;
            }
        }
        return Math.min(change, n - change);
    }

    private void test(String s, int expected) {
        assertEquals(expected, minOperations(s));
    }

    @Test public void test() {
        test("0100", 1);
        test("10", 0);
        test("1111", 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
