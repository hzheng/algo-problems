import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

// LC005: https://leetcode.com/problems/longest-palindromic-substring/
//
// Given a string s, return the longest palindromic substring in s.
//
// Constraints:
// 1 <= s.length <= 1000
// s consist of only digits and English letters.
public class LongestPalindromeSubstr {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 35.67%(72 ms for 103 tests)
    public String longestPalindrome(String s) {
        int len = s.length();
        if (len == 0) {return "";}

        int[][] dp = new int[len][len];
        int maxLen = 0;
        int maxIndex = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (s.charAt(i) != s.charAt(len - 1 - j)) {continue;}

                if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                }
                int curLen = dp[i][j];
                if ((i + j - curLen + 2 == len) && (curLen > maxLen)) {
                    maxLen = curLen;
                    maxIndex = i;
                }
            }
        }
        return s.substring(maxIndex - maxLen + 1, maxIndex + 1);
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 50.00%(45 ms for 103 tests)
    public String longestPalindrome_2(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int maxLen = 0;
        int maxIndex = 0;
        for (int d = 0; d < n; d++) {
            for (int i = 0, j = i + d; j < n; i++, j++) {
                if (s.charAt(i) != s.charAt(j)) {continue;}

                dp[i][j] = (j - i) < 2 || dp[i + 1][j - 1];
                if (dp[i][j] && (j - i + 1 > maxLen)) {
                    maxLen = j - i + 1;
                    maxIndex = i;
                }
            }
        }
        return s.substring(maxIndex, maxIndex + maxLen);
    }

    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // 174 ms(36.25%), 43.3 MB(25.00%) for 177 tests
    public String longestPalindrome_3(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int maxIndex = 0;
        int maxLen = 0;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) != s.charAt(j)) {continue;}

                dp[i][j] = (j - i) < 2 || dp[i + 1][j - 1];
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    maxIndex = i;
                }
            }
        }
        return s.substring(maxIndex, maxIndex + maxLen);
    }

    // time complexity: O(N ^ 2), space complexity: O(1)
    // beats 77.15%(16 ms for 103 tests)
    public String longestPalindrome2(String s) {
        int maxLen = 0;
        int maxIndex = 0;
        for (int i = 0, j = 0, len = s.length(); i < len; i = j) {
            for (char c = s.charAt(i); j < len && s.charAt(j) == c; j++) {}
            if (j == len) {
                if (j - i > maxLen) {
                    maxLen = j - i;
                    maxIndex = j;
                }
                break;
            }
            i--;
            for (int k = j; ; i--, k++) {
                if (i < 0 || k >= len || s.charAt(i) != s.charAt(k)) {
                    if (k - i - 1 > maxLen) {
                        maxLen = k - i - 1;
                        maxIndex = k;
                    }
                    break;
                }
            }
        }
        return s.substring(maxIndex - maxLen, maxIndex);
    }

    // time complexity: O(N ^ 2), space complexity: O(1)
    // 42 ms(50.98%), 40.0 MB(40.25%) for 177 tests
    public String longestPalindrome3(String s) {
        int start = 0;
        int end = -1;
        for (int i = 0, n = s.length(); i < n; i++) {
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
        for (int n = s.length(); left >= 0 && right < n && s.charAt(left) == s.charAt(right); ) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    // Solution of Choice
    // Manacher's Algorithm
    // from https://en.wikipedia.org/wiki/Longest_palindromic_substring
    // time complexity: O(N), space complexity: O(N)
    // beats 99.58%(9 ms for 103 tests)
    public String longestPalindrome4(String s) {
        char[] s2 = addBoundaries(s.toCharArray());
        int[] p = new int[s2.length]; // max palindrome at i
        int center = 0; // center of the max palindrome currently known
        int rBound = 0; // right-most boundary of the palindrome at 'center'
        int m = 0, n = 0; // The walking indices to compare 2 elements
        for (int i = 1; i < s2.length; i++) {
            if (i > rBound) { // reset
                p[i] = 0;
                m = i - 1;
                n = i + 1;
            } else {
                int j = center * 2 - i; // mirror of i
                if (p[j] < (rBound - i)) {
                    p[i] = p[j];
                    m = -1; // bypass the while loop below
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
        return removeBoundaries(Arrays.copyOfRange(s2, center - len, center + len + 1));
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
        if (cs.length < 3) {return "";}

        char[] cs2 = new char[(cs.length - 1) / 2];
        for (int i = 0; i < cs2.length; i++) {
            cs2[i] = cs[i * 2 + 1];
        }
        return String.valueOf(cs2);
    }

    // time complexity: O(N ^ 2), space complexity: O(1)
    // beats 79.72%(15 ms for 103 tests)
    public String longestPalindrome5(String s) {
        int maxLen = 0;
        int maxIndex = 0;
        for (int i = 0; i < s.length(); i++) {
            if (isPalindrome(s, i - maxLen - 1, i)) {
                maxIndex = i - maxLen - 1;
                maxLen += 2;
            } else if (isPalindrome(s, i - maxLen, i)) {
                maxIndex = i - (maxLen++);
            }
        }
        return s.substring(maxIndex, maxIndex + maxLen);
    }

    private boolean isPalindrome(String s, int begin, int end) {
        if (begin < 0) {return false;}
        while (begin < end) {
            if (s.charAt(begin++) != s.charAt(end--)) {return false;}
        }
        return true;
    }

    // TODO: convert to LCS problem and use suffix tree

    void test(String s, String expected) {
        assertEquals(expected, longestPalindrome(s));
        assertEquals(expected, longestPalindrome_2(s));
        assertEquals(expected, longestPalindrome_3(s));
        assertEquals(expected, longestPalindrome2(s));
        assertEquals(expected, longestPalindrome3(s));
        assertEquals(expected, longestPalindrome4(s));
        assertEquals(expected, longestPalindrome5(s));
    }

    @Test public void test1() {
        //        test("", "");
        test("c", "c");
        // test("ab", "a"); // no unique palindrom, answers vary
        test("aab", "aa");
        test("abb", "bb");
        test("abcba", "abcba");
        test("aaaaaaaaaa", "aaaaaaaaaa");
        test("cabad", "aba");
        test("bbbacdddde", "dddd");
        test("bananas", "anana");
        test("cabadefggfedab", "badefggfedab");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
