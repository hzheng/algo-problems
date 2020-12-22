import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1074: https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/
//
// Given a matrix and a target, return the number of non-empty submatrices that sum to target.
// A submatrix x1, y1, x2, y2 is the set of all cells matrix[x][y] with x1<=x<= x2 and y1<=y<= y2.
// Two submatrices (x1, y1, x2, y2) and (x1', y1', x2', y2') are different if they have some
// coordinate that is different: for example, if x1 != x1'.
//
// Constraints:
// 1 <= matrix.length <= 100
// 1 <= matrix[0].length <= 100
// -1000 <= matrix[i] <= 1000
// -10^8 <= target <= 10^8
public class NumSubmatrixSumTarget {
    // Dynamic Programming + Hash Table
    // time complexity: O(N^2*M), space complexity: O(N*M)
    // 198 ms(15.61%), 46.9 MB(8.48%) for 40 tests
    public int numSubmatrixSumTarget(int[][] matrix, int target) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] sum = new int[n + 1][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0, rowSum = 0; j < m; j++) {
                rowSum += matrix[i][j];
                sum[i + 1][j + 1] = sum[i][j + 1] + rowSum;
            }
        }
        int res = 0;
        Map<Integer, Integer> count = new HashMap<>();
        for (int r1 = 0; r1 < n; r1++) {
            for (int r2 = r1; r2 < n; r2++) {
                int[] sum2 = sum[r2 + 1];
                int[] sum1 = sum[r1];
                for (int i = 0; i <= m; i++) {
                    int diff = sum2[i] - sum1[i];
                    count.put(diff, count.getOrDefault(diff, 0) + 1);
                }
                // sum2[c2+1]-sum1[c2+1]=sum2[c1]-sum1[c1]+target (c2>=c1)
                for (int c1 = 0; c1 <= m; c1++) {
                    int diff = sum2[c1] - sum1[c1];
                    count.put(diff, count.getOrDefault(diff, 0) - 1); // make sure c2>=c1
                    res += count.getOrDefault(sum2[c1] - sum1[c1] + target, 0);
                }
            }
        }
        return res;
    }

    // Dynamic Programming + Hash Table + Sliding Window
    // time complexity: O(N^2*M), space complexity: O(N*M)
    // 93 ms(89.02%), 39.9 MB(54.72%) for 40 tests
    public int numSubmatrixSumTarget2(int[][] matrix, int target) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] sums = new int[n][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sums[i][j + 1] = sums[i][j] + matrix[i][j];
            }
        }
        int res = 0;
        Map<Integer, Integer> sumCounter = new HashMap<>();
        for (int i = 0; i < m; i++) {
            for (int j = i; j < m; j++) {
                sumCounter.clear();
                sumCounter.put(0, 1);
                int sum = 0;
                for (int[] rowSum : sums) {
                    sum += rowSum[j + 1] - rowSum[i];
                    res += sumCounter.getOrDefault(sum - target, 0);
                    sumCounter.put(sum, sumCounter.getOrDefault(sum, 0) + 1);
                }
            }
        }
        return res;
    }

    private void test(int[][] matrix, int target, int expected) {
        assertEquals(expected, numSubmatrixSumTarget(matrix, target));
        assertEquals(expected, numSubmatrixSumTarget2(matrix, target));
    }

    @Test public void test() {
        test(new int[][] {{1, -1}, {-1, 1}}, 0, 5);
        test(new int[][] {{904}}, 0, 0);
        test(new int[][] {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 0, 4);
        test(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 5, 3);
        test(new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}, 6, 2);
        test(new int[][] {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}, {0, 3, -3}}, 0, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
