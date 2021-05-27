import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1572: https://leetcode.com/problems/matrix-diagonal-sum/
//
// Given a square matrix mat, return the sum of the matrix diagonals.
// Only include the sum of all the elements on the primary diagonal and all the elements on the
// secondary diagonal that are not part of the primary diagonal.
//
// Constraints:
// n == mat.length == mat[i].length
// 1 <= n <= 100
// 1 <= mat[i][j] <= 100
public class DiagonalSum {
    // time complexity: O(N^2), space complexity: O(1)
    // 1 ms(26.54%), 39.2 MB(29.37%) for 113 tests
    public int diagonalSum(int[][] mat) {
        int res = 0;
        for (int n = mat.length, i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j || i + j == n - 1) {
                    res += mat[i][j];
                }
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 39.1 MB(42.18%) for 113 tests
    public int diagonalSum2(int[][] mat) {
        int res = 0;
        int n = mat.length;
        for (int i = 0; i < n; i++) {
            res += mat[i][i];
            res += mat[i][n - 1 - i];
        }
        return res - (n % 2 == 1 ? mat[n / 2][n / 2] : 0);
    }

    private void test(int[][] mat, int expected) {
        assertEquals(expected, diagonalSum(mat));
        assertEquals(expected, diagonalSum2(mat));
    }

    @Test public void test1() {
        test(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 25);
        test(new int[][] {{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}}, 8);
        test(new int[][] {{5}}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
