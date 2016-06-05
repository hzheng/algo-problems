import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a matrix, return all elements of the matrix in spiral order.
public class SpiralMatrix {
    // beats 3.18%
    public List<Integer> spiralOrder(int[][] matrix) {
        int rows = matrix.length;
        if (rows == 0) return Collections.emptyList();

        int cols = matrix[0].length;
        int layers = (Math.min(rows, cols) + 1) / 2;
        List<Integer> res = new ArrayList<>();
        int row = -1;
        int col = -1;
        for (int i = 0; i < layers; i++) {
            for (row++, col++; col < cols - i; col++) {
                res.add(matrix[row][col]);
            }
            for (row++, col--; row < rows - i; row++) {
                res.add(matrix[row][col]);
            }
            if (--row <= i) break;

            for (col--; col >= i; col--) {
                res.add(matrix[row][col]);
            }

            if (++col >= cols - i - 1) break;

            for (row--; row > i; row--) {
                res.add(matrix[row][col]);
            }
        }
        return res;
    }

    void test(int[][] matrix, Integer ... expected) {
        Integer[] res = spiralOrder(matrix).toArray(new Integer[0]);
        assertArrayEquals(expected, res);
    }

    @Test
    public void test1() {
        test(new int[][] { {1}, {2}, {3} }, 1, 2, 3);
        test(new int[][] { {1, 2} }, 1, 2);
        test(new int[][] { {1, 2, 3} }, 1, 2, 3);
        test(new int[][] { {1}, {2} }, 1, 2);
        test(new int[][] { {1, 2, 3}, {4, 5, 6} }, 1, 2, 3, 6, 5, 4);
        test(new int[][] { {1, 2, 3}, {4, 5, 6}, { 7, 8, 9} },
             1, 2, 3, 6, 9, 8, 7, 4, 5);
        test(new int[][] { {1, 2, 3, 4}, {5, 6, 7, 8},
                           { 9, 10, 11, 12}, {13, 14, 15, 16} },
             1, 2, 3, 4, 8, 12, 16, 15, 14, 13, 9, 5, 6, 7, 11, 10);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SpiralMatrix");
    }
}
