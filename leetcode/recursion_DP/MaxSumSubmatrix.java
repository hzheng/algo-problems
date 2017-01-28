import java.util.*;
import org.junit.Test;

import static org.junit.Assert.*;

// LC363: https://leetcode.com/problems/max-sum-of-sub-matrix-no-larger-than-k/
//
// Given a non-empty 2D matrix matrix and an integer k, find the max sum of a
// rectangle in the matrix such that its sum is no larger than k.
// Note:
// The rectangle inside the matrix must have an area > 0.
// What if the number of rows is much larger than the number of columns?
public class MaxSumSubmatrix {
    // Dynamic Programming
    // time complexity: O((M * N) ^ 2), space complexity: O(M * N)
    // beats 34.43%(370 ms for 27 tests)
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

    // Dynamic Programming + SortedSet
    // https://discuss.leetcode.com/topic/48923/2-accepted-java-solution/
    // https://discuss.leetcode.com/topic/48854/java-binary-search-solution-time-complexity-min-m-n-2-max-m-n-log-max-m-n/2
    // time complexity: O(M ^ 2 * N * log(N)), space complexity: O(M * N)
    // beats 5.92%(700 ms for 27 tests)
    public int maxSumSubmatrix2(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] sums = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sums[i + 1][j + 1] = matrix[i][j] + sums[i + 1][j]
                                     + sums[i][j + 1] - sums[i][j];
            }
        }
        int maxSum = Integer.MIN_VALUE;
        for (int i1 = 0; i1 < m; i1++) {
            for (int i2 = i1; i2 < m; i2++) {
                NavigableSet<Integer> set = new TreeSet<>();
                set.add(0); // padding(needed)
                for (int j = 0; j < n; j++) {
                    int sum = sums[i2 + 1][j + 1] - sums[i1][j + 1];
                    Integer ceiling = set.ceiling(sum - k);
                    if (ceiling != null) {
                        maxSum = Math.max(maxSum, sum - ceiling);
                    }
                    set.add(sum);
                }
            }
        }
        return maxSum;
    }

    // Solution of Choice
    // Dynamic Programming + SortedSet
    // time complexity: O(min(M, N) ^ 2 * max(M, N) * log(max(M, N)), space complexity: O(max(M, N))
    // https://discuss.leetcode.com/topic/48875/accepted-c-codes-with-explanation-and-references/
    // beats 52.17%(177 ms for 27 tests)
    public int maxSumSubmatrix3(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        boolean biggerRow = m > n;
        m = Math.max(m, n);
        n = Math.min(matrix.length, n);
        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int[] sums = new int[m];
            for (int j = i; j < n; j++) {
                NavigableSet<Integer> set = new TreeSet<>();
                set.add(0); // needed only if area can be zero
                int num = 0;
                for (int l = 0; l < m; l++) {
                    sums[l] += biggerRow ? matrix[l][j] : matrix[j][l];
                    num += sums[l];
                    // if (num <= k) {
                    //     maxSum = Math.max(maxSum, num);
                    // }
                    Integer minDiff = set.ceiling(num - k);
                    if (minDiff != null) {
                        maxSum = Math.max(maxSum, num - minDiff);
                    }
                    set.add(num);
                }
            }
        }
        return maxSum;
    }

    // Solution of Choice
    // Dynamic Programming + MergeSort
    // time complexity: O(min(M, N) ^ 2 * max(M, N) * log(max(M, N)), space complexity: O(max(M, N))
    // https://discuss.leetcode.com/topic/53939/java-117ms-beat-99-81-merge-sort
    // beats 98.55%(112 ms for 27 tests)
    public int maxSumSubmatrix4(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;
        int[] sum = new int[m + 1];
        for (int i1 = 0; i1 < n; i1++) {
            int[] sumInRow = new int[m];
            for (int i2 = i1; i2 < n; i2++) {
                for (int j = 0; j < m; j++) {
                    sumInRow[j] += matrix[j][i2];
                    sum[j + 1] = sum[j] + sumInRow[j];
                }
                maxSum = Math.max(maxSum, mergeSort(sum, 0, m + 1, k));
                // sum[0] = 0; // if necesary, reset to avoid overflow
            }
        }
        return maxSum;
    }

    private int mergeSort(int[] sum, int start, int end, int k) {
        if (end == start + 1) return Integer.MIN_VALUE;

        int mid = (start + end) >>> 1;
        int res = Math.max(mergeSort(sum, start, mid, k), mergeSort(sum, mid, end, k));
        int[] cache = new int[end - start];
        int index = 0;
        for (int i = start, j = mid, right = mid; i < mid; i++) {
            for (; j < end && sum[j] - sum[i] <= k; j++) {}
            if (j > mid) {
                res = Math.max(res, sum[j - 1] - sum[i]);
            }
            while (right < end && sum[right] < sum[i]) {
                cache[index++] = sum[right++];
            }
            cache[index++] = sum[i];
        }
        System.arraycopy(cache, 0, sum, start, index);
        return res;
    }

    void test(int[][] matrix, int k, int expected) {
        assertEquals(expected, maxSumSubmatrix(matrix, k));
        assertEquals(expected, maxSumSubmatrix2(matrix, k));
        assertEquals(expected, maxSumSubmatrix3(matrix, k));
        assertEquals(expected, maxSumSubmatrix4(matrix, k));
    }

    @Test
    public void test1() {
        test(new int[][] {{2, 2, -1}}, 0, -1);
        test(new int[][] {{1, 0, 1}, {0, -2, 3}, {1, 5, 4}}, 15, 13);
        test(new int[][] {{1, 0, 1}, {0, -2, 3}}, 2, 2);
        test(new int[][] {{5, -4, -3, 4}, {-3, -4, 4, 5}, {5, 1, 5, -4}}, 8, 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSumSubmatrix");
    }
}
