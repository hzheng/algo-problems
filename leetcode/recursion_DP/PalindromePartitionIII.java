import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1278: https://leetcode.com/problems/palindrome-partitioning-iii/
//
// You are given a string s containing lowercase letters and an integer k. You need to:
// First, change some characters of s to other lowercase English letters.
// Then divide s into k non-empty disjoint substrings such that each substring is palindrome.
// Return the minimal number of characters that you need to change to divide the string.
//
// Constraints:
// 1 <= k <= s.length <= 100.
// s only contains lowercase English letters.
public class PalindromePartitionIII {
    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2*K), space complexity: O(N*K)
    // 13 ms(38.91%), 37.1 MB(72.64%) for 30 tests
    public int palindromePartition(String s, int k) {
        int n = s.length();
        int[][] dp = new int[n + 1][k + 1];
        for (int[] a : dp) {
            Arrays.fill(a, n);
        }
        dp[0][0] = 0;
        char[] cs = s.toCharArray();
        for (int m = 1; m <= k; m++) {
            for (int i = 1; i <= n; i++) {
                for (int j = m - 1; j < i; j++) {
                    dp[i][m] = Math.min(dp[i][m], dp[j][m - 1] + change(cs, j, i - 1));
                }
            }
        }
        return dp[n][k];
    }

    private int change(char[] s, int start, int end) {
        int res = 0;
        for (int i = start, j = end; i < j; i++, j--) {
            res += (s[i] != s[j]) ? 1 : 0;
        }
        return res;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2*K), space complexity: O(N^2)
    // 4 ms(77.68%), 37.3 MB(53.52%) for 30 tests
    public int palindromePartition2(String s, int k) {
        int n = s.length();
        int[][] dp = new int[n + 1][k + 1];
        for (int[] a : dp) {
            Arrays.fill(a, n);
        }
        dp[0][0] = 0;
        char[] cs = s.toCharArray();
        int[][] change = new int[n][n];
        for (int len = 1; len < n; len++) {
            for (int i = 0; i + len < n; i++) {
                change[i][i + len] = change[i + 1][i + len - 1] + ((cs[i] == cs[i + len]) ? 0 : 1);
            }
        }
        for (int m = 1; m <= k; m++) {
            for (int i = 1; i <= n; i++) {
                for (int j = m - 1; j < i; j++) {
                    dp[i][m] = Math.min(dp[i][m], dp[j][m - 1] + change[j][i - 1]);
                }
            }
        }
        return dp[n][k];
    }

    // DFS + Recursion + 2-D Dynamic Programming(Top-Down)
    // time complexity: O(N^2*K), space complexity: O(N^2)
    // 3 ms(85.71%), 37.2 MB(53.52%) for 30 tests
    public int palindromePartition3(String s, int k) {
        int n = s.length();
        int[][] dp = new int[n][k + 1];
        for (int[] a : dp) {
            Arrays.fill(a, n + 1);
        }
        return dfs(s.toCharArray(), 0, k, dp);
    }

    private int dfs(char[] s, int cur, int k, int[][] dp) {
        int n = s.length;
        if (dp[cur][k] <= n) { return dp[cur][k]; }

        if (n - cur == k) { return 0; }
        if (k == 1) { return change(s, cur, n - 1); }

        int res = n;
        for (int i = cur + 1; i < n - k + 2; i++) {
            res = Math.min(res, dfs(s, i, k - 1, dp) + change(s, cur, i - 1));
        }
        return dp[cur][k] = res;
    }

    private void test(String s, int k, int expected) {
        assertEquals(expected, palindromePartition(s, k));
        assertEquals(expected, palindromePartition2(s, k));
        assertEquals(expected, palindromePartition3(s, k));
    }

    @Test public void test() {
        test("abacb", 2, 1);
        test("abcbacb", 2, 1);
        test("abcbacbbacdeaa", 4, 2);
        test("ababc", 2, 1);
        test("ab", 1, 1);
        test("abab", 2, 0);
        test("abc", 2, 1);
        test("abaca", 2, 1);
        test("aabbc", 3, 0);
        test("leetcode", 8, 0);
        test("fyhowoxzyrincxivwarjuwxrwealesxsimsepjdqsstfggjnjhilvrwwytbgsqbpnwjaojfnmiqiqnyzijfmvekgakefjaxryyml",
             32, 20);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
