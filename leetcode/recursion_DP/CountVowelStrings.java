import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1641: https://leetcode.com/problems/count-sorted-vowel-strings/
//
// Given an integer n, return the number of strings of length n that consist only of vowels
// (a, e, i, o, u) and are lexicographically sorted. A string s is lexicographically sorted if for
// all valid i, s[i] is the same as or comes before s[i+1] in the alphabet.
// Constraints:
// 1 <= n <= 50
public class CountVowelStrings {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 36.1 MB(93.33%) for 41 tests
    public int countVowelStrings(int n) {
        return count(n, 5, new HashMap<>());
    }

    private int count(int n, int k, Map<Integer, Integer> dp) {
        if (k == 1 || n == 1) { return k; }

        int key = 10 * n + k;
        int cache = dp.getOrDefault(key, 0);
        if (cache > 0) { return cache; }

        int res = 0;
        for (int i = 1; i <= k; i++) {
            res += count(n - 1, i, dp);
        }
        dp.put(key, res);
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 36.1 MB(93.33%) for 41 tests
    public int countVowelStrings2(int n) {
        return count(n, 5, new int[n + 1][6]);
    }

    private int count(int n, int k, int[][] dp) {
        if (k == 1 || n == 1) { return k; }

        if (dp[n][k] == 0) {
            dp[n][k] = count(n, k - 1, dp) + count(n - 1, k, dp);
        }
        return dp[n][k];
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 36.1 MB(93.33%) for 41 tests
    public int countVowelStrings3(int n) {
        int[][] dp = new int[n + 1][5]; // i-length strings ending at j-th vowel
        Arrays.fill(dp[1], 1);
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k <= j; k++) {
                    dp[i][j] += dp[i - 1][k];
                }
            }
        }
        int res = 0;
        for (int i = 0; i < 5; i++) {
            res += dp[n][i];
        }
        return res;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 36.1 MB(93.33%) for 41 tests
    public int countVowelStrings4(int n) {
        int[][] dp = new int[n + 1][6]; // i-length strings only from last j vowels(a,e,i,o,u)
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= 5; j++) {
                dp[i][j] = dp[i][j - 1] + (i > 1 ? dp[i - 1][j] : 1);
            }
        }
        return dp[n][5];
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 36.1 MB(93.33%) for 41 tests
    public int countVowelStrings5(int n) {
        int[] dp = new int[] {0, 1, 1, 1, 1, 1};
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= 5; j++) {
                dp[j] += dp[j - 1];
            }
        }
        return dp[5];
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 0 ms(100%), 36.1 MB(93.33%) for 41 tests
    public int countVowelStrings6(int n) {
        int max = Math.max(5, n) + 1;
        int[][] C = new int[max][max]; // pre-calculate combination
        for (int i = 0; i < max; i++) {
            C[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
            }
        }
        int res = 0;
        for (int i = 0; i < 5; i++) {
            // 1. choose first new vowel position C(n-1, i) 2. choose vowel combination C(5, i+1)
            res += C[n - 1][i] * C[5][i + 1];
        }
        return res;
    }

    // Math
    // https://en.wikipedia.org/wiki/Stars_and_bars_(combinatorics)
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100%), 36.1 MB(93.33%) for 41 tests
    public int countVowelStrings7(int n) {
        return (n + 4) * (n + 3) * (n + 2) * (n + 1) / 24; // C(n+4, 4)
    }

    private void test(int n, int expected) {
        assertEquals(expected, countVowelStrings(n));
        assertEquals(expected, countVowelStrings2(n));
        assertEquals(expected, countVowelStrings3(n));
        assertEquals(expected, countVowelStrings4(n));
        assertEquals(expected, countVowelStrings5(n));
        assertEquals(expected, countVowelStrings6(n));
    }

    @Test public void test() {
        test(1, 5);
        test(2, 15);
        test(5, 126);
        test(6, 210);
        test(33, 66045);
        test(50, 316251);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
