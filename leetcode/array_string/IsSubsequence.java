import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC392: https://leetcode.com/problems/is-subsequence/
public class IsSubsequence {
    // beats 63.62%(31 ms)
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

    // beats 52.69%(36 ms)
    public boolean isSubsequence2(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        int i = 0;
        for (int j = 0; i < sLen && j < tLen; j++) {
            if (s.charAt(i) == t.charAt(j)) {
                if (++i == sLen) return true;
            }
        }
        return sLen == 0;
    }

    void test(String s, String t, boolean expected) {
        assertEquals(expected, isSubsequence(s, t));
        assertEquals(expected, isSubsequence2(s, t));
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
