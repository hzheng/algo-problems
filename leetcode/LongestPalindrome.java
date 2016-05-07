import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string S, find the longest palindromic substring. Assume the maximum
// length of S is 1000, and there exists 1 unique longest palindromic substring.

public class LongestPalindrome {
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // Time Limit Exceeded
    public String longestPalindrome(String s) {
        int len = s.length();
        if (len == 0) return "";

        int[][] commons = new int[len][len];
        int maxLen = 0;
        int maxIndex = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (s.charAt(i) == s.charAt(len - 1 - j)) {
                    if (i == 0 || j == 0) {
                        commons[i][j] = 1;
                    } else {
                        commons[i][j] = commons[i - 1][j - 1] + 1;
                    }
                    int curLen = commons[i][j];
                    if (i + j - curLen + 2 == len) {
                        if (curLen > maxLen) {
                            maxLen = curLen;
                            maxIndex = i;
                        }
                    }
                }
            }
        }
        return s.substring(maxIndex - maxLen + 1, maxIndex + 1);
    }

    // time complexity: O(N ^ 2), space complexity: O(1)
    // beats 72.97%
    public String longestPalindrome2(String s) {
        int len = s.length();
        if (len == 0) return "";

        int maxLen = 0;
        int maxIndex = 0;
        for (int i = 0; i < len; ) {
            char c = s.charAt(i);
            int j = i;
            while (j < len && s.charAt(j) == c) {
                j++;
            }
            if (j == len) {
                if (j - i > maxLen) {
                    maxLen = j - i;
                    maxIndex = j;
                }
                break;
            }
            i--;
            for (int k = j;; i--, k++) {
                if (i < 0 || k >= len || s.charAt(i) != s.charAt(k)) {
                    if (k - i - 1 > maxLen) {
                        maxLen = k - i - 1;
                        maxIndex = k;
                    }
                    break;
                }
            }
            i = j;
        }
        return s.substring(maxIndex - maxLen, maxIndex);
    }

    // from the solution
    // https://leetcode.com/articles/longest-palindromic-substring/
    // time complexity: O(N ^ 2), space complexity: O(1)
    public String longestPalindrome3(String s) {
        int l = s.length();
        if (l == 0) return "";

        int start = 0, end = 0;
        for (int i = 0; i < l; i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }

    private int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length()
               && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    // from https://en.wikipedia.org/wiki/Longest_palindromic_substring
    // time complexity: O(N), space complexity: O(N ^ 2)
    // beats 96.88%
    public String longestPalindrome4(String s) {
        if (s == null || s.length() == 0) return "";

        char[] s2 = addBoundaries(s.toCharArray());
        int[] p = new int[s2.length];
        int center = 0; // center of the max palindrome currently known
        int rBound = 0; // right-most boundary of the palindrome at 'center'
        int m = 0, n = 0;   // The walking indices to compare 2 elements
        for (int i = 1; i < s2.length; i++) {
            if (i > rBound) { // reset
                p[i] = 0;
                m = i - 1;
                n = i + 1;
            } else {
                int j = center * 2 - i; // mirror of i
                if (p[j] < (rBound - i)) {
                    p[i] = p[j];
                    m = -1;     // bypass the while loop below
                } else {
                    p[i] = rBound - i;
                    n = rBound + 1;
                    m = i * 2 - n;
                }
            }
            for (; m >= 0 && n < s2.length && s2[m] == s2[n]; m--, n++) {
                p[i]++;
            }
            if ((i + p[i]) > rBound) { // update palindrome
                center = i;
                rBound = i + p[i];
            }
        }
        // find the longest one
        center = 0;
        int len = 0;
        for (int i = 1; i < s2.length; i++) {
            if (len < p[i]) {
                len = p[i];
                center = i;
            }
        }
        return removeBoundaries(Arrays.copyOfRange(
                                    s2, center - len, center + len + 1));
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

    private String removeBoundaries(char[] cs) {
        if (cs == null || cs.length < 3) return "";

        char[] cs2 = new char[(cs.length - 1) / 2];
        for (int i = 0; i < cs2.length; i++) {
            cs2[i] = cs[i * 2 + 1];
        }
        return String.valueOf(cs2);
    }

    void test(String s, String expected) {
        assertEquals(expected, longestPalindrome(s));
        assertEquals(expected, longestPalindrome2(s));
        assertEquals(expected, longestPalindrome3(s));
        assertEquals(expected, longestPalindrome4(s));
    }

    @Test
    public void test1() {
        test("", "");
        test("c", "c");
        // test("ab", "a"); // no unique palindrom, answers vary
        test("aab", "aa");
        test("abb", "bb");
        test("aaaaaaaaaa", "aaaaaaaaaa");
        test("cabad", "aba");
        test("bbbacdddde", "dddd");
        test("bananas", "anana");
        test("cabadefggfedab", "badefggfedab");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestPalindrome");
    }
}
