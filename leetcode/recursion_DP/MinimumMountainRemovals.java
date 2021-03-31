import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1671: https://leetcode.com/problems/minimum-number-of-removals-to-make-mountain-array/
//
// You may recall that an array arr is a mountain array if and only if:
// arr.length >= 3
// There exists some index i (0-indexed) with 0 < i < arr.length - 1 such that:
// arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
// arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
// Given an integer array nums​​​, return the minimum number of elements to remove to make
// nums a mountain array.
//
// Constraints:
// 3 <= nums.length <= 1000
// 1 <= nums[i] <= 10^9
// It is guaranteed that you can make a mountain array out of nums.
public class MinimumMountainRemovals {
    // Dynamic Programming
    // time complexity: O(N^2*log(N)), space complexity: O(N)
    // 329 ms(5.26%), 38.7 MB(95.07%) for 89 tests
    public int minimumMountainRemovals(int[] nums) {
        int n = nums.length;
        int res = n;
        for (int i = 1; i < n - 1; i++) {
            res = Math.min(res, remove(nums, i, true) + remove(nums, i, false));
        }
        return res;
    }

    private int remove(int[] nums, int end, boolean isLeft) {
        int n = nums.length;
        int[] seq = new int[isLeft ? end : n - end - 1];
        int len = 0;
        int max = isLeft ? end : n;
        for (int i = isLeft ? 0 : end + 1, limit = nums[end]; i < max; i++) {
            int num = nums[i];
            if (num >= limit) { continue; }

            num *= isLeft ? 1 : -1;
            int index = Arrays.binarySearch(seq, 0, len, num);
            if (index < 0) {
                index = -(index + 1);
            }
            seq[index] = num;
            if (index == len) {
                len++;
            }
        }
        return (len == 0) ? nums.length : seq.length - len;
    }

    // Dynamic Programming
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 5 ms(95.39%), 39 MB(49.67%) for 89 tests
    public int minimumMountainRemovals2(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] dp = new int[n];
        int len = 0;
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            int index = Arrays.binarySearch(left, 0, len, num);
            if (index < 0) {
                index = -index - 1;
            }
            left[index] = num;
            if (index == len) {
                len++;
            }
            dp[i] = len;
        }
        int[] right = new int[n];
        int res = n;
        len = 0;
        for (int i = n - 1; i > 0; i--) {
            int num = nums[i];
            int index = Arrays.binarySearch(right, 0, len, num);
            if (index < 0) {
                index = -index - 1;
            }
            right[index] = num;
            if (index == len) {
                len++;
            }
            if (dp[i] > 1 && len > 1) {
                res = Math.min(res, n - dp[i] - len + 1);
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N^2), space complexity: O(N)
    // 49 ms(52.96%), 38.8 MB(87.17%) for 89 tests
    public int minimumMountainRemovals3(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    left[i] = Math.max(left[i], left[j] + 1);
                }
            }
        }
        int[] right = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            for (int j = n - 1; j > i; j--) {
                if (nums[j] < nums[i]) {
                    right[i] = Math.max(right[i], right[j] + 1);
                }
            }
        }
        int max = 0;
        for (int i = 1; i < n - 1; i++) {
            if (right[i] > 0 && left[i] > 0) {
                max = Math.max(max, left[i] + right[i]);
            }
        }
        return n - max - 1;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, minimumMountainRemovals(nums));
        assertEquals(expected, minimumMountainRemovals2(nums));
        assertEquals(expected, minimumMountainRemovals3(nums));
    }

    @Test public void test() {
        test(new int[] {1, 3, 1}, 0);
        test(new int[] {2, 1, 1, 5, 6, 2, 3, 1}, 3);
        test(new int[] {4, 3, 2, 1, 1, 2, 3, 1}, 4);
        test(new int[] {1, 2, 3, 4, 4, 3, 2, 1}, 1);
        test(new int[] {100, 92, 89, 77, 74, 66, 64, 66, 64}, 6);
        test(new int[] {1, 2, 1, 3, 4, 4}, 3);
        test(new int[] {10, 9, 8, 7, 6, 5, 4, 5, 4}, 6);
        test(new int[] {4, 5, 13, 17, 1, 7, 6, 11, 2, 8, 10, 15, 3, 9, 12, 14, 16}, 10);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
