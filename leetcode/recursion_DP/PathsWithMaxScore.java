import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1301: https://leetcode.com/problems/number-of-paths-with-max-score/
//
// You are given a square board of characters. You can move on the board starting at the bottom
// right square marked with the character 'S'.
// You need to reach the top left square marked with the character 'E'. The rest of the squares are
// labeled either with a numeric character 1, 2, ..., 9 or with an obstacle 'X'. In one move you can
// go up, left or up-left (diagonally) only if there is no obstacle there.
// Return a list of two integers: the first integer is the maximum sum of numeric characters you can
// collect, and the second is the number of such paths that you can take to get that maximum sum,
// taken modulo 10^9 + 7.
// In case there is no path, return [0, 0].
//
// Constraints:
// 2 <= board.length == board[i].length <= 100
public class PathsWithMaxScore {
    private static final int MOD = 1_000_000_007;
    private static final int[][] MOVES = {{0, -1}, {-1, 0}, {-1, -1}};

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 8 ms(70.59%), 39.7 MB(31.37%) for 25 tests
    public int[] pathsWithMaxScore(List<String> board) {
        int n = board.size();
        char[][] grid = new char[n][];
        for (int i = 0; i < n; i++) {
            grid[i] = board.get(i).toCharArray();
        }
        grid[0][0] = '0';
        int[][][] dp = new int[n][n][2];
        dp[n - 1][n - 1][1] = 1;
        for (int x = n - 1; x >= 0; x--) {
            for (int y = n - 1; y >= 0; y--) {
                if (dp[x][y][1] == 0) { continue; }

                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx < 0 || ny < 0 || grid[nx][ny] == 'X') { continue; }

                    int nSum = dp[x][y][0] + grid[nx][ny] - '0';
                    if (dp[nx][ny][0] < nSum) {
                        dp[nx][ny][0] = nSum;
                        dp[nx][ny][1] = dp[x][y][1];
                    } else if (dp[nx][ny][0] == nSum) {
                        dp[nx][ny][1] = (dp[nx][ny][1] + dp[x][y][1]) % MOD;
                    }
                }
            }
        }
        return dp[0][0];
    }

    // FIXME: DFS doesn't work!
    // 2-D Dynamic Programming(Top-Down) + Recursion
    // time complexity: O(N^2), space complexity: O(N^2)
    public int[] pathsWithMaxScore2(List<String> board) {
        int n = board.size();
        char[][] grid = new char[n][];
        for (int i = 0; i < n; i++) {
            grid[i] = board.get(i).toCharArray();
        }
        grid[0][0] = '0';
        int[][][] dp = new int[n][n][2];
        dp[n - 1][n - 1][1] = 1;
        dfs(n - 1, n - 1, grid, dp, new HashSet<>());
        return dp[0][0];
    }

    private void dfs(int x, int y, char[][] grid, int[][][] dp, Set<String> visited) {
        for (int i = 0; i < MOVES.length; i++) {
            int[] move = MOVES[i];
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx < 0 || ny < 0 || grid[nx][ny] == 'X') { continue; }

            int nSum = dp[x][y][0] + grid[nx][ny] - '0';
            if (dp[nx][ny][0] < nSum) {
                dp[nx][ny][0] = nSum;
                dp[nx][ny][1] = dp[x][y][1];
            } else if (dp[nx][ny][0] == nSum) {
                dp[nx][ny][1] = (dp[nx][ny][1] + dp[x][y][1]) % MOD;
            }
            dfs(nx, ny, grid, dp, visited);
        }
    }

    private void test(String[] boardArray, int[] expected) {
        List<String> board = Arrays.asList(boardArray);
        assertArrayEquals(expected, pathsWithMaxScore(board));
//        assertArrayEquals(expected, pathsWithMaxScore2(board));
    }

    @Test public void test() {
        test(new String[] {"E23", "2X2", "12S"}, new int[] {7, 1});
        test(new String[] {"E12", "1X1", "21S"}, new int[] {4, 2});
        test(new String[] {"E2", "1S"}, new int[] {2, 1});
        test(new String[] {"E11", "XXX", "11S"}, new int[] {0, 0});
        test(new String[] {"E123", "1X21", "2X14", "2X1S"}, new int[] {11, 1});
        test(new String[] {"E11345", "X452XX", "3X43X4", "422812", "284522", "13422S"},
             new int[] {34, 3});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
