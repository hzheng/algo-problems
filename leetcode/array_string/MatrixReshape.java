import org.junit.Test;
import static org.junit.Assert.*;

// LC566: https://leetcode.com/problems/reshape-the-matrix/
//
// Given a matrix represented by a two-dimensional array, and two positive integers
// r and c representing the row number and column number of the wanted reshaped
// matrix, respectively. The reshaped matrix need to be filled with all the elements
// of the original matrix in the same row-traversing order as they were. If the
// 'reshape' operation with given parameters is possible and legal, output the new
// reshaped matrix; Otherwise, output the original matrix.
public class MatrixReshape {
    // beats 24.06%(10 ms for 56 tests)
    public int[][] matrixReshape(int[][] nums, int r, int c) {
        int m = nums.length;
        if (m == 0) return nums;

        int n = nums[0].length;
        if (m * n != r * c) return nums;

        int[][] res = new int[r][c];
        for (int i = 0, order = 0; i < r; i++) {
            for (int j = 0; j < c; j++, order++) {
                // int order = c * i + j;
                res[i][j] = nums[order / n][order % n];
            }
        }
        return res;
    }

    // beats 92.52%(7 ms for 56 tests)
    public int[][] matrixReshape2(int[][] nums, int r, int c) {
        int m = nums.length;
        if (m == 0) return nums;

        int n = nums[0].length;
        if (m * n != r * c) return nums;

        int[][] res = new int[r][c];
        for (int i = 0, rows = 0, cols = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[rows][cols] = nums[i][j];
                if (++cols == c) {
                    rows++;
                    cols = 0;
                }
            }
        }
        return res;
    }

    void test(int[][] nums, int r, int c, int[][] expected) {
        assertArrayEquals(expected, matrixReshape(nums, r, c));
        assertArrayEquals(expected, matrixReshape2(nums, r, c));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2}, {3, 4}}, 1, 4, new int[][] {{1, 2, 3, 4}});
        test(new int[][] {{1, 2}, {3, 4}}, 4, 1,
             new int[][] {{1}, {2}, {3}, {4}});
        test(new int[][] {{1, 2}, {3, 4}, {5, 6}}, 2, 3,
             new int[][] {{1, 2, 3}, {4, 5, 6}});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
