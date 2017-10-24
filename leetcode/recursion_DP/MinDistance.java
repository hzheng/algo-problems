import org.junit.Test;
import static org.junit.Assert.*;

// LC583: https://leetcode.com/contest/leetcode-weekly-contest-32/problems/delete-operation-for-two-strings
//
// Given two words word1 and word2, find the minimum number of steps required to make
// word1 and word2 the same, where in each step you can delete one character in either string.
public class MinDistance {
    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 19.20%(83 ms for 1307 tests)
    public int minDistance(String word1, String word2) {
        int n1 = word1.length();
        int n2 = word2.length();
        int[][] dp = new int[n1 + 1][n2 + 1];
        for (int i = 0; i <= n1; i++) {
            for (int j = 0; j <= n2; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = i + j;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i][j - 1], dp[i - 1][j]) + 1;
                }
            }
        }
        return dp[n1][n2];
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M * N), space complexity: O(N)
    // beats 62.13%(67 ms for 1307 tests)
    public int minDistance2(String word1, String word2) {
        int n1 = word1.length();
        int n2 = word2.length();
        int[] dp = new int[n2 + 1];
        for (int i = 0; i <= n1; i++) {
            int[] tmp = new int[n2 + 1];
            for (int j = 0; j <= n2; j++) {
                if (i == 0 || j == 0) {
                    tmp[j] = i + j;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    tmp[j] = dp[j - 1];
                } else {
                    tmp[j] = Math.min(dp[j], tmp[j - 1]) + 1;
                }
            }
            dp = tmp;
        }
        return dp[n2];
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 92.27%(55 ms for 1307 tests)
    public int minDistance3(String word1, String word2) {
        int n1 = word1.length();
        int n2 = word2.length();
        int[][] dp = new int[n1 + 1][n2 + 1];
        for (int i = 1; i <= n1; i++) {
            for (int j = 1; j <= n2; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        return n1 + n2 - 2 * dp[n1][n2];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 5.60%(128 ms for 1307 tests)
    public int minDistance4(String word1, String word2) {
        int n1 = word1.length();
        int n2 = word2.length();
        return n1 + n2 - 2 * lcs(word1, word2, n1, n2, new int[n1 + 1][n2 + 1]);
    }

    private int lcs(String s1, String s2, int n1, int n2, int[][] memo) {
        if (n1 == 0 || n2 == 0) return 0;
        if (memo[n1][n2] > 0) return memo[n1][n2];

        if (s1.charAt(n1 - 1) == s2.charAt(n2 - 1)) {
            return memo[n1][n2] = 1 + lcs(s1, s2, n1 - 1, n2 - 1, memo);
        }
        return memo[n1][n2] =
            Math.max(lcs(s1, s2, n1, n2 - 1, memo),
                     lcs(s1, s2, n1 - 1, n2, memo));
    }

    void test(String word1, String word2, int expected) {
        assertEquals(expected, minDistance(word1, word2));
        assertEquals(expected, minDistance2(word1, word2));
        assertEquals(expected, minDistance3(word1, word2));
        assertEquals(expected, minDistance4(word1, word2));
    }

    @Test
    public void test() {
        test("sea", "eat", 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
