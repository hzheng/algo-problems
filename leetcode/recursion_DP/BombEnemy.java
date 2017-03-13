import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC361: https://leetcode.com/problems/bomb-enemy
//
// Given a 2D grid, each cell is either a wall 'W', an enemy 'E' or empty '0',
// return the maximum enemies you can kill using one bomb.
// The bomb kills all the enemies in the same row and column from the planted
// point until it hits the wall since the wall is too strong to be destroyed.
// Note that you can only put the bomb at an empty cell.
public class BombEnemy {
    private static final int[][] SHIFTS = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // Brute Force
    // beats 4.59%(667 ms for 47 tests)
    public int maxKilledEnemies(char[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '0') {
                    max = Math.max(max, count(grid, m, n, i, j));
                }
            }
        }
        return max;
    }

    private int count(char[][] grid, int m, int n, int x, int y) {
        int sum = 0;
        for (int[] shift : SHIFTS) {
            int dx = shift[0];
            int dy = shift[1];
            for (int r = x + dx, c = y + dy; r >= 0 && r < m && c >= 0 && c < n && grid[r][c] != 'W'; r += dx, c += dy) {
                if (grid[r][c] == 'E') {
                    sum++;
                }
            }
        }
        return sum;
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 30.99%(69 ms for 47 tests)
    public int maxKilledEnemies2(char[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        int[][][] dpRows = new int[2][m][n + 1];
        int[][] rowStarts = new int[][] {{1, 0}, {n - 1, n - 1}};
        for (int i = 0; i < 2; i++) {
            int[][] dpRow = dpRows[i];
            int dy = SHIFTS[i][0];
            int[] start = rowStarts[i];
            for (int x = 0; x < m; x++) {
                int[] dp = dpRow[x];
                for (int j = start[0], y = start[1]; y >= 0 && y < n; y += dy, j += dy) {
                    switch (grid[x][y]) {
                    case 'E': dp[j] = dp[j - dy] + 1; break;
                    case 'W': dp[j] = 0; break;
                    default: dp[j] = dp[j - dy]; break;
                    }
                }
            }
        }
        int[][][] dpCols = new int[2][n][m + 1];
        int[][] colStarts = new int[][] {{1, 0}, {m - 1, m - 1}};
        for (int i = 0; i < 2; i++) {
            int[][] dpCol = dpCols[i];
            int dx = SHIFTS[i][0];
            int[] start = colStarts[i];
            for (int y = 0; y < n; y++) {
                int[] dp = dpCol[y];
                for (int j = start[0], x = start[1]; x >= 0 && x < m; x += dx, j += dx) {
                    switch (grid[x][y]) {
                    case 'E': dp[j] = dp[j - dx] + 1; break;
                    case 'W': dp[j] = 0; break;
                    default: dp[j] = dp[j - dx]; break;
                    }
                }
            }
        }
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '0') {
                    max = Math.max(max, dpRows[0][i][j] + dpRows[1][i][j] + dpCols[0][j][i] + dpCols[1][j][i]);
                }
            }
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 97.45%(35 ms for 47 tests)
    public int maxKilledEnemies3(char[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        int max = 0;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            int enemies = 0;
            for (int j = 0; j < n; j++) {
                switch (grid[i][j]) {
                    case 'E': enemies++; break;
                    case '0': dp[i][j] = enemies; break;
                    default: enemies = 0;
                }
            }
            enemies = 0;
            for (int j = n - 1;  j >= 0; j--) {
                switch (grid[i][j]) {
                    case 'E': enemies++; break;
                    case '0': dp[i][j] += enemies; break;
                    default: enemies = 0;
                }
            }
        }
        for (int j = 0; j < n; j++) {
            int enemies = 0;
            for (int i = 0; i < m; i++) {
                switch (grid[i][j]) {
                    case 'E': enemies++; break;
                    case '0': dp[i][j] += enemies; break;
                    default: enemies = 0;
                }
            }
            enemies = 0;
            for (int i = m - 1; i >= 0; i--) {
                switch (grid[i][j]) {
                    case 'E': enemies++; break;
                    case '0': max = Math.max(max, dp[i][j] += enemies); break;
                    default: enemies = 0;
                }
            }
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 58.31%(49 ms for 47 tests)
    public int maxKilledEnemies4(char[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0, head = 0, tail = 0; j < n; j++) {
                switch (grid[i][j]) {
                    case 'W': head = 0; break;
                    case 'E': head++; break;
                    case '0': dp[i][j] += head; break;
                }
                switch (grid[i][n - 1 - j]) {
                    case 'W': tail = 0; break;
                    case 'E': tail++; break;
                    case '0': dp[i][n - 1 - j] += tail; break;
                }
            }
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0, head = 0, tail = 0; i < m; i++) {
                switch (grid[i][j]) {
                    case 'W': head = 0; break;
                    case 'E': head++; break;
                    case '0': dp[i][j] += head; break;
                }
                switch (grid[m - 1 - i][j]) {
                    case 'W': tail = 0; break;
                    case 'E': tail++; break;
                    case '0': dp[m - 1 - i][j] += tail; break;
                }
            }
        }
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                max = Math.max(max, dp[i][j]);
            }
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(N)
    // beats 70.44%(46 ms for 47 tests)
    public int maxKilledEnemies5(char[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        int max = 0;
        int[] col = new int[n];
        for (int i = 0, row = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'W') continue;

                if (j == 0 || grid[i][j - 1] == 'W') {
                    row = 0;
                    for (int k = j; k < n && grid[i][k] != 'W'; k++) {
                        row += (grid[i][k] == 'E') ? 1 : 0;
                    }
                }
                if (i == 0 || grid[i - 1][j] == 'W') {
                    col[j] = 0;
                    for (int k = i; k < m && grid[k][j] != 'W'; k++) {
                        col[j] += (grid[k][j] == 'E') ? 1 : 0;
                    }
                }
                if (grid[i][j] == '0') {
                    max = Math.max(row + col[j], max);
                }
            }
        }
        return max;
    }

    void test(String[] s, int expected) {
        char[][] grid = Arrays.stream(s).map(x -> x.toCharArray()).toArray(char[][]::new);
        assertEquals(expected, maxKilledEnemies(grid));
        assertEquals(expected, maxKilledEnemies2(grid));
        assertEquals(expected, maxKilledEnemies3(grid));
        assertEquals(expected, maxKilledEnemies4(grid));
        assertEquals(expected, maxKilledEnemies5(grid));
    }

    @Test
    public void test() {
        test(new String[] {"000"}, 0);
        test(new String[] {"0E00", "E0WE", "0E00"}, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BombEnemy");
    }
}
