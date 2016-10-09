import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC417:
//
// Given an m x n matrix of non-negative integers representing the height of
// each unit cell in a continent, the "Pacific ocean" touches the left and top
// edges of the matrix and the "Atlantic ocean" touches the right and bottom edges.
// Water can only flow in four directions (up, down, left, or right) from a cell
// to another one with height equal or lower. Find the list of grid coordinates
// where water can flow to both the Pacific and Atlantic ocean.
// Example:
// Given the following 5x5 matrix:
//  Pacific ~   ~   ~   ~   ~
//       ~  1   2   2   3  (5) *
//       ~  3   2   3  (4) (4) *
//       ~  2   4  (5)  3   1  *
//       ~ (6) (7)  1   4   5  *
//       ~ (5)  1   1   2   4  *
//          *   *   *   *   * Atlantic
// Return:
// [[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]].
public class Waterflow {
    // beats N/A(25 ms for 113 tests)
    public List<int[]> pacificAtlantic(int[][] matrix) {
        List<int[]> res = new ArrayList<>();
        int m = matrix.length;
        if (m == 0) return res;

        int n = matrix[0].length;
        if (n == 0) return res;

        boolean[][] pacific = new boolean[m][n];
        boolean[][] atlantic = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            pacific[i][0] = true;
            mark(pacific, matrix, i, 0, m, n);
            atlantic[i][n - 1] = true;
            mark(atlantic, matrix, i, n - 1, m, n);
        }
        for (int j = 0; j < n; j++) {
            pacific[0][j] = true;
            mark(pacific, matrix, 0, j, m, n);
            atlantic[m - 1][j] = true;
            mark(atlantic, matrix, m - 1, j, m, n);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pacific[i][j] && atlantic[i][j]) {
                    res.add(new int[]{i, j});
                }
            }
        }
        return res;
    }

    private void mark(boolean[][] flags, int[][] matrix, int i, int j, int m, int n) {
        int height = matrix[i][j];
        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] move : moves) {
            int p = i + move[0];
            int q = j + move[1];
            if (p >= 0 && p < m && q >= 0 && q < n
                && !flags[p][q] && matrix[p][q] >= height) {
                flags[p][q] = true;
                mark(flags, matrix, p, q, m, n);
            }
        }
    }

    void test(Function<int[][], List<int[]>> waterflow,
              int[][] matrix, int[][] expected) {
        int[][] res = waterflow.apply(matrix).toArray(new int[0][0]);
        // System.out.println(Arrays.deepToString(res));
        Arrays.sort(res, new Utils.IntArrayComparator());
        Arrays.sort(expected, new Utils.IntArrayComparator());
        assertArrayEquals(expected, res);
    }

    void test(int[][] matrix, int[][] expected) {
        Waterflow w = new Waterflow();
        test(w::pacificAtlantic, matrix, expected);
    }

    @Test
    public void test() {
        test(new int[][] {{1, 1}, {1, 1}, {1, 1}},
             new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {2, 0}, {2, 1}});
        test(new int[][] {{3, 3, 3, 3, 3, 3}, {3, 0, 3, 3, 0, 3}, {3, 3, 3, 3, 3, 3}},
             new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5},
                           {1, 0}, {1, 2}, {1, 3}, {1, 5}, {2, 0}, {2, 1},
                            {2, 2}, {2, 3}, {2, 4}, {2, 5}});
        test(new int[][] {{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4},
                          {2, 4, 5, 3, 1}, {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}},
             new int[][] {{0, 4}, {1, 3}, {1, 4}, {2, 2}, {3, 0}, {3, 1}, {4, 0}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Waterflow");
    }
}
