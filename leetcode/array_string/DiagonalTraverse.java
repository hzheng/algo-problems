import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC498: https://leetcode.com/problems/diagonal-traverse/
//
// Given a matrix of M x N elements, N columns), return all elements of the matrix
// in diagonal order as shown in the below image.
public class DiagonalTraverse {
    // beats 6.25%(389 ms for 32 tests)
    public int[] findDiagonalOrder(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return new int[0];

        int n = matrix[0].length;
        int[] res = new int[m * n];
        for (int sum = 0, count = 0; sum < m + n - 1; sum++) {
            boolean even = (sum % 2 == 0);
            for (int i = 0; i <= sum; i++) {
                int x = even ? sum - i : i;
                int y = even ? i : sum - i;
                if (x < m && y < n) {
                    res[count++] = matrix[x][y];
                }
            }
        }
        return res;
    }

    // beats 45.22%(10 ms for 32 tests)
    public int[] findDiagonalOrder2(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return new int[0];

        int n = matrix[0].length;
        int[] res = new int[m * n];
        for (int sum = 0, count = 0; sum < m + n - 1; sum++) {
            boolean even = (sum % 2 == 0);
            int start = Math.max(0, sum - (even ? m : n) + 1);
            int end = Math.min(sum, (even ? n : m) - 1);
            for (int i = start; i <= end; i++) {
                res[count++] = matrix[even ? sum - i : i][even ? i : sum - i];
            }
        }
        return res;
    }

    // Heap
    // beats 20.22%(38 ms for 32 tests)
    public int[] findDiagonalOrder3(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return new int[0];

        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[0] + a[1] != b[0] + b[1]) return a[0] + a[1] - b[0] - b[1];
                return (a[0] + a[1]) % 2 == 0 ? a[1] - b[1] : b[1] - a[1];
            }
        });
        int n = matrix[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                pq.offer(new int[] {i, j});
            }
        }
        int[] res = new int[m * n];
        for (int i = 0; !pq.isEmpty(); i++) {
            int[] pos = pq.poll();
            res[i] = matrix[pos[0]][pos[1]];
        }
        return res;
    }

    // beats 53.68%(9 ms for 32 tests)
    public int[] findDiagonalOrder4(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return new int[0];

        int n = matrix[0].length;
        int[] res = new int[m * n];
        int[][] dirs = {{-1, 1}, {1, -1}};
        for (int i = 0, j = 0, row = 0, col = 0; i < res.length; i++) {
            res[i] = matrix[row][col];
            row += dirs[j][0];
            col += dirs[j][1];
            if (row >= m) {
                row = m - 1;
                col += 2;
                j = 1 - j;
            }
            if (col >= n) {
                col = n - 1;
                row += 2;
                j = 1 - j;
            }
            if (row < 0) {
                row = 0;
                j = 1 - j;
            }
            if (col < 0) {
                col = 0;
                j = 1 - j;
            }
        }
        return res;
    }

    // beats 68.01%(8 ms for 32 tests)
    public int[] findDiagonalOrder5(int[][] matrix) {
        int m = matrix.length;
        if (m == 0) return new int[0];

        int n = matrix[0].length;
        int[] res = new int[m * n];
        for (int i = 0, row = 0, col = 0; i < res.length; i++) {
            res[i] = matrix[row][col];
            if ((row + col) % 2 == 0) { // move up
                if (col == n - 1) {
                    row++;
                } else if (row == 0) {
                    col++;
                } else {
                    row--;
                    col++;
                }
            } else { // move down
                if (row == m - 1) {
                    col++;
                } else if (col == 0) {
                    row++;
                } else {
                    row++;
                    col--;
                }
            }
        }
        return res;
    }

    void test(int[][] matrix, int ... expected) {
        assertArrayEquals(expected, findDiagonalOrder(matrix));
        assertArrayEquals(expected, findDiagonalOrder2(matrix));
        assertArrayEquals(expected, findDiagonalOrder3(matrix));
        assertArrayEquals(expected, findDiagonalOrder4(matrix));
        assertArrayEquals(expected, findDiagonalOrder5(matrix));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}},
             1, 2, 4, 7, 5, 3, 6, 8, 9);
        test(new int[][] {{1, 2, 3, 4}, {5, 6, 7, 8}},
             1, 2, 5, 6, 3, 4, 7, 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DiagonalTraverse");
    }
}
