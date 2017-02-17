import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC161: https://leetcode.com/problems/one-edit-distance/
//
// Given two strings S and T, determine if they are both one edit distance apart.
public class OneEditDistance {
    // Recursion
    // beats 1.44%(52 ms for 131 tests)
    public boolean isOneEditDistance(String s, String t) {
        int len1 = s.length();
        int len2 = t.length();
        if (len1 == 0) return len2 == 1;
        if (len2 == 0) return len1 == 1;

        if (s.charAt(0) == t.charAt(0)) return isOneEditDistance(s.substring(1), t.substring(1));

        if (len1 == len2) return s.substring(1).equals(t.substring(1));

        return (len1 < len2) ? s.equals(t.substring(1)) : t.equals(s.substring(1));
    }

    // Two Pointers
    // beats 58.33%(2 ms for 131 tests)
    public boolean isOneEditDistance2(String s, String t) {
        int len1 = s.length();
        int len2 = t.length();
        // if (Math.abs(len1 - len2) > 1) return false; // for performance

        boolean diff = false;
        for (int i = 0, j = 0;; i++, j++) {
            if (i == len1) return diff && j == len2 || !diff && j == len2 - 1;
            if (j == len2) return diff && i == len1 || !diff && i == len1 - 1;

            if (s.charAt(i) == t.charAt(j)) continue;
            if (diff) return false;

            diff = true;
            if (len1 < len2) {
                i--;
            } else if (len1 > len2) {
                j--;
            }
        }
    }

    // beats 15.09%(3 ms for 131 tests)
    public boolean isOneEditDistance3(String s, String t) {
        int len1 = s.length();
        int len2 = t.length();
        for (int i = 0; i < Math.min(len1, len2); i++) {
            if (s.charAt(i) != t.charAt(i)) {
                return s.substring(i + (len1 >= len2 ? 1 : 0))
                       .equals(t.substring(i + (len1 <= len2 ? 1 : 0)));
            }
        }
        return Math.abs(len1 - len2) == 1;
    }

    // beats 58.33%(2 ms for 131 tests)
    public boolean isOneEditDistance3_2(String s, String t) {
        int len1 = s.length();
        int len2 = t.length();
        for (int i = 0; i < Math.min(len1, len2); i++) {
            if (s.charAt(i) != t.charAt(i)) {
                if (len1 > len2) return t.substring(i).equals(s.substring(i + 1));

                return s.substring((len1 == len2) ? i + 1: i).equals(t.substring(i + 1));
            }
        }
        return Math.abs(len1 - len2) == 1;
    }

    // beats 15.09%(3 ms for 131 tests)
    public boolean isOneEditDistance4(String s, String t) {
        int len1 = s.length();
        int len2 = t.length();
        if (Math.abs(len1 - len2) > 1) return false;

        if (len1 == len2) {
            boolean diff = false;
            for (int i = 0; i < len1; i++) {
                if (s.charAt(i) != t.charAt(i)) {
                    if (diff) return false;

                    diff = true;
                }
            }
            return diff;
        }
        return len1 > len2 ? isOneDel(s, t, len2) : isOneDel(t, s, len1);
    }

    private boolean isOneDel(String s, String t, int len) {
        for (int i = 0, j = 0; i < len; i++, j++) {
            if (s.charAt(i) != t.charAt(j)) {
                return s.substring(i + 1).equals(t.substring(j));
            }
        }
        return true;
    }

    void test(String s, String t, boolean expected) {
        assertEquals(expected, isOneEditDistance(s, t));
        assertEquals(expected, isOneEditDistance2(s, t));
        assertEquals(expected, isOneEditDistance3(s, t));
        assertEquals(expected, isOneEditDistance3_2(s, t));
        assertEquals(expected, isOneEditDistance4(s, t));
    }

    @Test
    public void test() {
        test("teacher", "tache", false);
        test("", "", false);
        test("ab", "ab", false);
        test("ab", "acc", false);
        test("ab", "accc", false);
        test("ab", "cb", true);
        test("ab", "ac", true);
        test("ab", "a", true);
        test("abc", "a", false);
        test("abc", "adc", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("OneEditDistance");
    }
}
