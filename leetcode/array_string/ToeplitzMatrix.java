import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC766: https://leetcode.com/problems/toeplitz-matrix/description/
//
// A matrix is Toeplitz if every diagonal from top-left to bottom-right has the
// same element.
// Now given an M x N matrix, return True if and only if the matrix is Toeplitz.
public class ToeplitzMatrix {
    // beats %(30 ms for 482 tests)
    public boolean isToeplitzMatrix(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return true;

        int n = matrix[0].length;
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] != matrix[i - 1][j - 1]) return false;
            }
        }
        return true;
    }

    // Hash Table
    // beats %(33 ms for 482 tests)
    public boolean isToeplitzMatrix2(int[][] matrix) {
        Map<Integer, Integer> groups = new HashMap<>();
        for (int r = 0; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                Integer v = groups.get(r - c);
                if (v == null) {
                    groups.put(r - c, matrix[r][c]);
                } else if (v != matrix[r][c]) return false;
            }
        }
        return true;
    }

    void test(int[][] matrix, boolean expected) {
        assertEquals(expected, isToeplitzMatrix(matrix));
        assertEquals(expected, isToeplitzMatrix2(matrix));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2, 3, 4}, {5, 1, 2, 3}, {9, 5, 1, 2}}, true);
        test(new int[][] {{1, 2}, {2, 2}}, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
