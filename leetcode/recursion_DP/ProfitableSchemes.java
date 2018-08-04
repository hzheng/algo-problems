import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

// LC879: https://leetcode.com/problems/profitable-schemes/
//
// There are G people in a gang, and a list of various crimes they could commit.
// The i-th crime generates a profit[i] and requires group[i] gang members. If a
// member participates in one crime, he can't participate in another crime.
// Let's call a profitable scheme any subset of these crimes that generates at
// least P profit, and the total number of gang members participating in that
// subset of crimes is at most G. How many schemes can be chosen?  Since the
// answer may be very large, return it modulo 10^9 + 7.
// Note:
// 1 <= G <= 100
// 0 <= P <= 100
// 1 <= group[i] <= 100
// 0 <= profit[i] <= 100
// 1 <= group.length = profit.length <= 100
public class ProfitableSchemes {
    // Dynamic Programming
    // time complexity: O(N * G * P), space complexity: O(N * G * Max(profit)),
    // beats 18.72%(107 ms for 43 tests)
    public int profitableSchemes(int G, int P, int[] group, int[] profit) {
        final int MOD = 1000_000_000 + 7;
        final int MAX_PROFIT = 100;
        int n = group.length;
        int M = MAX_PROFIT * n;
        int[][] dp = new int[G + 1][M + 1];
        dp[0][0] = 1;
        for (int i = 0, m = 0; i < n; i++) {
            int g = group[i];
            int p = profit[i];
            m += profit[i];
            for (int j = G; j >= g; j--) {
                for (int k = m; k >= p; k--) {
                    dp[j][k] += dp[j - g][k - p];
                    dp[j][k] %= MOD;
                }
            }
        }
        int res = 0;
        for (int i = 1; i <= G; i++) {
            for (int j = P; j <= M; j++) {
                res = (res + dp[i][j]) % MOD;
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N * G * P), space complexity: O(G * P),
    // beats 100.00%(14 ms for 43 tests)
    public int profitableSchemes2(int G, int P, int[] group, int[] profit) {
        final int MOD = 1000_000_000 + 7;
        int[][] dp = new int[P + 1][G + 1];
        dp[0][0] = 1;
        for (int k = group.length - 1; k >= 0; k--) {
            int g = group[k];
            int p = profit[k];
            for (int i = P; i >= 0; i--) {
                for (int j = G; j >= g; j--) {
                    int mp = Math.min(i + p, P);
                    dp[mp][j] = (dp[mp][j] + dp[i][j - g]) % MOD;
                }
            }
        }
        int res = 0;
        for (int x : dp[P]) {
            res = (res + x) % MOD;
        }
        return res;
    }

    // https://leetcode.com/problems/profitable-schemes/solution/
    // Dynamic Programming
    // time complexity: O(N * G * P), space complexity: O(G * P),
    // beats 31.91%(61 ms for 43 tests)
    public int profitableSchemes3(int G, int P, int[] group, int[] profit) {
        final int MOD = 1000_000_000 + 7;
        int n = group.length;
        long[][][] dp = new long[2][P + 1][G + 1];
        dp[0][0][0] = 1;
        for (int i = 0; i < n; i++) {
            int p = profit[i];
            int g = group[i];
            long[][] cur = dp[i % 2];
            long[][] next = dp[(i + 1) % 2];
            for (int j = 0; j <= P; j++) { // deep copy
                next[j] = Arrays.copyOf(cur[j], G + 1);
            }
            for (int j = 0; j <= P; j++) {
                int mp = Math.min(j + p, P);
                for (int k = 0; k <= G - g; k++) {
                    next[mp][g + k] += cur[j][k];
                    next[mp][g + k] %= MOD;
                }
            }
        }
        long res = 0;
        for (long x : dp[n % 2][P]) {
            res += x;
        }
        return (int) (res % MOD);
    }

    void test(int G, int P, int[] group, int[] profit, int expected) {
        assertEquals(expected, profitableSchemes(G, P, group, profit));
        assertEquals(expected, profitableSchemes2(G, P, group, profit));
        assertEquals(expected, profitableSchemes3(G, P, group, profit));
    }

    @Test
    public void test() {
        test(5, 3, new int[] { 2, 2 }, new int[] { 2, 3 }, 2);
        test(10, 5, new int[] { 2, 3, 5 }, new int[] { 6, 7, 8 }, 7);
        test(40, 50, new int[] { 2, 3, 5, 9, 10, 18, 72 },
                     new int[] { 6, 7, 8, 10, 23, 45, 35 }, 27);
        test(100, 100,
             new int[] { 2, 5, 36, 2, 5, 5, 14, 1, 12, 1, 14, 15, 1, 1, 27, 13,
                         6, 59, 6, 1, 7, 1, 2, 7, 6, 1, 6, 1,
                         3, 1, 2, 11, 3, 39, 21, 20, 1, 27, 26, 22, 11, 17, 3,
                         2, 4, 5, 6, 18, 4, 14, 1, 1, 1, 3, 12, 9,
                         7, 3, 16, 5, 1, 19, 4, 8, 6, 3, 2, 7, 3, 5, 12, 6, 15,
                         2, 11, 12, 12, 21, 5, 1, 13, 2, 29, 38,
                         10, 17, 1, 14, 1, 62, 7, 1, 14, 6, 4, 16, 6, 4, 32,
                         48 },
             new int[] { 21, 4, 9, 12, 5, 8, 8, 5, 14, 18, 43, 24, 3, 0, 20, 9,
                         0, 24, 4, 0, 0, 7, 3, 13, 6, 5, 19,
                         6, 3, 14, 9, 5, 5, 6, 4, 7, 20, 2, 13, 0, 1, 19, 4, 0,
                         11, 9, 6, 15, 15, 7, 1, 25, 17, 4, 4, 3,
                         43, 46, 82, 15, 12, 4, 1, 8, 24, 3, 15, 3, 6, 3, 0, 8,
                         10, 8, 10, 1, 21, 13, 10, 28, 11, 27, 17,
                         1, 13, 10, 11, 4, 36, 26, 4, 2, 2, 2, 10, 0, 11, 5, 22,
                         6 },
             692206787);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
