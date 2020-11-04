import org.junit.Test;

import static org.junit.Assert.*;

// LC688: https://leetcode.com/problems/knight-probability-in-chessboard/
//
// On an NxN chessboard, a knight starts at the r-th row and c-th column and attempts to make
// exactly K moves. The rows and columns are 0 indexed, so the top-left square is (0, 0), and the
// bottom-right square is (N-1, N-1). A chess knight has 8 possible moves it can make. Each move is
// two squares in a cardinal direction, then one square in an orthogonal direction. Each time the
// knight is to move, it chooses one of eight possible moves uniformly at random (even if the piece
// would go off the chessboard) and moves there. The knight continues moving until it has made
// exactly K moves or has moved off the chessboard. Return the probability that the knight remains
// on the board after it has stopped moving.
// Note:
// N will be between 1 and 25.
// K will be between 0 and 100.
// The knight always initially starts on the board.
public class KnightProbability {
    static final int[][] MOVES =
            {{1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1}};

    // 3D-Dynamic Programming(Bottom-Up)
    // time complexity: O(K*N^2), space complexity: O(K*N^2)
    // 5 ms(62.93%), 37.2 MB(10.77%) for 21 tests
    public double knightProbability(int N, int K, int r, int c) {
        double[][][] p = new double[N][N][K + 1];
        p[r][c][0] = 1.0;
        for (int k = 1; k <= K; k++) {
            for (int x = 0; x < N; x++) {
                for (int y = 0; y < N; y++) {
                    if (p[x][y][k - 1] == 0) { continue; }

                    for (int[] move : MOVES) {
                        int nx = x + move[0];
                        int ny = y + move[1];
                        if (nx >= 0 && nx < N && ny >= 0 && ny < N) {
                            p[nx][ny][k] += p[x][y][k - 1] / 8;
                        }
                    }
                }
            }
        }
        double res = 0;
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                res += p[x][y][K];
            }
        }
        return res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 4 ms(71.32%), 38.4 MB(10.77%) for 21 tests
    public double knightProbability2(int N, int K, int r, int c) {
        double[][] p = new double[N][N];
        p[r][c] = 1.0;
        for (int k = 1; k <= K; k++) {
            double[][] np = new double[N][N];
            for (int x = 0; x < N; x++) {
                for (int y = 0; y < N; y++) {
                    if (p[x][y] == 0) { continue; }

                    for (int[] move : MOVES) {
                        int nx = x + move[0];
                        int ny = y + move[1];
                        if (nx >= 0 && nx < N && ny >= 0 && ny < N) {
                            np[nx][ny] += p[x][y] / 8;
                        }
                    }
                }
            }
            p = np;
        }
        double res = 0;
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                res += p[x][y];
            }
        }
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(K*N^2), space complexity: O(K*N^2)
    // 3 ms(96.89%), 37.2 MB(10.77%) for 21 tests
    public double knightProbability3(int N, int K, int r, int c) {
        return dfs(N, K, r, c, new double[N][N][K + 1]);
    }

    private double dfs(int N, int K, int x, int y, double[][][] dp) {
        if (x < 0 || x > N - 1 || y < 0 || y > N - 1) { return 0; }
        if (K == 0) { return 1; }
        if (dp[x][y][K] != 0) { return dp[x][y][K]; }

        double p = 0;
        for (int[] move : MOVES) {
            p += dfs(N, K - 1, x + move[0], y + move[1], dp) / 8;
        }
        return dp[x][y][K] = p;
    }

    private void test(int N, int K, int r, int c, double expected) {
        assertEquals(expected, knightProbability(N, K, r, c), 1E-5);
        assertEquals(expected, knightProbability2(N, K, r, c), 1E-5);
        assertEquals(expected, knightProbability3(N, K, r, c), 1E-5);
    }

    @Test public void test() {
        test(3, 2, 0, 0, 0.0625);
        test(6, 7, 3, 3, 0.05306);
        test(20, 90, 10, 10, 0.01270);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
