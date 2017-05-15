import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC562: https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/
//
// Given a 01 matrix M, find the longest line of consecutive one in the matrix. The
// line could be horizontal, vertical, diagonal or anti-diagonal.
public class LongestConsecutiveOne {
    // Brute Force
    // time complexity: O(M * N), space complexity: O(1)
    // beats 79.11%(27 ms for 57 tests)
    public int longestLine(int[][] M) {
        int m = M.length;
        if (m == 0) return 0;

        int n = M[0].length;
        int max = 0;
        for (int[] row : M) { // horizontal
            for (int i = 0, last = -1; i < n; i++) {
                if (row[i] == 1) {
                    max = Math.max(max, i - last);
                } else {
                    last = i;
                }
            }
        }
        for (int j = 0; j < n; j++) { // vertical
            for (int i = 0, last = -1; i < m; i++) {
                if (M[i][j] == 1) {
                    max = Math.max(max, i - last);
                } else {
                    last = i;
                }
            }
        }
        for (int i = 0; i < m + n - 1; i++) { // diagonal
            for (int j = Math.max(i - n + 1, 0), last = j - 1; j <= Math.min(i, m - 1); j++) {
                if (M[j][i - j] == 1) {
                    max = Math.max(max, j - last);
                } else {
                    last = j;
                }
            }
        }
        for (int i = 1 - n; i < m; i++) { // anti-diagonal
            for (int j = Math.max(0, -i), last = j - 1; j <= Math.min(m - i, n) - 1; j++) {
                if (M[i + j][j] == 1) {
                    max = Math.max(max, j - last);
                } else {
                    last = j;
                }
            }
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 38.42%(39 ms for 57 tests)
    public int longestLine2(int[][] M) {
        int m = M.length;
        if (m == 0) return 0;

        int n = M[0].length;
        int[][][] dp = new int[m][n][4];
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (M[i][j] == 0) continue;

                dp[i][j][0] = j > 0 ? dp[i][j - 1][0] + 1 : 1; // horizontal
                dp[i][j][1] = i > 0 ? dp[i - 1][j][1] + 1 : 1; // vertical
                dp[i][j][2] = (i > 0 && j > 0) ? dp[i - 1][j - 1][2] + 1 : 1; // anti-diagonal
                dp[i][j][3] = (i > 0 && j < n - 1) ? dp[i - 1][j + 1][3] + 1 : 1; // diagonal
                for (int count : dp[i][j]) {
                    max = Math.max(max, count);
                }
            }
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(N)
    // beats 58.73%(33 ms for 57 tests)
    public int longestLine3(int[][] M) {
        int m = M.length;
        if (m == 0) return 0;

        int n = M[0].length;
        int[][] dp = new int[n][4];
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0, old = 0; j < n; j++) {
                if (M[i][j] == 1) {
                    dp[j][0] = j > 0 ? dp[j - 1][0] + 1 : 1; // horizontal
                    dp[j][1] = i > 0 ? dp[j][1] + 1 : 1; // vertical

                    int prev = dp[j][2];
                    dp[j][2] = (i > 0 && j > 0) ? old + 1 : 1; // anti-diagonal
                    old = prev;
                    dp[j][3] = (i > 0 && j < n - 1) ? dp[j + 1][3] + 1 : 1; // diagonal
                    for (int count : dp[j]) {
                        max = Math.max(max, count);
                    }
                } else {
                    old = dp[j][2];
                    Arrays.fill(dp[j], 0);
                }
            }
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M + N)
    // beats 91.21%(23 ms for 57 tests)
    public int longestLine4(int[][] M) {
        int m = M.length;
        if (m == 0) return 0;

        int n = M[0].length;
        int max = 0;
        int[] col = new int[n];
        int[] diag = new int[m + n];
        int[] antiDiag = new int[m + n];
        for (int i = 0; i < m; i++) {
            int row = 0;
            for (int j = 0; j < n; j++) {
                if (M[i][j] == 1) {
                    max = Math.max(max, Math.max(++row, ++col[j]));
                    max = Math.max(max, Math.max(++diag[j + i], ++antiDiag[j - i + m]));
                } else {
                    row = col[j] = diag[j + i] = antiDiag[j - i + m] = 0;
                }
            }
        }
        return max;
    }

    // Brute Force
    // time complexity: O(M * N), space complexity: O(1)
    // beats 20.51%(116 ms for 57 tests)
    public int longestLine5(int[][] M) {
        int max = 0;
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                if (M[i][j] == 1) {
                    max = Math.max(max, maxOnes(M, i, j));
                }
            }
        }
        return max;
    }

    static final int [][] DIRS = new int[][] {{1, 0}, {0, 1}, {1, 1}, {1, -1}};

    private int maxOnes(int [][] M, int x, int y){
        int max = 1;
        for (int [] dir : DIRS) {
            int i = x + dir[0];
            int j = y + dir[1];
            int count = 1;
            for (; isValid(M, i, j) && M[i][j] == 1; i += dir[0], j += dir[1], count++) {}
            max = Math.max(count, max);
        }
        return max;
    }

    private boolean isValid(int M[][], int i, int j) {
        return i >= 0 && j >= 0 && i < M.length && j < M[0].length;
    }

    void test(int[][] M, int expected) {
        assertEquals(expected, longestLine(M));
        assertEquals(expected, longestLine2(M));
        assertEquals(expected, longestLine3(M));
        assertEquals(expected, longestLine4(M));
        assertEquals(expected, longestLine5(M));
    }

    @Test
    public void test() {
        test(new int[][] {{0}, {1}}, 1);
        test(new int[][] {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 1}}, 3);
        test(new int[][] {{0, 1, 0, 1, 1}, {1, 1, 0, 0, 1}, {0, 0, 0, 1, 0},
                          {1, 0, 1, 1, 1}, {1, 0, 0, 0, 1}}, 3);

    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
