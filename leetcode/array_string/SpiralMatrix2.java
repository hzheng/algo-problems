import org.junit.Test;
import static org.junit.Assert.*;

// LC059: https://leetcode.com/problems/spiral-matrix-ii/
//
// Given an integer n, generate a square matrix filled with elements from 1 to
// n ^ 2 in spiral order.
public class SpiralMatrix2 {
    // beats 24.78%(2 ms)
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

    // Solution of Choice
    // beats 6.80%(3 ms)
    public int[][] generateMatrix2(int n) {
        int[][] matrix = new int[n][n];
        int[][] dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int[] steps = {n, n - 1};
        for (int x = 0, dir = 0, row = 0, col = -1; steps[dir & 1] > 0; dir = ++dir & 3) {
            for (int i = 0; i < steps[dir & 1]; i++) {
                row += dirs[dir][0];
                col += dirs[dir][1];
                matrix[row][col] = ++x;
            }
            steps[dir & 1]--;
        }
        return matrix;
    }

    void test(int n, int[][] expected) {
        assertArrayEquals(expected, generateMatrix(n));
        assertArrayEquals(expected, generateMatrix2(n));
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
