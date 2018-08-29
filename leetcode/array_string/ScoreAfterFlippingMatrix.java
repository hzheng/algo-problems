import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC861: https://leetcode.com/problems/score-after-flipping-matrix/
//
// We have a two dimensional matrix A where each value is 0 or 1.
// A move consists of choosing any row or column, and toggling each value in
// that row or column: changing all 0s to 1s, and all 1s to 0s. After making any
// number of moves, every row of this matrix is interpreted as a binary number,
// and the score of the matrix is the sum of these numbers.
// Return the highest possible score.
public class ScoreAfterFlippingMatrix {
    // time complexity: O(M * N), space complexity: O(1)
    // beats 12.31%(7 ms for 80 tests)
    public int matrixScore(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        for (int[] row : A) {
            if (row[0] == 0) {
                for (int i = 0; i < n; i++) {
                    row[i] ^= 1;
                }
            }
        }
        int res = 0;
        for (int i = n - 1, base = 1; i >= 0; i--, base <<= 1) {
            int count = 0;
            for (int j = 0; j < m; j++) {
                count += A[j][i];
            }
            res += Math.max(count, m - count) * base;
        }
        return res;
    }

    // time complexity: O(M * N), space complexity: O(1)
    // beats 12.31%(7 ms for 80 tests)
    public int matrixScore2(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < m; j++) {
                count += A[j][i] ^ A[j][0];
            }
            res += Math.max(count, m - count) * (1 << (n - 1 - i));
        }
        return res;
    }

    void test(int[][] A, int expected) {
        assertEquals(expected, matrixScore(A));
        assertEquals(expected, matrixScore2(A));
    }

    @Test
    public void test() {
        test(new int[][] { { 0, 0, 1, 1 }, { 1, 0, 1, 0 }, { 1, 1, 0, 0 } }, 39);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
