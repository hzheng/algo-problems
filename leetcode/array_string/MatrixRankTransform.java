import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1632: https://leetcode.com/problems/rank-transform-of-a-matrix/
//
// Given an m x n matrix, return a new matrix answer where answer[row][col] is the rank of
// matrix[row][col]. The rank is an integer that represents how large an element is compared to
// other elements. It is calculated using the following rules:
// The rank is an integer starting from 1.
// If two elements p and q are in the same row or column, then:
// If p < q then rank(p) < rank(q)
// If p == q then rank(p) == rank(q)
// If p > q then rank(p) > rank(q)
// The rank should be as small as possible.
// It is guaranteed that answer is unique under the given rules.
//
// Constraints:
// m == matrix.length
// n == matrix[i].length
// 1 <= m, n <= 500
// -10^9 <= matrix[row][col] <= 10^9
public class MatrixRankTransform {
    // Heap + Union Find
    // time complexity: O(M*N*log(M*N)), space complexity: O(M*N)
    // 400 ms(48.68%), 63.9 MB(5.28%) for 38 tests
    public int[][] matrixRankTransform(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparing(a -> a[2]));
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                pq.offer(new int[] {i, j, matrix[i][j]});
            }
        }
        int[][] res = new int[m][n];
        List<int[]> valGroup = new ArrayList<>();
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int prev = Integer.MIN_VALUE; !pq.isEmpty(); ) {
            int[] cur = pq.poll();
            int v = cur[2];
            if (v != prev) {
                rank(res, valGroup, rows, cols);
                prev = v;
                valGroup.clear();
            }
            valGroup.add(cur);
        }
        rank(res, valGroup, rows, cols);
        return res;
    }

    private void rank(int[][] res, List<int[]> valGroup, int[] rows, int[] cols) {
        int m = rows.length;
        int n = cols.length;
        int[] id = new int[m + n];
        Arrays.fill(id, -1);
        for (int[] cell : valGroup) {
            int x = cell[0];
            int y = cell[1];
            int px = root(id, x);
            int py = root(id, m + y);
            if (px != py) { // union
                id[px] = Math.min(Math.min(id[px], id[py]), -Math.max(rows[x], cols[y]) - 1);
                id[py] = px;
            }
        }
        for (int[] cell : valGroup) {
            int x = cell[0];
            int y = cell[1];
            res[x][y] = rows[x] = cols[y] = -id[root(id, x)];
        }
    }

    private int root(int[] id, int x) {
        for (; id[x] >= 0; x = id[x]) {}
        return x;
    }

    // Sort
    // time complexity: O(M^2*N^2), space complexity: O(M*N)
    // 100 ms(80.35%), 61.6 MB(5.28%) for 38 tests
    public int[][] matrixRankTransform2(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] cells = new int[m * n][];
        for (int i = 0, p = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cells[p++] = new int[] {matrix[i][j], i, j};
            }
        }
        Arrays.sort(cells, Comparator.comparing(x -> x[0]));
        int[] rows = new int[m];
        int[] cols = new int[n];
        int[][] res = new int[m][n];
        for (int i = 0, j, len = cells.length; i < len; i = j) {
            j = i;
            for (int v = cells[i][0]; j < len && cells[j][0] == v; j++) {
                int x = cells[j][1];
                int y = cells[j][2];
                res[x][y] = Math.max(rows[x], cols[y]) + 1;
            }
            for (boolean change = true; change; ) {
                change = false;
                for (int k = i; k < j; k++) {
                    int x = cells[k][1];
                    int y = cells[k][2];
                    rows[x] = Math.max(rows[x], res[x][y]);
                    cols[y] = Math.max(cols[y], res[x][y]);
                }
                for (int k = i; k < j; k++) {
                    int x = cells[k][1];
                    int y = cells[k][2];
                    int v = Math.max(rows[x], cols[y]);
                    if (res[x][y] < v) {
                        res[x][y] = v;
                        change = true;
                    }
                }
            }
        }
        return res;
    }

    private void test(int[][] matrix, int[][] expected) {
        assertArrayEquals(expected, matrixRankTransform(matrix));
        assertArrayEquals(expected, matrixRankTransform2(matrix));
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {3, 4}}, new int[][] {{1, 2}, {2, 3}});
        test(new int[][] {{7, 7}, {7, 7}}, new int[][] {{1, 1}, {1, 1}});
        test(new int[][] {{20, -21, 14}, {-19, 4, 19}, {22, -47, 24}, {-19, 4, 19}},
             new int[][] {{4, 2, 3}, {1, 3, 4}, {5, 1, 6}, {1, 3, 4}});
        test(new int[][] {{7, 3, 6}, {1, 4, 5}, {9, 8, 2}},
             new int[][] {{5, 1, 4}, {1, 2, 3}, {6, 3, 1}});
        test(new int[][] {{-37, -50, -3, 44}, {-37, 46, 13, -32}, {47, -42, -3, -40},
                          {-17, -22, -39, 24}},
             new int[][] {{2, 1, 4, 6}, {2, 6, 5, 4}, {5, 2, 4, 3}, {4, 3, 1, 5}});
        test(new int[][] {{-2, -35, -32, -5, -30, 33, -12}, {7, 2, -43, 4, -49, 14, 17},
                          {4, 23, -6, -15, -24, -17, 6}, {-47, 20, 39, -26, 9, -44, 39},
                          {-50, -47, 44, 43, -22, 33, -36}, {-13, 34, 49, 24, 23, -2, -35},
                          {-40, 43, -22, -19, -4, 23, -18}},
             new int[][] {{10, 3, 4, 9, 5, 15, 8}, {12, 4, 2, 10, 1, 13, 14},
                          {11, 13, 9, 8, 6, 7, 12}, {2, 10, 15, 4, 9, 3, 15},
                          {1, 2, 17, 16, 7, 15, 3}, {5, 14, 18, 11, 10, 8, 4},
                          {3, 15, 5, 6, 8, 14, 7}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
