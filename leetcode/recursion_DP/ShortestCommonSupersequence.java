import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.hamcrest.collection.IsIn.*;

// LC1092: https://leetcode.com/problems/shortest-common-supersequence/
//
// Given two strings str1 and str2, return the shortest string that has both str1 and str2 as
// subsequences. If multiple answers exist, you may return any of them.
//
// Note:
// 1 <= str1.length, str2.length <= 1000
// str1 and str2 consist of lowercase English letters.
public class ShortestCommonSupersequence {
    // 3-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(M*N)
    // 39 ms(10.02%), 68.7 MB(5.01%) for 47 tests
    public String shortestCommonSupersequence(String str1, String str2) {
        char[] s1 = str1.toCharArray();
        int m = s1.length;
        char[] s2 = str2.toCharArray();
        int n = s2.length;
        int[][][] dp = new int[m + 1][n + 1][2];
        for (int[][] a : dp) {
            for (int[] v : a) {
                v[0] = m + n + 1;
            }
        }
        dp[0][0] = new int[] {0, 0};
        for (int i = 1; i <= m; i++) {
            dp[i][0] = new int[] {i, 1};
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = new int[] {j, 2};
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    int v = dp[i - 1][j - 1][0] + 1;
                    if (v < dp[i][j][0]) {
                        dp[i][j][0] = v;
                        dp[i][j][1] = 0;
                    }
                    continue;
                }
                int v = dp[i - 1][j][0] + 1;
                if (v < dp[i][j][0]) {
                    dp[i][j][0] = v;
                    dp[i][j][1] = 1;
                }
                v = dp[i][j - 1][0] + 1;
                if (v < dp[i][j][0]) {
                    dp[i][j][0] = v;
                    dp[i][j][1] = 2;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = m, j = n; i > 0 || j > 0; ) {
            int v = dp[i][j][1];
            if (v == 0) {
                sb.append(s1[--i]);
                j--;
            } else if (v == 1) {
                sb.append(s1[--i]);
            } else {
                sb.append(s2[--j]);
            }
        }
        return sb.reverse().toString();
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(M*N)
    // 15 ms(50.09%), 42.6 MB(78.53%) for 47 tests
    public String shortestCommonSupersequence2(String str1, String str2) {
        char[] s1 = str1.toCharArray();
        int m = s1.length;
        char[] s2 = str2.toCharArray();
        int n = s2.length;
        int[][] dp = new int[m + 1][n + 1];
        int max = m + n + 1;
        for (int[] a : dp) {
            Arrays.fill(a, max - 1);
        }
        dp[0][0] = 0; // between 0 and max
        for (int i = 1; i <= m; i++) {
            dp[i][0] = i + max; // between max and max*2
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = j + max * 2; // between max*2 and max*3
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cur = dp[i][j] % max;
                if (s1[i - 1] == s2[j - 1]) {
                    int v = dp[i - 1][j - 1] % max + 1;
                    if (cur > v) {
                        dp[i][j] = v;
                    }
                    continue;
                }
                int v = dp[i - 1][j] % max + 1;
                if (cur > v) {
                    dp[i][j] = v + max;
                    cur = v;
                }
                v = dp[i][j - 1] % max + 1;
                if (cur > v) {
                    dp[i][j] = v + max * 2;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = m, j = n; i > 0 || j > 0; ) {
            int v = dp[i][j];
            if (v >= max * 2) {
                sb.append(s2[--j]);
            } else {
                sb.append(s1[--i]);
                if (v < max) {
                    j--;
                }
            }
        }
        return sb.reverse().toString();
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(M*N)
    // 6 ms(98.93%), 42.5 MB(93.20%) for 47 tests
    public String shortestCommonSupersequence3(String str1, String str2) {
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int m = s1.length;
        int n = s2.length;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (s1[i] == s2[j]) {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                } else {
                    dp[i + 1][j + 1] = Math.max(dp[i][j + 1], dp[i + 1][j]);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = m - 1, j = n - 1; i >= 0 || j >= 0; ) {
            if (i < 0) {
                sb.append(s2[j--]);
                continue;
            }
            if (j < 0) {
                sb.append(s1[i--]);
                continue;
            }
            if (dp[i + 1][j + 1] == dp[i + 1][j]) {
                sb.append(s2[j--]);
            } else {
                if (dp[i + 1][j + 1] != dp[i][j + 1]) {
                    j--;
                }
                sb.append(s1[i--]);
            }
        }
        return sb.reverse().toString();
    }

    private void test(String str1, String str2, String... expected) {
        assertThat(shortestCommonSupersequence(str1, str2), in(expected));
        assertThat(shortestCommonSupersequence2(str1, str2), in(expected));
        assertThat(shortestCommonSupersequence3(str1, str2), in(expected));
    }

    @Test public void test() {
        test("abac", "cab", "cabac");
        test("bbcbcaabc", "cacaabaaaa", "bbcabcaabaaaac", "bbcbacaabcaaaa");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
