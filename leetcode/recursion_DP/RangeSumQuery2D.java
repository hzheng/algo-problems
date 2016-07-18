import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/range-sum-query-2d-immutable/
//
//
// Given a 2D matrix matrix, find the sum of the elements inside the rectangle
// defined by its upper left corner (row1, col1) and lower right corner (row2, col2).
public class RangeSumQuery2D {
    interface INumMatrix {
        public int sumRegion(int row1, int col1, int row2, int col2);
    }

    // beats 11.19%(8 ms)
    class NumMatrix implements INumMatrix {
        private int[][] sums;

        // time complexity: O(M * N)
        public NumMatrix(int[][] matrix) {
            int m = matrix.length;
            if (m == 0) return;

            int n = matrix[0].length;
            sums = new int[m][n + 1];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    sums[i][j + 1] = sums[i][j] + matrix[i][j];
                }
            }
        }

        // time complexity: O(M)
        public int sumRegion(int row1, int col1, int row2, int col2) {
            int sum = 0;
            for (int i = row1; i <= row2; i++) {
                sum += sums[i][col2 + 1] - sums[i][col1];
            }
            return sum;
        }
    }

    // beats 11.19%(8 ms)
    class NumMatrix2 implements INumMatrix {
        private int[][] sums;

        // time complexity: O(M * N)
        public NumMatrix2(int[][] matrix) {
            int m = matrix.length;
            if (m == 0) return;

            int n = matrix[0].length;
            sums = new int[m + 1][n + 1];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    sums[i + 1][j + 1] = sums[i + 1][j] + sums[i][j + 1]
                                         - sums[i][j] + matrix[i][j];
                }
            }
        }

        // time complexity: O(1)
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return sums[row2 + 1][col2 + 1] - sums[row2 + 1][col1]
                   - sums[row1][col2 + 1] + sums[row1][col1];
        }
    }

    void test(INumMatrix obj, int[] ... queries) {
        for (int[] query : queries) {
            assertEquals(query[4], obj.sumRegion(query[0], query[1], query[2], query[3]));
        }
    }

    void test(int[][] matrix, int[] ... queries) {
        test(new NumMatrix(matrix), queries);
        test(new NumMatrix2(matrix), queries);
    }

    @Test
    public void test1() {
        test(new int[][] {
            {3, 0, 1, 4, 2},
            {5, 6, 3, 2, 1},
            {1, 2, 0, 1, 5},
            {4, 1, 0, 1, 7},
            {1, 0, 3, 0, 5}
        },
             new int[] {2, 1, 4, 3, 8}, new int[] {1, 1, 2, 2, 11},
             new int[] {1, 2, 2, 4, 12});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RangeSumQuery2D");
    }
}
