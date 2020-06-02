import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1397: https://leetcode.com/problems/find-all-good-strings/
//
// Given the strings s1 and s2 of size n, and the string evil. Return the number of good strings.
// A good string has size n, it is alphabetically greater than or equal to s1, it is alphabetically
// smaller than or equal to s2, and it does not contain the string evil as a substring. Since the
// answer can be a huge number, return this modulo 10^9 + 7.
// Constraints:
// s1.length == n
// s2.length == n
// s1 <= s2
// 1 <= n <= 500
// 1 <= evil.length <= 50
// All strings consist of lowercase English letters.
public class FindGoodStrings {
    private static final int MOD = 1000_000_000 + 7;

    // Recursion + Dynamic Programming(Top-Down) + KMP
    // time complexity: O(N*M), space complexity: O(N*M)
    // 13 ms(87.20%), 39.5 MB(100%) for 53 tests
    public int findGoodStrings(int n, String s1, String s2, String evil) {
        int[] dp = new int[1 << 17];
        char[] e = evil.toCharArray();
        int[] failTable = getFailTable(e);
        return dfs(s1.toCharArray(), s2.toCharArray(), e, 0, 0, true, true, failTable, dp);
    }

    private int dfs(char[] s1, char[] s2, char[] evil, int cur, int evilPos, boolean bigger,
                    boolean smaller, int[] failTable, int[] dp) {
        if (evilPos == evil.length) { return 0; }
        if (cur == s1.length) { return 1; }

        int key = getKey(cur, evilPos, bigger, smaller);
        if (dp[key] != 0) { return dp[key]; }

        char from = bigger ? s1[cur] : 'a';
        char to = smaller ? s2[cur] : 'z';
        int count = 0;
        for (char c = from; c <= to; c++) {
            int i = evilPos;
            while (i > 0 && evil[i] != c) {
                i = failTable[i - 1];
            }
            if (evil[i] == c) {
                i++;
            }
            count += dfs(s1, s2, evil, cur + 1, i, bigger && (c == from), smaller && (c == to),
                         failTable, dp);
            count %= MOD;
        }
        return dp[key] = count;
    }

    // Dynamic Programming(Bottom-Up) + KMP
    // time complexity: O(N*M), space complexity: O(N*M)
    // 34 ms(36.59%), 40.3 MB(100%) for 53 tests
    public int findGoodStrings2(int n, String s1, String s2, String evil) {
        char[] e = evil.toCharArray();
        int[] failTable = getFailTable(e);
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        int m = e.length;
        int[][][][] dp = new int[n + 1][m + 1][2][2];
        for (int i = 0; i < m; i++) {
            for (int[] a : dp[n][i]) {
                Arrays.fill(a, 1);
            }
        }
        for (int cur = n - 1; cur >= 0; cur--) {
            for (int evilPos = m - 1; evilPos >= 0; evilPos--) {
                for (int l = 0; l < 2; l++) {
                    for (int r = 0; r < 2; r++) {
                        char from = l > 0 ? c1[cur] : 'a';
                        char to = r > 0 ? c2[cur] : 'z';
                        int count = 0;
                        for (char c = from; c <= to; c++) {
                            int i = evilPos;
                            while (i > 0 && e[i] != c) {
                                i = failTable[i - 1];
                            }
                            if (e[i] == c) {
                                i++;
                            }
                            int smaller = l > 0 && (c == from) ? 1 : 0;
                            int larger = r > 0 && (c == to) ? 1 : 0;
                            count = (count + dp[cur + 1][i][smaller][larger]) % MOD;
                        }
                        dp[cur][evilPos][l][r] = count;
                    }
                }
            }
        }
        return dp[0][0][1][1];
    }

    private int[] getFailTable(char[] str) { // Longest Prefix also Suffix
        int len = str.length;
        int[] table = new int[len]; // failure table
        for (int cur = 1, prefix = 0; cur < len; cur++) {
            if (str[cur] == str[prefix]) { // matched and expanding
                table[cur] = ++prefix;
            } else if (prefix > 0) { // mismatched and try to expand the next best match
                prefix = table[prefix - 1];
                cur--;
            }
        }
        return table;
    }

    private int getKey(int n, int m, boolean b1, boolean b2) {
        return (n << 8) | (m << 2) | ((b1 ? 1 : 0) << 1) | (b2 ? 1 : 0);
    }

    private void test(int n, String s1, String s2, String evil, int expected) {
        assertEquals(expected, findGoodStrings(n, s1, s2, evil));
        assertEquals(expected, findGoodStrings2(n, s1, s2, evil));
    }

    @Test public void test() {
        test(2, "aa", "da", "b", 51);
        test(8, "leetcode", "leetgoes", "leet", 0);
        test(2, "gx", "gz", "x", 2);
        test(3, "abc", "bcd", "cd", 702);
        test(8, "pzdanyao", "wgpmtywi", "sdka", 500543753);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
