import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1800: https://leetcode.com/problems/maximum-ascending-subarray-sum/
//
// Given an array of positive integers nums, return the maximum possible sum of an ascending
// subarray in nums.
// A subarray is defined as a contiguous sequence of numbers in an array.
// A subarray [numsl, numsl+1, ..., numsr-1, numsr] is ascending if for all i where l <= i < r,
// numsi < numsi+1. Note that a subarray of size 1 is ascending.
//
// Constraints:
// 1 <= nums.length <= 100
// 1 <= nums[i] <= 100
public class MaxAscendingSum {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(%), 36.4 MB(%) for 104 tests
    public int maxAscendingSum(int[] nums) {
        int res = nums[0];
        for (int i = 1, sum = nums[0]; i < nums.length; i++) {
            sum = nums[i] + ((nums[i] > nums[i - 1]) ? sum : 0);
            res = Math.max(res, sum);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(%), 36.8 MB(%) for 104 tests
    public int maxAscendingSum2(int[] nums) {
        int res = 0;
        int prev = 0;
        int sum = 0;
        for (int num : nums) {
            sum = num + ((num > prev) ? sum : 0);
            res = Math.max(res, sum);
            prev = num;
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, maxAscendingSum(nums));
        assertEquals(expected, maxAscendingSum2(nums));
    }

    @Test public void test() {
        test(new int[] {10, 20, 30, 5, 10, 50}, 65);
        test(new int[] {10, 20, 30, 40, 50}, 150);
        test(new int[] {12, 17, 15, 13, 10, 11, 12}, 33);
        test(new int[] {100, 10, 1}, 100);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
