import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC903: https://leetcode.com/problems/valid-permutations-for-di-sequence/
//
// Given S, a length n string of characters from the set {'D', 'I'}. (stand for 
// "decreasing" and "increasing".) A valid permutation is a permutation P[0], 
// P[1], ..., P[n] of integers {0, 1, ..., n}, such that for all i:
// If S[i] == 'D', then P[i] > P[i+1], and;
// If S[i] == 'I', then P[i] < P[i+1].
// How many valid permutations are there?  Since the answer may be large, return
// your answer modulo 10^9 + 7.
public class NumPermsDISequence {
    static final int MOD = 1_000_000_000 + 7;

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats %(50 ms for 83 tests)
    public int numPermsDISequence(String S) {
        int n = S.length() + 1;
        // dp[i][j]:
        // # ways to put P_i s.t j remaining numbers are less than P_i
        // or: permutations whose length is i + 1 and ends with j
        int[][] dp = new int[n][n]; 
        dp[0][0] = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (S.charAt(i - 1) == 'D') {
                    for (int k = j; k < i; k++) {
                        dp[i][j] = (dp[i][j] + dp[i - 1][k]) % MOD;
                    }
                } else {
                    for (int k = 0; k < j; k++) {
                        dp[i][j] = (dp[i][j] + dp[i - 1][k]) % MOD;
                    }
                }
            }
        }
        int res = 0;
        for (int x : dp[n - 1]) {
            res = (res + x) % MOD;
        }
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats %(9 ms for 83 tests)
    public int numPermsDISequence2(String S) {
        int n = S.length() + 1;
        int res = 0;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            res = (res + numPerms(n - 1, i, S, dp)) % MOD;
        }
        return res;
    }

    private int numPerms(int i, int j, String S, int[][] dp) {
        if (i < j || j < 0) return 0;

        if (i == 0) return 1;

        if (dp[i][j] != 0) return dp[i][j];

        int res = 0;
        if (S.charAt(i - 1) == 'D') {
            res = (numPerms(i, j + 1, S, dp) + numPerms(i - 1, j, S, dp)) % MOD;
        } else {
            res = (numPerms(i, j - 1, S, dp) + numPerms(i - 1, j - 1, S, dp)) % MOD;
        }
        return dp[i][j] = res;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats %(15 ms for 83 tests)
    public int numPermsDISequence3(String S) {
        int n = S.length();
        // dp[i][j]: permutations of first i + 1 digits,
        // where the (i + 1)th digit is (j + 1)th smallest in the rest of digits
        int[][] dp = new int[n + 1][n + 1];
        Arrays.fill(dp[0], 1);
        for (int i = 0; i < n; i++) {
            if (S.charAt(i) == 'D') { // also correct if changed to 'I'?
                for (int j = n - i - 1, cur = 0; j >= 0; j--) {
                    dp[i + 1][j] = cur = (cur + dp[i][j + 1]) % MOD;
                }
            } else {
                for (int j = 0, cur = 0; j < n - i; j++) {
                    dp[i + 1][j] = cur = (cur + dp[i][j]) % MOD;
                }
            }
        }
        return dp[n][0];
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(12 ms for 83 tests)
    public int numPermsDISequence4(String S) {
        int n = S.length();
        int[] dp = new int[n + 1];
        Arrays.fill(dp, 1);
        for (int i = 0; i < n; i++) {
            int[] dp2 = new int[n + 1];
            if (S.charAt(i) == 'D') {
                for (int j = n - i - 1, cur = 0; j >= 0; j--) {
                    dp2[j] = cur = (cur + dp[j + 1]) % MOD;
                }
            } else {
                for (int j = 0, cur = 0; j < n - i; j++) {
                    dp2[j] = cur = (cur + dp[j]) % MOD;
                }
            }
            dp = dp2;
        }
        return dp[0];
    }

    // TODO: binomial method

    void test(String S, int expected) {
        assertEquals(expected, numPermsDISequence(S));
        assertEquals(expected, numPermsDISequence2(S));
        assertEquals(expected, numPermsDISequence3(S));
        assertEquals(expected, numPermsDISequence4(S));
    }

    @Test
    public void test() {
        test("DID", 5);
        test("IDDDIIDIIIIIIIIDIDID", 853197538);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
