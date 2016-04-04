import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.7:
 * Write an algorithm such that if an element in an MxN matrix is 0, its entire
 * row and column are set to 0.
 */
public class ZeroMatrix {
    // time complexity: O(M*N), space complexity: O(M+N)
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

    void test(int[][] matrix, int[][] expected) {
        zeroMatrix(matrix);
        assertArrayEquals(expected, matrix);
    }

    @Test
    public void test1() {
        test(new int[][] {{1}}, new int[][] {{1}});
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
