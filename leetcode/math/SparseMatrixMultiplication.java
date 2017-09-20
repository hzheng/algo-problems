import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC311: https://leetcode.com/problems/sparse-matrix-multiplication
//
// Given two sparse matrices A and B, return the result of AB.
// You may assume that A's column number is equal to B's row number.
public class SparseMatrixMultiplication {
    // Hash Table
    // beats 29.76%(126 ms for 12 tests)
    public int[][] multiply(int[][] A, int[][] B) {
        int r1 = A.length;
        int c1 = A[0].length;
        int r2 = B.length;
        int c2 = B[0].length;
        Map<Integer, Map<Integer, Integer> > nonzeros1 = new HashMap<>();
        for (int i = 0; i < r1; i++) {
            Map<Integer, Integer> rowMap = null;
            for (int j = 0; j < c1; j++) {
                if (A[i][j] != 0) {
                    if (rowMap == null) {
                        nonzeros1.put(i, rowMap = new HashMap<>());
                    }
                    rowMap.put(j, A[i][j]);
                }
            }
        }
        Map<Integer, Map<Integer, Integer> > nonzeros2 = new HashMap<>();
        for (int j = 0; j < c2; j++) {
            Map<Integer, Integer> colMap = null;
            for (int i = 0; i < r2; i++) {
                if (B[i][j] != 0) {
                    if (colMap == null) {
                        nonzeros2.put(j, colMap = new HashMap<>());
                    }
                    colMap.put(i, B[i][j]);
                }
            }
        }
        int[][] product = new int[r1][c2];
        for (int row : nonzeros1.keySet()) {
            Map<Integer, Integer> cols = nonzeros1.get(row);
            for (int col : nonzeros2.keySet()) {
                Map<Integer, Integer> rows = nonzeros2.get(col);
                for (int i : cols.keySet()) {
                    product[row][col] += cols.get(i) * rows.getOrDefault(i, 0);
                }
            }
        }
        return product;
    }

    // Hash Table
    // beats 64.85%(74 ms for 12 tests)
    public int[][] multiply2(int[][] A, int[][] B) {
        int r1 = A.length;
        int c1 = A[0].length;
        // int r2 = B.length;
        int c2 = B[0].length;
        Map<Integer, Map<Integer, Integer> > nonzeros1 = new HashMap<>();
        for (int i = 0; i < r1; i++) {
            Map<Integer, Integer> rowMap = null;
            for (int j = 0; j < c1; j++) {
                if (A[i][j] != 0) {
                    if (rowMap == null) {
                        nonzeros1.put(i, rowMap = new HashMap<>());
                    }
                    rowMap.put(j, A[i][j]);
                }
            }
        }
        int[][] product = new int[r1][c2];
        for (Map.Entry<Integer, Map<Integer, Integer>> rowEntry : nonzeros1.entrySet()) {
            int[] productRow = product[rowEntry.getKey()];
            for (Map.Entry<Integer, Integer> entry : rowEntry.getValue().entrySet()) {
                int[] rows2 = B[entry.getKey()];
                int val1 = entry.getValue();
                for (int col = 0; col < c2; col++) {
                    productRow[col] += val1 * rows2[col];
                }
            }
        }
        return product;
    }

    // Queue(or List)
    // beats 60.73%(76 ms for 12 tests)
    public int[][] multiply3(int[][] A, int[][] B) {
        int r1 = A.length;
        int c1 = A[0].length;
        int c2 = B[0].length;
        int[][] product = new int[r1][c2];
        @SuppressWarnings("unchecked")
        Queue<Integer>[] queues = new Queue[r1];
        for (int i = 0; i < r1; i++) {
            Queue<Integer> queue = new LinkedList<>();
            for (int j = 0; j < c1; j++) {
                if (A[i][j] != 0) {
                    queue.offer(j);
                    queue.offer(A[i][j]);
                }
            }
            queues[i] = queue;
        }
        for (int i = 0; i < r1; i++) {
            Queue<Integer> queue = queues[i];
            while (!queue.isEmpty()) {
                int[] rows2 = B[queue.poll()];
                int val1 = queue.poll();
                for (int j = 0; j < c2; j++) {
                    product[i][j] += val1 * rows2[j];
                }
            }
        }
        return product;
    }

    // beats 73.82%(70 ms for 12 tests)
    public int[][] multiply4(int[][] A, int[][] B) {
        int r1 = A.length;
        int c1 = A[0].length;
        int c2 = B[0].length;
        int[][] product = new int[r1][c2];
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c1; j++) {
                if (A[i][j] != 0) {
                    for (int k = 0; k < c2; k++) {
                        if (B[j][k] != 0) {
                            product[i][k] += A[i][j] * B[j][k];
                        }
                    }
                }
            }
        }
        return product;
    }

    void test(int[][] A, int[][] B, int[][] expected) {
        assertArrayEquals(expected, multiply(A, B));
        assertArrayEquals(expected, multiply2(A, B));
        assertArrayEquals(expected, multiply3(A, B));
        assertArrayEquals(expected, multiply4(A, B));
    }

    @Test
    public void test() {
        test(new int[][] {{1, -5}}, new int[][] {{12}, {-1}}, new int[][] {{17}});
        test(new int[][] {{1, 0, 0}, {-1, 0, 3}},
             new int[][] {{7, 0, 0}, {0, 0, 0}, {0, 0, 1}},
             new int[][] {{7, 0, 0}, {-7, 0, 3}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SparseMatrixMultiplication");
    }
}
