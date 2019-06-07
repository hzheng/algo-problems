import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1072: https://leetcode.com/problems/flip-columns-for-maximum-number-of-equal-rows/
//
// Given a matrix consisting of 0s and 1s, we may choose any number of columns in the matrix and
// flip every cell in that column.  Flipping a cell changes the value of that cell from 0 to 1 or
// from 1 to 0.
// Return the maximum number of rows that have all values equal after some number of flips.
// Note:
// 1 <= matrix.length <= 300
// 1 <= matrix[i].length <= 300
// All matrix[i].length's are equal
// matrix[i][j] is 0 or 1
public class MaxEqualRowsAfterFlips {
    // time complexity: O(N ^ 2 * M), space complexity: O(1)
    // 79 ms(42.76%), 51.6 MB(100%) for 84 tests
    public int maxEqualRowsAfterFlips(int[][] matrix) {
        int res = 0;
        int n = matrix.length;
        int m = matrix[0].length;
        for (int i = 0; i < n; i++) {
            int count = 1;
            outer:
            for (int j = i + 1; j < n; j++) {
                if (!Arrays.equals(matrix[i], matrix[j])) {
                    for (int k = 0; k < m; k++) {
                        if (matrix[i][k] + matrix[j][k] != 1) {
                            continue outer;
                        }
                    }
                }
                count++;
            }
            res = Math.max(res, count);
        }
        return res;
    }

    // time complexity: O(N ^ 2 * M), space complexity: O(M)
    // 135 ms(25.00%), 53.8 MB(100%) for 84 tests
    public int maxEqualRowsAfterFlips2(int[][] matrix) {
        int res = 0;
        int m = matrix[0].length;
        int[] flip = new int[m];
        for (int[] row1 : matrix) {
            int count = 0;
            for (int i = 0; i < m; i++) {
                flip[i] = 1 - row1[i];
            }
            for (int[] row2 : matrix) {
                if (Arrays.equals(row2, row1) || Arrays.equals(row2, flip)) {
                    count++;
                }
            }
            res = Math.max(res, count);
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N * M), space complexity: O(M)
    // 29 ms(83.20%), 56.5 MB(100%) for 84 tests
    public int maxEqualRowsAfterFlips3(int[][] matrix) {
        Map<String, Integer> map = new HashMap<>();
        for (int[] row : matrix) {
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int r : row) {
                sb1.append(r);
                sb2.append(1 - r);
            }
            String str1 = sb1.toString();
            String str2 = sb2.toString();
            map.put(str1, map.getOrDefault(str1, 0) + 1);
            map.put(str2, map.getOrDefault(str2, 0) + 1);
        }
        int res = 0;
        for (int v : map.values()) {
            res = Math.max(res, v);
        }
        return res;
    }

    void test(int[][] matrix, int expected) {
        assertEquals(expected, maxEqualRowsAfterFlips(matrix));
        assertEquals(expected, maxEqualRowsAfterFlips2(matrix));
        assertEquals(expected, maxEqualRowsAfterFlips3(matrix));
    }

    @Test
    public void test() {
        test(new int[][]{{0, 1}, {1, 1}}, 1);
        test(new int[][]{{0, 1}, {1, 0}}, 2);
        test(new int[][]{{0, 0, 0}, {0, 0, 1}, {1, 1, 0}}, 2);
        test(new int[][]{{0, 1, 0, 0}, {0, 0, 1, 1}, {1, 1, 0, 1}, {1, 0, 1, 0}}, 1);
        test(new int[][]{{0, 1, 0}, {0, 0, 1}, {1, 0, 1}, {1, 1, 0}, {0, 0, 1}}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
