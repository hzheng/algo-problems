import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1659: https://leetcode.com/problems/maximize-grid-happiness/
//
// You are given four integers, m, n, introvertsCount, and extrovertsCount. You have an m x n grid,
// and there are two types of people: introverts and extroverts. There are introvertsCount
// introverts and extrovertsCount extroverts. You should decide how many people you want to live in
// the grid and assign each of them one grid cell. Note that you do not have to have all the people
// living in the grid.
//The happiness of each person is calculated as follows:
// Introverts start with 120 happiness and lose 30 happiness for each neighbor.
// Extroverts start with 40 happiness and gain 20 happiness for each neighbor.
// Neighbors live in the directly adjacent cells north, east, south, and west of a person's cell.
// The grid happiness is the sum of each person's happiness.
// Return the maximum possible grid happiness.
//
// Constraints:
// 1 <= m, n <= 5
// 0 <= introvertsCount, extrovertsCount <= min(m * n, 6)
public class MaxGridHappiness {
    private static final int[][] MOVES = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final int[] INITIAL = new int[] {0, 120, 40};
    private static final int[] CHANGE = new int[] {0, -30, 20};

    // Time Limit Exceeded
    public int getMaxGridHappiness0(int m, int n, int introvertsCount, int extrovertsCount) {
        int[] max = new int[1];
        dfs(new int[m][n], introvertsCount, extrovertsCount, 0, 0, max);
        return max[0];
    }

    private void dfs(int[][] grid, int introvertsCount, int extrovertsCount, int cur, int score,
                     int[] max) {
        int m = grid.length;
        int n = grid[0].length;
        if (cur == m * n) {
            max[0] = Math.max(max[0], score);
            return;
        }

        int x = cur / n;
        int y = cur % n;
        dfs(grid, introvertsCount, extrovertsCount, cur + 1, score, max);
        if (introvertsCount > 0) {
            grid[x][y] = -1;
            int newScore = score + 120 + adjustScore(grid, x, y);
            dfs(grid, introvertsCount - 1, extrovertsCount, cur + 1, newScore, max);
            grid[x][y] = 0;
        }
        if (extrovertsCount > 0) {
            grid[x][y] = 1;
            int newScore = score + 40 + adjustScore(grid, x, y);
            dfs(grid, introvertsCount, extrovertsCount - 1, cur + 1, newScore, max);
            grid[x][y] = 0;
        }
    }

    private int adjustScore(int[][] grid, int x, int y) {
        int m = grid.length;
        int n = grid[0].length;
        int change = 0;
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] != 0) {
                if (grid[nx][ny] == 1) {
                    change += 20;
                } else {
                    change -= 30;
                }
                if (grid[x][y] == 1) {
                    change += 20;
                } else {
                    change -= 30;
                }
            }
        }
        return change;
    }

    // DFS + Recursion + 5-D Dynamic Programming(Top-Down)
    // time complexity: O(M*N*I*E*2^I*2^E), space complexity: O(M*N*I*E*2^I*2^E)
    // 55 ms(100.00%), 47.9 MB(9.09%) for 67 tests
    public int getMaxGridHappiness(int m, int n, int introvertsCount, int extrovertsCount) {
        return dfs(m, n, 0, introvertsCount, extrovertsCount, 0, 0,
                   new int[introvertsCount + 1][extrovertsCount + 1][1 << n][1 << n][m * n]);
    }

    private int dfs(int m, int n, int cur, int introverts, int extroverts, int inMask, int exMask,
                    int[][][][][] dp) {
        int x = cur / n;
        if (x >= m) { return 0; }

        if (dp[introverts][extroverts][inMask][exMask][cur] != 0) {
            return dp[introverts][extroverts][inMask][exMask][cur] - 1;
        }

        int nextInMask = (inMask << 1) & ((1 << n) - 1);
        int nextExMask = (exMask << 1) & ((1 << n) - 1);
        int res = dfs(m, n, cur + 1, introverts, extroverts, nextInMask, nextExMask, dp);
        int y = cur % n;
        if (introverts > 0) {
            int diff = cost(n, x, y, inMask, exMask, 1);
            res = Math.max(res,
                           diff + dfs(m, n, cur + 1, introverts - 1, extroverts, nextInMask + 1,
                                      nextExMask, dp));
        }
        if (extroverts > 0) {
            int diff = cost(n, x, y, inMask, exMask, 2);
            res = Math.max(res, diff + dfs(m, n, cur + 1, introverts, extroverts - 1, nextInMask,
                                           nextExMask + 1, dp));
        }
        dp[introverts][extroverts][inMask][exMask][cur] = res + 1; // distinguish from 0
        return res;
    }

    private int cost(int n, int x, int y, int inMask, int exMask, int choice) {
        int res = INITIAL[choice];
        if (y > 0) {
            final int leftNeighbor = 1;
            if ((inMask & leftNeighbor) != 0) {
                res += CHANGE[choice] + CHANGE[1];
            }
            if ((exMask & leftNeighbor) != 0) {
                res += CHANGE[choice] + CHANGE[2];
            }
        }
        if (x > 0) {
            final int upperNeighbor = (1 << (n - 1));
            if ((inMask & upperNeighbor) != 0) {
                res += CHANGE[choice] + CHANGE[1];
            }
            if ((exMask & upperNeighbor) != 0) {
                res += CHANGE[choice] + CHANGE[2];
            }
        }
        return res;
    }

    // 3-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N*I*E*3^2N), space complexity: O(I*E*3^N)
    // 847 ms(30.41%), 38.7 MB(83.61%) for 67 tests
    public int getMaxGridHappiness2(int m, int n, int introvertsCount, int extrovertsCount) {
        final int choiceCount = (int)Math.pow(3, n);
        int[][][] dp = initialize(introvertsCount, extrovertsCount, choiceCount);
        dp[0][0][0] = 0;
        for (int x = 0; x < m; x++) {
            int[][][] ndp = initialize(introvertsCount, extrovertsCount, choiceCount);
            int[] lowerRow = new int[n];
            for (int lc = 0; lc < choiceCount; nextChoice(lowerRow, ++lc)) { // lower choice
                int vScore = 0; // new vertical scores
                int ni = 0; // new introverts
                int ne = 0; // new extroverts
                for (int y = 0; y < n; y++) {
                    vScore += INITIAL[lowerRow[y]];
                    if (lowerRow[y] == 1) {
                        ni++;
                    } else if (lowerRow[y] == 2) {
                        ne++;
                    }
                    if (y < n - 1 && lowerRow[y + 1] != 0) {
                        vScore += CHANGE[lowerRow[y]];
                    }
                    if (y > 0 && lowerRow[y - 1] != 0) {
                        vScore += CHANGE[lowerRow[y]];
                    }
                }
                int[] row = new int[n];
                for (int uc = 0; uc < choiceCount; nextChoice(row, ++uc)) { // upper choice
                    for (int i = ni; i <= introvertsCount; i++) {
                        for (int e = ne; e <= extrovertsCount; e++) {
                            int hScore = 0; // new horizontal scores
                            for (int y = 0; y < n; y++) {
                                if (row[y] != 0 && lowerRow[y] != 0) {
                                    hScore += CHANGE[row[y]] + CHANGE[lowerRow[y]];
                                }
                            }
                            ndp[i][e][lc] = Math.max(ndp[i][e][lc],
                                                     vScore + hScore + dp[i - ni][e - ne][uc]);
                        }
                    }
                }
            }
            dp = ndp;
        }
        int res = 0;
        for (int i = 0; i <= introvertsCount; i++) {
            for (int e = 0; e <= extrovertsCount; e++) {
                for (int c = 0; c < choiceCount; c++) {
                    res = Math.max(res, dp[i][e][c]);
                }
            }
        }
        return res;
    }

    private int[][][] initialize(int introvertsCount, int extrovertsCount, int choiceCount) {
        int[][][] dp = new int[introvertsCount + 1][extrovertsCount + 1][choiceCount];
        for (int i = 0; i <= introvertsCount; i++) {
            for (int j = 0; j <= extrovertsCount; j++) {
                Arrays.fill(dp[i][j], Integer.MIN_VALUE / 2);
            }
        }
        return dp;
    }

    private void nextChoice(int[] array, int num) {
        final int base = 3;
        for (int x = num, i = array.length - 1; i >= 0; i--, x /= base) {
            array[i] = x % base;
        }
    }

    // 4-D Dynamic Programming(Bottom-Up)
    // time complexity: O(M*I*E*3^2N), space complexity: O(M*I*E*3^N)
    // 339 ms(71.59%), 39.5 MB(58.44%) for 67 tests
    public int getMaxGridHappiness3(int m, int n, int introvertsCount, int extrovertsCount) {
        final int choiceCount = (int)Math.pow(3, n);
        int[][] counts = new int[3][choiceCount];
        int[] vScores = new int[choiceCount];
        for (int i = 0; i < choiceCount; i++) {
            for (int j = 0, num = i, prev = 0, cur; j < n; j++, num /= 3, prev = cur) {
                cur = num % 3;
                counts[cur][i]++;
                vScores[i] += INITIAL[cur];
                if (cur * prev != 0) {
                    vScores[i] += CHANGE[cur] + CHANGE[prev];
                }
            }
        }
        int[][] hScores = new int[choiceCount][choiceCount];
        for (int c1 = 0; c1 < choiceCount; c1++) {
            for (int c2 = 0; c2 < choiceCount; c2++) {
                for (int i = 0, n1 = c1, n2 = c2; i < n; i++, n1 /= 3, n2 /= 3) {
                    int d1 = n1 % 3;
                    int d2 = n2 % 3;
                    if (d1 * d2 != 0) {
                        hScores[c1][c2] += CHANGE[d1] + CHANGE[d2];
                    }
                }
            }
        }
        int[][][][] dp =
                new int[m + 1][choiceCount][introvertsCount + n + 1][extrovertsCount + n + 1];
        for (int c1 = 0; c1 < choiceCount; c1++) {
            dp[0][c1][counts[1][c1]][counts[2][c1]] = vScores[c1];
        }
        for (int x = 1; x < m; x++) {
            for (int i = 0; i <= introvertsCount; i++) {
                for (int e = 0; e <= extrovertsCount; e++) {
                    for (int c1 = 0; c1 < choiceCount; c1++) {
                        if (dp[x - 1][c1][i][e] == 0) { continue; }

                        for (int c2 = 0; c2 < choiceCount; c2++) {
                            dp[x][c2][i + counts[1][c2]][e + counts[2][c2]] =
                                    Math.max(dp[x][c2][i + counts[1][c2]][e + counts[2][c2]],
                                             dp[x - 1][c1][i][e] + vScores[c2] + hScores[c1][c2]);
                        }
                    }
                }
            }
        }
        int res = 0;
        for (int x = 0; x < m; x++) {
            for (int choice = 0; choice < choiceCount; choice++) {
                for (int i = 0; i <= introvertsCount; i++) {
                    for (int e = 0; e <= extrovertsCount; e++) {
                        res = Math.max(res, dp[x][choice][i][e]);
                    }
                }
            }
        }
        return res;
    }

    private void test(int m, int n, int introvertsCount, int extrovertsCount, int expected) {
        assertEquals(expected, getMaxGridHappiness(m, n, introvertsCount, extrovertsCount));
        assertEquals(expected, getMaxGridHappiness2(m, n, introvertsCount, extrovertsCount));
        assertEquals(expected, getMaxGridHappiness3(m, n, introvertsCount, extrovertsCount));
    }

    @Test public void test() {
        test(2, 3, 1, 2, 240);
        test(3, 1, 2, 1, 260);
        test(2, 2, 4, 0, 240);
        test(3, 1, 1, 3, 230);
        test(5, 5, 3, 6, 880);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
