import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC712: https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/
//
// Given two strings, find the lowest ASCII sum of deleted characters to make 
// two strings equal.
public class MinimumDeleteSum {
    // DFS + Recursion
    // Time Limit Exceeded
    public int minimumDeleteSum(String s1, String s2) {
        int[] res = new int[] {Integer.MAX_VALUE};
        dfs(s1, s2, 0, 0, 0, res);
        return res[0];
    }

    private void dfs(String s1, String s2, int i1, int i2, int sum, int[] res) {
        if (sum >= res[0]) return;

        int n1 = s1.length();
        int n2 = s2.length();
        if (i1 >= n1 && i2 >= n2) {
            res[0] = Math.min(res[0], sum);
            return;
        }
        if (i1 >= n1) {
            dfs(s1, s2, i1, i2 + 1, sum + s2.charAt(i2), res);
        } else if (i2 >= n2) {
            dfs(s1, s2, i1 + 1, i2, sum + s1.charAt(i1), res);
        } else if (s1.charAt(i1) == s2.charAt(i2)) {
            dfs(s1, s2, i1 + 1, i2 + 1, sum, res);
        } else {
            dfs(s1, s2, i1, i2 + 1, sum + s2.charAt(i2), res);
            dfs(s1, s2, i1 + 1, i2, sum + s1.charAt(i1), res);
        }
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats ?%(31 ms for 93 tests)
    public int minimumDeleteSum2(String s1, String s2) {
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        int n1 = c1.length;
        int n2 = c2.length;
        int[][] dp = new int[n1 + 1][n2 + 1];
        for (int j = 1; j <= n2; j++) {
            dp[0][j] = dp[0][j - 1] + c2[j - 1];
        }
        for (int i = 1; i <= n1; i++) {
            dp[i][0] = dp[i - 1][0] + c1[i - 1];
            for (int j = 1; j <= n2; j++) {
                if (c1[i - 1] == c2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j] + c1[i - 1],
                                        dp[i][j - 1] + c2[j - 1]);
                }
            }
        }
        return dp[n1][n2];
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats ?%(49 ms for 93 tests)
    public int minimumDeleteSum3(String s1, String s2) {
        int n1 = s1.length();
        int n2 = s2.length();
        int[][] dp = new int[n1 + 1][n2 + 1];
        for (int j = n2 - 1; j >= 0; j--) {
            dp[n1][j] = dp[n1][j + 1] + s2.codePointAt(j);
        }
        for (int i = n1 - 1; i >= 0; i--) {
            dp[i][n2] = dp[i + 1][n2] + s1.codePointAt(i);
            for (int j = n2 - 1; j >= 0; j--) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    dp[i][j] = dp[i + 1][j + 1];
                } else {
                    dp[i][j] = Math.min(dp[i + 1][j] + s1.codePointAt(i),
                                        dp[i][j + 1] + s2.codePointAt(j));
                }
            }
        }
        return dp[0][0];
    }

    void test(String s1, String s2, int expected) {
        if (s1.length() + s2.length() < 30) {
            assertEquals(expected, minimumDeleteSum(s1, s2));
        }
        assertEquals(expected, minimumDeleteSum2(s1, s2));
        assertEquals(expected, minimumDeleteSum3(s1, s2));
    }

    @Test
    public void test() {
        test("sea", "eat", 231);
        test("delete", "leet", 403);
        test("ccaccjp", "fwosarcwge", 1399);
        test(
            "igijekdtywibepwonjbwykkqmrgmtybwhwjiqudxmnniskqjfbkpcxukrablqmwjndlhblxflgehddrvwfacarwkcpmcfqnajqfxyqwiugztocqzuikamtvmbjrypfqvzqiwooewpzcpwhdejmuahqtukistxgfafrymoaodtluaexucnndlnpeszdfsvfofdylcicrrevjggasrgdhwdgjwcchyanodmzmuqeupnpnsmdkcfszznklqjhjqaboikughrnxxggbfyjriuvdsusvmhiaszicfa",
            "ikhuivqorirphlzqgcruwirpewbjgrjtugwpnkbrdfufjsmgzzjespzdcdjcoioaqybciofdzbdieegetnogoibbwfielwungehetanktjqjrddkrnsxvdmehaeyrpzxrxkhlepdgpwhgpnaatkzbxbnopecfkxoekcdntjyrmmvppcxcgquhomcsltiqzqzmkloomvfayxhawlyqxnsbyskjtzxiyrsaobbnjpgzmetpqvscyycutdkpjpzfokvi",
            41731);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
