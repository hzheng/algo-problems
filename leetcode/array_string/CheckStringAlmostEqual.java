import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

// LC1790: https://leetcode.com/problems/check-if-one-string-swap-can-make-strings-equal/
//
// You are given two strings s1 and s2 of equal length. A string swap is an operation where you
// choose two indices in a string (not necessarily different) and swap the characters at these
// indices.
// Return true if it is possible to make both strings equal by performing at most one string swap on
// exactly one of the strings. Otherwise, return false.
//
// Constraints:
// 1 <= s1.length, s2.length <= 100
// s1.length == s2.length
// s1 and s2 consist of only lowercase English letters.
public class CheckStringAlmostEqual {
    // time complexity: O(N^2), space complexity: O(N)
    // 45 ms(100.00%), 39.1 MB(100.00%) for 113 tests
    public boolean areAlmostEqual(String s1, String s2) {
        int n = s1.length();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char[] s = s1.toCharArray();
                char c = s[i];
                s[i] = s[j];
                s[j] = c;
                if (s2.equals(String.valueOf(s))) { return true; }
            }
        }
        return false;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 37.1 MB(100.00%) for 113 tests
    public boolean areAlmostEqual2(String s1, String s2) {
        int n = s1.length();
        int[] freq = new int[26];
        for (int i = 0; i < n; i++) {
            freq[s1.charAt(i) - 'a']++;
            freq[s2.charAt(i) - 'a']--;
        }
        for (int f : freq) {
            if (f != 0) { return false; }
        }
        for (int i = 0, diff = 0; i < n; i++) {
            if (s1.charAt(i) != s2.charAt(i) && ++diff > 2) { return false; }
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.9 MB(100.00%) for 113 tests
    public boolean areAlmostEqual3(String s1, String s2) {
        int n = s1.length();
        int diff1 = 0;
        int diff2 = 0;
        for (int i = 0; i < n; i++) {
            if (s1.charAt(i) == s2.charAt(i)) { continue; }

            if (diff1 < 0) { return false; }

            if (diff1 == 0) {
                diff1 = s1.charAt(i);
                diff2 = s2.charAt(i);
            } else if (diff1 == s2.charAt(i) && diff2 == s1.charAt(i)) {
                diff1 = -1;
            } else { return false; }
        }
        return diff1 <= 0;
    }

    private void test(String s1, String s2, boolean expected) {
        assertEquals(expected, areAlmostEqual(s1, s2));
        assertEquals(expected, areAlmostEqual2(s1, s2));
        assertEquals(expected, areAlmostEqual3(s1, s2));
    }

    @Test public void test() {
        test("bank", "kanb", true);
        test("attack", "defend", false);
        test("kelb", "kelb", true);
        test("abcd", "dcba", false);
        test("x", "z", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
