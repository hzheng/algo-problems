import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC392: https://leetcode.com/problems/is-subsequence/
//
// Given a string s and a string t, check if s is subsequence of t.
// Follow up:
// If there are lots of incoming S, say S1, S2, ... , Sk where k >= 1B, and you
// want to check one by one to see if T has its subsequence. In this scenario,
// how would you change your code?
public class IsSubsequence {
    // Two Pointers
    // beats 44.79%(46 ms for 14 tests)
    public boolean isSubsequence(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        int i = 0;
        for (int j = 0; i < sLen && j < tLen; j++) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
        }
        return i == sLen;
    }

    // Two Pointers
    // beats 66.68%(33 ms for 14 tests)
    public boolean isSubsequence2(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        for (int i = 0, j = 0; i < sLen && j < tLen; j++) {
            if (s.charAt(i) == t.charAt(j)) {
                if (++i == sLen) return true;
            }
        }
        return sLen == 0;
    }

    // beats 96.66%(2 ms for 14 tests)
    public boolean isSubsequence3(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        if (tLen < sLen) return false;

        for (int i = 0, prev = 0; i < sLen; i++, prev++) {
            prev = t.indexOf(s.charAt(i), prev);
            if (prev == -1) return false;
        }
        return true;
    }

    // Solution of Choice
    // beats 96.66%(2 ms for 14 tests)
    public boolean isSubsequence4(String s, String t) {
        int index = 0;
        for (char c : s.toCharArray()) {
            index = t.indexOf(c, index);
            if (++index == 0) return false;
        }
        return true;
    }

    // Recursion
    // beats 45.96%(45 ms for 14 tests)
    public boolean isSubsequence5(String s, String t) {
        if (s.isEmpty()) return true;

        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) == s.charAt(0)) {
                return isSubsequence5(s.substring(1), t.substring(i + 1));
            }
        }
        return false;
    }

    // Recursion
    // beats 89.54%(11 ms for 14 tests)
    public boolean isSubsequence5_2(String s, String t) {
        return isSubsequence(s.toCharArray(), t.toCharArray(), 0, 0);
    }

    private boolean isSubsequence(char[] s, char[] t, int sIndex, int tIndex) {
        if (sIndex == s.length) return true;

        for (int i = tIndex; i < t.length; i++) {
            if (t[i] == s[sIndex]) return isSubsequence(s, t, sIndex + 1, ++i);
        }
        return false;
    }

    // Solution of Choice
    // Binary Search (for follow-up)
    // beats 27.80%(57 ms for 14 tests)
    public boolean isSubsequence6(String s, String t) {
        @SuppressWarnings("unchecked")
        List<Integer>[] list = new List[26];
        for (int i = 0; i < t.length(); i++) {
            int index = t.charAt(i) - 'a';
            if (list[index] == null) {
                list[index] = new ArrayList<>();
            }
            list[index].add(i);
        }
        for (int i = 0, prev = 0; i < s.length(); i++) {
            int index = s.charAt(i) - 'a';
            if (list[index] == null) return false;

            int j = Collections.binarySearch(list[index], prev);
            if (j < 0) {
                j = -j - 1;
            }
            if (j == list[index].size()) return false;
            prev = list[index].get(j) + 1;
        }
        return true;
    }

    void test(String s, String t, boolean expected) {
        assertEquals(expected, isSubsequence(s, t));
        assertEquals(expected, isSubsequence2(s, t));
        assertEquals(expected, isSubsequence3(s, t));
        assertEquals(expected, isSubsequence4(s, t));
        assertEquals(expected, isSubsequence5(s, t));
        assertEquals(expected, isSubsequence5_2(s, t));
        assertEquals(expected, isSubsequence6(s, t));
    }

    @Test
    public void test1() {
        test("", "ace", true);
        test("ac", "ace", true);
        test("ace", "abcde", true);
        test("aec", "abcde", false);
        test("abc", "ahbgdc", true);
        test("axc", "ahbgdc", false);
        test("abcd", "ahbagdbcccaaad", true);
        test("abcd", "ahbagdbcccaaa", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IsSubsequence");
    }
}
