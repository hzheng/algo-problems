import org.junit.Test;
import static org.junit.Assert.*;

// LC664: https://leetcode.com/problems/strange-printer/
//
// There is a strange printer with the following two special requirements:
// The printer can only print a sequence of the same character each time.
// At each turn, the printer can print new characters starting from and ending
// at any places, and will cover the original existing characters. Given a
// string consists of lower English letters only, your job is to count the
// minimum number of turns the printer needed in order to print it.
// Hint: Length of the given string will not exceed 100.
public class StrangePrinter {
    // Dynamic Programming
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 15.59%(44 ms for 201 tests)
    public int strangePrinter(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int[][] dp = new int[n][n];
        for (int len = 0; len < n; len++) {
            for (int i = 0; i < n - len; i++) {
                int j = i + len;
                dp[i][j] = len + 1;
                for (int k = i, save = (cs[i] == cs[j]) ? 1 : 0; k < j; k++) {
                    dp[i][j] =
                        Math.min(dp[i][j], dp[i][k] + dp[k + 1][j] - save);
                }
            }
        }
        return (n == 0 ? 0 : dp[0][n - 1]);
    }

    // Dynamic Programming
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 35.48%(34 ms for 201 tests)
    public int strangePrinter2(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                dp[i][j] = (i == j) ? 1 : 1 + dp[i + 1][j];
                for (int k = i + 1; k <= j; k++) {
                    if (s.charAt(k) != s.charAt(i)) continue;

                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][k - 1] + dp[k][j]);
                }
            }
        }
        return (n == 0 ? 0 : dp[0][n - 1]);
    }

    // Dynamic Programming
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 88.17%(22 ms for 201 tests)
    public int strangePrinter2_2(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        if (n == 0) return 0;

        int end = 0; // remove adjacent duplicates
        for (int i = 1; i < n; i++) {
            if (cs[i] != cs[end]) {
                cs[++end] = cs[i];
            }
        }
        n = end + 1;
        int[][] dp = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                dp[i][j] = (i == j) ? 1 : 1 + dp[i + 1][j];
                for (int k = i + 1; k <= j; k++) {
                    if (cs[k] != cs[i]) continue;

                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][k - 1] + dp[k][j]);
                }
            }
        }
        return dp[0][n - 1];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 24.73%(37 ms for 201 tests)
    public int strangePrinter3(String s) {
        int n = s.length();
        return minPrint(s, 0, n - 1, new int[n][n]);
    }

    private int minPrint(String s, int start, int end, int[][] dp) {
        if (start > end) return 0;

        if (dp[start][end] > 0) return dp[start][end];

        int res = minPrint(s, start + 1, end, dp) + 1;
        for (int i = start + 1; i <= end; i++) {
            if (s.charAt(i) != s.charAt(start)) continue;

            res = Math.min(res, minPrint(s, start, i - 1, dp) 
                                + minPrint(s, i + 1, end, dp));
        }
        return dp[start][end] = res;
    }

    void test(String s, int expected) {
        assertEquals(expected, strangePrinter(s));
        assertEquals(expected, strangePrinter2(s));
        assertEquals(expected, strangePrinter2_2(s));
        assertEquals(expected, strangePrinter3(s));
    }

    @Test
    public void test() {
        test("aba", 2);
        test("aaabbb", 2);
        test("", 0);
        test("bgtgb", 3);
        test("tbgtgb", 4);
        test("bacdadacbdbcabdabdbcdcbacbdcabca", 19);
        test("baacdddaaddaaaaccbddbcabdaabdbbcdcbbbacbddcabcaaa", 19);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
