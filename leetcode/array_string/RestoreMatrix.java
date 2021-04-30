import org.junit.Test;

import static org.junit.Assert.*;

// LC1605: https://leetcode.com/problems/find-valid-matrix-given-row-and-column-sums/
//
// You are given two arrays rowSum and colSum of non-negative integers where rowSum[i] is the sum of
// the elements in the ith row and colSum[j] is the sum of the elements of the jth column of a 2D
// matrix. In other words, you do not know the elements of the matrix, but you do know the sums of
// each row and column.
// Find any matrix of non-negative integers of size rowSum.length x colSum.length that satisfies the
// rowSum and colSum requirements.
// Return a 2D array representing any matrix that fulfills the requirements. It's guaranteed that at
// least one matrix that fulfills the requirements exists.
//
// Constraints:
// 1 <= rowSum.length, colSum.length <= 500
// 0 <= rowSum[i], colSum[i] <= 10^8
// sum(rows) == sum(columns)
public class RestoreMatrix {
    // Greedy
    // time complexity: O(M*N), space complexity: O(M*N)
    // 4 ms(81.48%), 47.6 MB(14.41%) for 84 tests
    public int[][] restoreMatrix(int[] rowSum, int[] colSum) {
        int m = rowSum.length;
        int n = colSum.length;
        int[][] res = new int[m][n];
        res[0] = colSum.clone();
        for (int r = 0; r < m - 1; r++) {
            int target = rowSum[r];
            long sum = 0;
            int[] row = res[r];
            for (int c = 0; c < n; c++) {
                sum += row[c];
            }
            long diff = sum - target;
            for (int c = 0; diff > 0; c++) {
                int shift = (int)Math.min(row[c], diff);
                row[c] -= shift;
                diff -= shift;
                res[r + 1][c] = shift;
            }
        }
        return res;
    }

    // Greedy
    // time complexity: O(M*N), space complexity: O(M*N)
    // 7 ms(54.37%), 47.2 MB(65.35%) for 84 tests
    public int[][] restoreMatrix2(int[] rowSum, int[] colSum) {
        int m = rowSum.length;
        int n = colSum.length;
        rowSum = rowSum.clone();
        colSum = colSum.clone();
        int[][] res = new int[m][n];
        for (int r = 0; r < m; r++) {
            for (int c = 0 ; c < n; c++) {
                res[r][c] = Math.min(rowSum[r], colSum[c]);
                rowSum[r] -= res[r][c];
                colSum[c] -= res[r][c];
            }
        }
        return res;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<int[], int[], int[][]> restoreMatrix, int[] rowSum, int[] colSum) {
        int[][] res = restoreMatrix.apply(rowSum, colSum);
        int m = rowSum.length;
        assertEquals(m, res.length);
        int n = colSum.length;
        assertEquals(n, res[0].length);
        for (int r = 0; r < m; r++) {
            int sum = 0;
            int[] row = res[r];
            for (int c = 0; c < n; c++) {
                assertTrue("matrix element should be nonnegative", row[c] >= 0);
                sum += row[c];
            }
            assertEquals(rowSum[r], sum);
        }
        for (int c = 0; c < n; c++) {
            int sum = 0;
            for (int r = 0; r < m; r++) {
                sum += res[r][c];
            }
            assertEquals(colSum[c], sum);
        }
    }

    private void test(int[] rowSum, int[] colSum) {
        RestoreMatrix r = new RestoreMatrix();
        test(r::restoreMatrix, rowSum, colSum);
        test(r::restoreMatrix2, rowSum, colSum);
    }

    @Test public void test1() {
        test(new int[] {3, 8}, new int[] {4, 7});
        test(new int[] {5, 7, 10}, new int[] {8, 6, 8});
        test(new int[] {14, 9}, new int[] {6, 9, 8});
        test(new int[] {1, 0}, new int[] {1});
        test(new int[] {0}, new int[] {0});
        int[] rowSum = new int[500];
        java.util.Arrays.fill(rowSum, (int)1E8);
        int[] colSum = new int[500];
        java.util.Arrays.fill(colSum, (int)1E8);
        test(rowSum, colSum);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
