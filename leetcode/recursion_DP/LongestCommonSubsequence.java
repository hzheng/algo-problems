import org.junit.Test;

import static org.junit.Assert.*;

// LC1143: https://leetcode.com/problems/longest-common-subsequence/
//
// Given two strings text1 and text2, return the length of their longest common subsequence.
// A subsequence of a string is a new string generated from the original string with some characters
// (can be none) deleted without changing the relative order of the remaining characters. (eg, "ace"
// is a subsequence of "abcde" while "aec" is not). A common subsequence of two strings is a
// subsequence that is common to both strings.
//
// Constraints:
// 1 <= text1.length <= 1000
// 1 <= text2.length <= 1000
// The input strings consist of lowercase English characters only.
public class LongestCommonSubsequence {
    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(M*N)
    // 14 ms(38.47%), 42.7 MB(62.51%) for 43 tests
    public int longestCommonSubsequence(String text1, String text2) {
        int n1 = text1.length();
        int n2 = text2.length();
        int[][] dp = new int[n1 + 1][n2 + 1];
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n2; j++) {
                dp[i + 1][j + 1] = dp[i][j] + ((text1.charAt(i) == text2.charAt(j)) ? 1 : 0);
                dp[i + 1][j + 1] = Math.max(dp[i + 1][j + 1], dp[i][j + 1]);
                dp[i + 1][j + 1] = Math.max(dp[i + 1][j + 1], dp[i + 1][j]);
            }
        }
        return dp[n1][n2];
    }

    private void test(String text1, String text2, int expected) {
        assertEquals(expected, longestCommonSubsequence(text1, text2));
    }

    @Test public void test() {
        test("abcde", "ace", 3);
        test("abc", "abc", 3);
        test("abc", "def", 0);
        test("abcgheicehjdfklasdjf", "deffkasdjlfkjasdklfj", 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
