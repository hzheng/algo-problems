import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1901: https://leetcode.com/problems/find-a-peak-element-ii/
//
// A peak element in a 2D grid is an element that is strictly greater than all of its adjacent
// neighbors to the left, right, top, and bottom.
// Given a 0-indexed m x n matrix mat where no two adjacent cells are equal, find any peak element
// mat[i][j] and return the length 2 array [i,j].
// You may assume that the entire matrix is surrounded by an outer perimeter with the value -1 in
// each cell.
// You must write an algorithm that runs in O(m log(n)) or O(n log(m)) time.
//
// Constraints:
// m == mat.length
// n == mat[i].length
// 1 <= m, n <= 500
// 1 <= mat[i][j] <= 10^5
// No two adjacent cells are equal.
public class PeakElementII {
    // Recursion + Divide & Conquer
    // time complexity: O(MIN(N*log(M), M*log(N))), space complexity: O(log(MAX(M,N)))
    // 1 ms(42.02%), 140.4 MB(31.32%) for 45 tests
    public int[] findPeakGrid(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        return find(mat, 0, Math.max(m, n) - 1, m > n);
    }

    private int getValue(int[][] mat, int i, int j, boolean byRow) {
        return byRow ? (i >= 0 && i < mat.length ? mat[i][j] : -1) :
               (i >= 0 && i < mat[0].length ? mat[j][i] : -1);
    }

    private int[] find(int[][] mat, int start, int end, boolean byRow) {
        int m = mat.length;
        int n = mat[0].length;
        int maxV = Integer.MIN_VALUE;
        int maxI = 0;
        int mid = (start + end) >>> 1;
        for (int i = (byRow ? n : m) - 1; i >= 0; i--) {
            int val = getValue(mat, mid, i, byRow);
            if (val > maxV) {
                maxV = val;
                maxI = i;
            }
        }
        if (maxV < getValue(mat, mid + 1, maxI, byRow)) {
            return find(mat, mid + 1, end, byRow);
        }
        if (maxV < getValue(mat, mid - 1, maxI, byRow)) {
            return find(mat, start, mid - 1, byRow);
        }
        return byRow ? new int[] {mid, maxI} : new int[] {maxI, mid};
    }

    // Binary Search
    // time complexity: O(M*log(N)), space complexity: O(1)
    // 0 ms(100.00%), 85.1 MB(44.03%) for 45 tests
    public int[] findPeakGrid2(int[][] mat) {
        for (int m = mat.length, n = mat[0].length, start = 0, end = n - 1; ; ) {
            int mid = (start + end) >>> 1;
            int maxI = 0;
            for (int row = 0; row < m; row++) {
                maxI = mat[row][mid] >= mat[maxI][mid] ? row : maxI;
            }
            if (mid + 1 <= end && mat[maxI][mid + 1] > mat[maxI][mid]) {
                start = mid + 1;
            } else if (mid - 1 >= start && mat[maxI][mid - 1] > mat[maxI][mid]) {
                end = mid - 1;
            } else {return new int[] {maxI, mid};}
        }
    }

    private void test(int[][] mat) {
        PeakElementII p = new PeakElementII();
        test(mat, p::findPeakGrid);
        test(mat, p::findPeakGrid2);
    }

    private void test(int[][] mat, Function<int[][], int[]> findPeakGrid) {
        int m = mat.length;
        int n = mat[0].length;
        int[] res = findPeakGrid.apply(mat);
        int row = res[0];
        int col = res[1];
        int val = mat[row][col];
        assertTrue(row == 0 || val > mat[row - 1][col]);
        assertTrue(row == m - 1 || val > mat[row + 1][col]);
        assertTrue(col == 0 || val > mat[row][col - 1]);
        assertTrue(col == n - 1 || val > mat[row][col + 1]);
    }

    @Test public void test() {
        test(new int[][] {{1, 4}, {3, 2}});
        test(new int[][] {{10, 20, 15}, {21, 30, 14}, {7, 16, 32}});
        test(new int[][] {{1, 2, 5}, {3, 7, 6}});
        test(new int[][] {
                {55, 77, 9, 50, 49, 77, 60, 68, 33, 71, 2, 88, 93, 15, 88, 69, 97, 35, 99, 83, 44,
                 15, 38},
                {56, 21, 59, 1, 93, 34, 65, 98, 23, 65, 14, 81, 39, 82, 65, 78, 26, 20, 48, 98, 21,
                 70, 100},
                {68, 1, 77, 42, 63, 3, 15, 47, 40, 31, 8, 31, 73, 11, 94, 63, 9, 98, 69, 99, 17, 85,
                 61},
                {71, 22, 34, 68, 78, 55, 28, 70, 97, 94, 89, 26, 92, 40, 52, 86, 84, 48, 57, 67, 58,
                 16, 32},
                {29, 9, 44, 3, 76, 71, 30, 76, 29, 1, 10, 91, 81, 8, 30, 9, 5, 43, 10, 66, 31, 36,
                 86},
                {63, 28, 70, 17, 93, 74, 61, 32, 61, 53, 25, 13, 85, 56, 46, 55, 53, 60, 94, 7, 87,
                 84, 83},
                {13, 8, 52, 94, 44, 14, 32, 25, 69, 58, 18, 55, 24, 36, 60, 32, 10, 57, 71, 13, 7,
                 70, 2}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
