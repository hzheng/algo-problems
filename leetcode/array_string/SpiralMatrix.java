import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC054: https://leetcode.com/problems/spiral-matrix/
//
// Given a matrix, return all elements of the matrix in spiral order.
public class SpiralMatrix {
    // beats 31.53%(2 ms)
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

    // Solution of Choice
    // https://discuss.leetcode.com/topic/15558/a-concise-c-implementation-based-on-directions
    // beats 8.05%(3 ms)
    public List<Integer> spiralOrder2(int[][] matrix) {
        List<Integer> res = new ArrayList<>();
        int rows = matrix.length;
        if (rows == 0) return res;

        int cols = matrix[0].length;
        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int[] steps = {cols, rows - 1};
        for (int dir = 0, row = 0, col = -1; steps[dir & 1] > 0; dir = ++dir & 3) {
            for (int i = 0; i < steps[dir & 1]; i++) {
                row += dirs[dir][0];
                col += dirs[dir][1];
                res.add(matrix[row][col]);
            }
            steps[dir & 1]--;
        }
        return res;
    }

    void test(Function<int[][], List<Integer>> spiralOrder,
             int[][] matrix, Integer ... expected) {
        Integer[] res = spiralOrder.apply(matrix).toArray(new Integer[0]);
        assertArrayEquals(expected, res);
    }

    void test(int[][] matrix, Integer ... expected) {
        SpiralMatrix s = new SpiralMatrix();
        test(s::spiralOrder, matrix, expected);
        test(s::spiralOrder2, matrix, expected);
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
