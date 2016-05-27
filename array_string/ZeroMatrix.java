import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.7:
 * Write an algorithm such that if an element in an MxN matrix is 0, its entire
 * row and column are set to 0.
 */
public class ZeroMatrix {
    // time complexity: O(M*N), space complexity: O(M+N)
    // beats 19.41%
    public void zeroMatrix(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        // we could use bit vector to save more space
        boolean[] rowFlags = new boolean[m];
        boolean[] colFlags = new boolean[n];
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                if (matrix[row][col] == 0) {
                    rowFlags[row] = true;
                    colFlags[col] = true;
                }
            }
        }
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                if (rowFlags[row] || colFlags[col]) {
                    matrix[row][col] = 0;
                }
            }
        }
    }

    // time complexity: O(M*N), space complexity: O(1)
    // beats 8.64%
    public void zeroMatrix2(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        boolean firstRowZero = false;
        for (int i = 0; i < n; i++) {
            if (matrix[0][i] == 0) {
                firstRowZero = true;
            }
        }
        boolean firstColZero = false;
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                firstColZero = true;
            }
        }

        // make the topmost row and leftmost column to flags
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        // fill the inside grids
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        // fill the edge girds
        if (firstRowZero) {
            for (int i = 0; i < n; i++) {
                matrix[0][i] = 0;
            }
        }
        if (firstColZero) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    void test(int[][] matrix, int[][] expected) {
        zeroMatrix2(matrix);
        // zeroMatrix(matrix);
        assertArrayEquals(expected, matrix);
    }

    @Test
    public void test1() {
        test(new int[][] {{1}}, new int[][] {{1}});
        test(new int[][] {{0, 1}}, new int[][] {{0, 0}});
        test(new int[][] {{0}, {1}}, new int[][] {{0}, {0}});
    }

    @Test
    public void test2() {
        /*
           1 2 3       1 0 3
           3 0 5 ->    0 0 0
         */
        test(new int[][] {{1, 2, 3}, {3, 0, 5}},
             new int[][] {{1, 0, 3}, {0, 0, 0}});
    }

    @Test
    public void test3() {
        /*
           1 2 3 0       0 0 0 0
           3 0 5 7 ->    0 0 0 0
           3 1 5 6 ->    3 0 5 0
         */
        test(new int[][] {{1, 2, 3, 0}, {3, 0, 5, 7}, {3, 1, 5, 6}},
             new int[][] {{0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0, 5, 0}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ZeroMatrix");
    }
}
