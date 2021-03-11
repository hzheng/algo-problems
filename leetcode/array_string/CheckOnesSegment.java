import org.junit.Test;

import static org.junit.Assert.*;

// LC1784: https://leetcode.com/problems/check-if-binary-string-has-at-most-one-segment-of-ones/
//
// Given a binary string s without leading zeros, return true if s contains at most one contiguous
// segment of ones. Otherwise, return false.
//
// Constraints:
// 1 <= s.length <= 100
// s[i] is either '0' or '1'.
// s[0] is '1'.
public class CheckOnesSegment {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.8 MB(100.00%) for 191 tests
    public boolean checkOnesSegment(String s) {
        boolean hasZero = false;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                hasZero = true;
            } else if (hasZero) { return false; }
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 37.2 MB(100.00%) for 191 tests
    public boolean checkOnesSegment2(String s) {
        for (int i = 2; i < s.length(); i++) {
            if (s.charAt(i - 1) == '0' && s.charAt(i) == '1') { return false; }
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.8 MB(100.00%) for 191 tests
    public boolean checkOnesSegment3(String s) {
        return !s.contains("01");
    }

    private void test(String s, boolean expected) {
        assertEquals(expected, checkOnesSegment(s));
        assertEquals(expected, checkOnesSegment2(s));
        assertEquals(expected, checkOnesSegment3(s));
    }

    @Test public void test() {
        test("1001", false);
        test("110", true);
        test("111110", true);
        test("111110111", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
