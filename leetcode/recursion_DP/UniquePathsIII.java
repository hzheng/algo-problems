import org.junit.Test;
import static org.junit.Assert.*;

// LC980: https://leetcode.com/problems/unique-paths-iii/
//
// On a 2-dimensional grid, there are 4 types of squares:
// 1 represents the starting square.  There is exactly one starting square.
// 2 represents the ending square.  There is exactly one ending square.
// 0 represents empty squares we can walk over.
// -1 represents obstacles that we cannot walk over.
// Return the number of 4-directional walks from the starting square to the
// ending square, that walk over every non-obstacle square exactly once.
// Note:
// 1 <= grid.length * grid[0].length <= 20
public class UniquePathsIII {
    private static final int[][] MOVES = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // DFS + Recursion + Backtracking
    // Time Complexity: O(4 ^ (R * C)), space complexity: O(R * C)
    // 2 ms(100.00%), 25.9 MB(100.00%) for 39 tests
    public int uniquePathsIII(int[][] grid) {
        int[] res = new int[1];
        int left = 0;
        int startX = 0;
        int startY = 0;
        for (int x = grid.length - 1; x >= 0; x--) {
            for (int y = grid[0].length - 1; y >= 0; y--) {
                if (grid[x][y] == -1) continue;

                left++;
                if (grid[x][y] == 1) {
                    startX = x;
                    startY = y;
                }
            }
        }
        dfs(grid, startX, startY, left, res);
        return res[0];
    }

    private void dfs(int[][] grid, int x, int y, int left, int[] res) {
        if (left-- <= 0) return;

        if (grid[x][y] == 2) {
            if (left == 0) {
                res[0]++;
            }
            return;
        }
        int n = grid.length;
        int m = grid[0].length;
        int old = grid[x][y];
        grid[x][y] = 3; // odd
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                if (grid[nx][ny] % 2 == 0) {
                    dfs(grid, nx, ny, left, res);
                }
            }
        }
        grid[x][y] = old;
    }

    // Recursion + Dynamic Programming(Top-Down) + Bit Manipulation
    // Time Complexity: O(R * C * 2 ^ (R * C)), space complexity: O(R * C)
    // 125 ms(7.83%), 357.6 MB(100.00%) for 39 tests
    public int uniquePathsIII2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int target = 0;
        int startX = 0;
        int startY = 0;
        for (int x = n - 1; x >= 0; x--) {
            for (int y = m - 1; y >= 0; y--) {
                if (grid[x][y] == 1) {
                    startX = x;
                    startY = y;
                } else if (grid[x][y] % 2 == 0) {
                    target |= code(x, y, m);
                }
            }
        }
        return dfs(grid, startX, startY, target, new Integer[n][m][1 << n * m]);
    }

    private int code(int x, int y, int m) {
        return 1 << (x * m + y);
    }

    private Integer dfs(int[][] grid, int x, int y, int target, Integer[][][] dp) {
        if (dp[x][y][target] != null) return dp[x][y][target];

        if (grid[x][y] == 2) return (target == 0) ? 1 : 0;

        int res = 0;
        int n = grid.length;
        int m = grid[0].length;
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                if ((target & code(nx, ny, m)) != 0) {
                    res += dfs(grid, nx, ny, target ^ code(nx, ny, m), dp);
                }
            }
        }
        return dp[x][y][target] = res;
    }

    // Dynamic Programming(Bottom-Up) + Bit Manipulation
    // Time Complexity: O(R * C * 2 ^ (R * C)), space complexity: O(R * C)
    // 2737 ms(0.60%), 464.6 MB(100.00%) for 39 tests
    public int uniquePathsIII3(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int start = 0;
        int end = 0;
        int target = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] < 0) continue;

                int pos = i * m + j;
                target |= 1 << pos;
                if (grid[i][j] == 1) {
                    start = pos;
                } else if (grid[i][j] == 2) {
                    end = pos;
                }
            }
        }
        int[][] dp = new int[1 << n * m][n * m];
        dp[1 << start][start] = 1;
        for (int t = 1; t < dp.length; t++) {
            if ((t & (t - 1)) == 0) continue; // skip 1-bit numbers

            for (int pos = 0; pos < n * m; pos++) {
                int x = pos / m;
                int y = pos % m;
                if ((t << ~pos) >= 0 || grid[x][y] < 0) continue;

                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                        dp[t][pos] += dp[t ^ (1 << pos)][nx * m + ny];
                    }
                }
            }
        }
        return dp[target][end];
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, uniquePathsIII(grid));
        assertEquals(expected, uniquePathsIII2(grid));
        assertEquals(expected, uniquePathsIII3(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 2, -1}}, 2);
        test(new int[][] {{1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 2}}, 4);
        test(new int[][] {{0, 1}, {2, 0}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
