import org.junit.Test;

import static org.junit.Assert.*;

// LC1582: https://leetcode.com/problems/special-positions-in-a-binary-matrix/
//
// Given a rows x cols matrix mat, where mat[i][j] is either 0 or 1, return the number of special
// positions in mat. A position (i,j) is called special if mat[i][j] == 1 and all other elements in
// row i and column j are 0 (rows and columns are 0-indexed).
// Constraints:
// rows == mat.length
// cols == mat[i].length
// 1 <= rows, cols <= 100
// mat[i][j] is 0 or 1.
public class SpecialPositions {
    // time complexity: O(M*(M+N)), space complexity: O(1)
    // 1 ms(99.93%), 39.2 MB(18.52%) for 95 tests
    public int numSpecial(int[][] mat) {
        int res = 0;
        int m = mat.length;
        int n = mat[0].length;
        outer:
        for (int i = 0; i < m; i++) {
            int one = -1;
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 1) {
                    if (one < 0) {
                        one = j;
                    } else {
                        continue outer;
                    }
                }
            }
            if (one < 0) { continue; }

            for (int k = 0; k < m; k++) {
                if (k != i && mat[k][one] == 1) { continue outer; }
            }
            res++;
        }
        return res;
    }

    // time complexity: O(M*N), space complexity: O(M+N)
    // 1 ms(99.93%), 39.2 MB(33.77%) for 95 tests
    public int numSpecial2(int[][] mat) {
        int res = 0;
        int m = mat.length;
        int n = mat[0].length;
        int[] col = new int[n];
        int[] row = new int[m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 1) {
                    col[j]++;
                    row[i]++;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 1 && row[i] == 1 && col[j] == 1) {
                    res++;
                }
            }
        }
        return res;
    }

    private void test(int[][] mat, int expected) {
        assertEquals(expected, numSpecial(mat));
        assertEquals(expected, numSpecial2(mat));
    }

    @Test public void test() {
        test(new int[][] {{1, 0, 0}, {0, 0, 1}, {1, 0, 0}}, 1);
        test(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}, 3);
        test(new int[][] {{0, 0, 0, 1}, {1, 0, 0, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, 2);
        test(new int[][] {{0, 0, 0, 0, 0}, {1, 0, 0, 0, 0}, {0, 1, 0, 0, 0}, {0, 0, 1, 0, 0},
                          {0, 0, 0, 1, 1}}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
