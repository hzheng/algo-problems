import java.util.*;
import java.util.function.Function;

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
        for (; i >= 0 && s.charAt(i) == s.charAt(j); i--, j++);
        if (i >= 0) return null;
        return complete(s, j);
    }

    private String complete(String s, int right) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= right; i--) {
            sb.append(s.charAt(i));
        }
        sb.append(s);
        return sb.toString();
    }

    // converted to problem: longest palindrome start from beginning
    // time complexity: O(N ^ 2)
    // Time Limit Exceeded
    public String shortestPalindrome2(String s) {
        for (int right = s.length() - 1; ; right--) {
            boolean isPalindrome = true;
            for (int i = 0, j = right; i < j; i++, j--) {
                if (s.charAt(i) != s.charAt(j)) {
                    isPalindrome = false;
                    break;
                }
            }
            if (isPalindrome) return complete(s, right + 1);
        }
    }

    // Manacher's algorithm
    // time complexity: O(N)
    // beats 60.08%(10 ms)
    public String shortestPalindrome3(String s) {
        int l = s.length();
        char[] s2 = addBoundaries(s.toCharArray());
        int[] maxPalindrome = new int[l + 1]; // max palindrome at i
        int center = 0; // center of the max palindrome currently known
        int rBound = 0; // right-most boundary of the palindrome at 'center'
        int m = 0, n = 0;  // walking indices to compare 2 elements
        for (int i = 1; i <= l; i++) {
            if (i > rBound) { // reset
                maxPalindrome[i] = 0;
                m = i - 1;
                n = i + 1;
            } else {
                int j = center * 2 - i; // mirror of i
                if (maxPalindrome[j] < (rBound - i)) {
                    maxPalindrome[i] = maxPalindrome[j];
                    m = -1;     // bypass the while loop below
                } else {
                    maxPalindrome[i] = rBound - i;
                    n = rBound + 1;
                    m = i * 2 - n;
                }
            }
            for (; m >= 0 && n < s2.length && s2[m] == s2[n]; m--, n++) {
                maxPalindrome[i]++;
            }
            if ((i + maxPalindrome[i]) > rBound) { // update palindrome
                center = i;
                rBound = i + maxPalindrome[i];
            }
        }
        // find the longest one
        int len = 0;
        for (int i = 1; i <= l; i++) {
            int curLen = maxPalindrome[i];
            if (curLen == i && len < curLen) {
                len = curLen;
            }
        }
        return complete(s, len);
    }

    private char[] addBoundaries(char[] cs) {
        final char DELIMITER = '|';
        char[] cs2 = new char[cs.length * 2 + 1];
        for (int i = 0; i < (cs2.length - 1); i += 2) {
            cs2[i] = DELIMITER;
            cs2[i + 1] = cs[i / 2];
        }
        cs2[cs2.length - 1] = DELIMITER;
        return cs2;
    }

    void test(Function<String, String> shortestPalindrome, String name,
              String s, String expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, shortestPalindrome.apply(s));
        if (s.length() > 20) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(String s, String expected) {
        ShortestPalindrome p = new ShortestPalindrome();
        test(p::shortestPalindrome, "shortestPalindrome", s, expected);
        test(p::shortestPalindrome2, "shortestPalindrome2", s, expected);
        test(p::shortestPalindrome3, "shortestPalindrome3", s, expected);
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
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
             "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ShortestPalindrome");
    }
}
