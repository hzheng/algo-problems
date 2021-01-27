import org.junit.Test;

import static org.junit.Assert.*;

// LC1444: https://leetcode.com/problems/number-of-ways-of-cutting-a-pizza/
//
// Given a rectangular pizza represented as a rows x cols matrix containing the following
// characters: 'A' (an apple) and '.' (empty cell) and given the integer k. You have to cut the
// pizza into k pieces using k-1 cuts. For each cut you choose the direction: vertical or
// horizontal, then you choose a cut position at the cell boundary and cut the pizza into two
// pieces. If you cut the pizza vertically, give the left part of the pizza to a person. If you cut
// the pizza horizontally, give the upper part of the pizza to a person. Give the last piece of
// pizza to the last person. Return the number of ways of cutting the pizza such that each piece
// contains at least one apple. Since the answer can be a huge number, return this modulo 10^9 + 7.
//
// Constraints:
// 1 <= rows, cols <= 50
// rows == pizza.length
// cols == pizza[i].length
// 1 <= k <= 10
// pizza consists of characters 'A' and '.' only.
public class WaysToCutPizza {
    private static final int MOD = 1_000_000_007;

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N*K*(M+N)), space complexity: O(M*N*K)
    // 10 ms(44.75%), 38.7 MB(19.89%) for 53 tests
    public int ways(String[] pizza, int k) {
        int m = pizza.length;
        int n = pizza[0].length();
        int[][] sum = new int[m + 1][n + 1];
        for (int x = m - 1; x >= 0; x--) {
            for (int y = n - 1; y >= 0; y--) {
                sum[x][y] = sum[x][y + 1] + sum[x + 1][y] - sum[x + 1][y + 1] + (
                        pizza[x].charAt(y) == 'A' ? 1 : 0);
            }
        }
        int[][][] dp = new int[m + 1][n + 1][k];
        for (int i = 0; i < k; i++) {
            for (int x = m - 1; x >= 0; x--) {
                for (int y = n - 1; y >= 0; y--) {
                    if (i == 0) {
                        dp[x][y][0] = (sum[x][y] > 0) ? 1 : 0;
                        continue;
                    }
                    long ways = 0;
                    for (int cut = x + 1; cut <= m; cut++) {
                        ways += dp[cut][y][i - 1] * (sum[cut][y] < sum[x][y] ? 1 : 0);
                    }
                    for (int cut = y + 1; cut <= n; cut++) {
                        ways += dp[x][cut][i - 1] * (sum[x][cut] < sum[x][y] ? 1 : 0);
                    }
                    dp[x][y][i] = (int)(ways % MOD);
                }
            }
        }
        return dp[0][0][k - 1];
    }

    // 2D-Dynamic Programming(Top-Down)
    // time complexity: O(M*N*K*(M+N)), space complexity: O(M*N*K)
    // 8 ms(54.14%), 36.5 MB(87.29%) for 53 tests
    public int ways2(String[] pizza, int k) {
        int m = pizza.length;
        int n = pizza[0].length();
        Integer[][][] dp = new Integer[k][m][n];
        int[][] sum = new int[m + 1][n + 1];
        for (int x = m - 1; x >= 0; x--) {
            for (int y = n - 1; y >= 0; y--) {
                sum[x][y] = sum[x][y + 1] + sum[x + 1][y] - sum[x + 1][y + 1] + (
                        pizza[x].charAt(y) == 'A' ? 1 : 0);
            }
        }
        return dfs(m, n, k - 1, 0, 0, dp, sum);
    }

    private int dfs(int m, int n, int k, int x, int y, Integer[][][] dp, int[][] sum) {
        if (sum[x][y] == 0) { return 0; }
        if (k == 0) { return 1; }
        if (dp[k][x][y] != null) { return dp[k][x][y]; }

        int res = 0;
        for (int nx = x + 1; nx < m; nx++) {
            if (sum[x][y] > sum[nx][y]) {
                res = (res + dfs(m, n, k - 1, nx, y, dp, sum)) % MOD;
            }
        }
        for (int ny = y + 1; ny < n; ny++) {
            if (sum[x][y] > sum[x][ny]) {
                res = (res + dfs(m, n, k - 1, x, ny, dp, sum)) % MOD;
            }
        }
        return dp[k][x][y] = res;
    }

    private void test(String[] pizza, int k, int expected) {
        assertEquals(expected, ways(pizza, k));
        assertEquals(expected, ways2(pizza, k));
    }

    @Test public void test() {
        test(new String[] {"A..", "A..", "..."}, 1, 1);
        test(new String[] {"A..", "AAA", "..."}, 3, 3);
        test(new String[] {"A..", "AA.", "..."}, 3, 1);
        test(new String[] {"..A.A.AAA...AAAAAA.AA..A..A.A......A.AAA.AAAAAA.AA",
                           "A.AA.A.....AA..AA.AA.A....AAA.A........AAAAA.A.AA.",
                           "A..AA.AAA..AAAAAAAA..AA...A..A...A..AAA...AAAA..AA",
                           "....A.A.AA.AA.AA...A.AA.AAA...A....AA.......A..AA.",
                           "AAA....AA.A.A.AAA...A..A....A..AAAA...A.A.A.AAAA..",
                           "....AA..A.AA..A.A...A.A..AAAA..AAAA.A.AA..AAA...AA",
                           "A..A.AA.AA.A.A.AA..A.A..A.A.AAA....AAAAA.A.AA..A.A",
                           ".AA.A...AAAAA.A..A....A...A.AAAA.AA..A.AA.AAAA.AA.",
                           "A.AA.AAAA.....AA..AAA..AAAAAAA...AA.A..A.AAAAA.A..",
                           "A.A...A.A...A..A...A.AAAA.A..A....A..AA.AAA.AA.AA.",
                           ".A.A.A....AAA..AAA...A.AA..AAAAAAA.....AA....A....",
                           "..AAAAAA..A..A...AA.A..A.AA......A.AA....A.A.AAAA.",
                           "...A.AA.AAA.AA....A..AAAA...A..AAA.AAAA.A.....AA.A",
                           "A.AAAAA..A...AAAAAAAA.AAA.....A.AAA.AA.A..A.A.A...",
                           "A.A.AA...A.A.AA...A.AA.AA....AA...AA.A..A.AA....AA",
                           "AA.A..A.AA..AAAAA...A..AAAAA.AA..AA.AA.A..AAAAA..A",
                           "...AA....AAAA.A...AA....AAAAA.A.AAAA.A.AA..AA..AAA",
                           "..AAAA..AA..A.AA.A.A.AA...A...AAAAAAA..A.AAA..AA.A",
                           "AA....AA....AA.A......AAA...A...A.AA.A.AA.A.A.AA.A",
                           "A.AAAA..AA..A..AAA.AAA.A....AAA.....A..A.AA.A.A...",
                           "..AA...AAAAA.A.A......AA...A..AAA.AA..A.A.A.AA..A.",
                           ".......AA..AA.AAA.A....A...A.AA..A.A..AAAAAAA.AA.A",
                           ".A.AAA.AA..A.A.A.A.A.AA...AAAA.A.A.AA..A...A.AAA..",
                           "A..AAAAA.A..A..A.A..AA..A...AAA.AA.A.A.AAA..A.AA..",
                           "A.AAA.A.AAAAA....AA..A.AAA.A..AA...AA..A.A.A.AA.AA",
                           ".A..AAAA.A.A.A.A.......AAAA.AA...AA..AAA..A...A.AA",
                           "A.A.A.A..A...AA..A.AAA..AAAAA.AA.A.A.A..AA.A.A....",
                           "A..A..A.A.AA.A....A...A......A.AA.AAA..A.AA...AA..",
                           ".....A..A...A.A...A..A.AA.A...AA..AAA...AA..A.AAA.",
                           "A...AA..A..AA.A.A.AAA..AA..AAA...AAA..AAA.AAAAA...",
                           "AA...AAA.AAA...AAAA..A...A..A...AA...A..AA.A...A..",
                           "A.AA..AAAA.AA.AAA.A.AA.A..AAAAA.A...A.A...A.AA....",
                           "A.......AA....AA..AAA.AAAAAAA.A.AA..A.A.AA....AA..",
                           ".A.A...AA..AA...AA.AAAA.....A..A..A.AA.A.AA...A.AA",
                           "..AA.AA.AA..A...AA.AA.AAAAAA.....A.AA..AA......A..",
                           "AAA..AA...A....A....AA.AA.AA.A.A.A..AA.AA..AAA.AAA",
                           "..AAA.AAA.A.AA.....AAA.A.AA.AAAAA..AA..AA.........",
                           ".AA..A......A.A.AAA.AAAA...A.AAAA...AAA.AAAA.....A",
                           "AAAAAAA.AA..A....AAAA.A..AA.A....AA.A...A.A....A..",
                           ".A.A.AA..A.AA.....A.A...A.A..A...AAA..A..AA..A.AAA",
                           "AAAA....A...A.AA..AAA..A.AAA..AA.........AA.AAA.A.",
                           "......AAAA..A.AAA.A..AAA...AAAAA...A.AA..A.A.AA.A.",
                           "AA......A.AAAAAAAA..A.AAA...A.A....A.AAA.AA.A.AAA.",
                           ".A.A....A.AAA..A..AA........A.AAAA.AAA.AA....A..AA",
                           ".AA.A...AA.AAA.A....A.A...A........A.AAA......A...",
                           "..AAA....A.A...A.AA..AAA.AAAAA....AAAAA..AA.AAAA..",
                           "..A.AAA.AA..A.AA.A...A.AA....AAA.A.....AAA...A...A",
                           ".AA.AA...A....A.AA.A..A..AAA.A.A.AA.......A.A...A.",
                           "...A...A.AA.A..AAAAA...AA..A.A..AAA.AA...AA...A.A.",
                           "..AAA..A.A..A..A..AA..AA...A..AA.AAAAA.A....A..A.A"}, 8, 641829390);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
