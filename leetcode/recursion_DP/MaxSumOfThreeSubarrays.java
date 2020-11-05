import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

// LC689: https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
//
// In a given array of positive integers, find three non-overlapping subarrays with maximum sum.
// Each subarray will be of size k, and we want to maximize the sum of all 3*k entries.
// Return the result as a list of indices representing the starting position of each interval
// (0-indexed). If there are multiple answers, return the lexicographically smallest one.
// Note:
// nums.length will be between 1 and 20000.
// nums[i] will be between 1 and 65535.
// k will be between 1 and floor(nums.length / 3).
public class MaxSumOfThreeSubarrays {
    // Dynamic Programming
    // time complexity: O(N^2), space complexity: O(N)
    // 277 ms(5.02%), 41.3 MB(9.4%) for 42 tests
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int n = nums.length;
        int[] sums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        int[][] dp = new int[n][3];
        dp[n - 2 * k] = new int[] {sums[n] - sums[n - 2 * k], n - 2 * k, n - k};
        for (int i = n - 2 * k - 1; i >= 0; i--) {
            int[] prev = dp[i + 1];
            int index3 = prev[2];
            int s = 0;
            for (int j = prev[2]; j >= i + k; j--) {
                final int sum = sums[j + k] - sums[j];
                if (sum >= s) {
                    s = sum;
                    index3 = j;
                }
            }
            int sum = sums[i + k] - sums[i] + s;
            dp[i] = (sum < prev[0]) ? prev : new int[] {sum, i, index3};
        }
        int[] res = null;
        int maxSum = 0;
        for (int i = 0; i < n - 2 * k; i++) {
            int[] a = dp[i + k];
            int curSum = sums[i + k] - sums[i] + a[0];
            if (curSum > maxSum) {
                maxSum = curSum;
                res = new int[] {i, a[1], a[2]};
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(99.91%), 41.1 MB(9.40%) for 42 tests
    public int[] maxSumOfThreeSubarrays2(int[] nums, int k) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        int[] leftIndex = new int[n];
        for (int i = k, total = sum[k] - sum[0]; i < n; i++) {
            int s = sum[i + 1] - sum[i + 1 - k];
            if (s <= total) {
                leftIndex[i] = leftIndex[i - 1];
            } else {
                leftIndex[i] = i + 1 - k;
                total = s;
            }
        }
        int[] rightIndex = new int[n];
        rightIndex[n - k] = n - k;
        for (int i = n - k - 1, total = sum[n] - sum[n - k]; i >= 0; i--) {
            int s = sum[i + k] - sum[i];
            if (s < total) {
                rightIndex[i] = rightIndex[i + 1];
            } else {
                rightIndex[i] = i;
                total = s;
            }
        }
        int[] res = new int[3];
        for (int maxSum = 0, mid = k; mid <= n - 2 * k; mid++) {
            int l = leftIndex[mid - 1];
            int r = rightIndex[mid + k];
            int total = (sum[mid + k] - sum[mid]) + (sum[l + k] - sum[l]) + (sum[r + k] - sum[r]);
            if (total > maxSum) {
                maxSum = total;
                res[0] = l;
                res[1] = mid;
                res[2] = r;
            }
        }
        return res;
    }

    // Dynamic Programming (could be generalized)
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(14.51%), 41.5 MB(9.40%) for 42 tests
    public int[] maxSumOfThreeSubarrays3(int[] nums, int k) {
        final int n = nums.length;
        int[] sums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + nums[i];
        }
        final int m = 3;
        int[][] max = new int[n + 1][m + 1];
        int[][] index = new int[n + 1][m + 1];
        for (int i = 1; i <= m; i++) {
            for (int end = i * k; end <= n; end++) {
                if (max[end - k][i - 1] + sums[end] - sums[end - k] > max[end - 1][i]) {
                    max[end][i] = max[end - k][i - 1] + sums[end] - sums[end - k];
                    index[end][i] = end - k;
                } else {
                    max[end][i] = max[end - 1][i];
                    index[end][i] = index[end - 1][i];
                }
            }
        }
        int[] res = new int[m];
        res[m - 1] = index[n][m];
        for (int i = m - 1; i > 0; i--) {
            res[i - 1] = index[res[i]][i];
        }
        return res;
    }

    // Sliding Window (could be generalized)
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(42.60%), 40.9 MB(9.40%) for 42 tests
    public int[] maxSumOfThreeSubarrays4(int[] nums, int k) {
        final int n = nums.length;
        final int m = 3;
        int[][] res = new int[m + 1][m];
        int[] sums = new int[m + 1];
        for (int i = 0; i < m; i++) {
            for (int j = i * k; j < (i + 1) * k; j++) {
                sums[i + 1] += nums[j];
            }
            res[i + 1] = res[i].clone();
            res[i + 1][i] = k * i;
        }
        int[] maxSum = new int[m + 1];
        for (int i = 0; i < m; i++) {
            maxSum[i + 1] = maxSum[i] + sums[i + 1];
        }
        for (int i = 1; i + k * m <= n; i++) {
            for (int j = 1; j <= m; j++) {
                sums[j] += (nums[k * j + i - 1] - nums[k * (j - 1) + i - 1]); // sliding sums
                if (maxSum[j] < maxSum[j - 1] + sums[j]) {
                    maxSum[j] = maxSum[j - 1] + sums[j];
                    res[j] = res[j - 1].clone();
                    res[j][j - 1] = k * (j - 1) + i;
                }
            }
        }
        return res[m];
    }

    private void test(int[] nums, int k, int[] expected) {
        assertArrayEquals(expected, maxSumOfThreeSubarrays(nums, k));
        assertArrayEquals(expected, maxSumOfThreeSubarrays2(nums, k));
        assertArrayEquals(expected, maxSumOfThreeSubarrays3(nums, k));
        assertArrayEquals(expected, maxSumOfThreeSubarrays4(nums, k));
    }

    @Test public void test() {
        test(new int[] {4, 3, 2, 1}, 1,
             new int[] {0, 1, 2}); //TODO: not update dp[i][j] when j == 0
        test(new int[] {16, 23, 1, 2, 3, 28, 9, 12, 7, 1, 13, 2, 1, 2, 6, 7, 5, 10, 8, 7, 3, 11, 4,
                        6, 9, 11, 7, 9, 3, 12, 17, 21, 13, 6, 9, 24, 6, 12}, 4,
             new int[] {5, 29, 34});
        test(new int[] {9, 12, 7, 1, 13, 2, 1, 2, 6, 7, 5, 10, 8, 7, 3, 11, 4, 6, 9, 11, 7, 9, 3,
                        12, 17, 21, 13, 6, 9, 24, 6, 12}, 4, new int[] {18, 23, 28});
        test(new int[] {1, 2, 1, 2, 6, 7, 5, 8, 7, 3, 11, 4, 6}, 3, new int[] {4, 7, 10});
        test(new int[] {1, 2, 1, 2, 6, 7, 5, 1}, 2, new int[] {0, 3, 5});
        test(new int[] {1, 2, 1, 2, 1, 2, 1, 2, 1}, 2, new int[] {0, 2, 4});
        test(new int[] {1, 2, 1, 1, 1, 2, 1}, 2, new int[] {0, 2, 4});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
