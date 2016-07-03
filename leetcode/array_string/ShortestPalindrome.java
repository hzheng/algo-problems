import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/shortest-palindrome/
//
// Given a string S, you are allowed to convert it to a palindrome by adding
// characters in front of it. Find and return the shortest palindrome you can
// find by performing this transformation.
public class ShortestPalindrome {
    // time complexity: O(N ^ 2)
    // beats 21.96%(123 ms)
    public String shortestPalindrome(String s) {
        int len = s.length();
        if (len < 2) return s;

        for (int i = (len - 1) / 2; i >= 0; i--) {
            if (s.charAt(i) == s.charAt(i + 1) && (2 * i + 1) < len)  {
                String res = palindromeString(s, i, i + 1);
                if (res != null) return res;
            }
            String res = palindromeString(s, i - 1, i + 1);
            if (res != null) return res;
        }
        return null;
    }

    private String palindromeString(String s, int left, int right) {
        int i = left;
        int j = right;
        for (; i >= 0 && s.charAt(i) == s.charAt(j); i--, j++) ;
        if (i >= 0) return null;

        StringBuilder sb = new StringBuilder();
        for (int k = s.length() - 1; k >= j; k--) {
            sb.append(s.charAt(k));
        }
        sb.append(s);
        return sb.toString();
    }

    void test(String s, String expected) {
        assertEquals(expected, shortestPalindrome(s));
    }

    @Test
    public void test1() {
        test("aa", "aa");
        test("abb", "bbabb");
        test("aacecaaa", "aaacecaaa");
        test("abcd", "dcbabcd");
        test("aba", "aba");
        test("a", "a");
        test("ab", "bab");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ShortestPalindrome");
    }
}
