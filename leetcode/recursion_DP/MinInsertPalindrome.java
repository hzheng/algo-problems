import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1312: https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/
//
// Given a string s. In one step you can insert any character at any index of the string.
// Return the minimum number of steps to make s palindrome.
// A Palindrome String is one that reads the same backward as well as forward.
//
// Constraints:
// 1 <= s.length <= 500
// All characters of s are lower case English letters.
public class MinInsertPalindrome {
    // Recursion + 2D-Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // 51 ms(8.53%), 53.2 MB(5.24%) for 57 tests
    public int minInsertions(String s) {
        int n = s.length();
        return minInsert(s.toCharArray(), 0, n - 1, new Integer[n][n]);
    }

    private int minInsert(char[] s, int start, int end, Integer[][] dp) {
        if (end - start <= 0) { return 0; }
        if (dp[start][end] != null) { return dp[start][end]; }

        int res;
        if (s[start] == s[end]) {
            res = minInsert(s, start + 1, end - 1, dp);
        } else {
            res = 1 + Math.min(minInsert(s, start + 1, end, dp), minInsert(s, start, end - 1, dp));
        }
        return dp[start][end] = res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // 51 ms(8.53%), 52.3 MB(5.24%) for 57 tests
    public int minInsertions2(String s) {
        int n = s.length();
        int[][] dp = new int[n + 1][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i + 1][j + 1] = (s.charAt(i) == s.charAt(n - 1 - j))
                                   ? dp[i][j] + 1 : Math.max(dp[i][j + 1], dp[i + 1][j]);
            }
        }
        return n - dp[n][n];
    }

    void test(String s, int expected) {
        assertEquals(expected, minInsertions(s));
        assertEquals(expected, minInsertions2(s));
    }

    @Test public void test1() {
        test("zzazz", 0);
        test("mbadm", 2);
        test("leetcode", 5);
        test("g", 0);
        test("no", 1);
        test("tldjbqjdogipebqsohdypcxjqkrqltpgviqtqz", 25);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
