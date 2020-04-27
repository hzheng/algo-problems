import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1422: https://leetcode.com/problems/maximum-score-after-splitting-a-string/
//
// Given a string s of zeros and ones, return the maximum score after splitting the string into two
// non-empty substrings (i.e. left substring and right substring).
// The score after splitting a string is the number of zeros in the left substring plus the number
// of ones in the right substring.
// Constraints:
// 2 <= s.length <= 500
// The string s consists of characters '0' and '1' only.
public class MaxScoreAfterSplit {
    // time complexity: O(N ^ 2), space complexity: O(1)
    // 7 ms(30.21%), 37.3 MB(100%) for 103 tests
    public int maxScore(String s) {
        int res = 0;
        for (int i = 0, n = s.length(); i < n - 1; i++) {
            int cur = 0;
            for (int j = 0; j < n; j++) {
                int digit = s.charAt(j) - '0';
                cur += (i < j) ? digit : (1 - digit);
            }
            res = Math.max(res, cur);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 37.3 MB(100%) for 103 tests
    public int maxScore2(String s) {
        int ones = 0;
        int diff = Integer.MIN_VALUE; // diff between left 0's and 1's
        for (int i = 0, zeros = 0, n = s.length(); i < n; i++) {
            if (s.charAt(i) == '0') {
                zeros++;
            } else {
                ones++;
            }
            if (i != n - 1) {
                diff = Math.max(zeros - ones, diff);
            }
        }
        return diff + ones;
    }

    private void test(String s, int expected) {
        assertEquals(expected, maxScore(s));
        assertEquals(expected, maxScore2(s));
    }

    @Test public void test() {
        test("011101", 5);
        test("00111", 5);
        test("1111", 3);
        test("00000", 4);
        test("0001110011111100000111111", 18);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
