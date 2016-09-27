import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC073: https://leetcode.com/problems/set-matrix-zeroes/
// Cracking the Coding Interview(5ed) Problem 1.7:
//
// Given a matrix, if an element is 0, set its entire row and column to 0. Do it in place.
public class ZeroMatrix {
    // time complexity: O(M*N), space complexity: O(M+N)
    // beats 26.88%(2 ms)
    public void setZeroes(int[][] matrix) {
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
    // beats 26.88%(2 ms)
    public void setZeroes2(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        boolean firstRowZero = false;
        for (int i = 0; i < n; i++) {
            if (matrix[0][i] == 0) {
                firstRowZero = true;
                break;
            }
        }
        boolean firstColZero = false;
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                firstColZero = true;
                break;
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

    // Solution of Choice
    // https://discuss.leetcode.com/topic/5056/any-shorter-o-1-space-solution
    // beats 16.34%(3 ms)
    public void setZeroes3(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean zeroFirstCol = false;
        for (int i = 0; i < rows; i++) { // top-down
            if (matrix[i][0] == 0) {
                zeroFirstCol = true;
            }
            for (int j = 1; j < cols; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = matrix[0][j] = 0;
                }
            }
        }

        for (int i = rows - 1; i >= 0; i--) { // bottom-up
            for (int j = cols - 1; j > 0; j--) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
            if (zeroFirstCol) {
                matrix[i][0] = 0;
            }
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<int[][]> zeroMatrix, final int[][] matrix, int[][] expected) {
        int[][] clonedMatrix = Arrays.stream(matrix).map(e -> e.clone()).toArray(__ -> matrix.clone());
        zeroMatrix.apply(clonedMatrix);
        assertArrayEquals(expected, clonedMatrix);
    }

    void test(int[][] matrix, int[][] expected) {
        ZeroMatrix z = new ZeroMatrix();
        test(z::setZeroes, matrix, expected);
        test(z::setZeroes2, matrix, expected);
        test(z::setZeroes3, matrix, expected);
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
