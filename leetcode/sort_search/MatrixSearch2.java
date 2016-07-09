import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/search-a-2d-matrix-ii/
//
// Write an efficient algorithm that searches for a value in an m x n matrix.
// This matrix has the following properties:
// Integers in each row are sorted in ascending from left to right.
// Integers in each column are sorted in ascending from top to bottom.
public class MatrixSearch2 {
    // beats 2.28%(52 ms)
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        return searchMatrix(matrix, 0, n - 1, 0, m - 1, target);
    }

    private boolean searchMatrix(int[][] matrix, int x1, int x2,
                                 int y1, int y2, int target) {
        if (x1 > x2 || y1 > y2) return false;

        if (y1 == y2) {
            return Arrays.binarySearch(matrix[y1], x1, x2 + 1, target) >= 0;
        }

        if (x1 == x2) {
            while (y1 <= y2) {
                int mid = y1 + (y2 - y1) / 2;
                if (matrix[mid][x1] == target) return true;

                if (matrix[mid][x1] < target) {
                    y1 = mid + 1;
                } else {
                    y2 = mid - 1;
                }
            }
            return false;
        }

        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;
        if (target == matrix[midY][midX]) return true;

        if (target < matrix[midY][midX]) {
            return searchMatrix(matrix, x1, midX, y1, midY, target)
            || searchMatrix(matrix, x1, midX - 1, midY + 1, y2, target)
            || searchMatrix(matrix, midX + 1, x2, y1, midY - 1, target);
        } else {
            return searchMatrix(matrix, midX + 1, x2, y1, midY, target)
            || searchMatrix(matrix, x1, midX, midY + 1, y2, target)
            || searchMatrix(matrix, midX + 1, x2, midY + 1, y2, target);
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(Function<int[][], Integer, Boolean> f, String name,
                      int[][] matrix, int x, boolean expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, f.apply(matrix, x));
        // System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    private void test(int[][] matrix, int[] tgts, int[] expected) {
        MatrixSearch2 m = new MatrixSearch2();
        for (int i = 0; i < tgts.length; i++) {
            test(m::searchMatrix, "searchMatrix", matrix, tgts[i], expected[i] > 0);
        }
    }

    @Test
    public void test1() {
        test(new int[][] {{1}}, new int[] {0}, new int[] {0});
        test(new int[][] {{1,   2,  3},
                          {4,   8,  9},
                          {7,  10, 12}},
             new int[] {1, 5, 10, 11, 12},
             new int[] {1, 0,  1,  0,  1});
        test(new int[][] {{1,   2,  3, 5},
                          {4,   8,  9, 10},
                          {7,  10, 12, 15}},
             new int[] {1, 5, 10, 11, 12, 15, 16},
             new int[] {1, 1,  1,  0,  1, 1, 0});
        test(new int[][] {{1,   4,  7,  11, 15},
                          {2,   5,  8,  12, 19},
                          {3,   6,  9,  16, 22},
                          {10, 13, 14,  17, 24},
                          {18, 21, 23,  26, 30}},
             new int[] {5, 20, 9, 74, 19, 21, 23, 24, 26, 51},
             new int[] {1, 0,  1,  0,  1,  1,  1,  1, 1, 0});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MatrixSearch2");
    }
}
