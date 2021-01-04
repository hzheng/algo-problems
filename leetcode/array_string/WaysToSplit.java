import org.junit.Test;

import static org.junit.Assert.*;

// LC1712: https://leetcode.com/problems/ways-to-split-array-into-three-subarrays/
//
// A split of an integer array is good if:
// The array is split into three non-empty contiguous subarrays - left, mid, right respectively
// from left to right. The sum of the elements in left is less than or equal to the sum of the
// elements in mid, and the sum of the elements in mid is less than or equal to the sum of the
// elements in right. Given nums, an array of non-negative integers, return the number of good ways
// to split nums. As the number may be too large, return it modulo 10^9 + 7.
//
// Constraints:
// 3 <= nums.length <= 10^5
// 0 <= nums[i] <= 10^4
public class WaysToSplit {
    private final static int MOD = 1_000_000_007;

    // Binary Search
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 23 ms(100.00%), 47.6 MB(100.00%) for 87 tests
    public int waysToSplit(int[] nums) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        int res = 0;
        for (int i = 1; i < n && sum[i] * 3 <= sum[n]; i++) {
            int lower = search(sum, i + 1, n, sum[i] * 2);
            if (lower >= n) { break; }

            int target = (sum[n] - sum[i]) / 2 + sum[i] + 1;
            int upper = search(sum, lower + 1, n, target) - 1;
            if (sum[n] - sum[upper] < sum[upper] - sum[i]) {
                upper--;
            }
            res = (res + upper - lower + 1) % MOD;
        }
        return res;
    }

    private int search(int[] a, int start, int end, int target) {
        int low = start;
        for (int high = end; low < high; ) {
            int mid = (low + high) >>> 1;
            if (a[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 11 ms(95.63%), 47.6 MB(96.67%) for 87 tests
    public int waysToSplit2(int[] nums) {
        int res = 0;
        int n = nums.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        for (int i = 1, j = 1, k = 1; i < n - 1; i++) {
            for (; j <= i || (j < n && sum[j] < sum[i] * 2); j++) {}
            for (; k < j || (k < n && sum[k] * 2 <= sum[i] + sum[n]); k++) {}
            res = (res + k - j) % MOD;
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, waysToSplit(nums));
        assertEquals(expected, waysToSplit2(nums));
    }

    @Test public void test() {
        test(new int[] {0, 0, 0}, 1);
        test(new int[] {10, 10, 5, 0, 3, 8, 9}, 3);
        test(new int[] {1, 1, 1}, 1);
        test(new int[] {1, 2, 2, 2, 5, 0}, 3);
        test(new int[] {3, 2, 1}, 0);
        test(new int[] {7, 2, 5, 5, 6, 2, 10, 9}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
