import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/max-sum-of-sub-matrix-no-larger-than-k/
//
// Given a non-empty 2D matrix matrix and an integer k, find the max sum of a
// rectangle in the matrix such that its sum is no larger than k.
// Note:
// The rectangle inside the matrix must have an area > 0.
// What if the number of rows is much larger than the number of columns?
public class MaxSumSubmatrix {
    // Dynamic Programming
    // time complexity: O((M * N) ^ 2), space complexity: O(M * N)
    // beats 36.05%(326 ms)
    public int maxSumSubmatrix(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;

        int[][] sums = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sums[i + 1][j + 1] = matrix[i][j] + sums[i + 1][j]
                                     + sums[i][j + 1] - sums[i][j];
            }
        }
    
        int diff = Integer.MAX_VALUE;
        for (int i1 = 0; i1 < m; i1++) {
            for (int j1 = 0; j1 < n; j1++) {
                for (int i2 = i1; i2 < m; i2++) {
                    for (int j2 = j1; j2 < n; j2++) {
                        int sum = sums[i2 + 1][j2 + 1] + sums[i1][j1]
                                  - sums[i2 + 1][j1] - sums[i1][j2 + 1];
                        if (sum == k) return k;

                        if (sum < k && (k - sum) < diff) {
                            diff = k - sum;
                        }
                    }
                }
            }
        }
        return k - diff;
    }

    void test(int[][] matrix, int k, int expected) {
        assertEquals(expected, maxSumSubmatrix(matrix, k));
    }

    @Test
    public void test1() {
        test(new int[][] {{2, 2, -1}}, 0, -1);
        test(new int[][] {{1, 0, 1}, {0, -2, 3}, {1, 5, 4}}, 15, 13);
        test(new int[][] {{1, 0, 1}, {0, -2, 3}}, 2, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSumSubmatrix");
    }
}
