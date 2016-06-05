import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Write an efficient algorithm that searches for a value in an m x n matrix.
 * This matrix has the following properties:
 * Integers in each row are sorted from left to right.
 * The first integer of each row is greater than the last one of the previous row
 */
public class MatrixSearch {
    // beats 3.00%
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        Coordinate low = new Coordinate(0, 0);
        Coordinate high = new Coordinate(m - 1, n -1);
        while (low.isBefore(high)) {
            Coordinate mid = Coordinate.middle(n, low, high);
            int cur = matrix[mid.row][mid.col];
            if (cur == target) return true;

            if (cur > target) {
                mid.decrease(n);
                high = mid;
            } else {
                mid.increase(n);
                low = mid;
            }
        }
        return false;
    }

    static class Coordinate {
        int row;
        int col;

        public Coordinate(int r, int c) {
            row = r;
            col = c;
        }

        public void increase(int n) {
            if (col + 1 < n) {
                col++;
            } else {
                col = 0;
                row++;
            }
        }

        public void decrease(int n) {
            if (col > 0) {
                col--;
            } else {
                col = n - 1;
                row--;
            }
        }

        public boolean isBefore(Coordinate p) {
            return row < p.row || (row == p.row && col <= p.col);
        }

        public static Coordinate middle(int n, Coordinate min, Coordinate max) {
            int midOrder = (min.row * n + min.col + max.row * n + max.col) / 2;
            return new Coordinate(midOrder / n, midOrder % n);
        }

        @Override
        public String toString() {
            return "(" + row + "," + col + ")";
        }
    }

    // beats 6.15%
    public boolean searchMatrix2(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        int low = 0;
        int high = m * n - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int midVal = matrix[mid / n][mid % n];
            if (midVal == target) return true;

            if (midVal < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    // beats 6.15%
    public boolean searchMatrix3(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;

        // find row first
        int low = 0;
        int high = m - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (matrix[mid][0] == target) return true;

            if (matrix[mid][0] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        int row = high;
        if (row < 0) return false;

        low = 0;
        high = n - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (matrix[row][mid] == target) return true;

            if (matrix[row][mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return false;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(Function<int[][], Integer, Boolean> f, String name,
    int[][] matrix, int x, boolean expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, f.apply(matrix, x));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    private void test(int[][] matrix, int[] tgts, int[] expected) {
        MatrixSearch m = new MatrixSearch();
        for (int i = 0; i < tgts.length; i++) {
            test(m::searchMatrix, "searchMatrix", matrix, tgts[i], expected[i] > 0);
            test(m::searchMatrix2, "searchMatrix2", matrix, tgts[i], expected[i] > 0);
            test(m::searchMatrix3, "searchMatrix3", matrix, tgts[i], expected[i] > 0);
        }
    }

    @Test
    public void test1() {
        test(new int[][] {{1}}, new int[] {0}, new int[] {0});
        test(new int[][] {{1,   3,  5,  7},
                          {10, 11, 16, 20},
                          {23, 30, 34, 50}},
             new int[] {3, 11, 5, 74, 9, 23, 50, 51},
             new int[] {1, 1, 1, 0, 0, 1, 1, 0});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MatrixSearch");
    }
}
