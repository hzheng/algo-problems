import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC516: https://leetcode.com/problems/longest-palindromic-subsequence/
//
// Given a string s, find the longest palindromic subsequence's length in s.
// You may assume that the maximum length of s is 1000.
public class LongestPalindromeSubseq {
    // Recursion
    // Time Limit Exceeded
    public int longestPalindromeSubseq0(String s) {
        return longestPalindromeSubseq0(s.toCharArray(), 0, s.length() - 1);
    }

    private int longestPalindromeSubseq0(char[] s, int start, int end) {
        if (start >= end) return end - start + 1;

        if (s[start] == s[end]) return 2 + longestPalindromeSubseq0(s, start + 1, end - 1);

        return Math.max(longestPalindromeSubseq0(s, start + 1, end),
                        longestPalindromeSubseq0(s, start, end - 1));
    }

    // Dynamic Programming(Top-Down)
    // beats 88.99%(54 ms for 83 tests)
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        return longestPalindromeSubseq(s.toCharArray(), 0, n - 1, new int[n][n]);
    }

    private int longestPalindromeSubseq(char[] s, int start, int end, int[][] dp) {
        if (start >= end) return end - start + 1;
        if (dp[start][end] > 0) return dp[start][end];

        int res = 0;
        if (s[start] == s[end]) {
            res = 2 + longestPalindromeSubseq(s, start + 1, end - 1, dp);
        } else {
            res = Math.max(longestPalindromeSubseq(s, start + 1, end, dp),
                           longestPalindromeSubseq(s, start, end - 1, dp));
        }
        return dp[start][end] = res;
    }

    // Dynamic Programming(Bottom-Up)
    // beats 47.34%(77 ms for 83 tests)
    public int longestPalindromeSubseq2(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        char[] cs = s.toCharArray();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                if (i == 0) {
                    dp[j][j] = 1;
                } else if (cs[j] == cs[j + i]) {
                    dp[j][j + i] = dp[j + 1][j + i - 1] + 2;
                } else {
                    dp[j][j + i] = Math.max(dp[j + 1][j + i], dp[j][j + i - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }

    // Dynamic Programming(Bottom-Up)
    // beats 96.70%(47 ms for 83 tests)
    public int longestPalindromeSubseq3(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        char[] cs = s.toCharArray();
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
        return dp[0][n - 1];
    }

    void test(String s, int expected) {
        if (s.length() < 30) {
            assertEquals(expected, longestPalindromeSubseq0(s));
        }
        assertEquals(expected, longestPalindromeSubseq(s));
        assertEquals(expected, longestPalindromeSubseq2(s));
        assertEquals(expected, longestPalindromeSubseq3(s));
    }

    @Test
    public void test() {
        test("bbbab", 4);
        test("cbbd", 2);
        test("euazbipzncptldueeuechubrco", 8);
        test("euazbipzncptldueeuechubrcourfpftcebik", 12);
        test("euazbipzncptldueeuechubrcourfpftcebikrxhybkymimgvldiwqvkszfycvqyvtiwfcke", 17);
        test("euazbipzncptldueeuechubrcourfpftcebikrxhybkymimgvldiwqvkszfycvqyvtiwfckexmowcxztkfyzqovbtmzpxojfofbvw", 31);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestPalindromeSubseq");
    }
}
