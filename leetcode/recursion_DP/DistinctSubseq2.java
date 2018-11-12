import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC940: https://leetcode.com/problems/distinct-subsequences-ii/
//
// Given a string S, count the number of distinct, non-empty subsequences of S.
// Since the result may be large, return the answer modulo 10^9 + 7.
// Note:
// S contains only lowercase letters.
// 1 <= S.length <= 2000
public class DistinctSubseq2 {
    static final int MOD = 1000_000_000 + 7;

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(250 ms for 109 tests)
    public int distinctSubseqII(String S) {
        int n = S.length();
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            char c = S.charAt(i);
            for (int j = 0; j < i; j++) {
                if (S.charAt(j) != c) {
                    dp[i] = (dp[i] + dp[j]) % MOD;
                }
            }
        }
        int res = 0;
        for (int x : dp) {
            res = (res + x) % MOD;
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(212 ms for 109 tests)
    public int distinctSubseqII_2(String S) {
        int n = S.length();
        int[] dp = new int[n];
        int res = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;
            char c = S.charAt(i);
            for (int j = 0; j < i; j++) {
                if (S.charAt(j) != c) {
                    dp[i] = (dp[i] + dp[j]) % MOD;
                }
            }
            res = (res + dp[i]) % MOD;
        }
        return res;
    }

    // From above solutions we know what really matters is counting by ending letter
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats %(15 ms for 109 tests)
    public int distinctSubseqII2(String S) {
        int[] dp = new int[26];
        int n = S.length();
        for (int i = 0; i < n; i++) {
            int j = S.charAt(i) - 'a';
            dp[j]++;
            for (int k = 0; k < 26; k++) {
                if (k != j) {
                    dp[j] = (dp[j] + dp[k]) % MOD;
                }
            }
        }
        int res = 0;
        for (int x : dp) {
            res = (res + x) % MOD;
        }
        return res;
    }

    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats %(6 ms for 109 tests)
    public int distinctSubseqII2_2(String S) {
        int[] dp = new int[26];
        int n = S.length();
        int res = 0;
        for (int i = 0; i < n; i++) {
            int j = S.charAt(i) - 'a';
            int more = (res - dp[j] + 1) % MOD;
            res = (res + more) % MOD;
            dp[j] = (dp[j] + more) % MOD;
        }
        return (res + MOD) % MOD;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats %(6 ms for 109 tests)
    public int distinctSubseqII2_3(String S) {
        int[] dp = new int[26];
        int n = S.length();
        int res = 0;
        for (int i = 0; i < n; i++) {
            int j = S.charAt(i) - 'a';
            int oldRes = res;
            res = (res * 2 % MOD - dp[j] + 1) % MOD;
            dp[j] = (oldRes + 1) % MOD;
        }
        return (res + MOD) % MOD;
    }

    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats %(54 ms for 109 tests)
    public int distinctSubseqII3(String S) {
        long dp[] = new long[26];
        for (char c : S.toCharArray()) {
            dp[c - 'a'] = Arrays.stream(dp).sum() % MOD + 1;
        }
        return (int)(Arrays.stream(dp).sum() % MOD);
    }

    void test(String S, int expected) {
        assertEquals(expected, distinctSubseqII(S));
        assertEquals(expected, distinctSubseqII_2(S));
        assertEquals(expected, distinctSubseqII2(S));
        assertEquals(expected, distinctSubseqII2_2(S));
        assertEquals(expected, distinctSubseqII2_3(S));
        assertEquals(expected, distinctSubseqII3(S));
    }

    @Test
    public void test() {
        test("abc", 7);
        test("aba", 6);
        test("aaa", 3);
        test("aabcdefghisjkklacddfioewiofjdnfa", 635980120);
        test("zchmliaqdgvwncfatcfivphddpzjkgyygueikthqzyeeiebczqbqhdytkoawkehkbizdmcnilcjjlpoeoqqoqpswtqdpvszfaksn",
             97915677);
        test("blljuffdyfrkqtwfyfztpdiyktrhftgtabxxoibcclbjvirnqyynkyaqlxgyybkgyzvcahmytjdqqtctirnxfjpktxmjkojlvvrr",
             589192369);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
