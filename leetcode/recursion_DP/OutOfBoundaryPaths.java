import org.junit.Test;
import static org.junit.Assert.*;

// LC576: https://leetcode.com/problems/out-of-boundary-paths/
//
// There is an m by n grid with a ball. Given the start coordinate of the ball, you
// can move the ball to adjacent cell or cross the grid boundary in four directions.
// However, you can at most move N times. Find out the number of paths to move the
// ball out of grid boundary. The answer may be very large, return it after mod 10 ^ 9 + 7.
public class OutOfBoundaryPaths {
    static final int MOD = 1000_000_000 + 7;

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N * m * n), space complexity: O(N * m * n)
    // beats 72.73%(31 ms for 94 tests)
    public int findPaths(int m, int n, int N, int i, int j) {
        if (N == 0) return 0;

        int[][][] dp = new int[m][n][N];
        dp[0][0][0] = 1;
        dp[0][n - 1][0]++;
        if (m > 1) {
            dp[m - 1][0][0] = 1;
            dp[m - 1][n - 1][0]++;
        }
        for (int y = 0; y < n; y++) {
            dp[0][y][0]++;
            dp[m - 1][y][0]++;
        }
        for (int x = 1; x < m - 1; x++) {
            dp[x][0][0] = 1;
            dp[x][n - 1][0]++;
        }
        for (int k = 1; k < N; k++) {
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    long count = (x == 0) ? 0 : dp[x - 1][y][k - 1];
                    count += (x == m - 1) ? 0 : dp[x + 1][y][k - 1];
                    count += (y == 0) ? 0 : dp[x][y - 1][k - 1];
                    count += (y == n - 1) ? 0 : dp[x][y + 1][k - 1];
                    dp[x][y][k] = (int)(count % MOD);
                }
            }
        }
        long count = 0;
        for (int k = 0; k < N; k++) {
            count += dp[i][j][k];
        }
        return (int)(count % MOD);
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N * m * n), space complexity: O(N * m * n)
    // beats 72.73%(36 ms for 94 tests)
    public int findPaths2(int m, int n, int N, int i, int j) {
        int[][][] dp = new int[m][n][N + 1];
        dp[i][j][0] = 1;
        int count = 0;
        int[][] dirs = new int[][] {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int k = 0; k < N; k++) {
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    for (int[] dir : dirs) {
                        int nx = x + dir[0];
                        int ny = y + dir[1];
                        if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                            dp[nx][ny][k + 1] = (dp[nx][ny][k + 1] + dp[x][y][k]) % MOD;
                        } else {
                            count = (count + dp[x][y][k]) % MOD;
                        }
                    }
                }
            }
        }
        return count;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N * m * n), space complexity: O(m * n)
    // beats 72.73%(36 ms for 94 tests)
    public int findPaths3(int m, int n, int N, int i, int j) {
        int dp[][] = new int[m][n];
        dp[i][j] = 1;
        int count = 0;
        for (int moves = 1; moves <= N; moves++) {
            int[][] buf = new int[m][n];
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    if (x == m - 1) {
                        count = (count + dp[x][y]) % MOD;
                    }
                    if (y == n - 1) {
                        count = (count + dp[x][y]) % MOD;
                    }
                    if (x == 0) {
                        count = (count + dp[x][y]) % MOD;
                    }
                    if (y == 0) {
                        count = (count + dp[x][y]) % MOD;
                    }
                    buf[x][y] = (((x > 0 ? dp[x - 1][y] : 0) + (x < m - 1 ? dp[x + 1][y] : 0)) % MOD
                                 + ((y > 0 ? dp[x][y - 1] : 0) + (y < n - 1 ? dp[x][y + 1] : 0)) % MOD) % MOD;
                }
            }
            dp = buf;
        }
        return count;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N * m * n), space complexity: O(N * m * n)
    // beats 72.73%(28 ms for 94 tests)
    public int findPaths4(int m, int n, int N, int i, int j) {
        int[][][] dp = new int[m][n][N + 1];
        for (int k = 0; k < N; k++) {
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    dp[x][y][k + 1] = (int)(((long)(x == 0 ? 1 : dp[x - 1][y][k])
                                         + (x == m - 1 ? 1 : dp[x + 1][y][k])
                                         + (y == 0 ? 1 : dp[x][y - 1][k])
                                         + (y == n - 1 ? 1 : dp[x][y + 1][k])) % MOD);
                }
            }
        }
        return dp[i][j][N];
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N * m * n), space complexity: O(m * n)
    // beats 72.73%(28 ms for 94 tests)
    public int findPaths4_2(int m, int n, int N, int i, int j) {
        int[][][] dp = new int[m][n][2];
        for (int k = N; k > 0; k--) { // for (int k = 0; k < N; k++) {
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    dp[x][y][(k + 1) % 2] = (int)(((long)(x == 0 ? 1 : dp[x - 1][y][k % 2])
                                         + (x == m - 1 ? 1 : dp[x + 1][y][k % 2])
                                         + (y == 0 ? 1 : dp[x][y - 1][k % 2])
                                         + (y == n - 1 ? 1 : dp[x][y + 1][k % 2])) % MOD);
                }
            }
        }
        return dp[i][j][0]; // return dp[i][j][N % 2];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N * m * n), space complexity: O(N * m * n)
    // Time Limit Exceeded
    public int findPaths5(int m, int n, int N, int i, int j) {
        return findPaths(m, n, N, i, j, new int[m][n][N + 1]);
    }

    private int findPaths(int m, int n, int move, int i, int j, int[][][] memo) {
        if (i == m || j == n || i < 0 || j < 0) return 1;

        if (move == 0) return 0;
        if (memo[i][j][move] > 0) return memo[i][j][move];

        long count = findPaths(m, n, move - 1, i - 1, j, memo);
        count = (count + findPaths(m, n, move - 1, i + 1, j, memo)) % MOD;
        count = (count + findPaths(m, n, move - 1, i, j - 1, memo)) % MOD;
        return memo[i][j][move] = (int)((count + findPaths(m, n, move - 1, i, j + 1, memo)) % MOD);
    }

    void test(int m,  int n, int N, int i, int j, int expected) {
        assertEquals(expected, findPaths(m, n, N, i, j));
        assertEquals(expected, findPaths2(m, n, N, i, j));
        assertEquals(expected, findPaths3(m, n, N, i, j));
        assertEquals(expected, findPaths4(m, n, N, i, j));
        assertEquals(expected, findPaths4_2(m, n, N, i, j));
        if (N < 30) {
            assertEquals(expected, findPaths5(m, n, N, i, j));
        }
    }

    @Test
    public void test() {
        test(2, 1, 0, 0, 0, 0);
        test(2, 1, 2, 0, 0, 6);
        test(2, 2, 2, 0, 0, 6);
        test(1, 3, 3, 0, 1, 12);
        test(3, 1, 5, 2, 0, 27);
        test(5, 10, 8, 1, 1, 4413);
        test(8, 50, 23, 5, 26, 914783380);
        test(45, 35, 47, 20, 31, 951853874);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
