import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC730: https://leetcode.com/problems/count-different-palindromic-subsequences/
//
// Given a string S, find the number of different non-empty palindromic
// subsequences in S, and return that number modulo 10^9 + 7.
// Note:
// The length of S will be in the range [1, 1000].
// Each character S[i] will be in the set {'a', 'b', 'c', 'd'}.
public class CountPalindromicSubsequences {
    final static int MOD = 1_000_000_000 + 7;

    // Dynamic Programming(Top-down) + Recursion + SortedSet
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N ^ 2)
    // beats 18.72%(213 ms for 366 tests)
    public int countPalindromicSubsequences(String S) {
        @SuppressWarnings("unchecked")
        NavigableSet<Integer>[] freqs = new TreeSet[4];
        for (int i = 0; i < 4; i++) {
            freqs[i] = new TreeSet<Integer>();
        }
        int n = S.length();
        for (int i = 0; i < n; i++) {
            freqs[S.charAt(i) - 'a'].add(i);
        }
        return count(freqs, 0, n, new Integer[n + 1][n + 1]);
    }

    private int count(NavigableSet<Integer>[] freqs, int start, int end,
                      Integer[][] dp) {
        if (start >= end) return 0;
        if (dp[start][end] != null) return dp[start][end];

        long res = 0;
        for (NavigableSet<Integer> freq : freqs) {
            Integer nextStart = freq.ceiling(start);
            Integer nextEnd = freq.lower(end);
            if (nextStart == null || nextStart >= end) continue;

            res += (nextStart != nextEnd) ? 2 : 1;
            res += count(freqs, nextStart + 1, nextEnd, dp);
        }
        return dp[start][end] = (int) (res % MOD);
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 49.75%(81 ms for 366 tests)
    public int countPalindromicSubsequences2(String S) {
        char[] cs = S.toCharArray();
        int n = cs.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        for (int len = 1; len < n; len++) {
            for (int i = 0, j = i + len; j < n; i++, j++) {
                if (cs[i] != cs[j]) {
                    dp[i][j] = dp[i][j - 1] + dp[i + 1][j] - dp[i + 1][j - 1];
                } else {
                    int low = i + 1;
                    int high = j - 1;
                    for (; low <= high && cs[low] != cs[j]; low++) {}
                    for (; low <= high && cs[high] != cs[j]; high--) {}
                    if (low > high) {
                        dp[i][j] = dp[i + 1][j - 1] * 2 + 2;
                    } else if (low == high) {
                        dp[i][j] = dp[i + 1][j - 1] * 2 + 1;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1] * 2 - dp[low + 1][high - 1];
                    }
                }
                dp[i][j] = (dp[i][j] % MOD + MOD) % MOD;
            }
        }
        return dp[0][n - 1];
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 24.39%(152 ms for 366 tests)
    public int countPalindromicSubsequences3(String S) {
        char[] cs = S.toCharArray();
        int n = cs.length;
        int[][][] dp = new int[n][3][4];
        // dp[i][len][c]: distinct palindromes of string starting at i of length len,
        // where the first character c. (only need 3 lengths:  len-2, len-1, len)
        for (int len = 1; len <= n; len++) {
            for (int i = 0, j = i + len - 1; j < n; i++, j++) {
                for (int k = 0; k < 4; k++)  {
                    int count = 0;
                    char c = (char)('a' + k);
                    if (len == 1) {
                        count = (cs[i] == c) ? 1 : 0;
                    } else {
                        if (cs[i] != c) {
                            count = dp[i + 1][1][k];
                        } else if (cs[j] != c) {
                            count = dp[i][1][k];
                        } else {
                            count = 2;
                            if (len > 2) {
                                for (int m = 0; m < 4; m++) {
                                    count = (count + dp[i + 1][0][m]) % MOD;
                                }
                            }
                        }
                    }
                    dp[i][2][k] = count;
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 4; k++) {
                        dp[i][j][k] = dp[i][j + 1][k];
                    }
                }
            }
        }
        int res = 0;
        for (int a : dp[0][2]) {
            res = (res + a) % MOD;
        }
        return res;
    }

    void test(String S, int expected) {
        assertEquals(expected, countPalindromicSubsequences(S));
        assertEquals(expected, countPalindromicSubsequences2(S));
        assertEquals(expected, countPalindromicSubsequences3(S));
    }

    @Test
    public void test() {
        test("bccb", 6);
        test("abcdabcdabcdabcdabcdabcdabcdabcddcbadcbadcbadcbadcbadcbadcbadcba",
             104860361);
        test("bcbacbabdcbcbdcbddcaaccdcbbcdbcabbcdddadaadddbdbbbdacbabaabdddcaccccdccdbabcddbdcccabccbbcdbcdbdaada",
             117990582);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
