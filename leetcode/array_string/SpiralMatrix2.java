import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an integer n, generate a square matrix filled with elements from 1 to
// n ^ 2 in spiral order.
public class SpiralMatrix2 {
    // beats 2.27%
    public int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int layers = (n + 1) / 2;
        int row = -1;
        int col = -1;
        int num = 1;
        for (int i = 0; i < layers; i++) {
            for (row++, col++; col < n - i; col++) {
                matrix[row][col] = num++;
            }
            for (row++, col--; row < n - i; row++) {
                matrix[row][col] = num++;
            }
            if (--row <= i) break;

            for (col--; col >= i; col--) {
                matrix[row][col] = num++;
            }

            if (++col >= n - i - 1) break;

            for (row--; row > i; row--) {
                matrix[row][col] = num++;
            }
        }
        return matrix;
    }

    void test(int n, int[][] expected) {
        assertArrayEquals(expected, generateMatrix(n));
    }

    @Test
    public void test1() {
        test(3, new int[][] { {1, 2, 3}, {8, 9, 4}, {7, 6, 5} });
        test(4, new int[][] { {1, 2, 3, 4}, {12, 13, 14, 5},
                              {11, 16, 15, 6}, {10, 9, 8, 7} });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SpiralMatrix2");
    }
}
