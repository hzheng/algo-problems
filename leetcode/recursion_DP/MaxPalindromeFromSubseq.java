import org.junit.Test;

import static org.junit.Assert.*;

// LC1771: https://leetcode.com/problems/maximize-palindrome-length-from-subsequences/
//
// You are given two strings, word1 and word2. You need construct a string in the following manner:
// Choose some non-empty subsequence subsequence1 from word1.
// Choose some non-empty subsequence subsequence2 from word2.
// Concatenate the subsequences: subsequence1 + subsequence2, to make the string.
// Return the length of the longest palindrome that can be constructed in the described manner. If
// no palindromes can be constructed, return 0.
//
// Constraints:
// 1 <= word1.length, word2.length <= 1000
// word1 and word2 consist of lowercase English letters.
public class MaxPalindromeFromSubseq {
    // Recursion + 2-D Dynamic Programming(Top-Down)
    // time complexity: O((M+N)^2), space complexity: O((M+N)^2)
    // 808 ms(6.67%), 88.6 MB(26.67%) for 21 tests (top-down DP)
    // 246 ms(13.33%), 89.5 MB(26.67%) for 21 tests (bottom-up DP)
    public int longestPalindrome(String word1, String word2) {
        int[] index1 = letterIndex(word1, true);
        int[] index2 = letterIndex(word2, false);
        int res = 0;
        for (int i = 0; i < 26; i++) {
            if (index1[i] * index2[i] != 0) {
                String w1 = word1.substring(index1[i]);
                String w2 = word2.substring(0, index2[i] - 1);
                res = Math.max(res, 2 + longestPalindromeSubseq(w1 + w2));
            }
        }
        return res;
    }

    private int[] letterIndex(String s, boolean first) {
        int[] index = new int[26];
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            if (!first || index[c] == 0) {
                index[c] = i + 1;
            }
        }
        return index;
    }

    private int longestPalindromeSubseq(String s) {
        int n = s.length();
        return longestPalindromeSubseq(s.toCharArray(), 0, n - 1, new int[n][n]);
    }

    private int longestPalindromeSubseq(char[] s, int start, int end, int[][] dp) {
        if (start >= end) { return end - start + 1; }
        if (dp[start][end] > 0) { return dp[start][end]; }

        int res;
        if (s[start] == s[end]) {
            res = 2 + longestPalindromeSubseq(s, start + 1, end - 1, dp);
        } else {
            res = Math.max(longestPalindromeSubseq(s, start + 1, end, dp),
                           longestPalindromeSubseq(s, start, end - 1, dp));
        }
        return dp[start][end] = res;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O((M+N)^2), space complexity: O((M+N)^2)
    // 22 ms(100.00%), 64 MB(60.00%) for 21 tests
    public int longestPalindrome2(String word1, String word2) {
        char[] cs = (word1 + word2).toCharArray();
        int n = cs.length;
        int[][] dp = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (cs[i] == cs[j]) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        int res = 0;
        for (int i = 0, n1 = word1.length(); i < n1; i++) {
            for (int j = word2.length() - 1; j >= 0; j--) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    res = Math.max(res, dp[i][n1 + j]);
                    break;
                }
            }
        }
        return res;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O((M+N)^2), space complexity: O((M+N)^2)
    // 27 ms(100.00%), 64 MB(60.00%) for 21 tests
    public int longestPalindrome3(String word1, String word2) {
        char[] cs = (word1 + word2).toCharArray();
        int n = cs.length;
        int[][] dp = new int[n][n];
        int res = 0;
        for (int i = n - 1, m = word1.length(); i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (cs[i] == cs[j]) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                    if (i < m && j >= m) {
                        res = Math.max(res, dp[i][j]);
                    }
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return res;
    }

    private void test(String word1, String word2, int expected) {
        assertEquals(expected, longestPalindrome(word1, word2));
        assertEquals(expected, longestPalindrome2(word1, word2));
        assertEquals(expected, longestPalindrome3(word1, word2));
    }

    @Test public void test() {
        test("cfe", "ef", 4);
        test("eeeecd", "eabfbeeb", 7);
        test("cacb", "cbba", 5);
        test("ab", "ab", 3);
        test("aa", "bb", 0);
        test("aa", "ba", 3);
        test("euazbipzncptldueeuechubrcourfpftcebik",
             "rxhybkymimgvldiwqvkszfycvqyvtiwfckexmowcxztkfyzqovbtmzpxojfofbvw", 31);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
