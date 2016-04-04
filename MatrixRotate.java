import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.6:
 * Given an image represented by an NxN matrix, where each pixel in the image
 * is 4 bytes, write a method to rotate the image by 90 degrees in place.
 */
public class MatrixRotate {
    // time complexity: O(n^2), space complexity: O(1)
    void rotate(int[][] matrix) {
        if (matrix == null) return;

        int n = matrix.length;
        for (int layer = 0; layer < n / 2; layer++) {
            rotateLayer(matrix, n, layer);
        }
    }

    void rotateLayer(int[][] matrix, int n, int layer) {
        int begin = layer;
        int end = n - 1 - layer;

        for (int index = begin; index < end; index++) {
            int indexBackwards = end + begin - index;
            int right = matrix[index][end];
            // top -> right
            matrix[index][end] = matrix[begin][index];
            // left -> top
            matrix[begin][index] = matrix[indexBackwards][begin];
            // bottom -> left
            matrix[indexBackwards][begin] = matrix[end][indexBackwards];
            // right -> bottom
            matrix[end][indexBackwards] = right;
        }
    }

    void print(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    void test(int[][] matrix, int[][] expected) {
        rotate(matrix);
        assertArrayEquals(expected, matrix);
    }

    @Test
    public void test1() {
        test(new int[][] {{1}}, new int[][] {{1}});
    }

    @Test
    public void test2() {
        /*
           1 2        3 1
           3 4  ->    4 2
         */
        test(new int[][] {{1, 2}, {3, 4}},
             new int[][] {{3, 1}, {4, 2}});
    }

    @Test
    public void test3() {
        /*
           1 2 3       7 4 1
           4 5 6 ->    8 5 2
           7 8 9       9 6 3
         */
        test(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}},
             new int[][] {{7, 4, 1}, {8, 5, 2}, {9, 6, 3}});
    }

    @Test
    public void test4() {
        /*
           1   2    3    4          13  9  5 1
           5   6    7    8    ->    14 10  6 2
           9   10  11   12          15 11  7 3
           13   14  15   16          16 12  8 4
         */
        test(new int[][] {{1, 2, 3, 4}, {5, 6, 7, 8},
                          {9, 10, 11, 12}, {13, 14, 15, 16}},
             new int[][] {{13, 9, 5, 1}, {14, 10, 6, 2},
                          {15, 11, 7, 3}, {16, 12, 8, 4}});
    }

    @Test
    public void test5() {
        /*
           11 12 13 14 15      51 41 31 21 11
           21 22 23 24 25      52 42 32 22 12
           31 32 33 34 35  ->  53 43 33 23 13
           41 42 43 44 45      54 44 34 24 14
           51 52 53 54 55      55 45 35 25 15
         */
        test(new int[][] {{11, 12, 13, 14, 15},
                          {21, 22, 23, 24, 25},
                          {31, 32, 33, 34, 35},
                          {41, 42, 43, 44, 45},
                          {51, 52, 53, 54, 55}},
             new int[][] {{51, 41, 31, 21, 11},
                          {52, 42, 32, 22, 12},
                          {53, 43, 33, 23, 13},
                          {54, 44, 34, 24, 14},
                          {55, 45, 35, 25, 15}});
    }

    @Test
    public void test6() {
        /*
           11 12 13 14 15 16     61 51 41 31 21 11
           21 22 23 24 25 26     62 52 42 32 22 12
           31 32 33 34 35 36     63 53 43 33 23 13
           41 42 43 44 45 46 ->  64 54 44 34 24 14
           51 52 53 54 55 56     65 55 45 35 25 15
           61 62 63 64 65 66     66 56 46 36 26 16
         */
        test(new int[][] {{11, 12, 13, 14, 15, 16},
                          {21, 22, 23, 24, 25, 26},
                          {31, 32, 33, 34, 35, 36},
                          {41, 42, 43, 44, 45, 46},
                          {51, 52, 53, 54, 55, 56},
                          {61, 62, 63, 64, 65, 66}},
             new int[][] {{61, 51, 41, 31, 21, 11},
                          {62, 52, 42, 32, 22, 12},
                          {63, 53, 43, 33, 23, 13},
                          {64, 54, 44, 34, 24, 14},
                          {65, 55, 45, 35, 25, 15},
                          {66, 56, 46, 36, 26, 16}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MatrixRotate");
    }
}
